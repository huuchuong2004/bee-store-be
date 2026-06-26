package vn.huuchuong.be_bee_store.cart_module.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartItemResponse {

    private Integer cartItemId;
    private Integer productVariantId;
    private String productName;
    private String size;
    private String color;
    private BigDecimal price;   // giá 1 cái theo varriant
    private Integer quantity;
    private BigDecimal subtotal; // price * quantity
    private List<String> images;
}
