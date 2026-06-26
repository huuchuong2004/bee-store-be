package vn.huuchuong.be_bee_store.payment_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.payment_module.entity.Payment;
import vn.huuchuong.be_bee_store.payment_module.payload.request.CreateCodPaymentRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentStatusResponse;
import vn.huuchuong.be_bee_store.payment_module.service.PaymentService;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "API quản lý thanh toán")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/cod")
    @Operation(
            summary = "Tạo thanh toán COD",
            description = "API tạo thanh toán bằng phương thức COD cho đơn hàng"
    )
    public ResponseEntity<BaseResponse<Map<String, Object>>> createCodPayment(
            @RequestBody CreateCodPaymentRequest req
    ) {
        Payment payment = paymentService.createCodPayment(req);

        Map<String, Object> response = new HashMap<>();
        response.put("paymentId", payment.getPaymentId());
        response.put("amount", payment.getAmount());
        response.put("status", payment.getStatus());
        response.put("orderId", payment.getOrder().getOrderId());

        if (payment.getPaymentMethod() != null) {
            response.put("paymentMethod", payment.getPaymentMethod().getName());
        } else {
            response.put("paymentMethod", "COD");
        }

        return ResponseEntity.ok(
                BaseResponse.success(response, "Tạo thanh toán COD thành công")
        );
    }

    @PutMapping("/cod/{paymentId}/confirm")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Xác nhận thanh toán COD",
            description = "API dành cho ADMIN hoặc STAFF để xác nhận thanh toán COD theo ID thanh toán"
    )
    public ResponseEntity<BaseResponse<Void>> confirmCodPayment(
            @Parameter(description = "ID của thanh toán COD cần xác nhận", example = "1")
            @PathVariable Integer paymentId
    ) {
        paymentService.confirmCodPayment(paymentId);
        return ResponseEntity.ok(
                BaseResponse.success(null, "Xác nhận thanh toán COD thành công")
        );
    }

    @GetMapping("/{orderID}")
    @Operation(
            summary = "Lấy thông tin thanh toán theo đơn hàng",
            description = "API lấy thông tin phương thức thanh toán, số tiền và trạng thái thanh toán theo ID đơn hàng"
    )
    public ResponseEntity<BaseResponse<Map<String, Object>>> getPaymentMethodByOrderID(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderID
    ) {
        Payment payment = paymentService.getPaymentMethodByOrderID(orderID);

        Map<String, Object> paymentInfo = new HashMap<>();
        if (payment != null) {
            paymentInfo.put("paymentId", payment.getPaymentId());

            if (payment.getPaymentMethod() != null) {
                paymentInfo.put("paymentMethod", payment.getPaymentMethod().getName());
            } else {
                paymentInfo.put("paymentMethod", "COD");
            }

            paymentInfo.put("amount", payment.getAmount());
            paymentInfo.put("status", payment.getStatus());
        }

        return ResponseEntity.ok(
                BaseResponse.success(paymentInfo, "Lấy thông tin thanh toán thành công")
        );
    }

    @GetMapping("/status/{orderId}")
    @Operation(
            summary = "Lấy trạng thái thanh toán theo đơn hàng",
            description = "API lấy trạng thái thanh toán của đơn hàng theo ID đơn hàng"
    )
    public ResponseEntity<BaseResponse<PaymentStatusResponse>> getPaymentStatusByOrderId(
            @Parameter(description = "ID của đơn hàng", example = "1")
            @PathVariable Integer orderId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        paymentService.getPaymentStatusByOrderId(orderId),
                        "Lấy trạng thái thanh toán thành công"
                )
        );
    }
}