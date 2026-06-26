package vn.huuchuong.be_bee_store.payment_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.order_module.entity.Order;
import vn.huuchuong.be_bee_store.payment_module.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {



    List<Payment> findByOrder(Order order);
}

