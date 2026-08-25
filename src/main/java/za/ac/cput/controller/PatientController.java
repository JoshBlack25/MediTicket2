package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.service.PatientService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/create")
    public Patient create(@RequestBody Patient patient){
        return patientService.create(patient);
    }

    @GetMapping("/read/{id}")
    public Patient read(@PathVariable Integer id) {
        return patientService.read(id);
    }

    @PutMapping("/update")
    public Patient update(@RequestBody Patient patient) {
        return patientService.update(patient);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        patientService.delete(id);
    }

    @GetMapping("/getall")
    public List<Patient> getAll() {
        return patientService.getAll();
    }

    @GetMapping("/email/{email}")
    public Patient findByEmail(@PathVariable String email) {
        return patientService.findByEmail(email);
    }

    @GetMapping("/dateregistered/{date}")
    public List<Patient> findByDateRegistered(@PathVariable String date) {
        return patientService.findByDateRegistered(LocalDate.parse(date));
    }

}

