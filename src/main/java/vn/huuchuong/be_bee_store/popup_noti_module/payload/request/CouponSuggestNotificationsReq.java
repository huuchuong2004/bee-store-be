package vn.huuchuong.be_bee_store.popup_noti_module.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CouponSuggestNotificationsReq {

    @NotBlank(message = "Header không được để trống")
    @Size(max = 100, message = "Header không được vượt quá 100 ký tự")
    private String header;

    @NotBlank(message = "Title không được để trống")
    @Size(max = 150, message = "Title không được vượt quá 500 ký tự")
    private String title;

    @NotBlank(message = "Description không được để trống")
    @Size(max = 500, message = "Description không được vượt quá 500 ký tự")
    private String description;


}
