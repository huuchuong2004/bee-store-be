package vn.huuchuong.be_bee_store.category_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.payload.request.CreateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.payload.request.UpdateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.payload.resposne.CategoryResponse;

import java.util.List;

public interface CategoryService {
    Page<CategoryResponse> findAll(Pageable pageable);

    List<CategoryResponse> findByParent(int id);

    List<CategoryResponse> findRoots();

    CategoryResponse create(CreateCategoryRequest request);

    Boolean delete(Integer id);

    List<CategoryResponse> findAllForAdmin();

    CategoryResponse update(Integer id, UpdateCategoryRequest request);

    Boolean restore(Integer id);
}
