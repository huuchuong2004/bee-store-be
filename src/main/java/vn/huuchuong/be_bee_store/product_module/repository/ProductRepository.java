package vn.huuchuong.be_bee_store.product_module.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.product_module.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findByCategory_IdIn(List<Integer> ids, Pageable pageable);

    Page<Product> findAll(Specification spec, Pageable pageable);
}
