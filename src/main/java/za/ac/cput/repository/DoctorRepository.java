package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.user.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByEmail(String email);

}
