package vn.huuchuong.be_bee_store.payment_module.payload.request;


import lombok.Data;

@Data
public class CreateCodPaymentRequest {
    private Integer orderId;
}
