package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.create(notification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> read(@PathVariable int id) {
        return ResponseEntity.ok(notificationService.read(id));
    }

    @PutMapping
    public ResponseEntity<Notification> update(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.update(notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Notification>> findByPatient(@PathVariable int patientId) {
        Patient patient = notificationService.read(patientId).getPatient();
        return ResponseEntity.ok(notificationService.findByPatient(patient));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Notification>> findByNotificationStatus(@PathVariable NotificationStatus status) {
        return ResponseEntity.ok(notificationService.findByNotificationStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> findByNotificationType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(notificationService.findByNotificationType(type));
    }
}