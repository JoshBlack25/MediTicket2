package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PatientTicketFactoryTest {

    private Patient patient;
    private Appointment appointment;
    private LocalDateTime createdDate;

    @BeforeEach
    void setUp() {

        Name name = new Name.Builder()
                .setFirstName("Joshua")
                .setMiddleName("Reid")
                .setLastName("Adams")
                .build();

        patient = new Patient.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("josh@example.com")
                .setCellPhone("0812345678")
                .setPassword("Password123")
                .setDob(LocalDate.of(2003, 5, 20))
                .setAccountStatus(UserStatus.ACTIVE)
                .setDateRegistered(LocalDate.now())
                .setEmergencyContact("0823456789")
                .build();

        appointment = new Appointment.Builder()
                .setAppointmentId(1)
                .setAppointmentDate(LocalDate.now())
                .setAppointmentTime(LocalTime.of(10, 30))
                .setConfirmationStatus(ConfirmationStatus.PENDING)
                .build();

        createdDate = LocalDateTime.now();
    }

    @Test
    void createPatientTicket() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                1,
                "Severe headache",
                createdDate,
                patient,
                appointment
        );

        assertNotNull(ticket);
        assertEquals(1, ticket.getTicketId());
        assertEquals("Severe headache", ticket.getTicketDescription());
        assertEquals(createdDate, ticket.getTicketCreatedDate());
        assertEquals(patient, ticket.getPatient());
        assertEquals(appointment, ticket.getAppointment());

        // Default status should be OPEN
        assertEquals(StatusType.OPEN, ticket.getCurrentStatus());
    }

    @Test
    void createPatientTicketWithInvalidTicketId() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                0,
                "Severe headache",
                createdDate,
                patient,
                appointment
        );

        assertNull(ticket);
    }

    @Test
    void createPatientTicketWithEmptyDescription() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                1,
                "",
                createdDate,
                patient,
                appointment
        );

        assertNull(ticket);
    }

    @Test
    void createPatientTicketWithNullCreatedDate() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                1,
                "Severe headache",
                null,
                patient,
                appointment
        );

        assertNull(ticket);
    }

    @Test
    void createPatientTicketWithNullPatient() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                1,
                "Severe headache",
                createdDate,
                null,
                appointment
        );

        assertNull(ticket);
    }

    @Test
    void createPatientTicketWithNullAppointment() {

        PatientTicket ticket = PatientTicketFactory.createPatientTicket(
                1,
                "Severe headache",
                createdDate,
                patient,
                null
        );

        assertNull(ticket);
    }
}