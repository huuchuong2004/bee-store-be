package vn.huuchuong.be_bee_store.product_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.product_module.payload.request.*;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductListResponse;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductResponse;
import vn.huuchuong.be_bee_store.product_module.service.ProductService;

@Tag(
        name = "Quản lý sản phẩm",
        description = "Nhóm API phục vụ quản lý sản phẩm, biến thể sản phẩm, hình ảnh sản phẩm và truy vấn sản phẩm theo danh mục"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Tạo mới sản phẩm",
            description = "API dành cho quản trị viên để tạo mới sản phẩm, bao gồm thông tin cơ bản như tên sản phẩm, mô tả, giá cơ bản, danh mục, biến thể và hình ảnh nếu có, Varrian khonog truyền SKU."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<ProductResponse>> create(
            @RequestBody CreateProductRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.createProduct(req), "Tạo sản phẩm thành công")
        );
    }

    @Operation(
            summary = "Lấy danh sách sản phẩm",
            description = "API lấy danh sách sản phẩm theo dạng phân trang. Hỗ trợ truyền các tham số page, size và sort. Mặc định sắp xếp theo productId giảm dần."
    )
    @GetMapping
    public ResponseEntity<BaseResponse<Page<ProductListResponse>>> getAllProduct(
            @PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<ProductListResponse> result = productService.findAll(pageable);
        return ResponseEntity.ok(BaseResponse.success(result, "Lấy danh sách thành công"));
    }

    @Operation(
            summary = "Lấy chi tiết sản phẩm",
            description = "API lấy thông tin chi tiết của một sản phẩm theo productId, bao gồm thông tin danh mục, danh sách biến thể và danh sách hình ảnh."
    )
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductResponse>> getDetail(@PathVariable Integer productId) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.getProductDetail(productId), "Lấy chi tiết thành công")
        );
    }

    @Operation(
            summary = "Cập nhật sản phẩm",
            description = "API dành cho quản trị viên để cập nhật thông tin sản phẩm theo productId. Có thể cập nhật tên, mô tả, giá cơ bản, danh mục hoặc các thông tin liên quan tùy theo dữ liệu request."
    )
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<ProductResponse>> update(
            @PathVariable Integer productId,
            @RequestBody UpdateProductRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.updateProduct(productId, req), "Cập nhật thành công")
        );
    }

    @Operation(
            summary = "Xóa sản phẩm",
            description = "API dành cho quản trị viên để xóa sản phẩm theo productId. Khi xóa sản phẩm, hệ thống có thể xóa kèm các biến thể và hình ảnh liên quan tùy theo cấu hình cascade trong entity."
    )
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(BaseResponse.success("Xóa sản phẩm thành công"));
    }

    // ====================== VARIANTS ==========================

    @Operation(
            summary = "Thêm biến thể sản phẩm",
            description = "API dành cho quản trị viên để thêm biến thể cho một sản phẩm cụ thể. Biến thể có thể bao gồm các thuộc tính như kích thước, màu sắc, số lượng tồn kho hoặc giá bán riêng."
    )
    @PostMapping("/{productId}/variants")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<ProductResponse>> addVariant(
            @PathVariable Integer productId,
            @RequestBody CreateProductVariantRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.createpv(productId, req), "Thêm biến thể thành công")
        );
    }

    @Operation(
            summary = "Cập nhật biến thể sản phẩm",
            description = "API dành cho quản trị viên để cập nhật thông tin một biến thể thuộc sản phẩm. Cần truyền productId và variantId để xác định đúng biến thể cần cập nhật."
    )
    @PutMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<ProductResponse>> updateVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId,
            @RequestBody UpdateProductVariantRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.updateVariant(productId, variantId, req),
                        "Cập nhật biến thể thành công")
        );
    }

    @Operation(
            summary = "Xóa biến thể sản phẩm",
            description = "API dành cho quản trị viên để xóa một biến thể khỏi sản phẩm. Cần truyền productId và variantId để đảm bảo biến thể thuộc đúng sản phẩm cần thao tác."
    )
    @DeleteMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId) {
        productService.deleteVariant(productId, variantId);
        return ResponseEntity.ok(BaseResponse.success("Xóa biến thể thành công"));
    }

    // ====================== IMAGES ==========================

    @Operation(
            summary = "Thêm hình ảnh cho sản phẩm",
            description = "API dành cho quản trị viên để thêm một hoặc nhiều hình ảnh cho sản phẩm theo productId. Request thường chứa danh sách URL hình ảnh cần thêm."
    )
    @PostMapping("/{productId}/images")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<ProductResponse>> addImages(
            @PathVariable Integer productId,
            @RequestBody AddProductImagesRequest req) {
        return ResponseEntity.ok(
                BaseResponse.success(productService.addImages(productId, req), "Thêm ảnh thành công")
        );
    }

    @Operation(
            summary = "Xóa hình ảnh sản phẩm",
            description = "API dành cho quản trị viên để xóa hình ảnh của sản phẩm. Có thể xóa bằng imageId hoặc imageUrl. Cần truyền ít nhất một trong hai tham số imageId hoặc imageUrl."
    )
    @DeleteMapping("/{productId}/images")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteImage(
            @PathVariable Integer productId,
            @RequestParam(required = false) Integer imageId,
            @RequestParam(required = false) String imageUrl) {

        if (imageId != null) {
            productService.deleteImage(productId, imageId);
        } else if (imageUrl != null) {
            productService.deleteImageByUrl(productId, imageUrl);
        } else {
            throw new BusinessException("Cần cung cấp imageId hoặc imageUrl để xóa");
        }

        return ResponseEntity.ok(BaseResponse.success("Xóa ảnh thành công"));
    }

    @Operation(
            summary = "Tìm kiếm và lọc sản phẩm",
            description = "API tìm kiếm sản phẩm theo các điều kiện lọc được truyền qua query params, ví dụ tên sản phẩm, danh mục, khoảng giá, màu sắc, kích thước hoặc các tiêu chí khác trong ProductFilter. Kết quả trả về theo dạng phân trang."
    )
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<ProductListResponse>>> search(
            @ModelAttribute ProductFilter productFilter,
            Pageable pageable) {

        Page<ProductListResponse> page = productService.search(productFilter, pageable);
        return ResponseEntity.ok(new BaseResponse<>(page, "Tìm kiếm thành công"));
    }

    @Operation(
            summary = "Lấy danh sách sản phẩm theo danh mục",
            description = "API lấy danh sách sản phẩm thuộc một danh mục cụ thể theo id danh mục. Có hỗ trợ phân trang thông qua page, size và sort."
    )
    @GetMapping("/categories/{id}/products")
    public ResponseEntity<BaseResponse<Page<ProductListResponse>>> getProductsByCategorys(
            @PathVariable Integer id,
            Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse<>(productService.getProductByCategpgys(id, pageable), "Lấy danh sách thành công")
        );
    }
}