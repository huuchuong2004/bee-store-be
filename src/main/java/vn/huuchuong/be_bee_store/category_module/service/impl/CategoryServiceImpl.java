package vn.huuchuong.be_bee_store.category_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.payload.request.CreateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.repository.CategoryRepository;
import vn.huuchuong.be_bee_store.category_module.service.CategoryService;
import vn.huuchuong.be_bee_store.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<Category> categories = null;
        categories=categoryRepository.findAll(pageable);
        return categories;
    }

    @Override
    public List<Category> findByParent(int id) {

        return categoryRepository.findAllByParentId(id);

    }

    @Override
    public List<Category> findRoots() {
        return categoryRepository.findAllByParentIsNull();
    }

    @Override
    public Category create(CreateCategoryRequest category) {
        Category cate = new Category();
        cate.setName(category.getName());
        cate.setDescription(category.getDescription());

        if (category.getParent() != null) {
            Category parent = categoryRepository.findById(category.getParent())
                    .orElseThrow(() -> new RuntimeException("Category Cha Không Tìm Thấy"));

            parent.addChild(cate);

        }

        return categoryRepository.save(cate);
    }

    @Override
    public Boolean delete(Integer id) {
        // 1. Tìm category, nếu không có thì báo lỗi rõ ràng
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found with id = " + id));

        // 2. Không cho xoá nếu vẫn còn category con
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            throw new BusinessException("Không thể xoá category vì vẫn còn category con");
        }


//         if (productRepository.existsByCategoryId(id)) {
//             throw new BusinessException("Không thể xoá category vì đang được sử dụng bởi sản phẩm");
//         }


        categoryRepository.delete(category);
        return true;
    }
}
