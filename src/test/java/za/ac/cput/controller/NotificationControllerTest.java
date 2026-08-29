package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.service.NotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setup() {
        Name name = new Name.Builder()
                .setFirstName("John")
                .setMiddleName("A")
                .setLastName("Doe")
                .build();

        Patient patient = new Patient.Builder()
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

        Appointment appointment = new Appointment.Builder()
                .setAppointmentId(1)
                .setAppointmentDate(LocalDate.now())
                .setAppointmentTime(LocalTime.of(10, 0))
                .setConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();

        PatientTicket ticket = new PatientTicket.Builder()
                .setTicketId(1)
                .setTicketDescription("General checkup")
                .setTicketCreatedDate(LocalDateTime.now())
                .setPatient(patient)
                .setAppointment(appointment)
                .build();

        notification = new Notification.Builder()
                .setNotificationId(1)
                .setNotificationType(NotificationType.EMAIL)
                .setNotificationStatus(NotificationStatus.PENDING)
                .setNotificationMessage("Your appointment is confirmed.")
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreate() throws Exception {
        when(notificationService.create(any(Notification.class))).thenReturn(notification);
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notificationId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void testRead() throws Exception {
        when(notificationService.read(1)).thenReturn(notification);
        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        when(notificationService.update(any(Notification.class))).thenReturn(notification);
        mockMvc.perform(put("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notificationId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(notificationService).delete(1);
        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAll() throws Exception {
        when(notificationService.getAll()).thenReturn(List.of(notification));
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByNotificationStatus() throws Exception {
        when(notificationService.findByNotificationStatus(NotificationStatus.PENDING))
                .thenReturn(List.of(notification));
        mockMvc.perform(get("/api/notifications/status/PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByNotificationType() throws Exception {
        when(notificationService.findByNotificationType(NotificationType.EMAIL))
                .thenReturn(List.of(notification));
        mockMvc.perform(get("/api/notifications/type/EMAIL"))
                .andExpect(status().isOk());
    }
}