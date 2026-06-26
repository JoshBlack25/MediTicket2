package za.ac.cput.factory;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class PatientTicketFactory {

    public static PatientTicket createPatientTicket(int ticketId,
                                                    String ticketDescription,
                                                    LocalDateTime ticketCreatedDate,
                                                    Patient patient,
                                                    Appointment appointment) {

        if (!Helper.isValidInt(ticketId)) return null;
        if (Helper.isNullOrEmpty(ticketDescription)) return null;
        if (!Helper.isValidObject(ticketCreatedDate)) return null;
        if (!Helper.isValidObject(patient)) return null;
        if (!Helper.isValidObject(appointment)) return null;

        PatientTicket ticket = new PatientTicket.Builder()
                .setTicketId(ticketId)
                .setTicketDescription(ticketDescription)
                .setTicketCreatedDate(ticketCreatedDate)
                .setPatient(patient)
                .setAppointment(appointment)
                .build();

        // optional: initialize default status
        ticket.addStatus(StatusType.OPEN);

        return ticket;
    }
}