package cl.hccr.paymentprocessor.service;

import static org.mockito.Mockito.*;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentConsumerServiceTest {
  private static final String TEST_MESSAGE =
      "{\"paymentId\":\"59e7be16-ee7c-4edf-91b0-4ccafb73ff97\", \"accountId\":1701, \"paymentType\":\"online\",\"creditCard\":\"6011826617911914\", \"amount\":52}";

  @Mock private ObjectMapper objectMapperMock;
  @Mock private PaymentStrategyManager paymentStrategyManagerMock;
  @InjectMocks private PaymentConsumerService paymentConsumerService;

  @Test
  void shouldProcessPaymentMessage() throws JsonProcessingException {

    PaymentMessage expectedPaymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    when(objectMapperMock.readValue(TEST_MESSAGE, PaymentMessage.class))
        .thenReturn(expectedPaymentMessage);

    paymentConsumerService.processMessage(TEST_MESSAGE);

    verify(paymentStrategyManagerMock, times(1)).process(expectedPaymentMessage);
  }
}
