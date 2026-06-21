/*
 Patient.java

 Patient POJO class

 Author: Aidan Barends (230155639)

 Date: 21st June 2026
*/

package za.ac.cput.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class Patient extends User {

    private LocalDate dateRegistered;
    private String emergencyContact;

    protected Patient() {
        // Required by JPA
    }

    private Patient(Builder builder) {
        super(builder);
        this.dateRegistered = builder.dateRegistered;
        this.emergencyContact = builder.emergencyContact;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "userId=" + getUserId() +
                ", dateRegistered=" + dateRegistered +
                ", emergencyContact='" + emergencyContact + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends User.Builder {
        private LocalDate dateRegistered;
        private String emergencyContact;

        public Builder setDateRegistered(LocalDate dateRegistered) {
            this.dateRegistered = dateRegistered;
            return this;
        }

        public Builder setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
            return this;
        }

        @Override
        public Builder setUserId(int userId){
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

        public Builder copy(Patient patient){
            super.copy(patient);
            this.dateRegistered = patient.dateRegistered;
            this.emergencyContact = patient.emergencyContact;
            return this;
        }

        @Override
        public Patient build() {
            return new Patient(this);
        }
    }
}
