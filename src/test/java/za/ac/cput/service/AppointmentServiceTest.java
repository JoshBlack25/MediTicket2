package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    private Doctor doctor;
    private ClinicStaff staff;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        doctor = new Doctor.Builder().setUserId(1).build();
        staff = new ClinicStaff.Builder().setUserId(2).build();

        appointment = new Appointment.Builder()
                .setAppointmentId(1)
                .setAppointmentDate(LocalDate.now().plusDays(1))
                .setAppointmentTime(LocalTime.of(9, 0))
                .setConfirmationStatus(ConfirmationStatus.PENDING)
                .setDoctor(doctor)
                .setStaff(staff)
                .build();
    }

    @Test
    void create_ValidAppointment_ReturnsSavedAppointment() {
        when(repository.save(appointment)).thenReturn(appointment);

        Appointment result = service.create(appointment);

        assertNotNull(result);
        assertEquals(appointment.getAppointmentId(), result.getAppointmentId());
        verify(repository, times(1)).save(appointment);
    }

    @Test
    void create_NullAppointment_ReturnsNull() {
        Appointment result = service.create(null);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void read_ExistingId_ReturnsAppointment() {
        when(repository.findById(1)).thenReturn(Optional.of(appointment));

        Appointment result = service.read(1);

        assertNotNull(result);
        assertEquals(1, result.getAppointmentId());
    }

    @Test
    void read_NonExistingId_ReturnsNull() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        Appointment result = service.read(99);

        assertNull(result);
    }

    @Test
    void update_ExistingAppointment_ReturnsUpdatedAppointment() {
        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(appointment)).thenReturn(appointment);

        Appointment result = service.update(appointment);

        assertNotNull(result);
        verify(repository, times(1)).save(appointment);
    }

    @Test
    void update_NonExistingAppointment_ReturnsNull() {
        when(repository.existsById(1)).thenReturn(false);

        Appointment result = service.update(appointment);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void update_NullAppointment_ReturnsNull() {
        Appointment result = service.update(null);

        assertNull(result);
        verify(repository, never()).existsById(anyInt());
    }

    @Test
    void delete_CallsRepositoryDeleteById() {
        service.delete(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void getAll_ReturnsAllAppointments() {
        when(repository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByDoctorUserId_ReturnsMatchingAppointments() {
        when(repository.findByDoctor_UserId(1)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findByDoctorUserId(1);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByDoctor_UserId(1);
    }

    @Test
    void findByStaffUserId_ReturnsMatchingAppointments() {
        when(repository.findByStaff_UserId(2)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findByStaffUserId(2);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByStaff_UserId(2);
    }

    @Test
    void findByAppointmentDate_ReturnsMatchingAppointments() {
        LocalDate date = appointment.getAppointmentDate();
        when(repository.findByAppointmentDate(date)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findByAppointmentDate(date);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByAppointmentDate(date);
    }

    @Test
    void findByConfirmationStatus_ReturnsMatchingAppointments() {
        when(repository.findByConfirmationStatus(ConfirmationStatus.PENDING)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findByConfirmationStatus(ConfirmationStatus.PENDING);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByConfirmationStatus(ConfirmationStatus.PENDING);
    }

    @Test
    void findByDoctorUserIdAndAppointmentDate_ReturnsMatchingAppointments() {
        LocalDate date = appointment.getAppointmentDate();
        when(repository.findByDoctor_UserIdAndAppointmentDate(1, date)).thenReturn(List.of(appointment));

        List<Appointment> result = service.findByDoctorUserIdAndAppointmentDate(1, date);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByDoctor_UserIdAndAppointmentDate(1, date);
    }
}