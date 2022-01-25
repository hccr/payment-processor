package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;

public interface PaymentStrategyManager {
  void process(PaymentMessage paymentMessage);
}
