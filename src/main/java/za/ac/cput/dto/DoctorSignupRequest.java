package za.ac.cput.dto;

import java.time.LocalDate;

public class DoctorSignupRequest {

    private String token;
    private String firstName;
    private String middleName;
    private String lastName;
    private String cellPhone;
    private String password;
    private LocalDate dob;
    private String specialty;
    private String licenseNumber;

    // Required no-arg constructor for Jackson deserialization
    public DoctorSignupRequest() {}

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

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
}