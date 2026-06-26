package vn.huuchuong.be_bee_store.payment_module.payload.response;

import lombok.Builder;
import lombok.Data;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentStatus;


import java.math.BigDecimal;

@Data
@Builder
public class PaymentStatusResponse {

    private Integer orderId;
    private Integer paymentId;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private BigDecimal amount;
}

