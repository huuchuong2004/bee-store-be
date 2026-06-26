package vn.huuchuong.be_bee_store.product_module.spec;


import org.springframework.data.jpa.domain.Specification;
import vn.huuchuong.be_bee_store.product_module.entity.Product;


import java.math.BigDecimal;

public class ProductSpectification {
    public static Specification<Product> hasName(String name) {

        // cu phap , root , query , builder
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%");

        };

    }

    public static Specification<Product> hasCategory(Integer id) {

        // cu phap , root , query , builder
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), "%" + id + "%");

        };

    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) return null;
            // So sánh trực tiếp số: baseprice >= minPrice
            return criteriaBuilder.greaterThanOrEqualTo(root.get("baseprice"), minPrice);
        };
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) return null;
            // So sánh trực tiếp số: baseprice <= maxPrice
            return criteriaBuilder.lessThanOrEqualTo(root.get("baseprice"), maxPrice);
        };
    }
}