package vn.huuchuong.be_bee_store.payment_module.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.auth_module.service.MailSenderService;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.invoice_module.service.InvoiceService;
import vn.huuchuong.be_bee_store.order_module.Enum.OrderStatus;
import vn.huuchuong.be_bee_store.order_module.entity.Order;
import vn.huuchuong.be_bee_store.order_module.repository.OrderRepository;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentMethodType;
import vn.huuchuong.be_bee_store.payment_module.entity.Enum.PaymentStatus;
import vn.huuchuong.be_bee_store.payment_module.entity.Payment;
import vn.huuchuong.be_bee_store.payment_module.entity.PaymentMethod;
import vn.huuchuong.be_bee_store.payment_module.payload.request.CreateCodPaymentRequest;
import vn.huuchuong.be_bee_store.payment_module.payload.response.PaymentStatusResponse;
import vn.huuchuong.be_bee_store.payment_module.repository.PaymentMethodRepository;
import vn.huuchuong.be_bee_store.payment_module.repository.PaymentRepository;
import vn.huuchuong.be_bee_store.payment_module.service.PaymentService;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    private final InvoiceService invoiceService;
    private final MailSenderService mailSenderService;



    // ====================== COD: tạo payment ======================

    @Transactional
    public Payment createCodPayment(CreateCodPaymentRequest req) {

        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new BusinessException("Đơn hàng không tồn tại"));

        User user = order.getUser();

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BusinessException("Trạng thái đơn hàng không hợp lệ để tạo thanh toán COD");
        }
        List<Payment> oldPayments = paymentRepository.findByOrder(order);
        if (oldPayments != null && !oldPayments.isEmpty()) {
            throw new BusinessException("Đơn hàng này đã có thông tin thanh toán");
        }

        PaymentMethod codMethod = paymentMethodRepository.findByCode(PaymentMethodType.COD)
                .orElseThrow(() -> new BusinessException("Chưa cấu hình phương thức COD"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(codMethod);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(null);

        Payment saved = paymentRepository.save(payment);

        // Business: chọn COD xong thì xem như đơn đã được xác nhận
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        BaseResponse<String> mailResult =
                mailSenderService.sendConfirmOrder(user.getEmail(), order);

        String message;
        if (mailResult.getData() == null) {
            message = "Đặt hàng thành công nhưng gửi email xác nhận thất bại: "
                    + mailResult.getMessage();
        } else {
            message = "Đặt hàng thành công! Vui lòng kiểm tra email để xem chi tiết đơn hàng.";
        }


        return saved;
    }


    // ====================== Admin xác nhận COD đã thu tiền ======================

    @Transactional
    public void confirmCodPayment(Integer paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy payment"));

        if (payment.getPaymentMethod().getCode() != PaymentMethodType.COD) {
            throw new BusinessException("Payment này không phải COD");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BusinessException("Payment COD này đã được xác nhận trước đó");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Trạng thái payment không hợp lệ để xác nhận COD");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));


        Order order = payment.getOrder();
        if (order.getStatus() != OrderStatus.SHIPPING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException("Chỉ xác nhận COD khi đơn đã được xác nhận hoặc đang giao (CONFIRMED/SHIPPING)");
        }

        // COD: thu tiền xong xem như giao xong
        order.setStatus(OrderStatus.DELIVERED);

        paymentRepository.save(payment);
        orderRepository.save(order);

        // ⭐ Tạo hoá đơn cho COD
        invoiceService.createInvoiceForOrderIfNotExists(order);
    }


    public Payment getPaymentMethodByOrderID(Integer orderID) {
        Order order = orderRepository.findById(orderID).orElse(null);
        if (order == null) return null;

        // Lấy danh sách các lần thanh toán
        List<Payment> payments = paymentRepository.findByOrder(order);

        if (payments == null || payments.isEmpty()) {
            return null;
        }

        // Sắp xếp để lấy cái mới nhất (giả sử ID lớn hơn là mới hơn)
        // Hoặc bạn có thể ưu tiên lấy cái có status = "COMPLETED"
        payments.sort((p1, p2) -> p2.getPaymentId().compareTo(p1.getPaymentId()));

        // Trả về cái mới nhất
        return payments.get(0);
    }

    public PaymentStatusResponse getPaymentStatusByOrderId(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Đơn hàng không tồn tại"));

        List<Payment> payments = paymentRepository.findByOrder(order);

        if (payments == null || payments.isEmpty()) {
            throw new BusinessException("Không tìm thấy thông tin thanh toán cho đơn hàng này");
        }

        // Giả sử lấy payment mới nhất dựa trên paymentId
        payments.sort((p1, p2) -> p2.getPaymentId().compareTo(p1.getPaymentId()));
        Payment latestPayment = payments.get(0);

        return PaymentStatusResponse.builder()
                .orderId(order.getOrderId())
                .paymentId(latestPayment.getPaymentId())
                .paymentStatus(latestPayment.getStatus())
                .amount(latestPayment.getAmount())
                .paymentMethod(
                        latestPayment.getPaymentMethod() != null
                                ? latestPayment.getPaymentMethod().getName()
                                : "COD"
                )
                .build();

    }
}
