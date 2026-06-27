package vn.huuchuong.be_bee_store.popup_noti_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon_suggest_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CouponSuggestNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int coupon_suggest_notification_id;

    private String header;

    private String title;

    private String description;


}
