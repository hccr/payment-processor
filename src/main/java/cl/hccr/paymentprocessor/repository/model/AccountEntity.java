package cl.hccr.paymentprocessor.repository.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int accountId;

  private String name;
  private String email;
  private LocalDate birthdate;
  private Timestamp lastPaymentDate;
  private Timestamp createdOn;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private List<PaymentEntity> payments;
}
