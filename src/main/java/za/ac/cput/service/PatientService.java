package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.repository.PatientRepository;
import za.ac.cput.service.impl.IPatientService;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService implements IPatientService {

    private PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient read(Integer id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient update(Patient patient) {
        Patient existing = this.patientRepository.findById(patient.getUserId()).orElse(null);
        if (existing == null) {
            return null;
        }
        Patient updated = new Patient.Builder().copy(existing).build();
        return this.patientRepository.save(updated);
    }

    @Override
    public void delete(Integer id) {
        patientRepository.deleteById(id);

    }

    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    public List<Patient> findByDateRegistered(LocalDate dateRegistered) {
        return patientRepository.findByDateRegistered(dateRegistered);
    }
}