/*
 TicketStatusControllerTest.java

 Controller Test class for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 18th July 2026
*/

package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.service.TicketStatusService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketStatusControllerTest {

    @Mock
    private TicketStatusService ticketStatusService;

    @InjectMocks
    private TicketStatusController ticketStatusController;

    private TicketStatus ticketStatus;

    @BeforeEach
    void setup() {
        ticketStatus = new TicketStatus.Builder()
                .setStatusId(1)
                .setStatusType(StatusType.OPEN)
                .setStatusDate(LocalDateTime.of(2026, 7, 18, 9, 0))
                .build();
    }

    @Test
    void testCreate() {
        when(ticketStatusService.create(ticketStatus)).thenReturn(ticketStatus);
        TicketStatus result = ticketStatusController.create(ticketStatus);
        assertNotNull(result);
        verify(ticketStatusService, times(1)).create(ticketStatus);
    }

    @Test
    void testRead() {
        when(ticketStatusService.read(1)).thenReturn(ticketStatus);
        TicketStatus result = ticketStatusController.read(1);
        assertNotNull(result);
        verify(ticketStatusService, times(1)).read(1);
    }

    @Test
    void testUpdate() {
        when(ticketStatusService.update(ticketStatus)).thenReturn(ticketStatus);
        TicketStatus result = ticketStatusController.update(ticketStatus);
        assertNotNull(result);
        verify(ticketStatusService, times(1)).update(ticketStatus);
    }

    @Test
    void testDelete() {
        doNothing().when(ticketStatusService).delete(1);
        ticketStatusController.delete(1);
        verify(ticketStatusService, times(1)).delete(1);
    }

    @Test
    void testGetAll() {
        when(ticketStatusService.getAll()).thenReturn(List.of(ticketStatus));
        List<TicketStatus> result = ticketStatusController.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketStatusService, times(1)).getAll();
    }

    @Test
    void testFindByStatusType() {
        when(ticketStatusService.findByStatusType(StatusType.OPEN)).thenReturn(List.of(ticketStatus));
        List<TicketStatus> result = ticketStatusController.findByStatusType(StatusType.OPEN);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(ticketStatusService, times(1)).findByStatusType(StatusType.OPEN);
    }
}