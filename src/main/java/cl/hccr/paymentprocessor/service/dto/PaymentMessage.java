package cl.hccr.paymentprocessor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMessage {
  @JsonProperty(value = "payment_id")
  private String paymentId;

  @JsonProperty(value = "account_id")
  private int accountId;

  @JsonProperty(value = "payment_type")
  private String paymentType;

  @JsonProperty(value = "credit_card")
  private String creditCard;

  private int amount;
}
