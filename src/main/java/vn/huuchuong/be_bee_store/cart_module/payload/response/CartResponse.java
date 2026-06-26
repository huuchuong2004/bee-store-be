package vn.huuchuong.be_bee_store.cart_module.payload.response;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {

    private Integer cartId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount; // tổng tiền giỏ
}
