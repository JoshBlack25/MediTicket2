package za.ac.cput.factory;

import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class PatientFactory {

    public static Patient createPatient(int userId,
                                        Name name,
                                        String email,
                                        String cellPhone,
                                        String password,
                                        LocalDate dob,
                                        UserStatus status,
                                        int patientId,
                                        LocalDate dateRegistered,
                                        String emergencyContact) {

        if (!Helper.isValidInt(userId)) return null;
        if (!Helper.isValidObject(name)) return null;
        if (!Helper.isValidEmail(email)) return null;
        if (!Helper.isValidMobileNumber(cellPhone)) return null;
        if (Helper.isNullOrEmpty(password)) return null;
        if (!Helper.isValidObject(dob)) return null;
        if (!Helper.isValidObject(status)) return null;

        if (!Helper.isValidInt(patientId)) return null;
        if (!Helper.isValidObject(dateRegistered)) return null;
        if (Helper.isNullOrEmpty(emergencyContact)) return null;

        return new Patient.Builder()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .setCellPhone(cellPhone)
                .setPassword(password)
                .setDob(dob)
                .setAccountStatus(status)
                .setDateRegistered(dateRegistered)
                .setEmergencyContact(emergencyContact)
                .build();
    }
}