# Challenge

## :computer: How to execute

To run this program please use the command `./gradlew bootRun ` to run using the default profile or use `./gradlew bootRun --args='--spring.profiles.active=dev'` to run the program with dev profile (Logger level set to DEBUG).  
_Please make sure to run the provided infrastructure before running this program_  

Unit Test were developed, to check the report please run `./gradlew clean test pitest` and it will generate a report under build folder, one folder for mutation test and other for unit test.


## :memo: Notes
I used a Layered Application Architecture to solve the assessment. The main layers are **Service**, **Repository**, and **Client**.  
One of the first decisions was to treat Online and Offline Payments as the same and delegate the corresponding processing to the PaymentStrategy, an implementation of Strategy Pattern.

The PaymentConsumerErrorHandler, an implementation of KafkaListenerErrorHandler, enables me to catch every exception during a message's processing and generate the corresponding logs to the LogSystem.

The running simulation had various trick, for example network failures on LogSystem, therefore I declared a "retryer" to retry certain request defined as "Retryable"

### Language and frameworks used ###
- Java 11
- Springboot 2.6.3
- Spring Data JPA
- Spring Kafka
- Spring Cloud OpenFeign
- Lombok

## :pushpin: Things to improve

1. I should improve the Kafka configuration and error handling. Messages with errors (database or networks) were received 2 times, so there were logged twice.
2. The message could be saved as Json so a JsonDeserializer could deserialize without problems.
