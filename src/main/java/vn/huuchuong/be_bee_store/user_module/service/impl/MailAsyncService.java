package vn.huuchuong.be_bee_store.user_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.service.IMailSenderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailAsyncService {

    private final IMailSenderService mailSenderService;

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
}