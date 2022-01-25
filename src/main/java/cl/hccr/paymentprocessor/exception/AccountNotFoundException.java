package cl.hccr.paymentprocessor.exception;

public class AccountNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Account %d not found in database";

  public AccountNotFoundException(int accountId) {
    super(String.format(MESSAGE, accountId));
  }
}
