package vn.huuchuong.be_bee_store.cart_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.cart_module.entity.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUser(User user);
    // 🔹 Lấy cart + items + variant + product bằng 1 query
    @Query("""
           select distinct c 
           from Cart c
           left join fetch c.items ci
           left join fetch ci.productVariant v
           left join fetch v.product p
           where c.user = :user
           """)
    Optional<Cart> findByUserFetchItems(User user);
}
