package za.ac.cput.factory;

import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class RefreshTokenFactory {

    private RefreshTokenFactory() {}

    public static RefreshToken createRefreshToken(int id,
                                                  String token,
                                                  int userId,
                                                  UserType userType,
                                                  LocalDateTime expiryDate,
                                                  boolean revoked) {

        if (Helper.isNullOrEmpty(token)) {
            return null;
        }

        if (userId <= 0) {
            return null;
        }

        if (!Helper.isValidObject(userType)) {
            return null;
        }

        if (!Helper.isValidObject(expiryDate)) {
            return null;
        }

        if (expiryDate.isBefore(LocalDateTime.now())) {
            return null;
        }

        return new RefreshToken.Builder()
                .setId(id)
                .setToken(token)
                .setUserId(userId)
                .setUserType(userType)
                .setExpiryDate(expiryDate)
                .setRevoked(revoked)
                .build();
    }

    // Convenience overload for creating a fresh (non-revoked) token
    public static RefreshToken createRefreshToken(String token, int userId, UserType userType, LocalDateTime expiryDate) {
        return createRefreshToken(0, token, userId, userType, expiryDate, false);
    }
}