package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.service.DoctorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/create")
    public ResponseEntity<Doctor> create(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.create(doctor));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Doctor> read(@PathVariable Integer id) {
        Doctor doctor = doctorService.read(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/update")
    public ResponseEntity<Doctor> update(@RequestBody Doctor doctor) {
        Doctor existing = doctorService.read(doctor.getUserId());
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctorService.update(doctor));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Doctor existing = doctorService.read(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Doctor> findByEmail(@PathVariable String email) {
        Optional<Doctor> doctor = doctorService.findByEmail(email);
        return doctor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}