package za.ac.cput.dto;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;

public class EmployeeInviteResponse {

    private String email;
    private UserType userType;
    private StaffRole staffRole; // null for DOCTOR invites

    // Required no-arg constructor for Jackson deserialization
    public EmployeeInviteResponse() {}

    public EmployeeInviteResponse(String email, UserType userType, StaffRole staffRole) {
        this.email = email;
        this.userType = userType;
        this.staffRole = staffRole;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public StaffRole getStaffRole() { return staffRole; }
    public void setStaffRole(StaffRole staffRole) { this.staffRole = staffRole; }
}