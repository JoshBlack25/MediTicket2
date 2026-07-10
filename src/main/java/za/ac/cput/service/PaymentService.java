/*
 PaymentService.java

 Service class for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 10th July 2026
*/

package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.repository.PaymentRepository;
import za.ac.cput.service.impl.IPaymentService;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository repository;

    @Override
    public Payment create(Payment payment) {
        if (payment == null) return null;
        return repository.save(payment);
    }

    @Override
    public Payment read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        if (payment == null) return null;
        return repository.save(payment);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Payment> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Payment> findByPaymentStatus(PaymentStatus paymentStatus) {
        return repository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Payment> findByPaymentMethod(PaymentMethod paymentMethod) {
        return repository.findByPaymentMethod(paymentMethod);
    }
}