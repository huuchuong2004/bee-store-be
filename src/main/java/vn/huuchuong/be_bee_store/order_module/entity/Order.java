package vn.huuchuong.be_bee_store.order_module.entity;


import jakarta.persistence.*;
import lombok.*;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.base.BaseEntity;
import vn.huuchuong.be_bee_store.coupon_module.entity.Coupon;
import vn.huuchuong.be_bee_store.order_module.Enum.OrderStatus;
import vn.huuchuong.be_bee_store.payment_module.entity.Payment;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Payment> payments;



    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

}

