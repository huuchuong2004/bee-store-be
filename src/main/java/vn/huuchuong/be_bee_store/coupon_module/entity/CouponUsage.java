package vn.huuchuong.be_bee_store.coupon_module.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "coupon_usage",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_coupon_usage_order",
                        columnNames = "order_id"
                )
        }
)
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponUsageId;

    // --- Coupon ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)

    private Coupon coupon;

    // --- User ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Order (unique: 1 order = 1 usage) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private LocalDateTime usedAt;
}