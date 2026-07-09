package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.PatientTicket;

@Repository
public interface PatientTicketRepository extends JpaRepository<PatientTicket, Integer> {
}
