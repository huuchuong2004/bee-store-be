package vn.huuchuong.be_bee_store.auth_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.auth_module.service.MailSenderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailAsyncService {

    private final MailSenderService mailSenderService;

    @Async("mailTaskExecutor")
    public void sendActivationEmailAsync(String email, String activationLink) {
        try {
            BaseResponse<String> result =
                    mailSenderService.sendActivationEmail(email, activationLink);

            if (result == null || result.getData() == null) {
                log.error("Gửi email kích hoạt thất bại cho email: {}, message: {}",
                        email,
                        result != null ? result.getMessage() : "null result");
                return;
            }

            log.info("Đã gửi email kích hoạt thành công tới: {}", email);

        } catch (Exception e) {
            log.error("Lỗi khi gửi email kích hoạt tới: {}", email, e);
        }
    }
    @Async("mailTaskExecutor")
    public void sendResetPasswordEmailAsync(String email, String resetLink) {
        try {
            BaseResponse<String> result =
                    mailSenderService.sendResetPasswordEmail(email, resetLink);

            if (result == null || result.getData() == null) {
                log.error("Gửi email reset password thất bại tới {}: {}",
                        email,
                        result != null ? result.getMessage() : "null result");
                return;
            }

            log.info("Đã gửi email reset password tới {}", email);

        } catch (Exception e) {
            log.error("Lỗi khi gửi email reset password tới {}", email, e);
        }
    }
}