package za.ac.cput.dto;

import java.time.LocalDate;

public class ClinicStaffSignupRequest {

    private String token;
    private String firstName;
    private String middleName;
    private String lastName;
    private String cellPhone;
    private String password;
    private LocalDate dob;
    private String department;
    // staffRole intentionally NOT here — it's decided at invite time by an
    // ADMIN and pulled from the signed invite token, not chosen by the invitee.

    // Required no-arg constructor for Jackson deserialization
    public ClinicStaffSignupRequest() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCellPhone() { return cellPhone; }
    public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}