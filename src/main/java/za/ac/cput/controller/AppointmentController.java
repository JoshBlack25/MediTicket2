package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.service.impl.IAppointmentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final IAppointmentService service;

    @Autowired
    public AppointmentController(IAppointmentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment created = service.create(appointment);
        if (created == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(created);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Appointment> read(@PathVariable Integer id) {
        Appointment appointment = service.read(id);
        if (appointment == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/update")
    public ResponseEntity<Appointment> update(@RequestBody Appointment appointment) {
        Appointment updated = service.update(appointment);
        if (updated == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/findByDoctor/{doctorId}")
    public ResponseEntity<List<Appointment>> findByDoctorUserId(@PathVariable int doctorId) {
        return ResponseEntity.ok(service.findByDoctorUserId(doctorId));
    }

    @GetMapping("/findByStaff/{staffId}")
    public ResponseEntity<List<Appointment>> findByStaffUserId(@PathVariable int staffId) {
        return ResponseEntity.ok(service.findByStaffUserId(staffId));
    }

    @GetMapping("/findByDate/{appointmentDate}")
    public ResponseEntity<List<Appointment>> findByAppointmentDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        return ResponseEntity.ok(service.findByAppointmentDate(appointmentDate));
    }

    @GetMapping("/findByStatus/{confirmationStatus}")
    public ResponseEntity<List<Appointment>> findByConfirmationStatus(@PathVariable ConfirmationStatus confirmationStatus) {
        return ResponseEntity.ok(service.findByConfirmationStatus(confirmationStatus));
    }

    @GetMapping("/findByDoctorAndDate/{doctorId}/{appointmentDate}")
    public ResponseEntity<List<Appointment>> findByDoctorUserIdAndAppointmentDate(
            @PathVariable int doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        return ResponseEntity.ok(service.findByDoctorUserIdAndAppointmentDate(doctorId, appointmentDate));
    }

    @GetMapping("/findByPatient/{patientId}")
    public ResponseEntity<List<Appointment>> findByPatientUserId(@PathVariable int patientId) {
        return ResponseEntity.ok(service.findByPatientUserId(patientId));
    }

    @PostMapping("/{appointmentId}/approve")
    public ResponseEntity<Appointment> approve(@PathVariable int appointmentId,
                                               @RequestParam int doctorId,
                                               @RequestParam int staffId) {
        Appointment approved = service.approveAppointment(appointmentId, doctorId, staffId);
        if (approved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/{appointmentId}/reject")
    public ResponseEntity<Appointment> reject(@PathVariable int appointmentId,
                                              @RequestParam int staffId,
                                              @RequestParam(required = false) String reason) {
        Appointment rejected = service.rejectAppointment(appointmentId, staffId, reason);
        if (rejected == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(rejected);
    }

}