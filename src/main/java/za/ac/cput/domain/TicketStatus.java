/*
 TicketStatus.java

 TicketStatus POJO class

 Author: Joshua Reid Adams (230317693)

 Date: 21st June 2026
*/

package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.StatusType;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status")
public class TicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    @Enumerated(EnumType.STRING) // Saves enum text value in database
    private StatusType statusType;

    private LocalDateTime statusDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id") // Maps the foreign key back to the parent ticket
    private PatientTicket ticket;

    protected TicketStatus() {
        // Required by JPA
    }

    private TicketStatus(Builder builder) {
        this.statusId = builder.statusId;
        this.statusType = builder.statusType;
        this.statusDate = builder.statusDate;
        this.ticket = builder.ticket;
    }

    public int getStatusId() {
        return statusId;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public LocalDateTime getStatusDate() {
        return statusDate;
    }

    public PatientTicket getTicket() {
        return ticket;
    }

    public boolean isEscalated() {
        return this.statusType == StatusType.ESCALATED;
    }

    public boolean isClosed() {
        return this.statusType == StatusType.CLOSED;
    }

    @Override
    public String toString() {
        // Removed direct ticket tracking string to avoid infinite recursive call stack loops
        return "TicketStatus{" +
                "statusId=" + statusId +
                ", statusType=" + statusType +
                ", statusDate=" + statusDate +
                '}';
    }

    public static class Builder {
        private int statusId;
        private StatusType statusType;
        private LocalDateTime statusDate;
        private PatientTicket ticket;

        public Builder setStatusId(int statusId) {
            this.statusId = statusId;
            return this;
        }

        public Builder setStatusType(StatusType statusType) {
            this.statusType = statusType;
            return this;
        }

        public Builder setStatusDate(LocalDateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        public Builder setTicket(PatientTicket ticket) {
            this.ticket = ticket;
            return this;
        }

        public Builder copy(TicketStatus status) {
            this.statusId = status.statusId;
            this.statusType = status.statusType;
            this.statusDate = status.statusDate;
            this.ticket = status.ticket;
            return this;
        }

        public TicketStatus build() {
            return new TicketStatus(this);
        }
    }
}
