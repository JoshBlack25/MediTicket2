package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentFactoryTest {

    private Doctor doctor;
    private ClinicStaff staff;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    @BeforeEach
    void setUp() {

        Name doctorName = new Name.Builder()
                .setFirstName("John")
                .setMiddleName("A")
                .setLastName("Smith")
                .build();

        Name staffName = new Name.Builder()
                .setFirstName("Mary")
                .setMiddleName("B")
                .setLastName("Jones")
                .build();

        doctor = new Doctor.Builder()
                .setUserId(1)
                .setName(doctorName)
                .setEmail("doctor@test.com")
                .setCellPhone("0811111111")
                .setPassword("Doctor123")
                .setDob(LocalDate.of(1980, 5, 10))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("General Practitioner")
                .setLicenseNumber("DOC12345")
                .build();

        staff = new ClinicStaff.Builder()
                .setUserId(2)
                .setName(staffName)
                .setEmail("staff@test.com")
                .setCellPhone("0822222222")
                .setPassword("Staff123")
                .setDob(LocalDate.of(1990, 8, 20))
                .setAccountStatus(UserStatus.ACTIVE)
                .setStaffRole("Receptionist")
                .setDepartment("Reception")
                .build();

        appointmentDate = LocalDate.now().plusDays(1);
        appointmentTime = LocalTime.of(10, 30);
    }

    @Test
    void createAppointment() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                appointmentDate,
                appointmentTime,
                ConfirmationStatus.PENDING,
                doctor,
                staff
        );

        assertNotNull(appointment);
        assertEquals(1, appointment.getAppointmentId());
        assertEquals(appointmentDate, appointment.getAppointmentDate());
        assertEquals(appointmentTime, appointment.getAppointmentTime());
        assertEquals(ConfirmationStatus.PENDING, appointment.getConfirmationStatus());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(staff, appointment.getStaff());
    }

    @Test
    void createAppointmentWithInvalidId() {

        Appointment appointment = AppointmentFactory.createAppointment(
                0,
                appointmentDate,
                appointmentTime,
                ConfirmationStatus.PENDING,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithPastDate() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                LocalDate.now().minusDays(1),
                appointmentTime,
                ConfirmationStatus.PENDING,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithNullDate() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                null,
                appointmentTime,
                ConfirmationStatus.PENDING,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithNullTime() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                appointmentDate,
                null,
                ConfirmationStatus.PENDING,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithNullConfirmationStatus() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                appointmentDate,
                appointmentTime,
                null,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithNullDoctor() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                appointmentDate,
                appointmentTime,
                ConfirmationStatus.PENDING,
                null,
                staff
        );

        assertNull(appointment);
    }

    @Test
    void createAppointmentWithNullStaff() {

        Appointment appointment = AppointmentFactory.createAppointment(
                1,
                appointmentDate,
                appointmentTime,
                ConfirmationStatus.PENDING,
                doctor,
                null
        );

        assertNull(appointment);
    }
}