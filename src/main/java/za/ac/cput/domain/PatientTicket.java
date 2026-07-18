/*
 PatientTicket.java

 PatientTicket POJO class

 Author: Joshua Reid Adams (230317693)

 Date: 21st June 2026
*/

package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.user.User;

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
    @JoinColumn(name = "patient_id") // Maps patient foreign key column
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true) // Maps appointment foreign key column
    private Appointment appointment;

    // mappedBy points to the 'ticket' field inside the TicketStatus class
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TicketStatus> statusHistory;

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
        TicketStatus status = new TicketStatus.Builder()
                .setStatusType(statusType)
                .setStatusDate(LocalDateTime.now())
                .setTicket(this)
                .build();

        this.statusHistory.add(status);
    }

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

        public Builder setUser(User user) {
            this.patient = (Patient) user;
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
