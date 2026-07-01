package vn.huuchuong.be_bee_store.popup_noti_module.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponSuggestNotificationResponse {

    private int id;
    private String header;
    private String title;
    private String description;
}
