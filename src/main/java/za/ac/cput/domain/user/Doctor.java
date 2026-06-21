/*
 Doctor.java

 Doctor POJO class

 Author: Jaden Clayton Abrahams (222206721)

 Date: 21st June 2026
*/

package za.ac.cput.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

@Entity
@Table(name = "doctor")
public class Doctor extends User {

    private String specialty;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    protected Doctor() {
        // Required by JPA
    }

    private Doctor(Builder builder) {
        super(builder);
        this.specialty = builder.specialty;
        this.licenseNumber = builder.licenseNumber;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "userId=" + getUserId() +
                ", specialty='" + specialty + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends User.Builder {
        private String specialty;
        private String licenseNumber;

        public Builder setSpecialty(String specialty) {
            this.specialty = specialty;
            return this;
        }

        public Builder setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        @Override
        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        @Override
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public Builder setName(Name name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
            return this;
        }

        @Override
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Builder setDob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        @Override
        public Builder setAccountStatus(UserStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        public Builder copy(Doctor doctor) {
            super.copy(doctor);
            this.specialty = doctor.specialty;
            this.licenseNumber = doctor.licenseNumber;
            return this;
        }

        @Override
        public Doctor build() {
            return new Doctor(this);
        }
    }
}
