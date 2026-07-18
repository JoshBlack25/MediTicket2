package za.ac.cput.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.valueObject.Name;
import za.ac.cput.service.ClinicStaffService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClinicStaffController.class)
class ClinicStaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClinicStaffService clinicStaffService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Name name = new Name.Builder()
            .setFirstName("Matthew")
            .setLastName("Barron")
            .build();

    private final ClinicStaff clinicStaff = new ClinicStaff.Builder()
            .setUserId(1)
            .setName(name)
            .setEmail("matthew@gmail.com")
            .setCellPhone("0821234567")
            .setPassword("Password123")
            .setDob(LocalDate.of(2005, 1, 1))
            .setAccountStatus(UserStatus.ACTIVE)
            .setStaffRole("Doctor")
            .setDepartment("General Practice")
            .build();

    @Test
    void create() throws Exception {

        Mockito.when(clinicStaffService.create(any(ClinicStaff.class)))
                .thenReturn(clinicStaff);

        mockMvc.perform(post("/api/clinicstaff/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinicStaff)))
                .andExpect(status().isOk());
    }

    @Test
    void read() throws Exception {

        Mockito.when(clinicStaffService.read(1))
                .thenReturn(clinicStaff);

        mockMvc.perform(get("/api/clinicstaff/read/1"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {

        Mockito.when(clinicStaffService.update(any(ClinicStaff.class)))
                .thenReturn(clinicStaff);

        mockMvc.perform(put("/api/clinicstaff/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinicStaff)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClinicStaff() throws Exception {

        mockMvc.perform(delete("/api/clinicstaff/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {

        Mockito.when(clinicStaffService.getAll())
                .thenReturn(Collections.singletonList(clinicStaff));

        mockMvc.perform(get("/api/clinicstaff/all"))
                .andExpect(status().isOk());
    }

    @Test
    void findByEmail() throws Exception {

        Mockito.when(clinicStaffService.findByEmail("matthew@gmail.com"))
                .thenReturn(clinicStaff);

        mockMvc.perform(get("/api/clinicstaff/email/matthew@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void findByDepartment() throws Exception {

        Mockito.when(clinicStaffService.findByDepartment("General Practice"))
                .thenReturn(Collections.singletonList(clinicStaff));

        mockMvc.perform(get("/api/clinicstaff/department/General Practice"))
                .andExpect(status().isOk());
    }

    @Test
    void findByStaffRole() throws Exception {

        Mockito.when(clinicStaffService.findByStaffRole("Doctor"))
                .thenReturn(Collections.singletonList(clinicStaff));

        mockMvc.perform(get("/api/clinicstaff/role/Doctor"))
                .andExpect(status().isOk());
    }
}