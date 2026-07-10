package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.factory.PatientFactory;
import za.ac.cput.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class PatientServiceTest {


    private PatientRepository patientRepository;


    private PatientService patientService;

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
        Patient created = patientService.create(patient);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_Read() {
        Patient read = patientService.read(1);

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_Update() {
        Patient updated = new Patient.Builder()
                .copy(patient)
                .setEmail("aidan@email.com")
                .build();

        Patient result = patientService.update(updated);

        assertNotNull(updated);
        System.out.println("Updated: " + result);
    }

    @Test
    void d_Delete() {

        patientService.delete(1);

        verify(patientRepository, times(1)).deleteById(1);
        System.out.println("Deleted patient with ID: 1");
    }

    @Test
    void e_GetAll() {

        List<Patient> patients = patientService.getAll();

        assertNotNull(patients);
        assertFalse(patients.isEmpty());
        System.out.println("All patients: " + patients);

    }

    @Test
    void f_FindBYyEmail(){

        Patient foundEmail = patientService.findByEmail("aidanbarends@email.com");

        assertNotNull(foundEmail);
        System.out.println("Found by Email: " + foundEmail);
    }

    @Test
    void g_findByDateRegistered(){
        List<Patient> found = patientService.findByDateRegistered(LocalDate.of(2024, 1, 10));

        assertNotNull(found);
        assertFalse(found.isEmpty());
        System.out.println("Found by date registered: " + found);
    }

}
