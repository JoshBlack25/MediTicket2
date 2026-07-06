package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DoctorFactoryTest {

    private Name name;
    private LocalDate dob;

    @BeforeEach
    void setUp() {
        // Create the Name object according to your Name class implementation
        // Example if using a Builder:
        name = new Name.Builder()
                .setFirstName("Jaden")
                .setMiddleName("Clayton")
                .setLastName("Abrahams")
                .build();

        dob = LocalDate.of(1995, 6, 21);
    }

    @Test
    void createDoctorSuccess() {

        Doctor doctor = DoctorFactory.createDoctor(
                1,
                name,
                "doctor@gmail.com",
                "0812345678",
                "Password123",
                dob,
                UserStatus.ACTIVE,
                "Cardiology",
                "LIC12345"
        );

        assertNotNull(doctor);
        assertEquals("Cardiology", doctor.getSpecialty());
        assertEquals("LIC12345", doctor.getLicenseNumber());
    }

    @Test
    void createDoctorWithInvalidEmail() {

        Doctor doctor = DoctorFactory.createDoctor(
                1,
                name,
                "invalidEmail",
                "0812345678",
                "Password123",
                dob,
                UserStatus.ACTIVE,
                "Cardiology",
                "LIC12345"
        );

        assertNull(doctor);
    }

    @Test
    void createDoctorWithEmptySpecialty() {

        Doctor doctor = DoctorFactory.createDoctor(
                1,
                name,
                "doctor@gmail.com",
                "0812345678",
                "Password123",
                dob,
                UserStatus.ACTIVE,
                "",
                "LIC12345"
        );

        assertNull(doctor);
    }
}