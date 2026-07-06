package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationFactoryTest {

    private Patient patient;
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
    void testCreateNotification_success() {
        Notification n = NotificationFactory.createNotification(
                1,
                NotificationType.EMAIL,
                NotificationStatus.PENDING,
                "Your appointment is confirmed.",
                patient,
                ticket,
                appointment,
                notificationDate
        );
        assertNotNull(n);
        assertEquals(NotificationType.EMAIL, n.getNotificationType());
        assertEquals(NotificationStatus.PENDING, n.getNotificationStatus());
        assertEquals("Your appointment is confirmed.", n.getNotificationMessage());
        assertEquals(patient, n.getPatient());
        assertEquals(ticket, n.getTicket());
        assertEquals(appointment, n.getAppointment());
        assertEquals(notificationDate, n.getNotificationDate());
    }

    @Test
    void testCreateNotification_invalidId_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                0, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullType_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, null, NotificationStatus.PENDING,
                "Message", patient, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullStatus_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, null,
                "Message", patient, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_emptyMessage_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "", patient, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullPatient_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", null, ticket, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullTicket_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, null, appointment, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullAppointment_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, ticket, null, notificationDate
        );
        assertNull(n);
    }

    @Test
    void testCreateNotification_nullDate_returnsNull() {
        Notification n = NotificationFactory.createNotification(
                1, NotificationType.EMAIL, NotificationStatus.PENDING,
                "Message", patient, ticket, appointment, null
        );
        assertNull(n);
    }
}