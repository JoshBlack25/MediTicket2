/*
 PaymentFactoryTest.java

 Factory Test class for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 26th June 2026
*/

package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {

    @Test
    void testCreatePayment_NullAmount_ReturnsNull() {
        Payment payment = PaymentFactory.createPayment(
                1,
                null,
                LocalDateTime.now(),
                PaymentMethod.CASH,
                PaymentStatus.PENDING,
                null
        );
        assertNull(payment);
    }

    @Test
    void testCreatePayment_NullMethod_ReturnsNull() {
        Payment payment = PaymentFactory.createPayment(
                2,
                new BigDecimal("200.00"),
                LocalDateTime.now(),
                null,
                PaymentStatus.PAID,
                null
        );
        assertNull(payment);
    }

    @Test
    void testCreatePayment_NegativeAmount_ReturnsNull() {
        Payment payment = PaymentFactory.createPayment(
                3,
                new BigDecimal("-50.00"),
                LocalDateTime.now(),
                PaymentMethod.CARD,
                PaymentStatus.PAID,
                null
        );
        assertNull(payment);
    }
}