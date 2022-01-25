package cl.hccr.paymentprocessor.repository.model;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity {

  @Id
  @Column(nullable = false)
  private String paymentId;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private AccountEntity account;

  private String paymentType;

  private String creditCard;

  private int amount;

  private Timestamp createdOn;
}
