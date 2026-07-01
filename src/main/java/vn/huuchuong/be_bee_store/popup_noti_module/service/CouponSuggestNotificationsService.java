package vn.huuchuong.be_bee_store.popup_noti_module.service;

import jakarta.validation.Valid;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.CouponSuggestNotificationsReq;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.request.UpdateCouponPopupRequest;
import vn.huuchuong.be_bee_store.popup_noti_module.payload.response.CouponSuggestNotificationResponse;

public interface CouponSuggestNotificationsService {

    Object getNoti();

    CouponSuggestNotificationResponse createCouponSuggestNotification(@Valid CouponSuggestNotificationsReq request);

    CouponSuggestNotificationResponse deleteCouponSuggestNotification(int id);

    CouponSuggestNotificationResponse updateCouponPopup(@Valid UpdateCouponPopupRequest request, int id);


}

   
