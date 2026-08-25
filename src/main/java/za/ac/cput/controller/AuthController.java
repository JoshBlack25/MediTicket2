package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.dto.*;
import za.ac.cput.security.JwtService;
import za.ac.cput.service.AuthService;
import za.ac.cput.service.EmployeeAccessRequestService;
import za.ac.cput.service.PasswordResetService;
import za.ac.cput.service.RefreshTokenService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final EmployeeAccessRequestService employeeAccessRequestService;
    private final PasswordResetService passwordResetService;

    public AuthController(
            AuthService authService,
            RefreshTokenService refreshTokenService,
            JwtService jwtService,
            EmployeeAccessRequestService employeeAccessRequestService,
            PasswordResetService passwordResetService) {

        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.employeeAccessRequestService = employeeAccessRequestService;
        this.passwordResetService = passwordResetService;
    }

    // ============================================================
    // PATIENT SIGNUP
    // ============================================================

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody PatientSignupRequest request) {

        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        Patient patient = new Patient.Builder()
                .setName(name)
                .setEmail(request.getEmail())
                .setCellPhone(request.getCellPhone())
                .setPassword(request.getPassword())
                .setDob(request.getDob())
                .setDateRegistered(request.getDateRegistered())
                .setEmergencyContact(request.getEmergencyContact())
                .setAccountStatus(UserStatus.INACTIVE)
                .build();

        Patient createdPatient = authService.signUp(patient);

        if (createdPatient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Signup failed. Please check your details and try again.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Signup successful. Please check your email to verify your account.");
    }

    // ============================================================
    // ACCOUNT VERIFICATION
    // ============================================================

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {

        boolean verified = authService.verifyAccount(token);

        if (!verified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired verification link.");
        }

        return ResponseEntity.ok(
                "Account verified successfully. You can now log in."
        );
    }

    // ============================================================
    // LOGIN
    // ============================================================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        AuthResponse authResponse =
                authService.logIn(
                        request.getEmail(),
                        request.getPassword()
                );

        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password, or account not yet verified.");
        }

        return ResponseEntity.ok(authResponse);
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequest request) {

        String oldToken = request.getRefreshToken();

        Optional<RefreshToken> found =
                refreshTokenService.findByToken(oldToken);

        if (found.isEmpty()
                || !refreshTokenService.isValid(oldToken)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token expired or invalid, please log in again");
        }

        RefreshToken refreshToken = found.get();

        String newAccessToken =
                jwtService.generateToken(
                        refreshToken.getUserId(),
                        refreshToken.getUserType().name()
                );

        return ResponseEntity.ok(
                new AuthResponse(
                        newAccessToken,
                        oldToken
                )
        );
    }

    // ============================================================
    // RESEND PATIENT VERIFICATION
    // ============================================================

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(
            @RequestParam String email) {

        boolean sent = authService.resendVerification(email);

        if (!sent) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to resend verification email. " +
                                    "Account may not exist or is already verified."
                    );
        }

        return ResponseEntity.ok(
                "Verification email resent. Please check your inbox."
        );
    }

    // ============================================================
    // CHANGE PASSWORD
    // ============================================================

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request) {

        boolean changed = authService.changePassword(
                request.getEmail(),
                request.getOldPassword(),
                request.getNewPassword()
        );

        if (!changed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to change password. " +
                                    "Check that your email and current password are correct."
                    );
        }

        return ResponseEntity.ok(
                "Password updated successfully."
        );
    }



    // ============================================================
    // FIRST ADMIN BOOTSTRAP
    // ============================================================
    //
    // This endpoint exists ONLY to create the first ADMIN account.
    //
    // It is protected by:
    //
    // 1. X-Bootstrap-Key header
    // 2. Database must contain ZERO ADMIN accounts
    //
    // Once the first ADMIN exists, this endpoint permanently refuses
    // to create another ADMIN.
    //
    // ============================================================

    @PostMapping("/bootstrap/admin")
    public ResponseEntity<?> bootstrapFirstAdmin(
            @RequestHeader(value = "X-Bootstrap-Key", required = false)
            String bootstrapKey,
            @RequestBody FirstAdminBootstrapRequest request) {

        ClinicStaff admin = authService.bootstrapFirstAdmin(
                bootstrapKey,
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getCellPhone(),
                request.getPassword(),
                request.getDob(),
                request.getDepartment()
        );

        if (admin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            "First-admin bootstrap failed. " +
                                    "The bootstrap key may be invalid, or an ADMIN account already exists."
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        "First ADMIN account created successfully. " +
                                "You can now log in."
                );
    }

    // ============================================================
    // EMPLOYEE INVITE
    // ============================================================

    @PostMapping("/employee/invite")
    public ResponseEntity<?> inviteEmployee(
            @RequestBody EmployeeInviteRequest request) {

        boolean sent = authService.inviteEmployee(
                request.getEmail(),
                request.getUserType(),
                request.getStaffRole()
        );

        if (!sent) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to send invite. Email may already be " +
                                    "registered, the role is invalid, or a staff " +
                                    "role is missing/unexpected for the chosen user type."
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        "Invitation sent. The employee should check " +
                                "their email to complete signup."
                );
    }

    // ============================================================
    // VERIFY EMPLOYEE INVITE
    // ============================================================

    @GetMapping("/employee/invite/verify")
    public ResponseEntity<?> verifyEmployeeInvite(
            @RequestParam String token) {

        EmployeeInviteResponse invite =
                authService.verifyEmployeeInvite(token);

        if (invite == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired invitation link.");
        }

        return ResponseEntity.ok(invite);
    }

    // ============================================================
    // DOCTOR SIGNUP
    // ============================================================

    @PostMapping("/employee/signup/doctor")
    public ResponseEntity<?> signupDoctor(
            @RequestBody DoctorSignupRequest request) {

        Doctor created =
                authService.signUpDoctor(request);

        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Signup failed. Invitation may be invalid, " +
                                    "expired, or already used."
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        "Doctor account created successfully. " +
                                "You can now log in."
                );
    }

    // ============================================================
    // CLINIC STAFF SIGNUP
    // ============================================================

    @PostMapping("/employee/signup/clinicstaff")
    public ResponseEntity<?> signupClinicStaff(
            @RequestBody ClinicStaffSignupRequest request) {

        ClinicStaff created =
                authService.signUpClinicStaff(request);

        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Signup failed. Invitation may be invalid, " +
                                    "expired, or already used."
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        "Clinic staff account created successfully. " +
                                "You can now log in."
                );
    }

    // ============================================================
    // EMPLOYEE ACCESS REQUEST
    // ============================================================

    @PostMapping("/employee/request-access")
    public ResponseEntity<?> requestAccess(
            @RequestBody EmployeeAccessRequestSubmission request) {

        boolean submitted =
                authService.requestEmployeeAccess(
                        request.getEmail(),
                        request.getUserType(),
                        request.getStaffRole()
                );

        if (!submitted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to submit request. Email may already " +
                                    "be registered, a request may already be pending, " +
                                    "or the role/staffRole combination is invalid " +
                                    "(self-service requests cannot request ADMIN)."
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        "Request submitted. An administrator will review it shortly."
                );
    }

    // ============================================================
    // FORGOT PASSWORD
    // ============================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.getEmail());

        // Deliberately identical response regardless of whether the email
        // exists — prevents account enumeration via this endpoint.
        return ResponseEntity.ok(
                "If an account exists for this email, a password reset code has been sent."
        );
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody VerifyResetCodeRequest request) {
        String sessionToken = passwordResetService.verifyCode(request.getEmail(), request.getCode());

        if (sessionToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired code.");
        }

        return ResponseEntity.ok(sessionToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean reset = passwordResetService.resetPassword(
                request.getEmail(),
                request.getResetSessionToken(),
                request.getNewPassword()
        );

        if (!reset) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unable to reset password. The session may have expired — please request a new code.");
        }

        return ResponseEntity.ok("Password reset successfully. You can now log in.");
    }

    // ============================================================
    // ADMIN - LIST ACCESS REQUESTS
    // ============================================================

    @GetMapping("/employee/access-requests")
    public ResponseEntity<?> listAccessRequests(
            @RequestParam(
                    required = false,
                    defaultValue = "PENDING"
            )
            RequestStatus status) {

        List<EmployeeAccessRequest> requests =
                employeeAccessRequestService.findByStatus(status);

        return ResponseEntity.ok(requests);
    }

    // ============================================================
    // ADMIN - APPROVE ACCESS REQUEST
    // ============================================================

    @PostMapping("/employee/access-requests/{id}/approve")
    public ResponseEntity<?> approveAccessRequest(
            @PathVariable int id,
            Authentication authentication) {

        int adminUserId =
                (Integer) authentication.getPrincipal();

        boolean approved =
                authService.approveAccessRequest(
                        id,
                        adminUserId
                );

        if (!approved) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to approve. Request may not exist, " +
                                    "may already be processed, or the acting user " +
                                    "is not a valid admin."
                    );
        }

        return ResponseEntity.ok(
                "Request approved. An invitation email has been sent."
        );
    }

    // ============================================================
    // ADMIN - REJECT ACCESS REQUEST
    // ============================================================

    @PostMapping("/employee/access-requests/{id}/reject")
    public ResponseEntity<?> rejectAccessRequest(
            @PathVariable int id,
            @RequestParam(required = false) String adminNotes,
            Authentication authentication) {

        int adminUserId =
                (Integer) authentication.getPrincipal();

        boolean rejected =
                authService.rejectAccessRequest(
                        id,
                        adminUserId,
                        adminNotes
                );

        if (!rejected) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Unable to reject. Request may not exist, " +
                                    "may already be processed, or the acting user " +
                                    "is not a valid admin."
                    );
        }

        return ResponseEntity.ok(
                "Request rejected."
        );
    }
}