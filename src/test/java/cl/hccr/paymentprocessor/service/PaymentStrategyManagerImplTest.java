package cl.hccr.paymentprocessor.service;

import static org.mockito.Mockito.*;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentStrategyManagerImplTest {
  @Mock private Map<String, PaymentStrategy> paymentStrategyMapMock;
  @Mock private PaymentStrategy paymentStrategyMock;
  @InjectMocks private PaymentStrategyManagerImpl paymentStrategyManager;

  @Test
  void shouldProcessPaymentMessage() {
    PaymentMessage expectedPaymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    when(paymentStrategyMapMock.get("online")).thenReturn(paymentStrategyMock);

    paymentStrategyManager.process(expectedPaymentMessage);

    verify(paymentStrategyMock, times(1)).process(expectedPaymentMessage);
  }

  @Test
  void shouldThrowRunTimeException() {
    PaymentMessage expectedPaymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("unknown")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    when(paymentStrategyMapMock.get("unknown")).thenReturn(null);

    var exception =
        Assertions.assertThrows(
            RuntimeException.class, () -> paymentStrategyManager.process(expectedPaymentMessage));

    Assertions.assertEquals("Strategy unknown not found", exception.getMessage());
  }
}
