package vn.huuchuong.be_bee_store.coupon_module.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CouponResponse {

    private Integer couponId;
    private String couponCode;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minimumOrderAmount;
    private Integer maxUsage;
    private Integer maxUsagePerUser;
    private Integer currentUsage;
}