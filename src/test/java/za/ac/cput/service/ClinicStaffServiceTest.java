package za.ac.cput.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.repository.ClinicStaffRepository;

@ExtendWith(MockitoExtension.class)
class ClinicStaffServiceTest {

    @Mock
    private ClinicStaffRepository clinicStaffRepository;

    @InjectMocks
    private ClinicStaffService clinicStaffService;

    @Test
    void create_shouldSaveClinicStaff() {
        ClinicStaff clinicStaff = mock(ClinicStaff.class);

        when(clinicStaffRepository.save(clinicStaff)).thenReturn(clinicStaff);

        ClinicStaff result = clinicStaffService.create(clinicStaff);

        assertNotNull(result);
        assertSame(clinicStaff, result);
        verify(clinicStaffRepository).save(clinicStaff);
    }

    @Test
    void read_shouldReturnClinicStaff() {
        ClinicStaff clinicStaff = mock(ClinicStaff.class);

        when(clinicStaffRepository.findById(1)).thenReturn(Optional.of(clinicStaff));

        ClinicStaff result = clinicStaffService.read(1);

        assertNotNull(result);
        assertEquals(clinicStaff, result);
    }

    @Test
    void update_shouldUpdateExistingClinicStaff() {
        ClinicStaff clinicStaff = mock(ClinicStaff.class);

        when(clinicStaff.getUserId()).thenReturn(1);
        when(clinicStaffRepository.findById(1)).thenReturn(Optional.of(clinicStaff));
        when(clinicStaffRepository.save(any(ClinicStaff.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClinicStaff result = clinicStaffService.update(clinicStaff);

        assertNotNull(result);
        verify(clinicStaffRepository).save(any(ClinicStaff.class));
    }

    @Test
    void update_shouldReturnNullIfClinicStaffNotFound() {
        ClinicStaff clinicStaff = mock(ClinicStaff.class);

        when(clinicStaff.getUserId()).thenReturn(10);
        when(clinicStaffRepository.findById(10)).thenReturn(Optional.empty());

        ClinicStaff result = clinicStaffService.update(clinicStaff);

        assertNull(result);
        verify(clinicStaffRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteClinicStaff() {
        clinicStaffService.delete(1);

        verify(clinicStaffRepository).deleteById(1);
    }

    @Test
    void getAll_shouldReturnClinicStaffList() {
        List<ClinicStaff> clinicStaffList = List.of(mock(ClinicStaff.class));

        when(clinicStaffRepository.findAll()).thenReturn(clinicStaffList);

        List<ClinicStaff> result = clinicStaffService.getAll();

        assertEquals(clinicStaffList, result);
    }

    @Test
    void findByEmail_shouldReturnClinicStaff() {
        ClinicStaff clinicStaff = mock(ClinicStaff.class);

        when(clinicStaffRepository.findByEmail("staff@clinic.com")).thenReturn(clinicStaff);

        ClinicStaff result = clinicStaffService.findByEmail("staff@clinic.com");

        assertSame(clinicStaff, result);
    }
}