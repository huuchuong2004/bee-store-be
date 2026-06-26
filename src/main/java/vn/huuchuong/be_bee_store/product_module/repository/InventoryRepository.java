package vn.huuchuong.be_bee_store.product_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.huuchuong.be_bee_store.product_module.entity.Inventory;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByProductVariant(ProductVariant variant);

}
