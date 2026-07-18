
package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.factory.PatientFactory;
import za.ac.cput.service.PatientService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private static Patient patient;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Aidan")
                .setMiddleName("James")
                .setLastName("Barends")
                .build();

        patient = PatientFactory.createPatient(
                1, name, "aidan.barends@email.com", "0812345678",
                "Aidan123", LocalDate.of(2003, 5, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), "Jane Barends: 0829876543"
        );
    }

    @Test
    void a_Create() {
        when(patientService.create(patient)).thenReturn(patient);

        Patient created = patientController.create(patient);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_Read() {
        when(patientService.read(1)).thenReturn(patient);

        Patient read = patientController.read(1);

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_Update() {
        Patient updated = new Patient.Builder()
                .copy(patient)
                .setEmergencyContact("John Barends: 0831234567")
                .build();

        when(patientService.update(updated)).thenReturn(updated);

        Patient result = patientController.update(updated);

        assertNotNull(result);
        System.out.println("Updated: " + result);
    }

    @Test
    void d_Delete() {
        doNothing().when(patientService).delete(1);

        patientController.delete(1);

        verify(patientService, times(1)).delete(1);
        System.out.println("Deleted patient with ID: 1");
    }

    @Test
    void e_GetAll() {
        when(patientService.getAll()).thenReturn(List.of(patient));

        List<Patient> patients = patientController.getAll();

        assertNotNull(patients);
        assertFalse(patients.isEmpty());
        System.out.println("All patients: " + patients);
    }

    @Test
    void f_FindByEmail() {
        when(patientService.findByEmail("aidan.barends@email.com")).thenReturn(patient);

        Patient foundEmail = patientController.findByEmail("aidan.barends@email.com");

        assertNotNull(foundEmail);
        System.out.println("Found by Email: " + foundEmail);
    }

    @Test
    void g_findByDateRegistered() {
        when(patientService.findByDateRegistered(LocalDate.of(2024, 1, 10)))
                .thenReturn(List.of(patient));

        List<Patient> found = patientController.findByDateRegistered("2024-01-10");

        assertNotNull(found);
        assertFalse(found.isEmpty());
        System.out.println("Found by date registered: " + found);
    }

}
