package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;

/**
 * Define a Payment Strategy contract to implement.
 */
public interface PaymentStrategy {
  void process(PaymentMessage paymentMessage);
}
