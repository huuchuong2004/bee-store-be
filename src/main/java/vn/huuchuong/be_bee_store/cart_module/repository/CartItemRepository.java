package vn.huuchuong.be_bee_store.cart_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.cart_module.entity.Cart;
import vn.huuchuong.be_bee_store.cart_module.entity.CartItem;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant variant);

    void deleteAllByCart(Cart cart);
}
