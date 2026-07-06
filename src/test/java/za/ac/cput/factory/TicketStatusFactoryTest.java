/*
 TicketStatusFactoryTest.java

 Factory Test class for TicketStatus

 Author: Abdullahi Raage Farah (230971091)

 Date: 26th June 2026
*/

package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.TicketStatus;
import za.ac.cput.domain.enums.StatusType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketStatusFactoryTest {

    @Test
    void testCreateTicketStatus_NullStatusType_ReturnsNull() {
        TicketStatus status = TicketStatusFactory.createTicketStatus(
                1,
                null,
                LocalDateTime.now(),
                null
        );
        assertNull(status);
    }

    @Test
    void testCreateTicketStatus_NullDate_ReturnsNull() {
        TicketStatus status = TicketStatusFactory.createTicketStatus(
                2,
                StatusType.OPEN,
                null,
                null
        );
        assertNull(status);
    }

    @Test
    void testCreateTicketStatus_FutureDate_ReturnsNull() {
        TicketStatus status = TicketStatusFactory.createTicketStatus(
                3,
                StatusType.OPEN,
                LocalDateTime.now().plusDays(1),
                null
        );
        assertNull(status);
    }
}