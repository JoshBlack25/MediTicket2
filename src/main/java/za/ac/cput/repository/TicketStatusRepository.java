/*
 TicketStatusRepository.java

 Repository interface for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 5th July 2026
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;

import java.util.List;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Integer> {

    List<TicketStatus> findByStatusType(StatusType statusType);

}