package cl.hccr.paymentprocessor.repository;

import cl.hccr.paymentprocessor.repository.model.PaymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, String> {}
