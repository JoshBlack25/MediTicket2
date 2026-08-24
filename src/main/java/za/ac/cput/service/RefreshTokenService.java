package za.ac.cput.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.auth.RefreshToken;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.factory.RefreshTokenFactory;
import za.ac.cput.repository.RefreshTokenRepository;
import za.ac.cput.service.impl.IRefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    private static final long EXPIRY_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken create(int userId, UserType userType) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusDays(EXPIRY_DAYS);

        RefreshToken refreshToken = RefreshTokenFactory.createRefreshToken(token, userId, userType, expiry);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken rotate(String oldToken) {
        RefreshToken existing = refreshTokenRepository.findByToken(oldToken).orElse(null);

        if (existing == null) {
            return null;
        }

        refreshTokenRepository.deleteByToken(oldToken);

        return create(existing.getUserId(), existing.getUserType());
    }

    @Override
    public boolean isValid(String token) {
        Optional<RefreshToken> found = refreshTokenRepository.findByToken(token);

        if (found.isEmpty()) {
            return false;
        }

        RefreshToken refreshToken = found.get();

        return !refreshToken.isRevoked() && !refreshToken.isExpired();
    }

    @Override
    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void revokeAllForUser(int userId, UserType userType) {
        refreshTokenRepository.deleteByUserIdAndUserType(userId, userType);
    }
}