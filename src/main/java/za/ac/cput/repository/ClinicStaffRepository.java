package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.user.ClinicStaff;

import java.util.Optional;

public interface ClinicStaffRepository extends JpaRepository<ClinicStaff, Integer> {

    Optional<ClinicStaff> findByEmail(String email);

}