/*
 ClinicStaff.java

 ClinicStaff POJO class

 Author: Matthew Barron (230398863)

 Date: 21st June 2026
*/

package za.ac.cput.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

@Entity
@Table(name = "clinic_staff")
public class ClinicStaff extends User {

    private String staffRole;
    private String department;

    protected ClinicStaff() {
        // Required by JPA
    }

    private ClinicStaff(Builder builder) {
        super(builder);
        this.staffRole = builder.staffRole;
        this.department = builder.department;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "ClinicStaff{" +
                "userId=" + getUserId() +
                ", staffRole='" + staffRole + '\'' +
                ", department='" + department + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends User.Builder {
        private String staffRole;
        private String department;

        public Builder setStaffRole(String staffRole) {
            this.staffRole = staffRole;
            return this;
        }

        public Builder setDepartment(String department) {
            this.department = department;
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

        public Builder copy(ClinicStaff clinicStaff) {
            super.copy(clinicStaff);
            this.staffRole = clinicStaff.staffRole;
            this.department = clinicStaff.department;
            return this;
        }

        @Override
        public ClinicStaff build() {
            return new ClinicStaff(this);
        }
    }
}
