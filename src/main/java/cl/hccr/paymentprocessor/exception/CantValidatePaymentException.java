package cl.hccr.paymentprocessor.exception;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import feign.FeignException;

public class CantValidatePaymentException extends RuntimeException {
  private static final String MESSAGE = "Can't validate payment %s, status code %s";

  public CantValidatePaymentException(
      PaymentMessage paymentMessage, FeignException feignException) {
    super(
        String.format(MESSAGE, paymentMessage.getPaymentId(), feignException.status()),
        feignException);
  }
}
