package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.factory.NotificationFactory;
import za.ac.cput.repository.AppointmentRepository;
import za.ac.cput.service.impl.IAppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository repository;
    private final DoctorService doctorService;
    private final ClinicStaffService clinicStaffService;
    private final PatientTicketService patientTicketService;
    private final NotificationService notificationService;

    @Autowired
    public AppointmentService(AppointmentRepository repository,
                              DoctorService doctorService,
                              ClinicStaffService clinicStaffService,
                              PatientTicketService patientTicketService,
                              NotificationService notificationService) {
        this.repository = repository;
        this.doctorService = doctorService;
        this.clinicStaffService = clinicStaffService;
        this.patientTicketService = patientTicketService;
        this.notificationService = notificationService;
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

    @Override
    public List<Appointment> findByPatientUserId(int patientId) {
        return repository.findByPatient_UserId(patientId);
    }

    // ===== Orchestration methods =====

    @Override
    @Transactional
    public Appointment approveAppointment(int appointmentId, int doctorId, int staffId) {
        Appointment existing = read(appointmentId);
        if (existing == null) return null;
        if (existing.getConfirmationStatus() != ConfirmationStatus.PENDING) return null;

        Doctor doctor = doctorService.read(doctorId);
        if (doctor == null) return null;

        ClinicStaff staff = clinicStaffService.read(staffId);
        if (staff == null) return null;

        Patient patient = existing.getPatient();
        if (patient == null) return null; // shouldn't happen post-migration, but don't NPE if it does

        // Step 1: confirm the appointment, assigning doctor + staff
        Appointment confirmed = new Appointment.Builder()
                .copy(existing)
                .setDoctor(doctor)
                .setStaff(staff)
                .setConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();
        confirmed = repository.save(confirmed);

        // Step 2: create the ticket, opened
        PatientTicket ticket = new PatientTicket.Builder()
                .setTicketDescription(confirmed.getReason())
                .setTicketCreatedDate(LocalDateTime.now())
                .setPatient(patient)
                .setAppointment(confirmed)
                .build();
        ticket.addStatus(StatusType.OPEN);
        ticket = patientTicketService.create(ticket);

        // Step 3: notify patient + doctor
        notifyAppointmentConfirmed(patient, doctor, ticket, confirmed);

        return confirmed;
    }

    @Override
    @Transactional
    public Appointment rejectAppointment(int appointmentId, int staffId, String reason) {
        Appointment existing = read(appointmentId);
        if (existing == null) return null;
        if (existing.getConfirmationStatus() != ConfirmationStatus.PENDING) return null;

        ClinicStaff staff = clinicStaffService.read(staffId);
        if (staff == null) return null;

        Patient patient = existing.getPatient();

        Appointment rejected = new Appointment.Builder()
                .copy(existing)
                .setStaff(staff)
                .setConfirmationStatus(ConfirmationStatus.REJECTED)
                .build();
        rejected = repository.save(rejected);

        if (patient != null) {
            notifyAppointmentRejected(patient, reason, rejected);
        }

        return rejected;
    }

    // ===== Private notification helpers =====

    private void notifyAppointmentConfirmed(Patient patient, Doctor doctor, PatientTicket ticket, Appointment appointment) {
        Notification patientNotification = NotificationFactory.createNotification(
                0,
                NotificationType.EMAIL,
                NotificationStatus.PENDING,
                "Your appointment has been confirmed. A ticket has been opened for your visit.",
                patient,
                null,
                null,
                ticket,
                appointment,
                LocalDateTime.now()
        );
        if (patientNotification != null) notificationService.create(patientNotification);

        Notification doctorNotification = NotificationFactory.createNotification(
                0,
                NotificationType.EMAIL,
                NotificationStatus.PENDING,
                "A new appointment has been assigned to you.",
                null,
                doctor,
                null,
                ticket,
                appointment,
                LocalDateTime.now()
        );
        if (doctorNotification != null) notificationService.create(doctorNotification);
    }

    private void notifyAppointmentRejected(Patient patient, String reason, Appointment appointment) {
        String message = (reason == null || reason.isBlank())
                ? "Your appointment request could not be approved. Please contact the clinic for details."
                : "Your appointment request could not be approved: " + reason;

        Notification notification = NotificationFactory.createNotification(
                0,
                NotificationType.EMAIL,
                NotificationStatus.PENDING,
                message,
                patient,
                null,
                null,
                null,
                appointment,
                LocalDateTime.now()
        );
        if (notification != null) notificationService.create(notification);
    }
}