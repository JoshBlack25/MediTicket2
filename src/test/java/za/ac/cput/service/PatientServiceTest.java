package za.ac.cput.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.repository.PatientRepository;
import za.ac.cput.service.PatientService;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void create_shouldSaveAndReturnPatient() {
        Patient patient = mock(Patient.class);

        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.create(patient);

        assertSame(patient, result);
        verify(patientRepository).save(patient);
    }

    @Test
    void read_shouldReturnPatient() {
        Patient patient = mock(Patient.class);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        assertSame(patient, patientService.read(1));
    }

    @Test
    void update_existingPatient_shouldSaveAndReturnUpdatedPatient() {
        Patient patient = mock(Patient.class);
        when(patient.getUserId()).thenReturn(1);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.update(patient);

        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void update_nonExistingPatient_shouldReturnNull() {
        Patient patient = mock(Patient.class);
        when(patient.getUserId()).thenReturn(99);

        when(patientRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(patientService.update(patient));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallRepository() {
        patientService.delete(1);

        verify(patientRepository).deleteById(1);
    }

    @Test
    void getAll_shouldReturnAllPatients() {
        List<Patient> patients = List.of(mock(Patient.class));

        when(patientRepository.findAll()).thenReturn(patients);

        assertEquals(patients, patientService.getAll());
    }

    @Test
    void findByEmail_shouldDelegateToRepository() {
        Patient patient = mock(Patient.class);

        when(patientRepository.findByEmail("a@b.com")).thenReturn(patient);

        assertSame(patient, patientService.findByEmail("a@b.com"));
    }

    @Test
    void findByDateRegistered_shouldDelegateToRepository() {
        LocalDate date = LocalDate.now();
        List<Patient> patients = List.of(mock(Patient.class));

        when(patientRepository.findByDateRegistered(date)).thenReturn(patients);

        assertEquals(patients, patientService.findByDateRegistered(date));
    }
}
