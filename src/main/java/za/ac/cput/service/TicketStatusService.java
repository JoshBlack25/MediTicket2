/*
 TicketStatusService.java

 Service class for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 10th July 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.repository.TicketStatusRepository;
import za.ac.cput.service.impl.ITicketStatusService;

import java.util.List;

@Service
public class TicketStatusService implements ITicketStatusService {

    @Autowired
    private TicketStatusRepository repository;

    @Override
    public TicketStatus create(TicketStatus ticketStatus) {
        if (ticketStatus == null) return null;
        return repository.save(ticketStatus);
    }

    @Override
    public TicketStatus read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public TicketStatus update(TicketStatus ticketStatus) {
        if (ticketStatus == null) return null;
        return repository.save(ticketStatus);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<TicketStatus> getAll() {
        return repository.findAll();
    }

    @Override
    public List<TicketStatus> findByStatusType(StatusType statusType) {
        return repository.findByStatusType(statusType);
    }
}
