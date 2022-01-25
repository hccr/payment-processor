package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.converter.PaymentMessageToPaymentEntityConverter;
import cl.hccr.paymentprocessor.exception.AccountNotFoundException;
import cl.hccr.paymentprocessor.exception.CantConvertToPaymentEntityException;
import cl.hccr.paymentprocessor.repository.AccountRepository;
import cl.hccr.paymentprocessor.repository.PaymentRepository;
import cl.hccr.paymentprocessor.repository.model.AccountEntity;
import cl.hccr.paymentprocessor.repository.model.PaymentEntity;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorePaymentServiceImpl implements StorePaymentService {

  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;
  private final PaymentMessageToPaymentEntityConverter paymentMessageToEntityConverter;

  @Override
  @Transactional
  public void store(PaymentMessage paymentMessage) {
    var accountEntity = retrieveAccountById(paymentMessage.getAccountId());
    var paymentEntity = convertToPaymentEntity(paymentMessage);

    var now = LocalDateTime.now();

    accountEntity.setLastPaymentDate(Timestamp.valueOf(now));

    paymentEntity.setCreatedOn(Timestamp.valueOf(now));
    paymentEntity.setAccount(accountEntity);

    saveAccountEntity(accountEntity);
    savePaymentEntity(paymentEntity);
  }

  private AccountEntity retrieveAccountById(int accountId) {
    return accountRepository
        .findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(accountId));
  }

  private PaymentEntity convertToPaymentEntity(PaymentMessage paymentMessage) {
    return Optional.ofNullable(paymentMessageToEntityConverter.convert(paymentMessage))
        .orElseThrow(CantConvertToPaymentEntityException::new);
  }

  private void savePaymentEntity(PaymentEntity paymentEntity) {
    paymentRepository.save(paymentEntity);
  }

  private void saveAccountEntity(AccountEntity accountEntity) {
    accountRepository.save(accountEntity);
  }
}
