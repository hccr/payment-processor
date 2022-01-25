package cl.hccr.paymentprocessor.converter;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentMessageToPaymentEntityConverterTest {

  @Test
  void shouldConvertSuccessfully() {

    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1701)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    PaymentMessageToPaymentEntityConverter converter = new PaymentMessageToPaymentEntityConverter();

    var paymentEntity = converter.convert(paymentMessage);

    Assertions.assertNotNull(paymentEntity);
    Assertions.assertEquals(paymentMessage.getPaymentId(), paymentEntity.getPaymentId());
    Assertions.assertEquals(paymentMessage.getPaymentType(), paymentEntity.getPaymentType());
    Assertions.assertEquals(paymentMessage.getAmount(), paymentEntity.getAmount());
    Assertions.assertEquals(paymentMessage.getCreditCard(), paymentEntity.getCreditCard());
  }
}
