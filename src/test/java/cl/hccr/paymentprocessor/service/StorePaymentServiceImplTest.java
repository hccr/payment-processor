package cl.hccr.paymentprocessor.service;

import cl.hccr.paymentprocessor.converter.PaymentMessageToPaymentEntityConverter;
import cl.hccr.paymentprocessor.exception.AccountNotFoundException;
import cl.hccr.paymentprocessor.repository.AccountRepository;
import cl.hccr.paymentprocessor.repository.PaymentRepository;
import cl.hccr.paymentprocessor.repository.model.AccountEntity;
import cl.hccr.paymentprocessor.repository.model.PaymentEntity;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StorePaymentServiceImplTest {

  @InjectMocks private StorePaymentServiceImpl storePaymentService;
  @Mock private AccountRepository accountRepository;
  @Mock private PaymentRepository paymentRepository;
  @Mock private PaymentMessageToPaymentEntityConverter paymentEntityConverter;

  @Captor private ArgumentCaptor<AccountEntity> accountEntityArgumentCaptor;
  @Captor private ArgumentCaptor<PaymentEntity> paymentEntityArgumentCaptor;

  @Test
  void shouldStorePaymentAndUpdatePaymentDate() {

    int accountId = 1701;

    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(accountId)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    AccountEntity accountEntity = AccountEntity.builder().accountId(accountId).build();

    PaymentEntity paymentEntity =
        PaymentEntity.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
    Mockito.when(paymentEntityConverter.convert(paymentMessage)).thenReturn(paymentEntity);

    storePaymentService.store(paymentMessage);

    Mockito.verify(accountRepository, Mockito.times(1)).save(accountEntityArgumentCaptor.capture());
    Mockito.verify(paymentRepository, Mockito.times(1)).save(paymentEntityArgumentCaptor.capture());

    var capturedAccountEntity = accountEntityArgumentCaptor.getValue();
    var capturedPaymentEntity = paymentEntityArgumentCaptor.getValue();

    Assertions.assertNotNull(capturedAccountEntity.getLastPaymentDate());
    Assertions.assertNotNull(capturedPaymentEntity.getCreatedOn());
    Assertions.assertEquals(
        capturedAccountEntity.getLastPaymentDate(), capturedPaymentEntity.getCreatedOn());
    Assertions.assertEquals(capturedAccountEntity, capturedPaymentEntity.getAccount());
  }

  @Test
  void shouldThrowAccountNotFoundException() {
    int accountId = 17011;
    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(accountId)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();
    Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

    var exception =
        Assertions.assertThrows(
            AccountNotFoundException.class, () -> storePaymentService.store(paymentMessage));

    Assertions.assertEquals("Account 17011 not found in database", exception.getMessage());
  }
}
