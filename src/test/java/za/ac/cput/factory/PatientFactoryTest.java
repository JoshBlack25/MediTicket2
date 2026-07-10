/*
 PatientFactoryTest.java

 JUnit 5 test class for PatientFactory

 Author: Aidan Barends (230155639)

 Date: 24th June 2026
*/

package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class PatientFactoryTest {

    private static Patient p1, p2, p3, p4, p5;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Aidan")
                .setMiddleName("James")
                .setLastName("Barends")
                .build();

        // Valid patient
        p1 = PatientFactory.createPatient(
                1, name, "aidan.barends@example.com", "0812345678",
                "SecurePass123", LocalDate.of(2004, 11, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), "Jane Barends: 0829876543"
        );

        // Fails - invalid userId (0)
        p2 = PatientFactory.createPatient(
                0, name, "aidan.barends@example.com", "0812345678",
                "SecurePass123", LocalDate.of(2003, 5, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), "Jane Barends: 0829876543"
        );

        // Fails - invalid email
        p3 = PatientFactory.createPatient(
                1, name, "not-a-valid-email", "0812345678",
                "SecurePass123", LocalDate.of(2003, 5, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), "Jane Barends: 0829876543"
        );

        // Fails - null password
        p4 = PatientFactory.createPatient(
                1, name, "aidan.barends@example.com", "0812345678",
                null, LocalDate.of(2003, 5, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), "Jane Barends: 0829876543"
        );

        // Fails - empty emergency contact
        p5 = PatientFactory.createPatient(
                1, name, "aidan.barends@example.com", "0812345678",
                "SecurePass123", LocalDate.of(2003, 5, 15),
                UserStatus.ACTIVE, 101,
                LocalDate.of(2024, 1, 10), ""
        );
    }

    @Test
    void a_testCreatePatient_Success() {
        assertNotNull(p1);
        System.out.println(p1);
    }

    @Test
    void b_testCreatePatient_InvalidUserId_Fails() {
        assertNull(p2);
        System.out.println("Patient2(p2) is null - invalid userId: " + p2);
    }

    @Test
    void c_testCreatePatient_InvalidEmail_Fails() {
        assertNull(p3);
        System.out.println("Patient3(p3) is null - invalid email: " + p3);
    }

    @Test
    void d_testCreatePatient_NullPassword_Fails() {
        assertNull(p4);
        System.out.println("Patient4(p4) is null - null password: " + p4);
    }

    @Test
    void e_testCreatePatient_EmptyEmergencyContact_Fails() {
        assertNull(p5);
        System.out.println("Patient5(p5) is null - empty emergency contact: " + p5);
    }
}