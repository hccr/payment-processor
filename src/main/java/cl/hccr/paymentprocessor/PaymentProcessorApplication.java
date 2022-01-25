package cl.hccr.paymentprocessor;

import feign.Retryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class PaymentProcessorApplication {

  /**
   * Create a Retryer bean to retry retryable errors with feign client.
   * @return Retryer
   */
  @Bean
  public Retryer retryer() {
    return new Retryer.Default();
  }

  public static void main(String[] args) {
    SpringApplication.run(PaymentProcessorApplication.class, args);
  }
}
