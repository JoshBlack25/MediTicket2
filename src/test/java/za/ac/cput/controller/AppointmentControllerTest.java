package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.service.impl.IAppointmentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private IAppointmentService service;

    @InjectMocks
    private AppointmentController controller;

    private Appointment appointment;
    private Doctor doctor;
    private ClinicStaff staff;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("John")
                .setLastName("Smith")
                .build();

        doctor = new Doctor.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("doctor@mediticket.co.za")
                .setCellPhone("0821234567")
                .setPassword("password123")
                .setDob(LocalDate.of(1980, 1, 1))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("Cardiology")
                .setLicenseNumber("LIC12345")
                .build();

        staff = new ClinicStaff.Builder()
                .setUserId(2)
                .setName(name)
                .setEmail("staff@mediticket.co.za")
                .setCellPhone("0837654321")
                .setPassword("password123")
                .setDob(LocalDate.of(1990, 1, 1))
                .setAccountStatus(UserStatus.ACTIVE)
                .setStaffRole(StaffRole.NURSE)
                .setDepartment("Front Desk")
                .build();

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
    void create_returnsOk_whenAppointmentIsValid() {
        when(service.create(any(Appointment.class))).thenReturn(appointment);

        ResponseEntity<Appointment> response = controller.create(appointment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
        verify(service, times(1)).create(appointment);
    }

    @Test
    void create_returnsBadRequest_whenServiceReturnsNull() {
        when(service.create(any(Appointment.class))).thenReturn(null);

        ResponseEntity<Appointment> response = controller.create(appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void read_returnsOk_whenAppointmentExists() {
        when(service.read(1)).thenReturn(appointment);

        ResponseEntity<Appointment> response = controller.read(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void read_returnsNotFound_whenAppointmentDoesNotExist() {
        when(service.read(99)).thenReturn(null);

        ResponseEntity<Appointment> response = controller.read(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void update_returnsOk_whenAppointmentIsUpdated() {
        when(service.update(any(Appointment.class))).thenReturn(appointment);

        ResponseEntity<Appointment> response = controller.update(appointment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void update_returnsBadRequest_whenServiceReturnsNull() {
        when(service.update(any(Appointment.class))).thenReturn(null);

        ResponseEntity<Appointment> response = controller.update(appointment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(service).delete(1);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(1);
    }

    @Test
    void getAll_returnsOkWithList() {
        when(service.getAll()).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAll_returnsOkWithEmptyList_whenNoAppointmentsExist() {
        when(service.getAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Appointment>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void findByDoctorUserId_returnsOkWithMatchingAppointments() {
        when(service.findByDoctorUserId(1)).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.findByDoctorUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByDoctorUserId(1);
    }

    @Test
    void findByStaffUserId_returnsOkWithMatchingAppointments() {
        when(service.findByStaffUserId(2)).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.findByStaffUserId(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByStaffUserId(2);
    }

    @Test
    void findByAppointmentDate_returnsOkWithMatchingAppointments() {
        LocalDate date = appointment.getAppointmentDate();
        when(service.findByAppointmentDate(date)).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.findByAppointmentDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByAppointmentDate(date);
    }

    @Test
    void findByConfirmationStatus_returnsOkWithMatchingAppointments() {
        when(service.findByConfirmationStatus(ConfirmationStatus.PENDING)).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.findByConfirmationStatus(ConfirmationStatus.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByConfirmationStatus(ConfirmationStatus.PENDING);
    }

    @Test
    void findByDoctorUserIdAndAppointmentDate_returnsOkWithMatchingAppointments() {
        LocalDate date = appointment.getAppointmentDate();
        when(service.findByDoctorUserIdAndAppointmentDate(1, date)).thenReturn(List.of(appointment));

        ResponseEntity<List<Appointment>> response = controller.findByDoctorUserIdAndAppointmentDate(1, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByDoctorUserIdAndAppointmentDate(1, date);
    }
}