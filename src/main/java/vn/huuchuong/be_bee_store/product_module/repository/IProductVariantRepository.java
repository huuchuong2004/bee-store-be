package vn.huuchuong.be_bee_store.product_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;

public interface IProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    boolean existsBySku(String sku);
}
