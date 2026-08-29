package za.ac.cput.dto;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;

public class EmployeeInviteRequest {

    private String email;
    private UserType userType;

    // Only meaningful when userType == CLINIC_STAFF. Must be null for DOCTOR invites.
    private StaffRole staffRole;

    // Required no-arg constructor for Jackson deserialization
    public EmployeeInviteRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public StaffRole getStaffRole() { return staffRole; }
    public void setStaffRole(StaffRole staffRole) { this.staffRole = staffRole; }
}