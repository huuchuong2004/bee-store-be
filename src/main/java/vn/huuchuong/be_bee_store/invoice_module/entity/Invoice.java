package vn.huuchuong.be_bee_store.invoice_module.entity;


import jakarta.persistence.*;
import lombok.*;
import vn.huuchuong.be_bee_store.base.BaseEntity;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    // 🔹 Mỗi invoice thuộc 1 order
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;


    @Column(nullable = false, length = 255)
    private String buyerName;

    @Column(nullable = false, length = 500)
    private String buyerAddress;

    @Column(nullable = false, length = 255)
    private String buyerEmail;

    @Column(nullable = false, length = 20)
    private String buyerPhone;

    // 🔹 1 invoice có nhiều item
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items;
}