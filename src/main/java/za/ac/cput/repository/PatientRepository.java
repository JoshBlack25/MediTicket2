package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Patient;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository <Patient, Integer> {

    List<Patient> findByDateRegistered(LocalDate dateRegistered);


    Patient findByEmail(String email);
}

