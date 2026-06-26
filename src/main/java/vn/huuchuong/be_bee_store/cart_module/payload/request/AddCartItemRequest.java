package vn.huuchuong.be_bee_store.cart_module.payload.request;

import lombok.Data;

@Data
public class AddCartItemRequest {

    private Integer productVariantId;
    private Integer quantity;
}