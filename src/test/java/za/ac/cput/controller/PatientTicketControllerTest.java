package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.service.PatientTicketService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientTicketControllerTest {

    @Mock
    private PatientTicketService service;

    @InjectMocks
    private PatientTicketController controller;

    private PatientTicket ticket;

    @BeforeEach
    void setUp() {
        ticket = new PatientTicket.Builder()
                .setTicketId(1)
                .setTicketDescription("Patient reports persistent headache")
                .setTicketCreatedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void create_ShouldReturnCreatedTicket() {
        when(service.create(ticket)).thenReturn(ticket);

        PatientTicket result = controller.create(ticket);

        assertNotNull(result);
        assertEquals(ticket.getTicketId(), result.getTicketId());
        assertEquals(ticket.getTicketDescription(), result.getTicketDescription());
        verify(service, times(1)).create(ticket);
    }

    @Test
    void read_ShouldReturnTicket_WhenIdExists() {
        int id = 1;
        when(service.read(id)).thenReturn(ticket);

        PatientTicket result = controller.read(id);

        assertNotNull(result);
        assertEquals(ticket.getTicketId(), result.getTicketId());
        assertEquals(ticket.getTicketDescription(), result.getTicketDescription());
        verify(service, times(1)).read(id);
    }

    @Test
    void read_ShouldReturnNull_WhenIdDoesNotExist() {
        int id = 99;
        when(service.read(id)).thenReturn(null);

        PatientTicket result = controller.read(id);

        assertNull(result);
        verify(service, times(1)).read(id);
    }

    @Test
    void update_ShouldReturnUpdatedTicket() {
        PatientTicket updatedTicket = new PatientTicket.Builder()
                .copy(ticket)
                .setTicketDescription("Updated: headache resolved after medication")
                .build();

        when(service.update(updatedTicket)).thenReturn(updatedTicket);

        PatientTicket result = controller.update(updatedTicket);

        assertNotNull(result);
        assertEquals("Updated: headache resolved after medication", result.getTicketDescription());
        verify(service, times(1)).update(updatedTicket);
    }

    @Test
    void delete_ShouldCallServiceDeleteOnce() {
        int id = 1;
        doNothing().when(service).delete(id);

        controller.delete(id);

        verify(service, times(1)).delete(id);
    }

    @Test
    void getAll_ShouldReturnListOfTickets() {
        PatientTicket ticket2 = new PatientTicket.Builder()
                .setTicketId(2)
                .setTicketDescription("Follow-up consultation required")
                .setTicketCreatedDate(LocalDateTime.now())
                .build();

        List<PatientTicket> tickets = Arrays.asList(ticket, ticket2);
        when(service.getAll()).thenReturn(tickets);

        List<PatientTicket> result = controller.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ticket.getTicketDescription(), result.get(0).getTicketDescription());
        assertEquals(ticket2.getTicketDescription(), result.get(1).getTicketDescription());
        verify(service, times(1)).getAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoTicketsExist() {
        when(service.getAll()).thenReturn(List.of());

        List<PatientTicket> result = controller.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, times(1)).getAll();
    }
}