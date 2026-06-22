package za.ac.cput.factory;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.util.Helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentFactory {

    public static Payment createPayment(int paymentId,
                                        BigDecimal paymentAmount,
                                        LocalDateTime paymentDate,
                                        PaymentMethod paymentMethod,
                                        PaymentStatus paymentStatus,
                                        Appointment appointment) {

        if (!Helper.isValidInt(paymentId)) return null;
        if (!Helper.isValidObject(paymentAmount)) return null;
        if (!Helper.isValidObject(paymentDate)) return null;
        if (!Helper.isValidObject(paymentMethod)) return null;
        if (!Helper.isValidObject(paymentStatus)) return null;
        if (!Helper.isValidObject(appointment)) return null;

        // extra business rule: payment must be positive
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) return null;

        return new Payment.Builder()
                .setPaymentId(paymentId)
                .setPaymentAmount(paymentAmount)
                .setPaymentDate(paymentDate)
                .setPaymentMethod(paymentMethod)
                .setPaymentStatus(paymentStatus)
                .setAppointment(appointment)
                .build();
    }
}
