package cl.hccr.paymentprocessor.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LogMessageDto {
  @JsonProperty("payment_id")
  private String paymentId;

  @JsonProperty("error_type")
  private ErrorType errorType;

  @JsonProperty("error_description")
  private String errorDescription;
}
