package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentFactoryTest {

    private Doctor doctor;
    private ClinicStaff staff;
    private LocalDate validDate;
    private LocalTime validTime;
    private int validAppointmentId;
    private ConfirmationStatus confirmationStatus;

    @BeforeEach
    public void setUp() {
        validAppointmentId = 1;
        validDate = LocalDate.now().plusDays(1);
        validTime = LocalTime.of(10, 30);
        confirmationStatus = ConfirmationStatus.PENDING;

        doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setName("Dr. Smith");

        staff = new ClinicStaff();
        staff.setStaffId(1);
        staff.setName("John Doe");
    }

    @Test
    public void testCreateAppointmentSuccess() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                validDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNotNull(appointment);
        assertEquals(validAppointmentId, appointment.getAppointmentId());
        assertEquals(validDate, appointment.getAppointmentDate());
        assertEquals(validTime, appointment.getAppointmentTime());
        assertEquals(confirmationStatus, appointment.getConfirmationStatus());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(staff, appointment.getStaff());
    }

    @Test
    public void testCreateAppointmentWithInvalidAppointmentId() {

        Appointment appointment = AppointmentFactory.createAppointment(
                0,
                validDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNegativeAppointmentId() {

        Appointment appointment = AppointmentFactory.createAppointment(
                -1,
                validDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNullDate() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                null,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNullTime() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                validDate,
                null,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                pastDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithTodayDate() {
        LocalDate todayDate = LocalDate.now();

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                todayDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNullConfirmationStatus() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                validDate,
                validTime,
                null,
                doctor,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNullDoctor() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                validDate,
                validTime,
                confirmationStatus,
                null,
                staff
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithNullStaff() {

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                validDate,
                validTime,
                confirmationStatus,
                doctor,
                null
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithAllNullParameters() {

        Appointment appointment = AppointmentFactory.createAppointment(
                0,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(appointment);
    }

    @Test
    public void testCreateAppointmentWithFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(30);

        Appointment appointment = AppointmentFactory.createAppointment(
                validAppointmentId,
                futureDate,
                validTime,
                confirmationStatus,
                doctor,
                staff
        );

        assertNotNull(appointment);
        assertEquals(futureDate, appointment.getAppointmentDate());
    }

    @Test
    public void testCreateAppointmentWithDifferentConfirmationStatuses() {
        ConfirmationStatus[] statuses = ConfirmationStatus.values();

        for (ConfirmationStatus status : statuses) {
            Appointment appointment = AppointmentFactory.createAppointment(
                    validAppointmentId,
                    validDate,
                    validTime,
                    status,
                    doctor,
                    staff
            );

            assertNotNull(appointment);
            assertEquals(status, appointment.getConfirmationStatus());
        }
    }

    @Test
    public void testCreateMultipleAppointments() {
        for (int i = 1; i <= 5; i++) {
            LocalDate appointmentDate = LocalDate.now().plusDays(i);
            int appointmentId = i;

            Appointment appointment = AppointmentFactory.createAppointment(
                    appointmentId,
                    appointmentDate,
                    validTime,
                    confirmationStatus,
                    doctor,
                    staff
            );

            assertNotNull(appointment);
            assertEquals(appointmentId, appointment.getAppointmentId());
        }
    }
}