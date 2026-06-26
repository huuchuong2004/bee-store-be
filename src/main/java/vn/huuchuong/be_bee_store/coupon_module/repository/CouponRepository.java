package vn.huuchuong.be_bee_store.coupon_module.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.huuchuong.be_bee_store.coupon_module.entity.Coupon;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCouponCode(String code);

    boolean existsByCouponCodeAndDeletedFalse(String couponCode);

    @Query(
            value = "SELECT * FROM coupon",
            countQuery = "SELECT COUNT(*) FROM coupon",
            nativeQuery = true
    )
    Page<Coupon> findAllForAdmin(Pageable pageable);
}

