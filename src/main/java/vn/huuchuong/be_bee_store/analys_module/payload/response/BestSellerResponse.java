package vn.huuchuong.be_bee_store.analys_module.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSellerResponse {
    private Integer productVariantId;
    private String productName;
    private String sku;
    private String size;
    private String color;
    private Long quantitySold;
    private BigDecimal revenue;
}