package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.service.ClinicStaffService;

import java.util.List;

@RestController
@RequestMapping("/api/clinicstaff")
public class ClinicStaffController {

    private final ClinicStaffService clinicStaffService;

    @Autowired
    public ClinicStaffController(ClinicStaffService clinicStaffService) {
        this.clinicStaffService = clinicStaffService;
    }

    @PostMapping("/create")
    public ResponseEntity<ClinicStaff> create(@RequestBody ClinicStaff clinicStaff) {
        ClinicStaff created = clinicStaffService.create(clinicStaff);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ClinicStaff> read(@PathVariable Integer id) {
        ClinicStaff clinicStaff = clinicStaffService.read(id);
        return ResponseEntity.ok(clinicStaff);
    }

    @PutMapping("/update")
    public ResponseEntity<ClinicStaff> update(@RequestBody ClinicStaff clinicStaff) {
        ClinicStaff updated = clinicStaffService.update(clinicStaff);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        clinicStaffService.delete(id);
        return ResponseEntity.ok("Clinic staff deleted successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClinicStaff>> getAll() {
        List<ClinicStaff> clinicStaffList = clinicStaffService.getAll();
        return ResponseEntity.ok(clinicStaffList);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClinicStaff> findByEmail(@PathVariable String email) {
        ClinicStaff clinicStaff = clinicStaffService.findByEmail(email);
        return ResponseEntity.ok(clinicStaff);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<ClinicStaff>> findByDepartment(@PathVariable String department) {
        List<ClinicStaff> clinicStaffList = clinicStaffService.findByDepartment(department);
        return ResponseEntity.ok(clinicStaffList);
    }

    @GetMapping("/role/{staffRole}")
    public ResponseEntity<List<ClinicStaff>> findByStaffRole(@PathVariable String staffRole) {
        List<ClinicStaff> clinicStaffList = clinicStaffService.findByStaffRole(staffRole);
        return ResponseEntity.ok(clinicStaffList);
    }
}