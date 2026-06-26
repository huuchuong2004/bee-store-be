package vn.huuchuong.be_bee_store.product_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.product_module.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
}
