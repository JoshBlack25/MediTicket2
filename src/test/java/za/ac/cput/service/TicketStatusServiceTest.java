/*
 TicketStatusServiceTest.java

 Service Test class for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 12th July 2026
*/

package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.repository.TicketStatusRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketStatusServiceTest {

    @Mock
    private TicketStatusRepository ticketStatusRepository;

    @InjectMocks
    private TicketStatusService ticketStatusService;

    private TicketStatus ticketStatus;

    @BeforeEach
    void setup() {
        ticketStatus = new TicketStatus.Builder()
                .setStatusId(1)
                .setStatusType(StatusType.OPEN)
                .setStatusDate(LocalDateTime.of(2026, 7, 12, 9, 0))
                .build();
    }

    @Test
    void testCreate() {
        when(ticketStatusRepository.save(ticketStatus)).thenReturn(ticketStatus);
        TicketStatus result = ticketStatusService.create(ticketStatus);
        assertNotNull(result);
        verify(ticketStatusRepository, times(1)).save(ticketStatus);
    }

    @Test
    void testRead() {
        when(ticketStatusRepository.findById(1)).thenReturn(Optional.of(ticketStatus));
        TicketStatus result = ticketStatusService.read(1);
        assertNotNull(result);
        verify(ticketStatusRepository, times(1)).findById(1);
    }

    @Test
    void testUpdate() {
        when(ticketStatusRepository.save(ticketStatus)).thenReturn(ticketStatus);
        TicketStatus result = ticketStatusService.update(ticketStatus);
        assertNotNull(result);
        verify(ticketStatusRepository, times(1)).save(ticketStatus);
    }

    @Test
    void testDelete() {
        doNothing().when(ticketStatusRepository).deleteById(1);
        ticketStatusService.delete(1);
        verify(ticketStatusRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAll() {
        when(ticketStatusRepository.findAll()).thenReturn(List.of(ticketStatus));
        List<TicketStatus> result = ticketStatusService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketStatusRepository, times(1)).findAll();
    }

    @Test
    void testFindByStatusType() {
        when(ticketStatusRepository.findByStatusType(StatusType.OPEN)).thenReturn(List.of(ticketStatus));
        List<TicketStatus> result = ticketStatusService.findByStatusType(StatusType.OPEN);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(ticketStatusRepository, times(1)).findByStatusType(StatusType.OPEN);
    }
}
