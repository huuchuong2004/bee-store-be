package vn.huuchuong.be_bee_store.category_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.payload.request.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);

    List<Category> findByParent(int id);

    List<Category> findRoots();

    Category create(CreateCategoryRequest category);

    Boolean delete(Integer id);
}
