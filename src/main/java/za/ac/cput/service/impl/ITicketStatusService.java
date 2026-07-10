/*
 ITicketStatusService.java

 Service interface for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 10th July 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;

import java.util.List;

public interface ITicketStatusService extends IService<TicketStatus, Integer> {

    List<TicketStatus> findByStatusType(StatusType statusType);

}