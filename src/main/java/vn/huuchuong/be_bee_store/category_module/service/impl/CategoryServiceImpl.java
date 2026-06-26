package vn.huuchuong.be_bee_store.category_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.payload.request.CreateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.payload.request.UpdateCategoryRequest;
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
        Page<Category> categories = categoryRepository.findAllByIsActiveTrue(pageable);

        if (categories.isEmpty()) {
            throw new BusinessException("Không tìm thấy danh mục nào");
        }

        return categories;
    }

    @Override
    public List<Category> findByParent(int id) {
        Category parent = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy danh mục cha = " + id));

        return categoryRepository.findAllByParentIdAndIsActiveTrue(parent.getId());
    }

    @Override
    public List<Category> findRoots() {
        return categoryRepository.findAllByParentIsNullAndIsActiveTrue();
    }

    @Override
    @Transactional
    public Category create(CreateCategoryRequest request) {
        if (categoryRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new BusinessException("Tên danh mục đã tồn tại");
        }

        Category cate = new Category();
        cate.setName(request.getName());
        cate.setDescription(request.getDescription());
        cate.setIsActive(true);

        if (request.getParent() != null) {
            Category parent = categoryRepository.findByIdAndIsActiveTrue(request.getParent())
                    .orElseThrow(() -> new BusinessException("Category cha không tìm thấy"));

            parent.addChild(cate);
        }

        return categoryRepository.save(cate);
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException("Category không có trong hệ thống = " + id));

        if (categoryRepository.existsByParentIdAndIsActiveTrue(id)) {
            throw new BusinessException("Không thể xóa danh mục vì đang có danh mục con hoạt động");
        }

        category.setIsActive(false);
        categoryRepository.save(category);

        return true;
    }

    @Override
    public List<Category> findAllForAdmin() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new BusinessException("Không tìm thấy danh mục nào");
        }

        return categories;
    }

    @Override
    @Transactional
    public Category update(Integer id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy danh mục = " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();

            if (categoryRepository.existsByNameAndIdNotAndIsActiveTrue(newName, id)) {
                throw new BusinessException("Tên danh mục đã tồn tại");
            }

            category.setName(newName);
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription().trim());
        }

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BusinessException("Danh mục cha không được trùng với chính danh mục hiện tại");
            }

            Category parent = categoryRepository.findByIdAndIsActiveTrue(request.getParentId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy danh mục cha = " + request.getParentId()));

            category.setParent(parent);
        }

        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Boolean restore(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category không có trong hệ thống = " + id));

        if (Boolean.TRUE.equals(category.getIsActive())) {
            throw new BusinessException("Category này đang hoạt động, không cần khôi phục");
        }

        restoreRecursive(category);

        categoryRepository.save(category);
        return true;
    }

    private void restoreRecursive(Category category) {
        category.setIsActive(true);

        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                restoreRecursive(child);
            }
        }
    }

    private void softDeleteRecursive(Category category) {
        category.setIsActive(false);

        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                if (Boolean.TRUE.equals(child.getIsActive())) {
                    softDeleteRecursive(child);
                }
            }
        }
    }
}