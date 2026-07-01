package vn.huuchuong.be_bee_store.coupon_module.payload.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Boolean deleted ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lastModifiedBy;
    private String createdBy;
}