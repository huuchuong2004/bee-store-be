package vn.huuchuong.be_bee_store.auth_module.service;

import vn.huuchuong.be_bee_store.base.BaseResponse;

public interface IMailSenderService {
    BaseResponse<String> sendMessageWithAttachment(String to, String subject, String text);

    BaseResponse<String> sendActivationEmail(String to, String activationLink);

    BaseResponse<String> sendResetPasswordEmail(String toEmail, String resetLink);
}

