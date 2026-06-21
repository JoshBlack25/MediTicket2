/*
 Payment.java

 Payment POJO class

 Author: Abdullahi Raage Farah (230971091)

 Date: 21st June 2026
*/

package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    private BigDecimal paymentAmount;
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true) // Maps the foreign key column to Appointment
    private Appointment appointment;

    protected Payment() {
        // Required by JPA
    }

    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.paymentAmount = builder.paymentAmount;
        this.paymentDate = builder.paymentDate;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
        this.appointment = builder.appointment;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentAmount=" + paymentAmount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod=" + paymentMethod +
                ", paymentStatus=" + paymentStatus +
                ", appointment=" + appointment +
                '}';
    }

    public static class Builder {
        private int paymentId;
        private BigDecimal paymentAmount;
        private LocalDateTime paymentDate;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private Appointment appointment;

        public Builder setPaymentId(int paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setPaymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder setPaymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setAppointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public Builder copy(Payment payment) {
            this.paymentId = payment.paymentId;
            this.paymentAmount = payment.paymentAmount;
            this.paymentDate = payment.paymentDate;
            this.paymentMethod = payment.paymentMethod;
            this.paymentStatus = payment.paymentStatus;
            this.appointment = payment.appointment;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
