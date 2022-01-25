package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumerService {
  private final PaymentStrategyManager paymentStrategyManager;
  private final ObjectMapper objectMapper;

  @KafkaListener(
      topics = {"online", "offline"},
      groupId = "payment-processor-id",
      errorHandler = "paymentConsumerErrorHandler")
  public void processMessage(String paymentMessage) throws JsonProcessingException {

    log.debug(String.format("Processing payment %s", paymentMessage));

    paymentStrategyManager.process(objectMapper.readValue(paymentMessage, PaymentMessage.class));
  }
}
