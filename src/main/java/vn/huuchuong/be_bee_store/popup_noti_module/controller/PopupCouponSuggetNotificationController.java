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
@Tag(name = "Quản Lý Thông Báo - Popup Trên Trang", description = "API hiển thị popup gợi ý coupon cho người dùng")
@RequestMapping("/api/v1/popup-coupon")
public class PopupCouponSuggetNotificationController {

    private final CouponSuggestNotificationsService couponSuggestNotificationsService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(summary = "Hiển thị popup gợi ý coupon", description = "API hiển thị popup gợi ý coupon cho người dùng")
    public Object coupon_sugget_notification() {
        return ResponseEntity.ok(new BaseResponse<>(couponSuggestNotificationsService.getNoti(),"Lấy Thông Tin Của Thông Báo Thành CÔng"));

    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(summary = "Tạo thông báo gợi ý coupon", description = "API tạo thông báo gợi ý coupon cho người dùng")
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
    ResponseEntity<BaseResponse<CouponSuggestNotificationResponse>> deleteCouponSuggestNotification(@PathVariable int id) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        couponSuggestNotificationsService.deleteCouponSuggestNotification(id),
                        "Xóa thông báo gợi ý coupon thành công"
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<CouponSuggestNotificationResponse>> updateCouponSuggestNotification(@Valid @RequestBody UpdateCouponPopupRequest request, @PathVariable int id) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        couponSuggestNotificationsService.updateCouponPopup(request,id),
                        "Update thông báo gợi ý coupon thành công"
                )
        );
    }
}
