package vn.huuchuong.be_bee_store.cart_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.cart_module.payload.request.AddCartItemRequest;
import vn.huuchuong.be_bee_store.cart_module.payload.request.UpdateCartItemRequest;
import vn.huuchuong.be_bee_store.cart_module.payload.response.CartResponse;
import vn.huuchuong.be_bee_store.cart_module.service.CartService;


@Tag(
        name = "Cart",
        description = "API quản lý giỏ hàng của người dùng. Sản phẩm trong giỏ được giữ trong 30 phút."
)
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Lấy giỏ hàng của người dùng hiện tại",
            description = """
                    Trả về giỏ hàng của người dùng đang đăng nhập.
                    Nếu người dùng chưa có giỏ hàng, hệ thống sẽ tự động tạo giỏ hàng mới.
                    Các sản phẩm đã hết hạn giữ hàng sẽ được tự động xóa khỏi giỏ.
                    """
    )
    @GetMapping
    public ResponseEntity<BaseResponse<CartResponse>> getMyCart() {
        CartResponse res = cartService.getMyCart();
        return ResponseEntity.ok(BaseResponse.success(res, "Lấy giỏ hàng thành công"));
    }

    @Operation(
            summary = "Thêm sản phẩm vào giỏ hàng",
            description = """
                    Thêm một biến thể sản phẩm vào giỏ hàng của người dùng.

                    Điều kiện:
                    - quantity phải lớn hơn 0.
                    - productVariantId phải tồn tại.
                    - biến thể sản phẩm phải có thông tin tồn kho.
                    - số lượng thêm không được vượt quá tồn kho hiện tại.

                    Nếu sản phẩm đã tồn tại trong giỏ:
                    - nếu còn hạn giữ hàng, hệ thống cộng thêm số lượng mới vào số lượng cũ.
                    - nếu đã hết hạn giữ hàng, hệ thống xem số lượng cũ là 0 và giữ lại theo số lượng mới.

                    Sau khi thêm, sản phẩm được giữ trong 30 phút.
                    """
    )
    @PostMapping("/items")
    @Transactional
    public ResponseEntity<BaseResponse<CartResponse>> addItem(
            @RequestBody AddCartItemRequest request
    ) {
        CartResponse res = cartService.addItem(request);
        return ResponseEntity.ok(BaseResponse.success(res, "Thêm sản phẩm vào giỏ thành công"));
    }

    @Operation(
            summary = "Cập nhật số lượng sản phẩm trong giỏ hàng",
            description = """
                    Cập nhật số lượng của một sản phẩm trong giỏ hàng.

                    Điều kiện:
                    - quantity phải lớn hơn 0.
                    - cartItemId phải tồn tại.
                    - cartItem phải thuộc giỏ hàng của người dùng hiện tại.
                    - sản phẩm chưa hết hạn giữ hàng.
                    - số lượng cập nhật không được vượt quá tồn kho hiện tại.

                    Sau khi cập nhật, thời gian giữ sản phẩm được gia hạn thêm 30 phút.
                    """
    )
    @PutMapping("/items/{cartItemId}")
    @Transactional
    public ResponseEntity<BaseResponse<CartResponse>> updateItem(
            @Parameter(description = "ID của sản phẩm trong giỏ hàng cần cập nhật", example = "1")
            @PathVariable Integer cartItemId,
            @RequestBody UpdateCartItemRequest request
    ) {
        CartResponse res = cartService.updateItem(cartItemId, request);
        return ResponseEntity.ok(BaseResponse.success(res, "Cập nhật giỏ hàng thành công"));
    }

    @Operation(
            summary = "Xóa một sản phẩm khỏi giỏ hàng",
            description = """
                    Xóa một sản phẩm khỏi giỏ hàng theo cartItemId.

                    Điều kiện:
                    - cartItemId không được để trống.
                    - cartItemId phải tồn tại.
                    - sản phẩm phải thuộc giỏ hàng của người dùng hiện tại.
                    """
    )
    @DeleteMapping("/items/{cartItemId}")
    @Transactional
    public ResponseEntity<BaseResponse<CartResponse>> removeItem(
            @Parameter(description = "ID của sản phẩm trong giỏ hàng cần xóa", example = "1")
            @PathVariable Integer cartItemId
    ) {
        CartResponse res = cartService.removeItem(cartItemId);
        return ResponseEntity.ok(BaseResponse.success(res, "Xóa sản phẩm khỏi giỏ thành công"));
    }

    @Operation(
            summary = "Xóa toàn bộ giỏ hàng",
            description = """
                    Xóa tất cả sản phẩm trong giỏ hàng của người dùng hiện tại.
                    Sau khi xóa, hệ thống trả về giỏ hàng rỗng.
                    """
    )
    @DeleteMapping("/clear")
    @Transactional
    public ResponseEntity<BaseResponse<CartResponse>> clearCart() {
        CartResponse res = cartService.clearMyCart();
        return ResponseEntity.ok(BaseResponse.success(res, "Xóa toàn bộ giỏ hàng thành công"));
    }
}