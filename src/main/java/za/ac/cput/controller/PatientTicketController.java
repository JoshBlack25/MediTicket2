package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.service.PatientTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/patienttickets")
public class PatientTicketController {

    private final PatientTicketService service;

    public PatientTicketController(PatientTicketService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public PatientTicket create(@RequestBody PatientTicket ticket) {
        return service.create(ticket);
    }

    @GetMapping("/read/{id}")
    public PatientTicket read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public PatientTicket update(@RequestBody PatientTicket ticket) {
        return service.update(ticket);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/getall")
    public List<PatientTicket> getAll() {
        return service.getAll();
    }
}

