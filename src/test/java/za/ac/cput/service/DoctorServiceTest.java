package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.repository.DoctorRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    private Doctor doctor;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        doctorService = new DoctorService(doctorRepository);

        Name name = new Name.Builder()
                .setFirstName("Jaden")
                .setMiddleName("Clayton")
                .setLastName("Abrahams")
                .build();

        doctor = new Doctor.Builder()
                .setUserId(1)
                .setName(name)
                .setEmail("doctor@gmail.com")
                .setCellPhone("0812345678")
                .setPassword("Password123")
                .setDob(LocalDate.of(1995, 6, 21))
                .setAccountStatus(UserStatus.ACTIVE)
                .setSpecialty("Cardiology")
                .setLicenseNumber("LIC12345")
                .build();
    }


    @Test
    void createDoctorSuccess() {

        when(doctorRepository.save(doctor))
                .thenReturn(doctor);

        Doctor created = doctorService.create(doctor);

        assertNotNull(created);
        assertEquals("Cardiology", created.getSpecialty());

        verify(doctorRepository).save(doctor);
    }


    @Test
    void readDoctorSuccess() {

        when(doctorRepository.findById(1))
                .thenReturn(Optional.of(doctor));

        Doctor found = doctorService.read(1);

        assertNotNull(found);
        assertEquals(1, found.getUserId());

        verify(doctorRepository).findById(1);
    }


    @Test
    void readDoctorNotFound() {

        when(doctorRepository.findById(1))
                .thenReturn(Optional.empty());

        Doctor found = doctorService.read(1);

        assertNull(found);

        verify(doctorRepository).findById(1);
    }


    @Test
    void updateDoctorSuccess() {

        when(doctorRepository.save(doctor))
                .thenReturn(doctor);

        Doctor updated = doctorService.update(doctor);

        assertNotNull(updated);
        assertEquals("LIC12345", updated.getLicenseNumber());

        verify(doctorRepository).save(doctor);
    }


    @Test
    void deleteDoctorSuccess() {

        doNothing().when(doctorRepository)
                .deleteById(1);

        doctorService.delete(1);

        verify(doctorRepository)
                .deleteById(1);
    }


    @Test
    void getAllDoctorsSuccess() {

        List<Doctor> doctors = Arrays.asList(doctor);

        when(doctorRepository.findAll())
                .thenReturn(doctors);

        List<Doctor> result = doctorService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(doctorRepository).findAll();
    }


    @Test
    void findDoctorByEmailSuccess() {

        when(doctorRepository.findByEmail("doctor@gmail.com"))
                .thenReturn(Optional.of(doctor));

        Optional<Doctor> result =
                doctorService.findByEmail("doctor@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(
                "doctor@gmail.com",
                result.get().getEmail()
        );

        verify(doctorRepository)
                .findByEmail("doctor@gmail.com");
    }
}
