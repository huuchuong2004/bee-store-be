package vn.huuchuong.be_bee_store.coupon_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.coupon_module.payload.request.CreateCouponRequest;
import vn.huuchuong.be_bee_store.coupon_module.payload.response.CouponResponse;

public interface CouponService {
    Page<CouponResponse> getCoupons(Pageable pageable);

    CouponResponse createCoupon(CreateCouponRequest request);


    Boolean deleteCoupon(Integer id);
}
