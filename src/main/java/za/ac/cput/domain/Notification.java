package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private String notificationMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private PatientTicket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private LocalDateTime notificationDate;

    protected Notification() {
    }

    private Notification(Builder builder) {
        this.notificationId = builder.notificationId;
        this.notificationType = builder.notificationType;
        this.notificationStatus = builder.notificationStatus;
        this.notificationMessage = builder.notificationMessage;
        this.notificationDate = builder.notificationDate;
        this.appointment = builder.appointment;
        this.patient = builder.patient;
        this.ticket = builder.ticket;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public Patient getPatient() {
        return patient;
    }

    public PatientTicket getTicket() {
        return ticket;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", notificationType=" + notificationType +
                ", notificationStatus=" + notificationStatus +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", patient=" + patient +
                ", ticket=" + ticket +
                ", appointment=" + appointment +
                ", notificationDate=" + notificationDate +
                '}';
    }

    public static class Builder {
        private int notificationId;
        private NotificationType notificationType;
        private NotificationStatus notificationStatus;
        private String notificationMessage;
        private Patient patient;
        private Appointment appointment;
        private PatientTicket ticket;
        private LocalDateTime notificationDate;

        public Builder setNotificationId(int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public Builder setNotificationStatus(NotificationStatus notificationStatus) {
            this.notificationStatus = notificationStatus;
            return this;
        }

        public Builder setNotificationMessage(String notificationMessage) {
            this.notificationMessage = notificationMessage;
            return this;
        }

        public Builder setTicket(PatientTicket ticket) {
            this.ticket = ticket;
            return this;
        }

        public Builder setAppointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public Builder setNotificationDate(LocalDateTime notificationDate) {
            this.notificationDate = notificationDate;
            return this;
        }

        public Builder setPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public Builder copy(Notification notification) {
            this.notificationId = notification.notificationId;
            this.notificationType = notification.notificationType;
            this.notificationStatus = notification.notificationStatus;
            this.notificationMessage = notification.notificationMessage;
            this.patient = notification.patient;
            this.ticket = notification.ticket;
            this.appointment = notification.appointment;
            this.notificationDate = notification.notificationDate;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}