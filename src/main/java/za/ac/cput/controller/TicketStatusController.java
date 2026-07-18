/*
 TicketStatusController.java

 REST Controller for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 18th July 2026
*/

package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.service.TicketStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/ticketstatus")
public class TicketStatusController {

    private final TicketStatusService service;

    public TicketStatusController(TicketStatusService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public TicketStatus create(@RequestBody TicketStatus ticketStatus) {
        return service.create(ticketStatus);
    }

    @GetMapping("/read/{id}")
    public TicketStatus read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public TicketStatus update(@RequestBody TicketStatus ticketStatus) {
        return service.update(ticketStatus);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/getall")
    public List<TicketStatus> getAll() {
        return service.getAll();
    }

    @GetMapping("/statustype/{statusType}")
    public List<TicketStatus> findByStatusType(@PathVariable StatusType statusType) {
        return service.findByStatusType(statusType);
    }
}
