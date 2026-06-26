package vn.huuchuong.be_bee_store.coupon_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.coupon_module.entity.Coupon;
import vn.huuchuong.be_bee_store.coupon_module.entity.CouponUsage;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {
    long countByCoupon(Coupon coupon);

    long countByCouponAndUser(Coupon coupon, User user);

    boolean existsByOrder(Order order);
}