package vn.huuchuong.be_bee_store.order_module.payload.request;



import lombok.Data;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentMethodType;


@Data
public class CheckoutRequest {

    private String shippingAddress;
    private String couponCode; // optional, có thể null
    private PaymentMethodType paymentMethodType;
}
