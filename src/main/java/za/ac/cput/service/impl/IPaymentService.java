
/*
 IPaymentService.java

 Service interface for Payment

 Author: Abdullahi Raage Farah (230971091)

 Date: 10th July 2026
*/

package za.ac.cput.service.impl;

import za.ac.cput.domain.Payment;
import za.ac.cput.domain.enums.PaymentMethod;
import za.ac.cput.domain.enums.PaymentStatus;

import java.util.List;

public interface IPaymentService extends IService<Payment, Integer> {

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

}