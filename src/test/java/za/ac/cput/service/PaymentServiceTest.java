/*
 PaymentServiceTest.java

 Service Test class for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 12th July 2026
*/

package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setup() {
        payment = new Payment.Builder()
                .setPaymentId(1)
                .setPaymentAmount(new BigDecimal("500.00"))
                .setPaymentDate(LocalDateTime.of(2026, 7, 12, 10, 30))
                .setPaymentMethod(PaymentMethod.CARD)
                .setPaymentStatus(PaymentStatus.PAID)
                .build();
    }

    @Test
    void testCreate() {
        when(paymentRepository.save(payment)).thenReturn(payment);
        Payment result = paymentService.create(payment);
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testRead() {
        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
        Payment result = paymentService.read(1);
        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    void testUpdate() {
        when(paymentRepository.save(payment)).thenReturn(payment);
        Payment result = paymentService.update(payment);
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testDelete() {
        doNothing().when(paymentRepository).deleteById(1);
        paymentService.delete(1);
        verify(paymentRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAll() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        List<Payment> result = paymentService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testFindByPaymentStatus() {
        when(paymentRepository.findByPaymentStatus(PaymentStatus.PAID)).thenReturn(List.of(payment));
        List<Payment> result = paymentService.findByPaymentStatus(PaymentStatus.PAID);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(paymentRepository, times(1)).findByPaymentStatus(PaymentStatus.PAID);
    }

    @Test
    void testFindByPaymentMethod() {
        when(paymentRepository.findByPaymentMethod(PaymentMethod.CARD)).thenReturn(List.of(payment));
        List<Payment> result = paymentService.findByPaymentMethod(PaymentMethod.CARD);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(paymentRepository, times(1)).findByPaymentMethod(PaymentMethod.CARD);
    }
}