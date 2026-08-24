package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.VerificationToken;
import za.ac.cput.domain.enums.UserType;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    Optional<VerificationToken> findByToken(String token);

    // Used to delete any existing token before issuing a new one
    Optional<VerificationToken> findByUserIdAndUserType(int userId, UserType userType);

    void deleteByUserIdAndUserType(int userId, UserType userType);
}