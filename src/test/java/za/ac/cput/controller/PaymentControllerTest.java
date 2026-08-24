/*
 PaymentControllerTest.java

 Controller Test class for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 18th July 2026
*/

package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;

    @BeforeEach
    void setup() {
        payment = new Payment.Builder()
                .setPaymentId(1)
                .setPaymentAmount(new BigDecimal("500.00"))
                .setPaymentDate(LocalDateTime.of(2026, 7, 18, 10, 30))
                .setPaymentMethod(PaymentMethod.CARD)
                .setPaymentStatus(PaymentStatus.PAID)
                .build();
    }

    @Test
    void testCreate() {
        when(paymentService.create(payment)).thenReturn(payment);
        Payment result = paymentController.create(payment);
        assertNotNull(result);
        verify(paymentService, times(1)).create(payment);
    }

    @Test
    void testRead() {
        when(paymentService.read(1)).thenReturn(payment);
        Payment result = paymentController.read(1);
        assertNotNull(result);
        verify(paymentService, times(1)).read(1);
    }

    @Test
    void testUpdate() {
        when(paymentService.update(payment)).thenReturn(payment);
        Payment result = paymentController.update(payment);
        assertNotNull(result);
        verify(paymentService, times(1)).update(payment);
    }

    @Test
    void testDelete() {
        doNothing().when(paymentService).delete(1);
        paymentController.delete(1);
        verify(paymentService, times(1)).delete(1);
    }

    @Test
    void testGetAll() {
        when(paymentService.getAll()).thenReturn(List.of(payment));
        List<Payment> result = paymentController.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentService, times(1)).getAll();
    }

    @Test
    void testFindByPaymentStatus() {
        when(paymentService.findByPaymentStatus(PaymentStatus.PAID)).thenReturn(List.of(payment));
        List<Payment> result = paymentController.findByPaymentStatus(PaymentStatus.PAID);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(paymentService, times(1)).findByPaymentStatus(PaymentStatus.PAID);
    }

    @Test
    void testFindByPaymentMethod() {
        when(paymentService.findByPaymentMethod(PaymentMethod.CARD)).thenReturn(List.of(payment));
        List<Payment> result = paymentController.findByPaymentMethod(PaymentMethod.CARD);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(paymentService, times(1)).findByPaymentMethod(PaymentMethod.CARD);
    }
}