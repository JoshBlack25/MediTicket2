package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.factory.ClinicStaffFactory;
import za.ac.cput.service.ClinicStaffService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ClinicStaffControllerTest {

    @Mock
    private ClinicStaffService clinicStaffService;

    @InjectMocks
    private ClinicStaffController clinicStaffController;

    private static ClinicStaff clinicStaff;

    @BeforeEach
    void setUp() {

        Name name = new Name.Builder()
                .setFirstName("Matthew")
                .setMiddleName("John")
                .setLastName("Barron")
                .build();

        clinicStaff = ClinicStaffFactory.createClinicStaff(
                1,
                name,
                "matthew.barron@email.com",
                "0812345678",
                "Password123",
                LocalDate.of(2002, 6, 21),
                UserStatus.ACTIVE,
                StaffRole.NURSE,
                "Emergency"
        );
    }

    @Test
    void a_Create() {

        when(clinicStaffService.create(clinicStaff)).thenReturn(clinicStaff);

        ClinicStaff created = clinicStaffController.create(clinicStaff);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_Read() {

        when(clinicStaffService.read(1)).thenReturn(clinicStaff);

        ClinicStaff read = clinicStaffController.read(1);

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_Update() {

        ClinicStaff updated = new ClinicStaff.Builder()
                .copy(clinicStaff)
                .setDepartment("Pediatrics")
                .build();

        when(clinicStaffService.update(updated)).thenReturn(updated);

        ClinicStaff result = clinicStaffController.update(updated);

        assertNotNull(result);
        System.out.println("Updated: " + result);
    }

    @Test
    void d_Delete() {

        doNothing().when(clinicStaffService).delete(1);

        clinicStaffController.delete(1);

        verify(clinicStaffService, times(1)).delete(1);
        System.out.println("Deleted Clinic Staff with ID: 1");
    }

    @Test
    void e_GetAll() {

        when(clinicStaffService.getAll()).thenReturn(List.of(clinicStaff));

        List<ClinicStaff> staff = clinicStaffController.getAll();

        assertNotNull(staff);
        assertFalse(staff.isEmpty());
        System.out.println("All Clinic Staff: " + staff);
    }

    @Test
    void f_FindByEmail() {

        when(clinicStaffService.findByEmail("matthew.barron@email.com"))
                .thenReturn(Optional.of(clinicStaff));

        Optional<ClinicStaff> found = clinicStaffController.findByEmail("matthew.barron@email.com");

        assertTrue(found.isPresent());
        assertEquals(clinicStaff, found.get());
        System.out.println("Found by Email: " + found.get());
    }

    @Test
    void g_FindByDepartment() {

        when(clinicStaffService.findByDepartment("Emergency"))
                .thenReturn(List.of(clinicStaff));

        List<ClinicStaff> found = clinicStaffController.findByDepartment("Emergency");

        assertNotNull(found);
        assertFalse(found.isEmpty());
        System.out.println("Found by Department: " + found);
    }

    @Test
    void h_FindByStaffRole() {

        when(clinicStaffService.findByStaffRole(StaffRole.NURSE))
                .thenReturn(List.of(clinicStaff));

        List<ClinicStaff> found = clinicStaffController.findByStaffRole(StaffRole.NURSE);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        System.out.println("Found by Staff Role: " + found);
    }
}