package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.service.ClinicStaffService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinicstaff")
public class ClinicStaffController {

    private final ClinicStaffService clinicStaffService;

    public ClinicStaffController(ClinicStaffService clinicStaffService) {
        this.clinicStaffService = clinicStaffService;
    }

    @PostMapping("/create")
    public ClinicStaff create(@RequestBody ClinicStaff clinicStaff) {
        return clinicStaffService.create(clinicStaff);
    }

    @GetMapping("/read/{id}")
    public ClinicStaff read(@PathVariable Integer id) {
        return clinicStaffService.read(id);
    }

    @PutMapping("/update")
    public ClinicStaff update(@RequestBody ClinicStaff clinicStaff) {
        return clinicStaffService.update(clinicStaff);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        clinicStaffService.delete(id);
    }

    @GetMapping("/getall")
    public List<ClinicStaff> getAll() {
        return clinicStaffService.getAll();
    }

    @GetMapping("/email/{email}")
    public Optional<ClinicStaff> findByEmail(@PathVariable String email) {
        return clinicStaffService.findByEmail(email);
    }

    @GetMapping("/department/{department}")
    public List<ClinicStaff> findByDepartment(@PathVariable String department) {
        return clinicStaffService.findByDepartment(department);
    }

    @GetMapping("/staffrole/{staffRole}")
    public List<ClinicStaff> findByStaffRole(@PathVariable StaffRole staffRole) {
        return clinicStaffService.findByStaffRole(staffRole);
    }
}