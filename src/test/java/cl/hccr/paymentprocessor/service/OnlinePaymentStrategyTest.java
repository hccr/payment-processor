package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.client.PaymentGatewayClient;
import cl.hccr.paymentprocessor.exception.CantValidatePaymentException;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OnlinePaymentStrategyTest {

  @Mock private StorePaymentService storePaymentService;
  @Mock private PaymentGatewayClient paymentGatewayClient;
  @InjectMocks private OnlinePaymentStrategy onlinePaymentStrategy;

  @Test
  void shouldProcessMessage() {
    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    onlinePaymentStrategy.process(paymentMessage);

    Mockito.verify(paymentGatewayClient, Mockito.times(1)).validatePayment(paymentMessage);
    Mockito.verify(storePaymentService, Mockito.times(1)).store(paymentMessage);
  }

  @Test
  void shouldThrowPaymentNotValidException() {
    var mockException = Mockito.mock(FeignException.class);

    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    Mockito.when(mockException.status()).thenReturn(504);
    Mockito.when(paymentGatewayClient.validatePayment(paymentMessage)).thenThrow(mockException);

    var exception =
        Assertions.assertThrows(
            CantValidatePaymentException.class,
            () -> onlinePaymentStrategy.process(paymentMessage));

    Assertions.assertEquals(
        "Can't validate payment 59e7be16-ee7c-4edf-91b0-4ccafb73ff97, status code 504",
        exception.getMessage());

    Mockito.verify(storePaymentService, Mockito.never()).store(paymentMessage);
  }
}
