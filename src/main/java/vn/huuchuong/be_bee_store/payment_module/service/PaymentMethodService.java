package vn.huuchuong.be_bee_store.payment_module.service;



import vn.huuchuong.be_bee_store.payment_module.payload.request.PaymentMethodRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethodResponse> getAllPaymentMethods();

    PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request);

    PaymentMethodResponse updatePaymentMethod(Integer paymentMethodId, PaymentMethodRequest request);

    void deletePaymentMethod(Integer paymentMethodId);
}