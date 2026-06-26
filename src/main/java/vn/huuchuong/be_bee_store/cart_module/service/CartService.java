package vn.huuchuong.be_bee_store.cart_module.service;

import vn.huuchuong.be_bee_store.cart_module.payload.request.AddCartItemRequest;
import vn.huuchuong.be_bee_store.cart_module.payload.request.UpdateCartItemRequest;
import vn.huuchuong.be_bee_store.cart_module.payload.response.CartResponse;

public interface CartService {
    CartResponse getMyCart();

    CartResponse addItem(AddCartItemRequest request);

    CartResponse updateItem(Integer cartItemId, UpdateCartItemRequest request);

    CartResponse removeItem(Integer cartItemId);

    CartResponse clearMyCart();
}
