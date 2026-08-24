/*
 PaymentController.java

 REST Controller for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 18th July 2026
*/

package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;
import za.ac.cput.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Payment create(@RequestBody Payment payment) {
        return service.create(payment);
    }

    @GetMapping("/read/{id}")
    public Payment read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public Payment update(@RequestBody Payment payment) {
        return service.update(payment);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/getall")
    public List<Payment> getAll() {
        return service.getAll();
    }

    @GetMapping("/status/{status}")
    public List<Payment> findByPaymentStatus(@PathVariable PaymentStatus status) {
        return service.findByPaymentStatus(status);
    }

    @GetMapping("/method/{method}")
    public List<Payment> findByPaymentMethod(@PathVariable PaymentMethod method) {
        return service.findByPaymentMethod(method);
    }
}