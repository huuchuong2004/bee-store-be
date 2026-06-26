package vn.huuchuong.be_bee_store.product_module.payload.response;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {
    private Integer productVariantId;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer quantityInStock;
    private String sku;
}
