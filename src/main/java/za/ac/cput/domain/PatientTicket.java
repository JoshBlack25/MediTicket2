package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.domain.user.Patient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient_ticket")
public class PatientTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    private String ticketDescription;
    private LocalDateTime ticketCreatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TicketStatus> statusHistory;

    // NEW — denormalized copy of the latest statusHistory entry's type,
    // kept in sync exclusively inside addStatus(). Exists purely so
    // PatientTicketRepository can query "give me all tickets currently
    // OPEN" as an indexed column lookup instead of a JPQL scan over
    // statusHistory. statusHistory remains the source of truth for
    // history/auditing — this field is a read-optimization only.
    @Enumerated(EnumType.STRING)
    private StatusType currentStatus;

    protected PatientTicket() {
        this.statusHistory = new ArrayList<>();
    }

    private PatientTicket(Builder builder) {
        this.ticketId = builder.ticketId;
        this.ticketDescription = builder.ticketDescription;
        this.ticketCreatedDate = builder.ticketCreatedDate;
        this.patient = builder.patient;
        this.appointment = builder.appointment;
        this.statusHistory = new ArrayList<>();
        this.currentStatus = null; // set via addStatus(), never at construction
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public LocalDateTime getTicketCreatedDate() {
        return ticketCreatedDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public List<TicketStatus> getStatusHistory() {
        return statusHistory;
    }

    public void addStatus(StatusType statusType) {
        addStatus(statusType, null);
    }

    public void addStatus(StatusType statusType, String notes) {
        if (this.statusHistory == null) {
            this.statusHistory = new ArrayList<>();
        }

        TicketStatus status = new TicketStatus.Builder()
                .setStatusType(statusType)
                .setStatusDate(LocalDateTime.now())
                .setTicket(this)
                .setNotes(notes)
                .build();

        this.statusHistory.add(status);
        this.currentStatus = statusType;
    }

    // Unchanged in behavior — still derives from statusHistory, so it stays
    // correct even for any row where currentStatus hasn't been backfilled yet.
    public StatusType getCurrentStatus() {
        if (statusHistory.isEmpty()) {
            return null;
        }
        return statusHistory.get(statusHistory.size() - 1).getStatusType();
    }

    public void assignAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public String toString() {
        return "PatientTicket{" +
                "ticketId=" + ticketId +
                ", ticketDescription='" + ticketDescription + '\'' +
                ", ticketCreatedDate=" + ticketCreatedDate +
                ", patient=" + patient +
                ", appointment=" + appointment +
                ", currentStatus=" + currentStatus +
                '}';
    }

    public static class Builder {
        private int ticketId;
        private String ticketDescription;
        private LocalDateTime ticketCreatedDate;
        private Patient patient;
        private Appointment appointment;

        public Builder setTicketId(int ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public Builder setTicketDescription(String ticketDescription) {
            this.ticketDescription = ticketDescription;
            return this;
        }

        public Builder setTicketCreatedDate(LocalDateTime ticketCreatedDate) {
            this.ticketCreatedDate = ticketCreatedDate;
            return this;
        }

        public Builder setPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public Builder setAppointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public Builder copy(PatientTicket ticket) {
            this.ticketId = ticket.ticketId;
            this.ticketDescription = ticket.ticketDescription;
            this.ticketCreatedDate = ticket.ticketCreatedDate;
            this.patient = ticket.patient;
            this.appointment = ticket.appointment;
            return this;
        }

        public PatientTicket build() {
            return new PatientTicket(this);
        }
    }
}