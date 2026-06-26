package vn.huuchuong.be_bee_store.product_module.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.huuchuong.be_bee_store.product_module.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByProductIdAndDeletedFalse(Integer productId);

    Page<Product> findByDeletedFalse(Pageable pageable);

    Page<Product> findByDeletedTrue(Pageable pageable);

    Page<Product> findAllBy(Pageable pageable);

    Optional<Product> findByProductId(Integer productId);
    Page<Product> findByCategory_IdInAndDeletedFalse(List<Integer> categoryIds, Pageable pageable);
}