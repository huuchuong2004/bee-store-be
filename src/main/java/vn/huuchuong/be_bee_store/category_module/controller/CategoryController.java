package vn.huuchuong.be_bee_store.category_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import vn.huuchuong.be_bee_store.category_module.payload.request.UpdateCategoryRequest;
import vn.huuchuong.be_bee_store.category_module.service.CategoryService;

import java.util.List;
@Tag(
        name = "Quản lý Danh Mục",
        description = "Nhóm API phục vụ quản lý liên quan đến Danh Mục, bao gồm tạo mới, xóa, lấy danh sách danh mục và cây danh mục"
)

@RestController
@RequestMapping("/api/v1/categorys")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Lấy Tất Cả Danh Mục", description = "API lấy ra tất cả các danh mục , kể cả danh mục con")
    public ResponseEntity<BaseResponse<Page<Category>>> getAllCategory(Pageable pageable) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findAll(pageable), "Lấy Danh Sách Thành Công"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy Cây Danh Mục", description = "API Lấy Cây Danh Mục Theo ID Cha")
    public ResponseEntity<BaseResponse<List<Category>>> getCategoryById(@PathVariable int id) { // Lấy Cây Category
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findByParent(id), null));
    }

    @GetMapping("/root")
    @Operation(summary = "Lấy Tất Cả Danh Mục Gốc )", description = "API : Lấy Tất Cả Danh Mục Gốc (Không phụ thuộc vào Danh Mục nào)")
    public ResponseEntity<BaseResponse<List<Category>>> getCategoryRoot() { // Lấy Các Category Gốc
        return ResponseEntity.ok(new BaseResponse<>(categoryService.findRoots(), null));
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Tạo Category", description = "API tạo Category mới, chỉ admin mới có quyền tạo")
    public ResponseEntity<BaseResponse<Category>> addCategory(@RequestBody CreateCategoryRequest category) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.create(category), "Tao thanh cong"));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Xóa Category", description = "API xóa Category, chỉ admin mới có quyền xóa ( Xóa Mềm ) ")
    public ResponseEntity<BaseResponse<Boolean>> deleteCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.delete(id), "Xoa thanh cong"));

    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @Operation(
            summary = "Lấy danh sách Category For Admin",
            description = "API lấy danh sách tất cả Category dành cho ADMIN và STAFF"
    )
    public ResponseEntity<BaseResponse<List<Category>>> getAllCategoryForAdmin() {
        return ResponseEntity.ok(
                new BaseResponse<>(categoryService.findAllForAdmin(), "Lấy danh sách thành công")
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @Operation(
            summary = "Cập nhật Category",
            description = "API cập nhật thông tin Category theo id. ADMIN và STAFF có quyền cập nhật tên, mô tả, danh mục cha hoặc trạng thái hoạt động."
    )
    public ResponseEntity<BaseResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(
                new BaseResponse<>(categoryService.update(id, request), "Cập nhật danh mục thành công")
        );
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @Operation(
            summary = "Khôi phục Category",
            description = "API khôi phục Category đã bị xóa mềm bằng cách chuyển trạng thái isActive từ false sang true. Nếu Category có danh mục con, hệ thống sẽ khôi phục cả các danh mục con."
    )
    public ResponseEntity<BaseResponse<Boolean>> restoreCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(
                new BaseResponse<>(categoryService.restore(id), "Khôi phục danh mục thành công")
        );
    }


}