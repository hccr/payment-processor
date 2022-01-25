package cl.hccr.paymentprocessor.exception;

public class CantConvertToPaymentEntityException extends RuntimeException {

  private static final String MESSAGE = "Can't convert payment message to payment entity";

  public CantConvertToPaymentEntityException() {
    super(MESSAGE);
  }
}
