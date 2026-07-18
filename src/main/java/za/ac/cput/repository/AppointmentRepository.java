package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByDoctor_UserId(int doctorId);

    List<Appointment> findByStaff_UserId(int staffId);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByConfirmationStatus(ConfirmationStatus confirmationStatus);

    List<Appointment> findByDoctor_UserIdAndAppointmentDate(int doctorId, LocalDate appointmentDate);
}