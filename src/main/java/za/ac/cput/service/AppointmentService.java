package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.repository.AppointmentRepository;
import za.ac.cput.service.impl.IAppointmentService;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository repository;

    @Autowired
    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Appointment create(Appointment appointment) {
        if (appointment == null) return null;
        return repository.save(appointment);
    }

    @Override
    public Appointment read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Appointment update(Appointment appointment) {
        if (appointment == null) return null;
        if (!repository.existsById(appointment.getAppointmentId())) return null;
        return repository.save(appointment);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Appointment> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Appointment> findByDoctorUserId(int doctorId) {
        return repository.findByDoctor_UserId(doctorId);
    }

    @Override
    public List<Appointment> findByStaffUserId(int staffId) {
        return repository.findByStaff_UserId(staffId);
    }

    @Override
    public List<Appointment> findByAppointmentDate(LocalDate appointmentDate) {
        return repository.findByAppointmentDate(appointmentDate);
    }

    @Override
    public List<Appointment> findByConfirmationStatus(ConfirmationStatus confirmationStatus) {
        return repository.findByConfirmationStatus(confirmationStatus);
    }

    @Override
    public List<Appointment> findByDoctorUserIdAndAppointmentDate(int doctorId, LocalDate appointmentDate) {
        return repository.findByDoctor_UserIdAndAppointmentDate(doctorId, appointmentDate);
    }
}