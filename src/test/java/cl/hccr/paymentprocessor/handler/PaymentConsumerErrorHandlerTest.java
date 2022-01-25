package cl.hccr.paymentprocessor.handler;

import cl.hccr.paymentprocessor.client.LogSystemClient;
import cl.hccr.paymentprocessor.client.dto.ErrorType;
import cl.hccr.paymentprocessor.client.dto.LogMessageDto;
import cl.hccr.paymentprocessor.exception.AccountNotFoundException;
import cl.hccr.paymentprocessor.exception.CantValidatePaymentException;
import cl.hccr.paymentprocessor.service.dto.PaymentMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@ExtendWith(MockitoExtension.class)
class PaymentConsumerErrorHandlerTest {
  private static final String TEST_MESSAGE =
      "{\"paymentId\":\"59e7be16-ee7c-4edf-91b0-4ccafb73ff97\", \"accountId\":1000, \"paymentType\":\"online\",\"creditCard\":\"6011826617911914\", \"amount\":52}";

  @InjectMocks private PaymentConsumerErrorHandler paymentConsumerErrorHandler;
  @Mock private Message<?> messageMock;
  @Mock private ListenerExecutionFailedException exceptionMock;
  @Mock private ObjectMapper objectMapperMock;
  @Mock private LogSystemClient logSystemClient;
  @Captor private ArgumentCaptor<LogMessageDto> logMessageDtoCaptor;

  @ParameterizedTest
  @MethodSource("provideArgumentsToHandleErrors")
  void shouldHandleErrorGivenExceptions(
      Class<? extends RuntimeException> classToMock, ErrorType expectedErrorType)
      throws JsonProcessingException {
    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1000)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    var causeExceptionMock = Mockito.mock(classToMock);

    Mockito.when(messageMock.getPayload()).thenAnswer(argument -> TEST_MESSAGE);
    Mockito.when(objectMapperMock.readValue(TEST_MESSAGE, PaymentMessage.class))
        .thenReturn(paymentMessage);
    Mockito.when(exceptionMock.getCause()).thenReturn(causeExceptionMock);
    Mockito.when(causeExceptionMock.getMessage()).thenReturn("SOME CAUSE");

    paymentConsumerErrorHandler.handleError(messageMock, exceptionMock);

    Mockito.verify(logSystemClient, Mockito.times(1)).postLog(logMessageDtoCaptor.capture());

    var capturedLogMessage = logMessageDtoCaptor.getValue();

    Assertions.assertNotNull(capturedLogMessage);
    Assertions.assertEquals(
        "59e7be16-ee7c-4edf-91b0-4ccafb73ff97", capturedLogMessage.getPaymentId());
    Assertions.assertEquals(expectedErrorType, capturedLogMessage.getErrorType());
    Assertions.assertEquals("SOME CAUSE", capturedLogMessage.getErrorDescription());
  }

  @Test
  void shouldCatchLogSystemException() throws JsonProcessingException {
    PaymentMessage paymentMessage =
        PaymentMessage.builder()
            .paymentId("59e7be16-ee7c-4edf-91b0-4ccafb73ff97")
            .accountId(1000)
            .paymentType("online")
            .amount(52)
            .creditCard("6011826617911914")
            .build();

    var causeExceptionMock = Mockito.mock(AccountNotFoundException.class);

    Mockito.when(messageMock.getPayload()).thenAnswer(argument -> TEST_MESSAGE);
    Mockito.when(objectMapperMock.readValue(TEST_MESSAGE, PaymentMessage.class))
        .thenReturn(paymentMessage);
    Mockito.when(exceptionMock.getCause()).thenReturn(causeExceptionMock);
    Mockito.when(causeExceptionMock.getMessage()).thenReturn("SOME CAUSE");
    Mockito.when(logSystemClient.postLog(ArgumentMatchers.any(LogMessageDto.class)))
        .thenThrow(Mockito.mock(FeignException.class));

    var result = paymentConsumerErrorHandler.handleError(messageMock, exceptionMock);
    Assertions.assertNull(result);
  }

  @Test
  void shouldReturnUnknownIdGivenMalformedJson() throws JsonProcessingException {
    var causeExceptionMock = Mockito.mock(AccountNotFoundException.class);

    Mockito.when(messageMock.getPayload()).thenAnswer(argument -> "");
    Mockito.when(objectMapperMock.readValue("", PaymentMessage.class))
        .thenThrow(Mockito.mock(JsonProcessingException.class));
    Mockito.when(exceptionMock.getCause()).thenReturn(causeExceptionMock);
    Mockito.when(causeExceptionMock.getMessage()).thenReturn("SOME CAUSE");

    paymentConsumerErrorHandler.handleError(messageMock, exceptionMock);

    Mockito.verify(logSystemClient, Mockito.times(1)).postLog(logMessageDtoCaptor.capture());

    var capturedLogMessage = logMessageDtoCaptor.getValue();

    Assertions.assertNotNull(capturedLogMessage);
    Assertions.assertEquals("UNKNOWN_ID", capturedLogMessage.getPaymentId());
    Assertions.assertEquals(ErrorType.DATABASE, capturedLogMessage.getErrorType());
    Assertions.assertEquals("SOME CAUSE", capturedLogMessage.getErrorDescription());
  }

  @Test
  void shouldReturnUnknownIdGivenNonStringMessage() {
    var causeExceptionMock = Mockito.mock(AccountNotFoundException.class);

    Mockito.when(messageMock.getPayload()).thenAnswer(argument -> new Object());
    Mockito.when(exceptionMock.getCause()).thenReturn(causeExceptionMock);
    Mockito.when(causeExceptionMock.getMessage()).thenReturn("SOME CAUSE");

    paymentConsumerErrorHandler.handleError(messageMock, exceptionMock);

    Mockito.verify(logSystemClient, Mockito.times(1)).postLog(logMessageDtoCaptor.capture());

    var capturedLogMessage = logMessageDtoCaptor.getValue();

    Assertions.assertNotNull(capturedLogMessage);
    Assertions.assertEquals("UNKNOWN_ID", capturedLogMessage.getPaymentId());
    Assertions.assertEquals(ErrorType.DATABASE, capturedLogMessage.getErrorType());
    Assertions.assertEquals("SOME CAUSE", capturedLogMessage.getErrorDescription());
  }

  private static Stream<Arguments> provideArgumentsToHandleErrors() {
    return Stream.of(
        Arguments.of(AccountNotFoundException.class, ErrorType.DATABASE),
        Arguments.of(CantValidatePaymentException.class, ErrorType.NETWORK),
        Arguments.of(RuntimeException.class, ErrorType.OTHER));
  }
}
