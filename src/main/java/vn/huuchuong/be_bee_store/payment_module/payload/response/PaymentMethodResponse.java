package vn.huuchuong.be_bee_store.payment_module.payload.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentMethodType;

@Getter
@Setter
@Builder
public class PaymentMethodResponse {

    private Integer paymentMethodId;

    private PaymentMethodType code;

    private String name;

    private String description;

    private Boolean isActive;
}