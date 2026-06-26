package vn.huuchuong.be_bee_store.invoice_module.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.huuchuong.be_bee_store.order_module.entity.OrderItem;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // 🔹 Mỗi invoice item dựa trên một order item
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal total;
}