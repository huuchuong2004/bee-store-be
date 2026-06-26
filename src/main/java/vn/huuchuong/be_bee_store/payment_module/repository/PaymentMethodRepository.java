package vn.huuchuong.be_bee_store.payment_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentMethodType;
import vn.huuchuong.be_bee_store.payment_module.entity.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

    Optional<PaymentMethod> findByCode(PaymentMethodType code);
}