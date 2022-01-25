package cl.hccr.paymentprocessor.handler;

import cl.hccr.paymentprocessor.client.LogSystemClient;
import cl.hccr.paymentprocessor.client.dto.ErrorType;
import cl.hccr.paymentprocessor.client.dto.LogMessageDto;
import cl.hccr.paymentprocessor.exception.AccountNotFoundException;
import cl.hccr.paymentprocessor.exception.CantValidatePaymentException;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumerErrorHandler implements KafkaListenerErrorHandler {
  private static final String UNKNOWN_ID = "UNKNOWN_ID";
  private final LogSystemClient logSystemClient;
  private final ObjectMapper objectMapper;

  @Override
  public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
    var paymentId = retrievePaymentId(message);

    var logMessageBuilder = LogMessageDto.builder();
    logMessageBuilder.paymentId(paymentId);

    var cause = exception.getCause();

    if (cause instanceof AccountNotFoundException) {
      logMessageBuilder.errorDescription(cause.getMessage()).errorType(ErrorType.DATABASE);
    } else if (cause instanceof CantValidatePaymentException) {
      logMessageBuilder.errorDescription(cause.getMessage()).errorType(ErrorType.NETWORK);
    } else {
      logMessageBuilder.errorDescription(cause.getMessage()).errorType(ErrorType.OTHER);
    }

    var logMessageDto = logMessageBuilder.build();
    try {

      logSystemClient.postLog(logMessageDto);

    } catch (Exception e) {
      log.error(String.format("Can't post log %s", logMessageDto.toString()), e);
    }
    return null;
  }

  private String retrievePaymentId(Message<?> message) {
    try {
      if (message.getPayload() instanceof String) {
        String messageString = (String) message.getPayload();
        var paymentMessage = objectMapper.readValue(messageString, PaymentMessage.class);
        return paymentMessage.getPaymentId();
      } else {
        log.error("Unknown payload format");
      }
    } catch (Exception e) {
      log.error("Can't retrieve payment id from message", e);
    }

    return UNKNOWN_ID;
  }
}
