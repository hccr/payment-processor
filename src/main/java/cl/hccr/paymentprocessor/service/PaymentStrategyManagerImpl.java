package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * A Map based implementation of {@link PaymentStrategyManager}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStrategyManagerImpl implements PaymentStrategyManager {
  private final Map<String, PaymentStrategy> paymentStrategyMap;

  @Override
  public void process(PaymentMessage paymentMessage) {
    log.debug(paymentMessage.toString());
    retrievePaymentStrategy(paymentMessage.getPaymentType()).process(paymentMessage);
  }

  /**
   * It retrieves the {@link PaymentStrategy} according the payment type.
   * @param paymentType a payment type [online , offline]
   * @return a payment strategy, or throw {@link RuntimeException} if not found
   */
  private PaymentStrategy retrievePaymentStrategy(String paymentType) {
    return Optional.ofNullable(paymentStrategyMap.get(paymentType))
        .orElseThrow(
            () -> new RuntimeException(String.format("Strategy %s not found", paymentType)));
  }
}
