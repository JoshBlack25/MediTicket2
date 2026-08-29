package za.ac.cput.service.impl;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.dto.AuthResponse;
import za.ac.cput.dto.ClinicStaffSignupRequest;
import za.ac.cput.dto.DoctorSignupRequest;
import za.ac.cput.dto.EmployeeInviteResponse;

public interface IAuthService {

    Patient signUp(Patient patient);

    boolean verifyAccount(String token);

    AuthResponse logIn(String email, String rawPassword);

    boolean resendVerification(String email);

    boolean changePassword(String email, String oldPassword, String newPassword);

    boolean inviteEmployee(String email, UserType userType, StaffRole staffRole);

    EmployeeInviteResponse verifyEmployeeInvite(String token);

    Doctor signUpDoctor(DoctorSignupRequest request);

    ClinicStaff signUpClinicStaff(ClinicStaffSignupRequest request);

    // NEW — self-service request, gated by admin approval
    boolean requestEmployeeAccess(String email, UserType userType, StaffRole staffRole);

    boolean approveAccessRequest(int requestId, int adminUserId);

    boolean rejectAccessRequest(int requestId, int adminUserId, String adminNotes);
}