package cl.hccr.paymentprocessor.client;

import cl.hccr.paymentprocessor.client.dto.LogMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "LogSystemClient", url = "${external-services.log-system.url}")
public interface LogSystemClient {

  @PostMapping("/log")
  ResponseEntity<Void> postLog(LogMessageDto logMessageDto);
}
