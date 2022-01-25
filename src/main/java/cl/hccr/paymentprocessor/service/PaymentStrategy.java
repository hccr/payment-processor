package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;

public interface PaymentStrategy {
  void process(PaymentMessage paymentMessage);
}
