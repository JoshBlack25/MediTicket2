package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface EmployeeAccessRequestRepository extends JpaRepository<EmployeeAccessRequest, Integer> {

    Optional<EmployeeAccessRequest> findByEmailAndStatus(String email, RequestStatus status);

    List<EmployeeAccessRequest> findByStatus(RequestStatus status);
}