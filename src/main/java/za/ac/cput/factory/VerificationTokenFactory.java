package za.ac.cput.factory;

import za.ac.cput.domain.VerificationToken;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationTokenFactory {

    public static VerificationToken createVerificationToken(int userId,
                                                            UserType userType) {

        if (!Helper.isValidInt(userId)) return null;
        if (!Helper.isValidObject(userType)) return null;

        return new VerificationToken.Builder()
                .setUserId(userId)
                .setUserType(userType)
                .setToken(UUID.randomUUID().toString())
                .setExpiryDate(LocalDateTime.now().plusHours(24))
                .build();
    }
}