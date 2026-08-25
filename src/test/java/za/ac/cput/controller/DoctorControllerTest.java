package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.service.DoctorService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController controller;

    private Doctor doctor;
    private Name name;

    @BeforeEach
    void setUp() {
        name = mock(Name.class);

        doctor = new Doctor.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("dr.jones@mediticket.co.za")
                .setCellPhone("0821234567")
                .setPassword("hashedPassword123")
                .setDob(LocalDate.of(1980, 5, 12))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("Cardiology")
                .setLicenseNumber("LIC-000123")
                .build();
    }

    // ---------- create ----------

    @Test
    void create_ShouldReturnCreatedDoctor() {
        when(doctorService.create(doctor)).thenReturn(doctor);

        ResponseEntity<Doctor> response = controller.create(doctor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctor.getUserId(), response.getBody().getUserId());
        assertEquals(doctor.getSpecialty(), response.getBody().getSpecialty());
        assertEquals(doctor.getLicenseNumber(), response.getBody().getLicenseNumber());
        verify(doctorService, times(1)).create(doctor);
    }

    // ---------- read ----------

    @Test
    void read_ShouldReturnDoctor_WhenIdExists() {
        int id = 1;
        when(doctorService.read(id)).thenReturn(doctor);

        ResponseEntity<Doctor> response = controller.read(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctor.getUserId(), response.getBody().getUserId());
        verify(doctorService, times(1)).read(id);
    }

    @Test
    void read_ShouldReturnNotFound_WhenIdDoesNotExist() {
        int id = 99;
        when(doctorService.read(id)).thenReturn(null);

        ResponseEntity<Doctor> response = controller.read(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(doctorService, times(1)).read(id);
    }

    // ---------- update ----------

    @Test
    void update_ShouldReturnUpdatedDoctor_WhenExists() {
        Doctor updated = new Doctor.Builder()
                .copy(doctor)
                .setSpecialty("Neurology")
                .build();

        when(doctorService.read(1)).thenReturn(doctor);
        when(doctorService.update(updated)).thenReturn(updated);

        ResponseEntity<Doctor> response = controller.update(updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Neurology", response.getBody().getSpecialty());
        verify(doctorService, times(1)).read(1);
        verify(doctorService, times(1)).update(updated);
    }

    @Test
    void update_ShouldReturnNotFound_WhenDoctorDoesNotExist() {
        Doctor nonExistent = new Doctor.Builder()
                .copy(doctor)
                .setUserId(999)
                .build();

        when(doctorService.read(999)).thenReturn(null);

        ResponseEntity<Doctor> response = controller.update(nonExistent);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(1)).read(999);
        verify(doctorService, never()).update(any());
    }

    // ---------- delete ----------

    @Test
    void delete_ShouldReturnNoContent_WhenExists() {
        int id = 1;
        when(doctorService.read(id)).thenReturn(doctor);
        doNothing().when(doctorService).delete(id);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).read(id);
        verify(doctorService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenIdDoesNotExist() {
        int id = 99;
        when(doctorService.read(id)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(1)).read(id);
        verify(doctorService, never()).delete(anyInt());
    }

    // ---------- getAll ----------

    @Test
    void getAll_ShouldReturnListOfDoctors() {
        Doctor doctor2 = new Doctor.Builder()
                .setUserId(2)
                .setName(mock(Name.class))
                .setEmail("dr.smith@mediticket.co.za")
                .setCellPhone("0837654321")
                .setPassword("hashedPassword456")
                .setDob(LocalDate.of(1975, 3, 20))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("Pediatrics")
                .setLicenseNumber("LIC-000456")
                .build();

        List<Doctor> doctors = Arrays.asList(doctor, doctor2);
        when(doctorService.getAll()).thenReturn(doctors);

        ResponseEntity<List<Doctor>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(doctorService, times(1)).getAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoneExist() {
        when(doctorService.getAll()).thenReturn(List.of());

        ResponseEntity<List<Doctor>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(doctorService, times(1)).getAll();
    }

    // ---------- findByEmail ----------

    @Test
    void findByEmail_ShouldReturnDoctor_WhenEmailExists() {
        String email = "dr.jones@mediticket.co.za";
        when(doctorService.findByEmail(email)).thenReturn(Optional.of(doctor));

        ResponseEntity<Doctor> response = controller.findByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(email, response.getBody().getEmail());
        verify(doctorService, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_ShouldReturnNotFound_WhenEmailDoesNotExist() {
        String email = "unknown@mediticket.co.za";
        when(doctorService.findByEmail(email)).thenReturn(Optional.empty());

        ResponseEntity<Doctor> response = controller.findByEmail(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(doctorService, times(1)).findByEmail(email);
    }
}