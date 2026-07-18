package za.ac.cput.service;

import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
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

        notification = new Notification.Builder()
                .setNotificationId(1)
                .setNotificationType(NotificationType.EMAIL)
                .setNotificationStatus(NotificationStatus.PENDING)
                .setNotificationMessage("Your appointment is confirmed.")
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(notificationDate)
                .build();
    }

    @Test
    void testCreate() {
        when(notificationRepository.save(notification)).thenReturn(notification);
        Notification result = notificationService.create(notification);
        assertNotNull(result);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testRead() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        Notification result = notificationService.read(1);
        assertNotNull(result);
        verify(notificationRepository, times(1)).findById(1);
    }

    @Test
    void testUpdate() {
        when(notificationRepository.save(notification)).thenReturn(notification);
        Notification result = notificationService.update(notification);
        assertNotNull(result);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testDelete() {
        doNothing().when(notificationRepository).deleteById(1);
        notificationService.delete(1);
        verify(notificationRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAll() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));
        List<Notification> result = notificationService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testFindByPatient() {
        when(notificationRepository.findByPatient(patient)).thenReturn(List.of(notification));
        List<Notification> result = notificationService.findByPatient(patient);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(notificationRepository, times(1)).findByPatient(patient);
    }

    @Test
    void testFindByNotificationType() {
        when(notificationRepository.findByNotificationType(NotificationType.EMAIL)).thenReturn(List.of(notification));
        List<Notification> result = notificationService.findByNotificationType(NotificationType.EMAIL);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(notificationRepository, times(1)).findByNotificationType(NotificationType.EMAIL);
    }

    @Test
    void testFindByNotificationStatus() {
        when(notificationRepository.findByNotificationStatus(NotificationStatus.PENDING)).thenReturn(List.of(notification));
        List<Notification> result = notificationService.findByNotificationStatus(NotificationStatus.PENDING);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(notificationRepository, times(1)).findByNotificationStatus(NotificationStatus.PENDING);
    }
}