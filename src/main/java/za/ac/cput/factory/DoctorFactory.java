package za.ac.cput.factory;

import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class DoctorFactory {

    public static Doctor createDoctor(int userId,
                                      Name name,
                                      String email,
                                      String cellPhone,
                                      String password,
                                      LocalDate dob,
                                      UserStatus status,
                                      String specialty,
                                      String licenseNumber) {

        if (!Helper.isValidInt(userId)) return null;
        if (!Helper.isValidObject(name)) return null;
        if (!Helper.isValidEmail(email)) return null;
        if (!Helper.isValidMobileNumber(cellPhone)) return null;
        if (Helper.isNullOrEmpty(password)) return null;
        if (!Helper.isValidObject(dob)) return null;
        if (!Helper.isValidObject(status)) return null;

        if (Helper.isNullOrEmpty(specialty)) return null;
        if (Helper.isNullOrEmpty(licenseNumber)) return null;

        return new Doctor.Builder()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .setCellPhone(cellPhone)
                .setPassword(password)
                .setDob(dob)
                .setAccountStatus(status)
                .setSpecialty(specialty)
                .setLicenseNumber(licenseNumber)
                .build();
    }
}
