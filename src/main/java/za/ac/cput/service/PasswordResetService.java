package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.PasswordResetRequest;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.repository.PasswordResetRequestRepository;
import za.ac.cput.service.impl.IPasswordResetService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService implements IPasswordResetService {

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;

    private final PasswordResetRequestRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ClinicStaffService clinicStaffService;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public PasswordResetService(PasswordResetRequestRepository repository,
                                PasswordEncoder passwordEncoder,
                                PatientService patientService,
                                DoctorService doctorService,
                                ClinicStaffService clinicStaffService,
                                EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.clinicStaffService = clinicStaffService;
        this.emailService = emailService;
    }

    @Override
    public boolean requestReset(String email) {
        // Deliberately do NOT reveal whether the email exists — check
        // internally, but always return true so the controller's response
        // wording is identical either way (account enumeration protection).
        boolean userExists = patientService.findByEmail(email).isPresent()
                || doctorService.findByEmail(email).isPresent()
                || clinicStaffService.findByEmail(email).isPresent();

        if (!userExists) {
            return true; // silently no-op — nobody to email
        }

        String rawCode = generateCode();

        PasswordResetRequest request = new PasswordResetRequest.Builder()
                .setEmail(email)
                .setCodeHash(passwordEncoder.encode(rawCode))
                .setVerified(false)
                .setUsed(false)
                .setAttempts(0)
                .setCreatedDate(LocalDateTime.now())
                .setExpiryDate(LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES))
                .build();

        repository.save(request);

        emailService.sendPasswordResetEmail(email, rawCode);
        return true;
    }

    @Override
    public String verifyCode(String email, String code) {
        Optional<PasswordResetRequest> latest = findLatestUsable(email);
        if (latest.isEmpty()) return null;

        PasswordResetRequest request = latest.get();

        if (request.isExpired() || request.isUsed() || request.getAttempts() >= MAX_ATTEMPTS) {
            return null;
        }

        if (!passwordEncoder.matches(code, request.getCodeHash())) {
            PasswordResetRequest incremented = new PasswordResetRequest.Builder()
                    .copy(request)
                    .setAttempts(request.getAttempts() + 1)
                    .build();
            repository.save(incremented);
            return null;
        }

        String sessionToken = UUID.randomUUID().toString();

        PasswordResetRequest verified = new PasswordResetRequest.Builder()
                .copy(request)
                .setVerified(true)
                .setResetSessionToken(sessionToken)
                .build();
        repository.save(verified);

        return sessionToken;
    }

    @Override
    public boolean resetPassword(String email, String resetSessionToken, String newPassword) {
        if (resetSessionToken == null || resetSessionToken.isBlank()) return false;

        Optional<PasswordResetRequest> latest = findLatestUsable(email);
        if (latest.isEmpty()) return false;

        PasswordResetRequest request = latest.get();

        if (!request.isVerified()
                || request.isUsed()
                || request.isExpired()
                || !resetSessionToken.equals(request.getResetSessionToken())) {
            return false;
        }

        boolean updated = updatePasswordForEmail(email, newPassword);
        if (!updated) return false;

        PasswordResetRequest consumed = new PasswordResetRequest.Builder()
                .copy(request)
                .setUsed(true)
                .build();
        repository.save(consumed);

        return true;
    }

    // ── Internal helpers ──────────────────────────────────────────

    private Optional<PasswordResetRequest> findLatestUsable(String email) {
        List<PasswordResetRequest> all = repository.findByEmailOrderByCreatedDateDesc(email);
        return all.isEmpty() ? Optional.empty() : Optional.of(all.get(0));
    }

    private String generateCode() {
        int code = 100000 + secureRandom.nextInt(900000); // always 6 digits, no leading zero issue
        return String.valueOf(code);
    }

    // Mirrors AuthService.changePassword()'s three-way lookup, but without
    // the oldPassword check — the reset flow's proof-of-identity is the
    // verified code, not the current password.
    private boolean updatePasswordForEmail(String email, String newPassword) {
        Optional<Patient> patientOpt = patientService.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Patient updated = new Patient.Builder()
                    .copy(patient)
                    .setPassword(passwordEncoder.encode(newPassword))
                    .build();
            patientService.update(updated);
            return true;
        }

        Optional<Doctor> doctorOpt = doctorService.findByEmail(email);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Doctor updated = new Doctor.Builder()
                    .copy(doctor)
                    .setPassword(passwordEncoder.encode(newPassword))
                    .build();
            doctorService.update(updated);
            return true;
        }

        Optional<ClinicStaff> staffOpt = clinicStaffService.findByEmail(email);
        if (staffOpt.isPresent()) {
            ClinicStaff staff = staffOpt.get();
            ClinicStaff updated = new ClinicStaff.Builder()
                    .copy(staff)
                    .setPassword(passwordEncoder.encode(newPassword))
                    .build();
            clinicStaffService.update(updated);
            return true;
        }

        return false; // shouldn't happen — requestReset() already confirmed existence
    }
}