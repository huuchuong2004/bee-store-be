package vn.huuchuong.be_bee_store.category_module.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.category_module.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Lấy tất cả category không có parent
    List<Category> findAllByParentIsNull();

    // Lấy tất cả category con theo parent id
    List<Category> findAllByParentId(Integer parentId);
}
