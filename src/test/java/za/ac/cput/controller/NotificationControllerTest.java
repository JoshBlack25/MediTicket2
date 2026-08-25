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
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.service.ClinicStaffService;
import za.ac.cput.service.DoctorService;
import za.ac.cput.service.NotificationService;
import za.ac.cput.service.PatientService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private ClinicStaffService clinicStaffService;

    @InjectMocks
    private NotificationController controller;

    private Notification notification;
    private Patient patient;
    private Doctor doctor;
    private ClinicStaff clinicStaff;
    private PatientTicket ticket;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        patient = mock(Patient.class);
        doctor = mock(Doctor.class);
        clinicStaff = mock(ClinicStaff.class);
        ticket = mock(PatientTicket.class);
        appointment = mock(Appointment.class);

        notification = new Notification.Builder()
                .setNotificationId(1)
                .setNotificationType(NotificationType.EMAIL)
                .setNotificationStatus(NotificationStatus.PENDING)
                .setNotificationMessage("Your appointment is confirmed")
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(LocalDateTime.now())
                .build();
    }

    // ---------- create ----------

    @Test
    void create_ShouldReturnCreatedNotification_WhenValid() {
        when(notificationService.create(any(Notification.class))).thenReturn(notification);

        ResponseEntity<Notification> response = controller.create(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notification.getNotificationMessage(), response.getBody().getNotificationMessage());
        verify(notificationService, times(1)).create(any(Notification.class));
    }

    @Test
    void create_ShouldReturnCreatedNotification_WhenRecipientIsDoctor() {
        Notification doctorNotification = new Notification.Builder()
                .copy(notification)
                .setPatient(null)
                .setDoctor(doctor)
                .build();

        when(notificationService.create(any(Notification.class))).thenReturn(doctorNotification);

        ResponseEntity<Notification> response = controller.create(doctorNotification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctor, response.getBody().getDoctor());
        verify(notificationService, times(1)).create(any(Notification.class));
    }

    @Test
    void create_ShouldReturnCreatedNotification_WhenRecipientIsClinicStaff() {
        Notification staffNotification = new Notification.Builder()
                .copy(notification)
                .setPatient(null)
                .setClinicStaff(clinicStaff)
                .build();

        when(notificationService.create(any(Notification.class))).thenReturn(staffNotification);

        ResponseEntity<Notification> response = controller.create(staffNotification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clinicStaff, response.getBody().getClinicStaff());
        verify(notificationService, times(1)).create(any(Notification.class));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenMessageIsBlank() {
        Notification invalid = new Notification.Builder()
                .copy(notification)
                .setNotificationMessage("")
                .build();

        ResponseEntity<Notification> response = controller.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(notificationService);
    }

    @Test
    void create_ShouldReturnBadRequest_WhenNoRecipientIsSet() {
        Notification invalid = new Notification.Builder()
                .copy(notification)
                .setPatient(null)
                .build();

        ResponseEntity<Notification> response = controller.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(notificationService);
    }

    @Test
    void create_ShouldReturnBadRequest_WhenMoreThanOneRecipientIsSet() {
        Notification invalid = new Notification.Builder()
                .copy(notification)
                .setPatient(patient)
                .setDoctor(doctor)
                .build();

        ResponseEntity<Notification> response = controller.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(notificationService);
    }

    // ---------- read ----------

    @Test
    void read_ShouldReturnNotification_WhenIdExists() {
        int id = 1;
        when(notificationService.read(id)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.read(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notification.getNotificationId(), response.getBody().getNotificationId());
        verify(notificationService, times(1)).read(id);
    }

    @Test
    void read_ShouldReturnNotFound_WhenIdDoesNotExist() {
        int id = 99;
        when(notificationService.read(id)).thenReturn(null);

        ResponseEntity<Notification> response = controller.read(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(notificationService, times(1)).read(id);
    }

    // ---------- update ----------

    @Test
    void update_ShouldReturnUpdatedNotification_WhenExists() {
        Notification updated = new Notification.Builder()
                .copy(notification)
                .setNotificationStatus(NotificationStatus.FAILED)
                .build();

        when(notificationService.read(notification.getNotificationId())).thenReturn(notification);
        when(notificationService.update(updated)).thenReturn(updated);

        ResponseEntity<Notification> response = controller.update(updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NotificationStatus.FAILED, response.getBody().getNotificationStatus());
        verify(notificationService, times(1)).read(notification.getNotificationId());
        verify(notificationService, times(1)).update(updated);
    }

    @Test
    void update_ShouldReturnNotFound_WhenNotificationDoesNotExist() {
        Notification nonExistent = new Notification.Builder()
                .copy(notification)
                .setNotificationId(999)
                .build();

        when(notificationService.read(999)).thenReturn(null);

        ResponseEntity<Notification> response = controller.update(nonExistent);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(notificationService, times(1)).read(999);
        verify(notificationService, never()).update(any());
    }

    // ---------- delete ----------

    @Test
    void delete_ShouldReturnNoContent_WhenExists() {
        int id = 1;
        when(notificationService.read(id)).thenReturn(notification);
        doNothing().when(notificationService).delete(id);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).read(id);
        verify(notificationService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenIdDoesNotExist() {
        int id = 99;
        when(notificationService.read(id)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(notificationService, times(1)).read(id);
        verify(notificationService, never()).delete(anyInt());
    }

    // ---------- getAll ----------

    @Test
    void getAll_ShouldReturnListOfNotifications() {
        Notification notification2 = new Notification.Builder()
                .setNotificationId(2)
                .setNotificationType(NotificationType.SMS)
                .setNotificationStatus(NotificationStatus.SENT)
                .setNotificationMessage("Reminder: appointment tomorrow")
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(LocalDateTime.now())
                .build();

        when(notificationService.getAll()).thenReturn(Arrays.asList(notification, notification2));

        ResponseEntity<List<Notification>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(notificationService, times(1)).getAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoneExist() {
        when(notificationService.getAll()).thenReturn(List.of());

        ResponseEntity<List<Notification>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(notificationService, times(1)).getAll();
    }

    // ---------- findByPatient ----------

    @Test
    void findByPatient_ShouldReturnNotifications_WhenPatientExists() {
        int patientId = 5;
        List<Notification> patientNotifications = List.of(notification);

        when(patientService.read(patientId)).thenReturn(patient);
        when(notificationService.findByPatient(patient)).thenReturn(patientNotifications);

        ResponseEntity<List<Notification>> response = controller.findByPatient(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).read(patientId);
        verify(notificationService, times(1)).findByPatient(patient);
    }

    @Test
    void findByPatient_ShouldReturnNotFound_WhenPatientDoesNotExist() {
        int patientId = 999;
        when(patientService.read(patientId)).thenReturn(null);

        ResponseEntity<List<Notification>> response = controller.findByPatient(patientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(patientService, times(1)).read(patientId);
        verifyNoInteractions(notificationService);
    }

    // ---------- findByDoctor ----------

    @Test
    void findByDoctor_ShouldReturnNotifications_WhenDoctorExists() {
        int doctorId = 7;
        Notification doctorNotification = new Notification.Builder()
                .copy(notification)
                .setPatient(null)
                .setDoctor(doctor)
                .build();
        List<Notification> doctorNotifications = List.of(doctorNotification);

        when(doctorService.read(doctorId)).thenReturn(doctor);
        when(notificationService.findByDoctor(doctor)).thenReturn(doctorNotifications);

        ResponseEntity<List<Notification>> response = controller.findByDoctor(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).read(doctorId);
        verify(notificationService, times(1)).findByDoctor(doctor);
    }

    @Test
    void findByDoctor_ShouldReturnNotFound_WhenDoctorDoesNotExist() {
        int doctorId = 999;
        when(doctorService.read(doctorId)).thenReturn(null);

        ResponseEntity<List<Notification>> response = controller.findByDoctor(doctorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(1)).read(doctorId);
        verifyNoInteractions(notificationService);
    }

    // ---------- findByClinicStaff ----------

    @Test
    void findByClinicStaff_ShouldReturnNotifications_WhenClinicStaffExists() {
        int staffId = 9;
        Notification staffNotification = new Notification.Builder()
                .copy(notification)
                .setPatient(null)
                .setClinicStaff(clinicStaff)
                .build();
        List<Notification> staffNotifications = List.of(staffNotification);

        when(clinicStaffService.read(staffId)).thenReturn(clinicStaff);
        when(notificationService.findByClinicStaff(clinicStaff)).thenReturn(staffNotifications);

        ResponseEntity<List<Notification>> response = controller.findByClinicStaff(staffId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(clinicStaffService, times(1)).read(staffId);
        verify(notificationService, times(1)).findByClinicStaff(clinicStaff);
    }

    @Test
    void findByClinicStaff_ShouldReturnNotFound_WhenClinicStaffDoesNotExist() {
        int staffId = 999;
        when(clinicStaffService.read(staffId)).thenReturn(null);

        ResponseEntity<List<Notification>> response = controller.findByClinicStaff(staffId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clinicStaffService, times(1)).read(staffId);
        verifyNoInteractions(notificationService);
    }

    // ---------- findByNotificationStatus ----------

    @Test
    void findByNotificationStatus_ShouldReturnMatchingNotifications() {
        NotificationStatus status = NotificationStatus.SENT;
        when(notificationService.findByNotificationStatus(status)).thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response = controller.findByNotificationStatus(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).findByNotificationStatus(status);
    }

    @Test
    void findByNotificationStatus_ShouldReturnEmptyList_WhenNoneMatch() {
        NotificationStatus status = NotificationStatus.FAILED;
        when(notificationService.findByNotificationStatus(status)).thenReturn(List.of());

        ResponseEntity<List<Notification>> response = controller.findByNotificationStatus(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(notificationService, times(1)).findByNotificationStatus(status);
    }

    // ---------- findByNotificationType ----------

    @Test
    void findByNotificationType_ShouldReturnMatchingNotifications() {
        NotificationType type = NotificationType.EMAIL;
        when(notificationService.findByNotificationType(type)).thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response = controller.findByNotificationType(type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).findByNotificationType(type);
    }

    @Test
    void findByNotificationType_ShouldReturnEmptyList_WhenNoneMatch() {
        NotificationType type = NotificationType.SMS;
        when(notificationService.findByNotificationType(type)).thenReturn(List.of());

        ResponseEntity<List<Notification>> response = controller.findByNotificationType(type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(notificationService, times(1)).findByNotificationType(type);
    }
}