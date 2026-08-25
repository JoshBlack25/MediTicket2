package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.*;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationFactoryTest {

    private Patient patient;
    private Doctor doctor;
    private ClinicStaff clinicStaff;
    private Appointment appointment;
    private PatientTicket ticket;
    private LocalDateTime notificationDate;

    @BeforeEach
    void setup() {
        Name name = new Name.Builder()
                .setFirstName("John")
                .setMiddleName("A")
                .setLastName("Doe")
                .build();

        patient = new Patient.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("john.doe@email.com")
                .setCellPhone("0821234567")
                .setPassword("password123")
                .setDob(LocalDate.of(1990, 1, 1))
                .setAccountStatus(UserStatus.ACTIVE)
                .setDateRegistered(LocalDate.now())
                .setEmergencyContact("0829876543")
                .build();

        Name doctorName = new Name.Builder()
                .setFirstName("Sarah")
                .setMiddleName("B")
                .setLastName("Smith")
                .build();

        doctor = new Doctor.Builder()
                .setUserId(2)
                .setName(doctorName)
                .setEmail("sarah.smith@mediticket.com")
                .setCellPhone("0837654321")
                .setPassword("password456")
                .setDob(LocalDate.of(1985, 6, 15))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("Cardiology")
                .setLicenseNumber("LIC-00123")
                .build();

        Name staffName = new Name.Builder()
                .setFirstName("Mike")
                .setMiddleName("C")
                .setLastName("Jones")
                .build();

        clinicStaff = new ClinicStaff.Builder()
                .setUserId(3)
                .setName(staffName)
                .setEmail("mike.jones@mediticket.com")
                .setCellPhone("0845551234")
                .setPassword("password789")
                .setDob(LocalDate.of(1992, 3, 22))
                .setAccountStatus(UserStatus.ACTIVE)
                .setStaffRole(StaffRole.NURSE)
                .setDepartment("Front Desk")
                .build();

        appointment = new Appointment.Builder()
                .setAppointmentId(1)
                .setAppointmentDate(LocalDate.now())
                .setAppointmentTime(LocalTime.of(10, 0))
                .setConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();

        ticket = new PatientTicket.Builder()
                .setTicketId(1)
                .setTicketDescription("General checkup")
                .setTicketCreatedDate(LocalDateTime.now())
                .setPatient(patient)
                .setAppointment(appointment)
                .build();

        notificationDate = LocalDateTime.now();
    }

    @Test
    void testCreateNotification_success_withPatient() {
        Notification n = NotificationFactory.createNotification(
                1,
                NotificationType.EMAIL,
                NotificationStatus.PENDING,
                "Your appointment is confirmed.",
                patient,
                null,
                null,
                ticket,
                appointment,
                notificationDate
        );
        assertNotNull(n);
        assertEquals(NotificationType.EMAIL, n.getNotificationType());
        assertEquals(NotificationStatus.PENDING, n.getNotificationStatus());
        assertEquals("Your appointment is confirmed.", n.getNotificationMessage());
        assertEquals(patient, n.getPatient());
        assertNull(n.getDoctor());
        assertNull(n.getClinicStaff());
        assertEquals(ticket, n.getTicket());
        assertEquals(appointment, n.getAppointment());
        assertEquals(notificationDate, n.getNotificationDate());
    }

    @Test
    void testCreateNotification_success_withDoctor() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Reminder: appointment tomorrow.",
                null, doctor, null, ticket, appointment, notificationDate
        );
        assertNotNull(n);
        assertEquals(doctor, n.getDoctor());
        assertNull(n.getPatient());
        assertNull(n.getClinicStaff());
    }

    @Test
    void testCreateNotification_success_withClinicStaff() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "New ticket assigned.",
                null, null, clinicStaff, ticket, appointment, notificationDate
        );
        assertNotNull(n);
        assertEquals(clinicStaff, n.getClinicStaff());
        assertNull(n.getPatient());
        assertNull(n.getDoctor());
    }

    @Test
    void testCreateNotification_invalidId_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                0, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, null, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullType_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, null, NotificationStatus.PENDING,
                "Message", patient, null, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullStatus_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, null,
                "Message", patient, null, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_emptyMessage_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "", patient, null, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_noRecipientSet_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", null, null, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_multipleRecipientsSet_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, doctor, null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_allThreeRecipientsSet_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, doctor, clinicStaff, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullTicket_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, null, null, null, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullAppointment_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, null, null, ticket, null, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullDate_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, null, null, ticket, appointment, null
        );
        assertNull(n);
    }
}