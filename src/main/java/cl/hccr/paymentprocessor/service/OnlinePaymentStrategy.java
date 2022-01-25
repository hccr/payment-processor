package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.client.PaymentGatewayClient;
import cl.hccr.paymentprocessor.exception.CantValidatePaymentException;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = "online")
@RequiredArgsConstructor
public class OnlinePaymentStrategy implements PaymentStrategy {
  private final PaymentGatewayClient paymentGatewayClient;
  private final StorePaymentService storePaymentService;

  @Override
  public void process(PaymentMessage paymentMessage) {
    validatePayment(paymentMessage);
    storePayment(paymentMessage);
  }

  private void validatePayment(PaymentMessage paymentMessage) {
    try {
      paymentGatewayClient.validatePayment(paymentMessage);
    } catch (FeignException e) {
      throw new CantValidatePaymentException(paymentMessage, e);
    }
  }

  private void storePayment(PaymentMessage paymentMessage) {
    storePaymentService.store(paymentMessage);
  }
}
