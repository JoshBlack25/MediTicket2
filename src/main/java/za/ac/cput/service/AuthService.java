package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.VerificationToken;
import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.dto.AuthResponse;
import za.ac.cput.dto.ClinicStaffSignupRequest;
import za.ac.cput.dto.DoctorSignupRequest;
import za.ac.cput.dto.EmployeeInviteResponse;
import za.ac.cput.security.JwtService;
import za.ac.cput.service.impl.IAuthService;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ClinicStaffService clinicStaffService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmployeeAccessRequestService employeeAccessRequestService;

    @Value("${app.bootstrap.admin-key}")
    private String bootstrapAdminKey;

    @Autowired
    public AuthService(
            PatientService patientService,
            DoctorService doctorService,
            ClinicStaffService clinicStaffService,
            VerificationTokenService verificationTokenService,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            EmployeeAccessRequestService employeeAccessRequestService) {

        this.patientService = patientService;
        this.doctorService = doctorService;
        this.clinicStaffService = clinicStaffService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.employeeAccessRequestService = employeeAccessRequestService;
    }

    // ============================================================
    // PATIENT SIGNUP
    // ============================================================

    @Override
    public Patient signUp(Patient patient) {

        if (patient == null) {
            return null;
        }

        Patient inactivePatient =
                new Patient.Builder()
                        .copy(patient)
                        .setPassword(
                                passwordEncoder.encode(
                                        patient.getPassword()
                                )
                        )
                        .setAccountStatus(UserStatus.INACTIVE)
                        .build();

        Patient savedPatient =
                patientService.create(inactivePatient);

        if (savedPatient == null) {
            return null;
        }

        VerificationToken token =
                verificationTokenService.generateAndSaveToken(
                        savedPatient.getUserId(),
                        savedPatient.getUserType()
                );

        emailService.sendVerificationEmail(
                savedPatient.getEmail(),
                savedPatient.getName().getFirstName(),
                token.getToken()
        );

        return savedPatient;
    }

    // ============================================================
    // PATIENT ACCOUNT VERIFICATION
    // ============================================================

    @Override
    public boolean verifyAccount(String token) {

        Optional<VerificationToken> verificationToken =
                verificationTokenService.findByToken(token);

        if (verificationToken.isEmpty()) {
            return false;
        }

        if (verificationToken.get().isExpired()) {
            return false;
        }

        if (verificationToken.get().getUserType()
                != UserType.PATIENT) {
            return false;
        }

        Patient patient =
                patientService.read(
                        verificationToken.get().getUserId()
                );

        if (patient == null) {
            return false;
        }

        Patient activatedPatient =
                new Patient.Builder()
                        .copy(patient)
                        .setAccountStatus(UserStatus.ACTIVE)
                        .build();

        patientService.update(activatedPatient);

        verificationTokenService.delete(
                verificationToken.get().getTokenId()
        );

        return true;
    }

    // ============================================================
    // LOGIN
    // ============================================================

    @Override
    public AuthResponse logIn(
            String email,
            String rawPassword) {

        // --------------------------------------------------------
        // PATIENT
        // --------------------------------------------------------

        Optional<Patient> patientOpt =
                patientService.findByEmail(email);

        if (patientOpt.isPresent()) {

            Patient patient = patientOpt.get();

            if (patient.getAccountStatus()
                    != UserStatus.ACTIVE) {
                return null;
            }

            if (!passwordEncoder.matches(
                    rawPassword,
                    patient.getPassword())) {
                return null;
            }

            String accessToken =
                    jwtService.generateToken(
                            patient.getUserId(),
                            patient.getUserType().name()
                    );

            RefreshToken refreshToken =
                    refreshTokenService.create(
                            patient.getUserId(),
                            patient.getUserType()
                    );

            return new AuthResponse(
                    accessToken,
                    refreshToken.getToken()
            );
        }

        // --------------------------------------------------------
        // DOCTOR
        // --------------------------------------------------------

        Optional<Doctor> doctorOpt =
                doctorService.findByEmail(email);

        if (doctorOpt.isPresent()) {

            Doctor doctor = doctorOpt.get();

            if (doctor.getAccountStatus()
                    != UserStatus.ACTIVE) {
                return null;
            }

            if (!passwordEncoder.matches(
                    rawPassword,
                    doctor.getPassword())) {
                return null;
            }

            String accessToken =
                    jwtService.generateToken(
                            doctor.getUserId(),
                            doctor.getUserType().name()
                    );

            RefreshToken refreshToken =
                    refreshTokenService.create(
                            doctor.getUserId(),
                            doctor.getUserType()
                    );

            return new AuthResponse(
                    accessToken,
                    refreshToken.getToken()
            );
        }

        // --------------------------------------------------------
        // CLINIC STAFF
        // --------------------------------------------------------

        Optional<ClinicStaff> staffOpt =
                clinicStaffService.findByEmail(email);

        if (staffOpt.isPresent()) {

            ClinicStaff staff = staffOpt.get();

            if (staff.getAccountStatus()
                    != UserStatus.ACTIVE) {
                return null;
            }

            if (!passwordEncoder.matches(
                    rawPassword,
                    staff.getPassword())) {
                return null;
            }

            String accessToken =
                    jwtService.generateToken(
                            staff.getUserId(),
                            staff.getUserType().name(),
                            staff.getStaffRole().name()
                    );

            RefreshToken refreshToken =
                    refreshTokenService.create(
                            staff.getUserId(),
                            staff.getUserType()
                    );

            return new AuthResponse(
                    accessToken,
                    refreshToken.getToken()
            );
        }

        return null;
    }

    // ============================================================
    // FIRST ADMIN BOOTSTRAP
    // ============================================================

    /**
     * Creates the FIRST ClinicStaff ADMIN account.
     *
     * Security rules:
     *
     * 1. Bootstrap key must match the server-side configured key.
     * 2. No ADMIN may already exist.
     * 3. User must not already exist under any account type.
     * 4. Password is always BCrypt encoded here.
     * 5. Account is immediately ACTIVE.
     * 6. StaffRole is forced to ADMIN.
     * 7. UserType is determined by the ClinicStaff entity.
     */
    public ClinicStaff bootstrapFirstAdmin(
            String suppliedBootstrapKey,
            String firstName,
            String middleName,
            String lastName,
            String email,
            String cellPhone,
            String rawPassword,
            LocalDate dob,
            String department) {

        // --------------------------------------------------------
        // 1. Validate bootstrap key
        // --------------------------------------------------------

        if (suppliedBootstrapKey == null
                || bootstrapAdminKey == null
                || !bootstrapAdminKey.equals(
                suppliedBootstrapKey)) {

            return null;
        }

        // --------------------------------------------------------
        // 2. FIRST ADMIN CHECK
        // --------------------------------------------------------
        //
        // We use getAll() because your ClinicStaffService already
        // exposes getAll() and the existing controller uses it.
        //
        // If ANY ADMIN exists, bootstrap is permanently closed.
        // --------------------------------------------------------

        boolean adminAlreadyExists =
                clinicStaffService.getAll()
                        .stream()
                        .anyMatch(
                                staff ->
                                        staff.getStaffRole()
                                                == StaffRole.ADMIN
                        );

        if (adminAlreadyExists) {
            return null;
        }

        // --------------------------------------------------------
        // 3. Basic validation
        // --------------------------------------------------------

        if (email == null
                || email.isBlank()
                || rawPassword == null
                || rawPassword.isBlank()
                || firstName == null
                || firstName.isBlank()
                || lastName == null
                || lastName.isBlank()) {

            return null;
        }

        // --------------------------------------------------------
        // 4. Make sure email does not already belong to anyone
        // --------------------------------------------------------

        if (patientService.findByEmail(email).isPresent()) {
            return null;
        }

        if (doctorService.findByEmail(email).isPresent()) {
            return null;
        }

        if (clinicStaffService.findByEmail(email).isPresent()) {
            return null;
        }

        // --------------------------------------------------------
        // 5. Build Name
        // --------------------------------------------------------

        Name name =
                new Name.Builder()
                        .setFirstName(firstName)
                        .setMiddleName(middleName)
                        .setLastName(lastName)
                        .build();

        // --------------------------------------------------------
        // 6. Create FIRST ADMIN
        // --------------------------------------------------------

        ClinicStaff admin =
                new ClinicStaff.Builder()
                        .setName(name)
                        .setEmail(email)
                        .setCellPhone(cellPhone)
                        .setPassword(
                                passwordEncoder.encode(
                                        rawPassword
                                )
                        )
                        .setDob(dob)
                        .setStaffRole(StaffRole.ADMIN)
                        .setDepartment(department)
                        .setAccountStatus(UserStatus.ACTIVE)
                        .build();

        // --------------------------------------------------------
        // 7. Persist
        // --------------------------------------------------------

        return clinicStaffService.create(admin);
    }

    // ============================================================
    // RESEND VERIFICATION
    // ============================================================

    @Override
    public boolean resendVerification(String email) {

        Optional<Patient> patientOpt =
                patientService.findByEmail(email);

        if (patientOpt.isEmpty()) {
            return false;
        }

        Patient patient = patientOpt.get();

        if (patient.getAccountStatus()
                == UserStatus.ACTIVE) {
            return false;
        }

        VerificationToken token =
                verificationTokenService.generateAndSaveToken(
                        patient.getUserId(),
                        patient.getUserType()
                );

        emailService.sendVerificationEmail(
                patient.getEmail(),
                patient.getName().getFirstName(),
                token.getToken()
        );

        return true;
    }

    // ============================================================
    // CHANGE PASSWORD
    // ============================================================

    @Override
    public boolean changePassword(
            String email,
            String oldPassword,
            String newPassword) {

        Optional<Patient> patientOpt =
                patientService.findByEmail(email);

        if (patientOpt.isPresent()) {

            Patient patient = patientOpt.get();

            if (!passwordEncoder.matches(
                    oldPassword,
                    patient.getPassword())) {
                return false;
            }

            Patient updated =
                    new Patient.Builder()
                            .copy(patient)
                            .setPassword(
                                    passwordEncoder.encode(
                                            newPassword
                                    )
                            )
                            .build();

            patientService.update(updated);

            return true;
        }

        Optional<Doctor> doctorOpt =
                doctorService.findByEmail(email);

        if (doctorOpt.isPresent()) {

            Doctor doctor = doctorOpt.get();

            if (!passwordEncoder.matches(
                    oldPassword,
                    doctor.getPassword())) {
                return false;
            }

            Doctor updated =
                    new Doctor.Builder()
                            .copy(doctor)
                            .setPassword(
                                    passwordEncoder.encode(
                                            newPassword
                                    )
                            )
                            .build();

            doctorService.update(updated);

            return true;
        }

        Optional<ClinicStaff> staffOpt =
                clinicStaffService.findByEmail(email);

        if (staffOpt.isPresent()) {

            ClinicStaff staff = staffOpt.get();

            if (!passwordEncoder.matches(
                    oldPassword,
                    staff.getPassword())) {
                return false;
            }

            ClinicStaff updated =
                    new ClinicStaff.Builder()
                            .copy(staff)
                            .setPassword(
                                    passwordEncoder.encode(
                                            newPassword
                                    )
                            )
                            .build();

            clinicStaffService.update(updated);

            return true;
        }

        return false;
    }

    // ============================================================
    // EMPLOYEE INVITE
    // ============================================================

    private void sendInvite(
            String email,
            UserType userType,
            StaffRole staffRole) {

        String inviteToken =
                jwtService.generateInviteToken(
                        email,
                        userType,
                        staffRole
                );

        emailService.sendEmployeeInviteEmail(
                email,
                userType,
                inviteToken
        );
    }

    @Override
    public boolean inviteEmployee(
            String email,
            UserType userType,
            StaffRole staffRole) {

        if (userType != UserType.DOCTOR
                && userType != UserType.CLINIC_STAFF) {
            return false;
        }

        if (userType == UserType.DOCTOR
                && staffRole != null) {
            return false;
        }

        if (userType == UserType.CLINIC_STAFF
                && staffRole == null) {
            return false;
        }

        if (patientService.findByEmail(email).isPresent()
                || doctorService.findByEmail(email).isPresent()
                || clinicStaffService.findByEmail(email).isPresent()) {
            return false;
        }

        sendInvite(
                email,
                userType,
                staffRole
        );

        return true;
    }

    // ============================================================
    // VERIFY EMPLOYEE INVITE
    // ============================================================

    @Override
    public EmployeeInviteResponse verifyEmployeeInvite(
            String token) {

        return jwtService.parseInviteToken(token);
    }

    // ============================================================
    // DOCTOR SIGNUP
    // ============================================================

    @Override
    public Doctor signUpDoctor(
            DoctorSignupRequest request) {

        EmployeeInviteResponse invite =
                jwtService.parseInviteToken(
                        request.getToken()
                );

        if (invite == null
                || invite.getUserType()
                != UserType.DOCTOR) {
            return null;
        }

        if (doctorService.findByEmail(
                invite.getEmail()).isPresent()) {
            return null;
        }

        Name name =
                new Name.Builder()
                        .setFirstName(request.getFirstName())
                        .setMiddleName(request.getMiddleName())
                        .setLastName(request.getLastName())
                        .build();

        Doctor doctor =
                new Doctor.Builder()
                        .setName(name)
                        .setEmail(invite.getEmail())
                        .setCellPhone(request.getCellPhone())
                        .setPassword(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .setDob(request.getDob())
                        .setSpecialty(request.getSpecialty())
                        .setLicenseNumber(
                                request.getLicenseNumber()
                        )
                        .setAccountStatus(UserStatus.ACTIVE)
                        .build();

        return doctorService.create(doctor);
    }

    // ============================================================
    // CLINIC STAFF SIGNUP
    // ============================================================

    @Override
    public ClinicStaff signUpClinicStaff(
            ClinicStaffSignupRequest request) {

        EmployeeInviteResponse invite =
                jwtService.parseInviteToken(
                        request.getToken()
                );

        if (invite == null
                || invite.getUserType()
                != UserType.CLINIC_STAFF) {
            return null;
        }

        if (invite.getStaffRole() == null) {
            return null;
        }

        if (clinicStaffService.findByEmail(
                invite.getEmail()).isPresent()) {
            return null;
        }

        Name name =
                new Name.Builder()
                        .setFirstName(request.getFirstName())
                        .setMiddleName(request.getMiddleName())
                        .setLastName(request.getLastName())
                        .build();

        ClinicStaff clinicStaff =
                new ClinicStaff.Builder()
                        .setName(name)
                        .setEmail(invite.getEmail())
                        .setCellPhone(request.getCellPhone())
                        .setPassword(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .setDob(request.getDob())
                        .setStaffRole(
                                invite.getStaffRole()
                        )
                        .setDepartment(
                                request.getDepartment()
                        )
                        .setAccountStatus(
                                UserStatus.ACTIVE
                        )
                        .build();

        return clinicStaffService.create(clinicStaff);
    }

    // ============================================================
    // SELF-SERVICE ACCESS REQUEST
    // ============================================================

    @Override
    public boolean requestEmployeeAccess(
            String email,
            UserType userType,
            StaffRole staffRole) {

        if (userType != UserType.DOCTOR
                && userType != UserType.CLINIC_STAFF) {
            return false;
        }

        // ADMIN can NEVER be requested through this endpoint.
        if (staffRole == StaffRole.ADMIN) {
            return false;
        }

        if (userType == UserType.DOCTOR
                && staffRole != null) {
            return false;
        }

        if (userType == UserType.CLINIC_STAFF
                && staffRole != StaffRole.NURSE) {
            return false;
        }

        if (patientService.findByEmail(email).isPresent()
                || doctorService.findByEmail(email).isPresent()
                || clinicStaffService.findByEmail(email).isPresent()) {
            return false;
        }

        if (employeeAccessRequestService
                .findByEmailAndStatus(
                        email,
                        RequestStatus.PENDING
                )
                .isPresent()) {
            return false;
        }

        EmployeeAccessRequest accessRequest =
                new EmployeeAccessRequest.Builder()
                        .setEmail(email)
                        .setRequestedUserType(userType)
                        .setRequestedStaffRole(staffRole)
                        .setStatus(RequestStatus.PENDING)
                        .setRequestDate(
                                LocalDateTime.now()
                        )
                        .build();

        return employeeAccessRequestService
                .create(accessRequest) != null;
    }

    // ============================================================
    // APPROVE ACCESS REQUEST
    // ============================================================

    @Override
    public boolean approveAccessRequest(
            int requestId,
            int adminUserId) {

        EmployeeAccessRequest existing =
                employeeAccessRequestService.read(
                        requestId
                );

        if (existing == null
                || existing.getStatus()
                != RequestStatus.PENDING) {
            return false;
        }

        ClinicStaff admin =
                clinicStaffService.read(adminUserId);

        if (admin == null
                || admin.getStaffRole()
                != StaffRole.ADMIN) {
            return false;
        }

        EmployeeAccessRequest approved =
                new EmployeeAccessRequest.Builder()
                        .copy(existing)
                        .setStatus(RequestStatus.APPROVED)
                        .setProcessedDate(
                                LocalDateTime.now()
                        )
                        .setProcessedBy(admin)
                        .build();

        employeeAccessRequestService.update(
                approved
        );

        sendInvite(
                existing.getEmail(),
                existing.getRequestedUserType(),
                existing.getRequestedStaffRole()
        );

        return true;
    }

    // ============================================================
    // REJECT ACCESS REQUEST
    // ============================================================

    @Override
    public boolean rejectAccessRequest(
            int requestId,
            int adminUserId,
            String adminNotes) {

        EmployeeAccessRequest existing =
                employeeAccessRequestService.read(
                        requestId
                );

        if (existing == null
                || existing.getStatus()
                != RequestStatus.PENDING) {
            return false;
        }

        ClinicStaff admin =
                clinicStaffService.read(adminUserId);

        if (admin == null
                || admin.getStaffRole()
                != StaffRole.ADMIN) {
            return false;
        }

        EmployeeAccessRequest rejected =
                new EmployeeAccessRequest.Builder()
                        .copy(existing)
                        .setStatus(RequestStatus.REJECTED)
                        .setProcessedDate(
                                LocalDateTime.now()
                        )
                        .setProcessedBy(admin)
                        .setAdminNotes(adminNotes)
                        .build();

        employeeAccessRequestService.update(
                rejected
        );

        return true;
    }
}