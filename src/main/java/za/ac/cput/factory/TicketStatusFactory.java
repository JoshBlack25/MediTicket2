package za.ac.cput.factory;

import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class TicketStatusFactory {

    public static TicketStatus createTicketStatus(int statusId,
                                                  StatusType statusType,
                                                  LocalDateTime statusDate,
                                                  PatientTicket ticket) {

        if (!Helper.isValidInt(statusId)) return null;
        if (!Helper.isValidObject(statusType)) return null;
        if (!Helper.isValidObject(statusDate)) return null;
        if (!Helper.isValidObject(ticket)) return null;

        // business rule: status date cannot be in the future
        if (statusDate.isAfter(LocalDateTime.now())) return null;

        return new TicketStatus.Builder()
                .setStatusId(statusId)
                .setStatusType(statusType)
                .setStatusDate(statusDate)
                .setTicket(ticket)
                .build();
    }
}