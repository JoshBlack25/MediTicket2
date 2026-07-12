package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.repository.DoctorRepository;
import za.ac.cput.service.impl.IDoctorService;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService implements IDoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    @Override
    public Doctor create(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    @Override
    public Doctor read(Integer id) {
        return doctorRepository.findById(id).orElse(null);
    }


    @Override
    public Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    @Override
    public void delete(Integer id) {
        doctorRepository.deleteById(id);
    }


    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }


    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
}