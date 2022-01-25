package cl.hccr.paymentprocessor.converter;

import cl.hccr.paymentprocessor.repository.model.PaymentEntity;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageToPaymentEntityConverter
    implements Converter<PaymentMessage, PaymentEntity> {

  /**
   * Convert PaymentMessage into PaymentEntity.
   * @param source a payment message
   * @return a payment entity
   */

  @Override
  public PaymentEntity convert(PaymentMessage source) {

    return PaymentEntity.builder()
        .paymentId(source.getPaymentId())
        .paymentType(source.getPaymentType())
        .amount(source.getAmount())
        .creditCard(source.getCreditCard())
        .build();
  }
}
