package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.service.AppointmentService;
import za.ac.cput.service.PatientTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/patienttickets")
public class PatientTicketController {

    private final PatientTicketService service;
    private final AppointmentService appointmentService;

    @Autowired
    public PatientTicketController(PatientTicketService service, AppointmentService appointmentService) {
        this.service = service;
        this.appointmentService = appointmentService;
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

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PatientTicket>> findByCurrentStatus(@PathVariable StatusType status) {
        return ResponseEntity.ok(service.findByCurrentStatus(status));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientTicket>> findByPatientUserId(@PathVariable int patientId) {
        return ResponseEntity.ok(service.findByPatientUserId(patientId));
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<PatientTicket> progressStatus(@PathVariable int ticketId,
                                                        @RequestParam StatusType newStatus,
                                                        @RequestParam(required = false) String notes) {
        PatientTicket updated = service.progressStatus(ticketId, newStatus, notes);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // NEW — used by the admin/nurse Appointments page to check whether a
    // CONFIRMED appointment already has a ticket, before showing "Create Ticket".
    @GetMapping("/byappointment/{appointmentId}")
    public ResponseEntity<PatientTicket> findByAppointmentId(@PathVariable int appointmentId) {
        Appointment appointment = appointmentService.read(appointmentId);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        PatientTicket ticket = service.findByAppointment(appointment);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }
}