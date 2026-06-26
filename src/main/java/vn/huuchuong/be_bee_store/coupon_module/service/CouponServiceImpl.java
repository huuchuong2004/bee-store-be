package vn.huuchuong.be_bee_store.coupon_module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.huuchuong.be_bee_store.coupon_module.entity.Coupon;
import vn.huuchuong.be_bee_store.coupon_module.payload.request.CreateCouponRequest;
import vn.huuchuong.be_bee_store.coupon_module.payload.response.CouponResponse;
import vn.huuchuong.be_bee_store.coupon_module.repository.CouponRepository;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.order_module.entity.Order;
import vn.huuchuong.be_bee_store.order_module.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl  implements CouponService {

    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    @Override
    public Page<CouponResponse> getCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public CouponResponse createCoupon(CreateCouponRequest request) {

        if (couponRepository.findByCouponCode(request.getCouponCode()).isPresent()) {
            throw new BusinessException("Đã Tồn tại mã giảm giá");
        }

        Coupon coupon = new Coupon();
        coupon.setCouponCode(request.getCouponCode());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setStartDate(LocalDate.now());
        coupon.setEndDate(request.getEndDate());
        coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
        coupon.setMaxUsage(request.getMaxUsage());
        coupon.setMaxUsagePerUser(request.getMaxUsagePerUser());
        coupon.setCurrentUsage(0);
        Coupon savedCoupon = couponRepository.save(coupon);
        return toResponse(savedCoupon);

    }

    @Override
    @Transactional
    public Boolean deleteCoupon(Integer id) {
        Coupon couponToDelete = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tồn tại mã giảm giá"));


        List<Order> ordersUsingCoupon = orderRepository.findByCoupon(couponToDelete);
        for (Order order : ordersUsingCoupon) {
            order.setCoupon(null);
        }
        orderRepository.saveAll(ordersUsingCoupon);

        couponRepository.delete(couponToDelete);

        return true;
    }


    private CouponResponse toResponse(Coupon c) {
        return CouponResponse.builder()
                .couponId(c.getCouponId())
                .couponCode(c.getCouponCode())
                .discountValue(c.getDiscountValue())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .minimumOrderAmount(c.getMinimumOrderAmount())
                .maxUsage(c.getMaxUsage())
                .maxUsagePerUser(c.getMaxUsagePerUser())
                .currentUsage(c.getCurrentUsage())
                .build();
    }
}