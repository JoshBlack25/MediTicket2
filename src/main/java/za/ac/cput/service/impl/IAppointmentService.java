package za.ac.cput.service.impl;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService extends IService<Appointment, Integer> {

    List<Appointment> findByDoctorUserId(int doctorId);

    List<Appointment> findByStaffUserId(int staffId);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByConfirmationStatus(ConfirmationStatus confirmationStatus);

    List<Appointment> findByDoctorUserIdAndAppointmentDate(int doctorId, LocalDate appointmentDate);

    List<Appointment> findByPatientUserId(int patientId);

    Appointment approveAppointment(int appointmentId, int doctorId, int staffId);

    Appointment rejectAppointment(int appointmentId, int staffId, String reason);

    // completeAppointment REMOVED — completion is now an automatic side
    // effect of PatientTicketService.progressStatus(RESOLVED), not a
    // manually-triggered clinic-staff action.
}