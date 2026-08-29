package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.ConfirmationStatus;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private ConfirmationStatus confirmationStatus;

    // NEW — the patient this appointment belongs to. Required at creation:
    // without this there's no way to trace a PENDING appointment back to
    // whoever booked it, since PatientTicket (the other patient link) only
    // gets created once a nurse approves.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private ClinicStaff staff;

    // NEW — the patient's stated reason for booking, captured at creation time.
    @Column(length = 1000)
    private String reason;

    protected Appointment() {
        // Required by JPA
    }

    private Appointment(Builder builder) {
        this.appointmentId = builder.appointmentId;
        this.appointmentDate = builder.appointmentDate;
        this.appointmentTime = builder.appointmentTime;
        this.confirmationStatus = builder.confirmationStatus;
        this.patient = builder.patient;
        this.doctor = builder.doctor;
        this.staff = builder.staff;
        this.reason = builder.reason;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public ClinicStaff getStaff() {
        return staff;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", confirmationStatus=" + confirmationStatus +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", staff=" + staff +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static class Builder {
        private int appointmentId;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private ConfirmationStatus confirmationStatus;
        private Patient patient;
        private Doctor doctor;
        private ClinicStaff staff;
        private String reason;

        public Builder setAppointmentId(int appointmentId) {
            this.appointmentId = appointmentId;
            return this;
        }

        public Builder setAppointmentDate(LocalDate appointmentDate) {
            this.appointmentDate = appointmentDate;
            return this;
        }

        public Builder setAppointmentTime(LocalTime appointmentTime) {
            this.appointmentTime = appointmentTime;
            return this;
        }

        public Builder setConfirmationStatus(ConfirmationStatus confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
            return this;
        }

        public Builder setPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public Builder setDoctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public Builder setStaff(ClinicStaff staff) {
            this.staff = staff;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder copy(Appointment appointment) {
            this.appointmentId = appointment.appointmentId;
            this.appointmentDate = appointment.appointmentDate;
            this.appointmentTime = appointment.appointmentTime;
            this.confirmationStatus = appointment.confirmationStatus;
            this.patient = appointment.patient;
            this.doctor = appointment.doctor;
            this.staff = appointment.staff;
            this.reason = appointment.reason;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }
}