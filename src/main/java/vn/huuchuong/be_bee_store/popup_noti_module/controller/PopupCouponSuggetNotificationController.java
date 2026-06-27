package vn.huuchuong.be_bee_store.popup_noti_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.CouponSuggestNotificationsReq;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.UpdateCouponPopupRequest;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.response.CouponSuggestNotificationResponse;
import vn.huuchuong.be_bee_store.popup_noti_module.service.CouponSuggestNotificationsService;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Quản Lý Thông Báo - Popup Coupon",
        description = "Các API quản lý thông báo popup gợi ý coupon hiển thị trên trang"
)
@RequestMapping("/api/v1/popup-coupon")
public class PopupCouponSuggetNotificationController {

    private final CouponSuggestNotificationsService couponSuggestNotificationsService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Lấy danh sách popup gợi ý coupon",
            description = "API lấy toàn bộ danh sách thông báo popup gợi ý coupon đang có trong hệ thống"
    )
    public Object coupon_sugget_notification() {
        return ResponseEntity.ok(
                new BaseResponse<>(
                        couponSuggestNotificationsService.getNoti(),
                        "Lấy thông tin của thông báo thành công"
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Tạo popup gợi ý coupon",
            description = "API tạo mới một thông báo popup gợi ý coupon cho người dùng"
    )
    public ResponseEntity<BaseResponse<CouponSuggestNotificationResponse>> createCouponSuggestNotification(
            @Valid @RequestBody CouponSuggestNotificationsReq request) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        couponSuggestNotificationsService.createCouponSuggestNotification(request),
                        "Tạo thông báo gợi ý coupon thành công"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Xóa popup gợi ý coupon",
            description = "API xóa một thông báo popup gợi ý coupon theo ID"
    )
    public ResponseEntity<BaseResponse<CouponSuggestNotificationResponse>> deleteCouponSuggestNotification(
            @PathVariable int id) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        couponSuggestNotificationsService.deleteCouponSuggestNotification(id),
                        "Xóa thông báo gợi ý coupon thành công"
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Cập nhật popup gợi ý coupon",
            description = "API cập nhật thông tin popup gợi ý coupon theo ID. Nếu field truyền vào rỗng thì giữ nguyên dữ liệu hiện tại"
    )
    public ResponseEntity<BaseResponse<CouponSuggestNotificationResponse>> updateCouponSuggestNotification(
            @Valid @RequestBody UpdateCouponPopupRequest request,
            @PathVariable int id) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        couponSuggestNotificationsService.updateCouponPopup(request, id),
                        "Update thông báo gợi ý coupon thành công"
                )
        );
    }
}add