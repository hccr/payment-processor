package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  private PaymentStrategy retrievePaymentStrategy(String paymentType) {
    return Optional.ofNullable(paymentStrategyMap.get(paymentType))
        .orElseThrow(
            () -> new RuntimeException(String.format("Strategy %s not found", paymentType)));
  }
}
