package cl.hccr.paymentprocessor.repository;

import cl.hccr.paymentprocessor.repository.model.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {}
