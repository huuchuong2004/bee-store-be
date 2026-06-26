package vn.huuchuong.be_bee_store.coupon_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.coupon_module.entity.Coupon;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCouponCode(String code);
}
