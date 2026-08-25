package za.ac.cput.service.impl;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.StatusType;

import java.util.List;

public interface IPatientTicketService extends IService<PatientTicket, Integer> {

    List<PatientTicket> findByCurrentStatus(StatusType status);

    List<PatientTicket> findByPatientUserId(int patientId);

    PatientTicket progressStatus(int ticketId, StatusType newStatus, String notes);

    PatientTicket findByAppointment(Appointment appointment);
}