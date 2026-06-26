package vn.huuchuong.be_bee_store.payment_module.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.payment_module.payload.request.PaymentMethodRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentMethodResponse;
import vn.huuchuong.be_bee_store.payment_module.service.PaymentMethodService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-methods")
@Tag(name = "Payment Method", description = "API quản lý phương thức thanh toán")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách phương thức thanh toán",
            description = "API lấy danh sách tất cả phương thức thanh toán"
    )
    public ResponseEntity<BaseResponse<List<PaymentMethodResponse>>> getAllPaymentMethods() {
        return ResponseEntity.ok(
                BaseResponse.success(
                        paymentMethodService.getAllPaymentMethods(),
                        "Lấy danh sách phương thức thanh toán thành công"
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Thêm phương thức thanh toán",
            description = "API dành cho ADMIN hoặc STAFF để thêm phương thức thanh toán mới"
    )
    public ResponseEntity<BaseResponse<PaymentMethodResponse>> createPaymentMethod(
            @RequestBody PaymentMethodRequest request
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        paymentMethodService.createPaymentMethod(request),
                        "Thêm phương thức thanh toán thành công"
                )
        );
    }

    @PutMapping("/{paymentMethodId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Cập nhật phương thức thanh toán",
            description = "API dành cho ADMIN hoặc STAFF để cập nhật thông tin phương thức thanh toán"
    )
    public ResponseEntity<BaseResponse<PaymentMethodResponse>> updatePaymentMethod(
            @Parameter(description = "ID của phương thức thanh toán", example = "1")
            @PathVariable Integer paymentMethodId,

            @RequestBody PaymentMethodRequest request
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        paymentMethodService.updatePaymentMethod(paymentMethodId, request),
                        "Cập nhật phương thức thanh toán thành công"
                )
        );
    }

    @DeleteMapping("/{paymentMethodId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @Operation(
            summary = "Xóa phương thức thanh toán",
            description = "API dành cho ADMIN hoặc STAFF để xóa phương thức thanh toán theo ID"
    )
    public ResponseEntity<BaseResponse<Void>> deletePaymentMethod(
            @Parameter(description = "ID của phương thức thanh toán", example = "1")
            @PathVariable Integer paymentMethodId
    ) {
        paymentMethodService.deletePaymentMethod(paymentMethodId);

        return ResponseEntity.ok(
                BaseResponse.success(null, "Xóa phương thức thanh toán thành công")
        );
    }
}
