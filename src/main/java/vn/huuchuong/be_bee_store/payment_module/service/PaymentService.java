package vn.huuchuong.be_bee_store.payment_module.service;

import vn.huuchuong.be_bee_store.payment_module.entity.Payment;
import vn.huuchuong.be_bee_store.payment_module.payload.request.CreateCodPaymentRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentStatusResponse;

public interface PaymentService {
    Payment createCodPayment(CreateCodPaymentRequest req);

    void confirmCodPayment(Integer paymentId);

    Payment getPaymentMethodByOrderID(Integer orderID);

    PaymentStatusResponse getPaymentStatusByOrderId(Integer orderId);
}
