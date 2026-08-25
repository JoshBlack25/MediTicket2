package za.ac.cput.service.impl;

import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.UserType;

import java.util.Optional;

public interface IRefreshTokenService {

    RefreshToken create(int userId, UserType userType);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken rotate(String oldToken);

    boolean isValid(String token);

    void revoke(String token);

    void revokeAllForUser(int userId, UserType userType);
}