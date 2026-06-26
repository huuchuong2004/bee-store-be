package vn.huuchuong.be_bee_store.order_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.order_module.payload.request.CheckoutRequest;
import vn.huuchuong.be_bee_store.order_module.payload.response.OrderResponse;
import vn.huuchuong.be_bee_store.order_module.payload.response.UserOrderResponse;
import vn.huuchuong.be_bee_store.order_module.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order", description = "API quản lý đơn hàng")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả đơn hàng",
            description = "API dành cho ADMIN hoặc STAFF để lấy danh sách tất cả đơn hàng có phân trang"
    )
    public ResponseEntity<BaseResponse<Page<OrderResponse>>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getAll(pageable),
                        "Lấy danh sách đơn hàng thành công"
                )
        );
    }

    @PostMapping("/checkout")
    @Operation(
            summary = "Tạo đơn hàng",
            description = "API tạo đơn hàng mới từ thông tin checkout của người dùng"
    )
    public ResponseEntity<BaseResponse<OrderResponse>> checkout(
            @RequestBody CheckoutRequest request
    ) {
        OrderResponse res = orderService.checkout(request);
        return ResponseEntity.ok(
                BaseResponse.success(res, "Tạo đơn hàng thành công")
        );
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Lấy thông tin đơn hàng",
            description = "API lấy thông tin chi tiết của một đơn hàng theo ID"
    )
    public ResponseEntity<BaseResponse<OrderResponse>> getOrder(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId
    ) {
        OrderResponse res = orderService.getOrderById(orderId);
        return ResponseEntity.ok(
                BaseResponse.success(res, "Lấy đơn hàng thành công")
        );
    }

    @GetMapping("/my")
    @Operation(
            summary = "Lấy danh sách đơn hàng của người dùng hiện tại",
            description = "API lấy danh sách đơn hàng của tài khoản đang đăng nhập có phân trang"
    )
    public ResponseEntity<BaseResponse<Page<OrderResponse>>> getMyOrders(Pageable pageable) {
        Page<OrderResponse> res = orderService.getMyOrders(pageable);
        return ResponseEntity.ok(
                BaseResponse.success(res, "Lấy danh sách đơn hàng của bạn thành công")
        );
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(
            summary = "Hủy đơn hàng",
            description = "API hủy một đơn hàng theo ID"
    )
    public ResponseEntity<BaseResponse<OrderResponse>> cancelOrder(
            @Parameter(description = "ID của đơn hàng cần hủy", example = "1")
            @PathVariable Integer orderId
    ) {
        OrderResponse res = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(
                BaseResponse.success(res, "Huỷ đơn hàng thành công")
        );
    }

    @GetMapping("/admin/details/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Lấy chi tiết đơn hàng cho admin",
            description = "API dành cho ADMIN hoặc STAFF để lấy chi tiết đơn hàng theo ID"
    )
    public ResponseEntity<BaseResponse<OrderResponse>> getOrdersDetailsWithAdminRole(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId
    ) {
        OrderResponse res = orderService.getDetailsAdminRole(orderId);
        return ResponseEntity.ok(
                BaseResponse.success(res, "Lấy chi tiết đơn hàng thành công")
        );
    }

    @GetMapping("/user/{orderId}")
    @Operation(
            summary = "Lấy thông tin khách hàng theo đơn hàng",
            description = "API lấy thông tin khách hàng từ ID đơn hàng"
    )
    public ResponseEntity<BaseResponse<UserOrderResponse>> getUserByOrderId(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getUserByOrderId(orderId),
                        "Lấy thông tin khách hàng thành công"
                )
        );
    }

    @PutMapping("/{orderId}/update-status-shipping/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Cập nhật trạng thái đang giao hàng",
            description = "API dành cho ADMIN hoặc STAFF để cập nhật trạng thái đơn hàng sang đang giao hàng"
    )
    public ResponseEntity<BaseResponse<Boolean>> setStatusIsShipping(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable("orderId") Integer orderId
    ) {
        boolean result = orderService.setStatusIsShipping(orderId);
        return ResponseEntity.ok(
                BaseResponse.success(
                        result,
                        "Cập nhật trạng thái đơn hàng thành công (Đang giao hàng)"
                )
        );
    }

    @PutMapping("/{orderId}/update-status-delivered/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Cập nhật trạng thái đã giao hàng",
            description = "API dành cho ADMIN hoặc STAFF để cập nhật trạng thái đơn hàng sang đã giao hàng thành công"
    )
    public ResponseEntity<BaseResponse<Boolean>> setStatusIsDelivered(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId
    ) {
        boolean result = orderService.setStatusIsDelivered(orderId);
        return ResponseEntity.ok(
                BaseResponse.success(
                        result,
                        "Cập nhật trạng thái đơn hàng thành công (Đã giao hàng thành công)"
                )
        );
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Cập nhật trạng thái đơn hàng",
            description = "API dành cho ADMIN hoặc STAFF để cập nhật trạng thái đơn hàng theo giá trị truyền vào"
    )
    public ResponseEntity<BaseResponse<Boolean>> updateOrderStatus(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId,

            @Parameter(description = "Trạng thái mới của đơn hàng", example = "SHIPPING")
            @RequestParam("status") String status
    ) {
        boolean result = orderService.updateStatus(orderId, status);
        return ResponseEntity.ok(
                BaseResponse.success(result, "Cập nhật trạng thái đơn hàng thành công")
        );
    }

    @GetMapping("/count-by-status-deliverred")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Đếm số lượng đơn hàng đã giao",
            description = "API dành cho ADMIN hoặc STAFF để đếm số lượng đơn hàng đã giao thành công"
    )
    public ResponseEntity<BaseResponse<Integer>> countOrdersByStatusDeliverred() {
        int count = orderService.countOrdersByStatusDeliverred();
        return ResponseEntity.ok(
                BaseResponse.success(count, "Đếm số lượng đơn hàng đã giao thành công")
        );
    }

    @GetMapping("/count-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Đếm tổng số lượng đơn hàng",
            description = "API dành cho ADMIN hoặc STAFF để đếm tổng số lượng đơn hàng trong hệ thống"
    )
    public ResponseEntity<BaseResponse<Integer>> countTotalOrders() {
        int count = orderService.countTotalOrders();
        return ResponseEntity.ok(
                BaseResponse.success(count, "Đếm tổng số lượng đơn hàng thành công")
        );
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Lấy tổng doanh thu",
            description = "API dành cho ADMIN hoặc STAFF để lấy tổng doanh thu từ đơn hàng"
    )
    public ResponseEntity<BaseResponse<BigDecimal>> getTotalRevenue() {
        BigDecimal revenue = orderService.getTotalRevenue();
        return ResponseEntity.ok(
                BaseResponse.success(revenue, "Lấy tổng doanh thu thành công")
        );
    }

    @GetMapping("/getAddress")
    @Operation(
            summary = "Lấy danh sách địa chỉ giao hàng",
            description = "API lấy danh sách địa chỉ giao hàng từ các đơn hàng"
    )
    public ResponseEntity<BaseResponse<List<String>>> getAddressFromOrder() {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getAddressFromOrder(),
                        "Lấy địa chỉ giao hàng thành công"
                )
        );
    }
}