package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.StatusType;

import java.util.List;

@Repository
public interface PatientTicketRepository extends JpaRepository<PatientTicket, Integer> {
    List<PatientTicket> findByCurrentStatus(StatusType currentStatus);

    List<PatientTicket> findByPatientUserId(int patientId);

    // NEW — appointment is a @OneToOne field on PatientTicket. Used by
    // PaymentService to find the ticket tied to a payment's appointment,
    // so it can be auto-closed once the payment is confirmed PAID.
    PatientTicket findByAppointment(Appointment appointment);

}
