package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = "offline")
@RequiredArgsConstructor
public class OfflinePaymentStrategy implements PaymentStrategy {
  private final StorePaymentService storePaymentService;

  @Override
  public void process(PaymentMessage paymentMessage) {

    storePaymentService.store(paymentMessage);
  }
}
