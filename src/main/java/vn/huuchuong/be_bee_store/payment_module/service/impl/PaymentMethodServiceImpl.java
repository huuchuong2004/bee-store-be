package vn.huuchuong.be_bee_store.payment_module.service.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.payment_module.entity.PaymentMethod;
import vn.huuchuong.be_bee_store.payment_module.payload.request.PaymentMethodRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentMethodResponse;
import vn.huuchuong.be_bee_store.payment_module.repository.PaymentMethodRepository;
import vn.huuchuong.be_bee_store.payment_module.service.PaymentMethodService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request) {

        if (request.getCode() == null) {
            throw new BusinessException("Mã phương thức thanh toán không được để trống");
        }

        if (paymentMethodRepository.existsByCode(request.getCode())) {
            throw new BusinessException("Phương thức thanh toán này đã tồn tại");
        }

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public PaymentMethodResponse updatePaymentMethod(Integer paymentMethodId, PaymentMethodRequest request) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phương thức thanh toán"));

        if (request.getCode() != null && request.getCode() != paymentMethod.getCode()) {
            if (paymentMethodRepository.existsByCode(request.getCode())) {
                throw new BusinessException("Mã phương thức thanh toán này đã tồn tại");
            }
            paymentMethod.setCode(request.getCode());
        }

        if (request.getName() != null) {
            paymentMethod.setName(request.getName());
        }

        if (request.getDescription() != null) {
            paymentMethod.setDescription(request.getDescription());
        }

        if (request.getIsActive() != null) {
            paymentMethod.setIsActive(request.getIsActive());
        }

        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);

        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Integer paymentMethodId) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phương thức thanh toán"));

        paymentMethodRepository.delete(paymentMethod);
    }

    private PaymentMethodResponse toResponse(PaymentMethod paymentMethod) {
        return PaymentMethodResponse.builder()
                .paymentMethodId(paymentMethod.getPaymentMethodId())
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .isActive(paymentMethod.getIsActive())
                .build();
    }
}