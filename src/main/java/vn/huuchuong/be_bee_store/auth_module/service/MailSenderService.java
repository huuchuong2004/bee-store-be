package vn.huuchuong.be_bee_store.auth_module.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

public interface MailSenderService {
    BaseResponse<String> sendMessageWithAttachment(String to, String subject, String text);

    BaseResponse<String> sendActivationEmail(String to, String activationLink);

    BaseResponse<String> sendResetPasswordEmail(String toEmail, String resetLink);

    BaseResponse<String> sendConfirmOrder(@Email @NotBlank String email, Order order);
}

