package za.ac.cput.factory;

import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class UserFactory {

    // Shared validation method for all users
    protected static boolean isValidUserBase(int userId,
                                             Name name,
                                             String email,
                                             String cellPhone,
                                             String password,
                                             LocalDate dob,
                                             UserStatus status) {

        if (!Helper.isValidInt(userId)) return false;
        if (!Helper.isValidObject(name)) return false;
        if (!Helper.isValidEmail(email)) return false;
        if (!Helper.isValidMobileNumber(cellPhone)) return false;
        if (Helper.isNullOrEmpty(password)) return false;
        if (!Helper.isValidObject(dob)) return false;
        if (!Helper.isValidObject(status)) return false;

        return true;
    }
}