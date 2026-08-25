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
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.domain.enums.StatusType;
import za.ac.cput.repository.PatientTicketRepository;
import za.ac.cput.repository.PaymentRepository;
import za.ac.cput.service.impl.IPaymentService;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository repository;

    // NEW — needed to auto-close the ticket tied to this payment's
    // appointment once the payment is confirmed PAID. This is a
    // cross-domain write living in PaymentService rather than the
    // controller, so every caller of update() gets correct behavior
    // automatically instead of relying on each caller to remember to
    // close the ticket separately.
    @Autowired
    private PatientTicketRepository patientTicketRepository;

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

        Payment saved = repository.save(payment);
        closeTicketIfPaid(saved);
        return saved;
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

    // NEW — isolated as a private helper so update() stays readable.
    private void closeTicketIfPaid(Payment payment) {
        if (payment.getPaymentStatus() != PaymentStatus.PAID || payment.getAppointment() == null) {
            return;
        }

        PatientTicket ticket = patientTicketRepository.findByAppointment(payment.getAppointment());
        if (ticket == null || ticket.getCurrentStatus() == StatusType.CLOSED) {
            return; // no ticket yet, or already closed — nothing to do
        }

        ticket.addStatus(StatusType.CLOSED, "Closed automatically — payment received");
        patientTicketRepository.save(ticket);
    }
}