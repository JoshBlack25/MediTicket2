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
}