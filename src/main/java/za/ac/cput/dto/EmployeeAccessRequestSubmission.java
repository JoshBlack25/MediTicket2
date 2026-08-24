package za.ac.cput.dto;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;

public class EmployeeAccessRequestSubmission {

    private String email;
    private UserType userType;
    private StaffRole staffRole; // must be null for DOCTOR, NURSE for CLINIC_STAFF (never ADMIN)

    // Required no-arg constructor for Jackson deserialization
    public EmployeeAccessRequestSubmission() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public StaffRole getStaffRole() { return staffRole; }
    public void setStaffRole(StaffRole staffRole) { this.staffRole = staffRole; }
}