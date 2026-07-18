package za.ac.cput.factory;



import org.junit.jupiter.api.Test;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClinicStaffFactoryTest {

    private final Name name = new Name.Builder()
            .setFirstName("Matthew")
            .setLastName("Barron")
            .build();

    @Test
    void createClinicStaff() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "matthew@gmail.com",
                "0821234567",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "Doctor",
                "General Practice"
        );

        assertNotNull(staff);
        assertEquals(1, staff.getUserId());
        assertEquals("Doctor", staff.getStaffRole());
        assertEquals("General Practice", staff.getDepartment());
        assertEquals("matthew@gmail.com", staff.getEmail());
    }

    @Test
    void createClinicStaffWithInvalidEmail() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "invalid-email",
                "0821234567",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "Doctor",
                "General Practice"
        );

        assertNull(staff);
    }

    @Test
    void createClinicStaffWithBlankRole() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "matthew@gmail.com",
                "0821234567",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "",
                "General Practice"
        );

        assertNull(staff);
    }

    @Test
    void createClinicStaffWithBlankDepartment() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "matthew@gmail.com",
                "0821234567",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "Doctor",
                ""
        );

        assertNull(staff);
    }

    @Test
    void createClinicStaffWithInvalidPhone() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "matthew@gmail.com",
                "123",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "Doctor",
                "General Practice"
        );

        assertNull(staff);
    }

    @Test
    void createClinicStaffWithNullName() {

        ClinicStaff staff = ClinicStaffFactory.createClinicStaff(
                1,
                null,
                "matthew@gmail.com",
                "0821234567",
                "Password123",
                LocalDate.of(2005, 1, 1),
                UserStatus.ACTIVE,
                "Doctor",
                "General Practice"
        );

        assertNull(staff);
    }
}