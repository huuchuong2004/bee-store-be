package vn.huuchuong.be_bee_store.product_module.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.huuchuong.be_bee_store.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_variant")
public class ProductVariant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_variant_id")
    private Integer productVariantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 100)
    private String sku;

    @Column(length = 50)
    private String size;

    @Column(length = 50)
    private String color;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;
}