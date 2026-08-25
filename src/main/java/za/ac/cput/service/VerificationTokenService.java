package za.ac.cput.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.VerificationToken;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.repository.VerificationTokenRepository;
import za.ac.cput.service.impl.IVerificationTokenService;

import java.util.List;
import java.util.Optional;

@Service
public class VerificationTokenService implements IVerificationTokenService {

    @Autowired
    private final VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public VerificationToken create(VerificationToken verificationToken) {
        if (verificationToken == null) return null;
        return repository.save(verificationToken);
    }

    @Override
    public VerificationToken read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public VerificationToken update(VerificationToken verificationToken) {
        if (verificationToken == null) return null;
        return repository.save(verificationToken);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<VerificationToken> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    @Transactional
    public VerificationToken generateAndSaveToken(int userId, UserType userType) {
        // Enforce one active token per user — clear any existing one first
        deleteExistingToken(userId, userType);

        VerificationToken token = new VerificationToken.Builder()
                .generateFor(userId, userType)
                .build();

        return repository.save(token);
    }

    @Override
    @Transactional
    public void deleteExistingToken(int userId, UserType userType) {
        repository.deleteByUserIdAndUserType(userId, userType);
    }
}