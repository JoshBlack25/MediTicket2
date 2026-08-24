package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.PasswordResetRequest;

import java.util.List;

public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Integer> {

    // Most recent request for an email, regardless of state — used both at
    // verify-code time (to find the pending code) and reset-password time
    // (to find the verified session). Ordering + taking first in the service
    // layer since "latest" isn't expressible as a single derived-query method
    // without a Pageable.
    List<PasswordResetRequest> findByEmailOrderByCreatedDateDesc(String email);
}