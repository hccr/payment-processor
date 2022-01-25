package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;

/**
 * Define a Strategy Manager contract to implement
 */
public interface PaymentStrategyManager {
  void process(PaymentMessage paymentMessage);
}
