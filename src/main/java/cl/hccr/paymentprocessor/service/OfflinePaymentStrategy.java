package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/** The Offline implementation of {@link PaymentStrategy} */
@Service(value = "offline")
@RequiredArgsConstructor
public class OfflinePaymentStrategy implements PaymentStrategy {
  private final StorePaymentService storePaymentService;
  /**
   * Process an Offline payment.
   *
   * @param paymentMessage a Offline Payment Message
   */
  @Override
  public void process(PaymentMessage paymentMessage) {

    storePaymentService.store(paymentMessage);
  }
}
