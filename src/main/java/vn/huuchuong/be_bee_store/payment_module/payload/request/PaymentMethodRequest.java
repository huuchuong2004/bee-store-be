package vn.huuchuong.be_bee_store.payment_module.payload.request;


import lombok.Getter;
import lombok.Setter;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentMethodType;

@Getter
@Setter
public class PaymentMethodRequest {

    private PaymentMethodType code;

    private String name;

    private String description;

    private Boolean isActive;
}