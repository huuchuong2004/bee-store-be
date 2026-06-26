package vn.huuchuong.be_bee_store.category_module.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.category_module.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Lấy tất cả category không có parent
    List<Category> findAllByParentIsNull();
    boolean existsByParentIdAndIsActiveTrue(Integer parentId);

    // Lấy tất cả category con theo parent id
    List<Category> findAllByParentId(Integer parentId);
    Page<Category> findAllByIsActiveTrue(Pageable pageable);

    Optional<Category> findByIdAndIsActiveTrue(Integer id);

    List<Category> findAllByParentIdAndIsActiveTrue(Integer parentId);

    List<Category> findAllByParentIsNullAndIsActiveTrue();

    boolean existsByNameAndIsActiveTrue(String name);

    boolean existsByNameAndIdNotAndIsActiveTrue(String newName, Integer id);
}
