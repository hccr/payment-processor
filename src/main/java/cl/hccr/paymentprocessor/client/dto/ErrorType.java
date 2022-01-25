package cl.hccr.paymentprocessor.client.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorType {
  DATABASE("database"),
  NETWORK("network"),
  OTHER("other");

  private final String value;

  @JsonValue
  public String getValue() {
    return value;
  }
}
