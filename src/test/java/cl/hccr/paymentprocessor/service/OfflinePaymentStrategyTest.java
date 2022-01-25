package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OfflinePaymentStrategyTest {

  @InjectMocks private OfflinePaymentStrategy offlinePaymentStrategy;
  @Mock private StorePaymentService storePaymentService;

  @Test
  void shouldProcessMessage() {
    PaymentMessage expectedPaymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("offline")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    offlinePaymentStrategy.process(expectedPaymentMessage);

    Mockito.verify(storePaymentService, Mockito.times(1)).store(expectedPaymentMessage);
  }
}
