package vn.huuchuong.be_bee_store.popup_noti_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.huuchuong.be_bee_store.base.BaseEntity;

@Entity
@Table(name = "coupon_suggest_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponSuggestNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_suggest_notification_id")
    private Integer id;

    @Column(length = 255)
    private String header;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean status = true;
}