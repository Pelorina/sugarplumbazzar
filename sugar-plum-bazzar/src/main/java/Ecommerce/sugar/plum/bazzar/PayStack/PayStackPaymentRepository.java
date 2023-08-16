package Ecommerce.sugar.plum.bazzar.PayStack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayStackPaymentRepository extends JpaRepository<PaymentEntity,Long > {
}
