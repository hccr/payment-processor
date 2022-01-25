package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;

public interface StorePaymentService {
  void store(PaymentMessage paymentMessage);
}
