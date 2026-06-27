package vn.huuchuong.be_bee_store.popup_noti_module.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class ListCouponNotificationResponse {

    private List<CouponSuggestNotificationResponse> couponNotifications;
}
