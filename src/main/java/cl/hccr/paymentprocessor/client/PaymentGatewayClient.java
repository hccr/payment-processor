package cl.hccr.paymentprocessor.client;

import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PaymentGatewayClient", url = "${external-services.payment-gateway.url}")
public interface PaymentGatewayClient {

  @PostMapping("/payment")
  ResponseEntity<Void> validatePayment(PaymentMessage paymentMessage);
}
