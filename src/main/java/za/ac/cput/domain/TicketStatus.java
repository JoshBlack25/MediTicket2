package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import za.ac.cput.domain.enums.StatusType;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status")
public class TicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    private LocalDateTime statusDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @JsonBackReference
    private PatientTicket ticket;


    @Column(length = 2000)
    private String notes;

    protected TicketStatus() {
        // Required by JPA
    }

    private TicketStatus(Builder builder) {
        this.statusId = builder.statusId;
        this.statusType = builder.statusType;
        this.statusDate = builder.statusDate;
        this.ticket = builder.ticket;
        this.notes = builder.notes;
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

    public String getNotes() {
        return notes;
    }

    public boolean isEscalated() {
        return this.statusType == StatusType.ESCALATED;
    }

    public boolean isClosed() {
        return this.statusType == StatusType.CLOSED;
    }

    @Override
    public String toString() {
        return "TicketStatus{" +
                "statusId=" + statusId +
                ", statusType=" + statusType +
                ", statusDate=" + statusDate +
                ", notes='" + notes + '\'' +
                '}';
    }

    public static class Builder {
        private int statusId;
        private StatusType statusType;
        private LocalDateTime statusDate;
        private PatientTicket ticket;
        private String notes;

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

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder copy(TicketStatus status) {
            this.statusId = status.statusId;
            this.statusType = status.statusType;
            this.statusDate = status.statusDate;
            this.ticket = status.ticket;
            this.notes = status.notes;
            return this;
        }

        public TicketStatus build() {
            return new TicketStatus(this);
        }
    }
}