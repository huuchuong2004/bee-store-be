package vn.huuchuong.be_bee_store.category_module.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.payload.request.CreateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorys")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Category>>> getAllCategory(Pageable pageable) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findAll(pageable), "Lấy Danh Sách Thành Công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<List<Category>>> getCategoryById(@PathVariable int id) { // Lấy Cây Category
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findByParent(id), null));
    }

    @GetMapping("/root")
    public ResponseEntity<BaseResponse<List<Category>>> getCategoryRoot() { // Lấy Các Category Gốc
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findRoots(), null));
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Category>> addCategory(@RequestBody CreateCategoryRequest category) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.create(category), "Tao thanh cong"));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Boolean>> deleteCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.delete(id), "Xoa thanh cong"));

    }
}