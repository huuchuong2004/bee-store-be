package vn.huuchuong.be_bee_store.coupon_module.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import vn.huuchuong.be_bee_store.base.BaseEntity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "coupon",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_coupon_code", columnNames = "coupon_code")
        }
)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

    @Column(name = "coupon_code", nullable = false, length = 100)
    private String couponCode;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private LocalDate startDate;



    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal minimumOrderAmount;

    @Column(nullable = false)
    private Integer maxUsage;

    @Column(nullable = false)
    private Integer maxUsagePerUser;

    @Column(nullable = false)
    private Integer currentUsage;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<CouponUsage> couponUsages;

}