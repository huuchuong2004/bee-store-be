package vn.huuchuong.be_bee_store.user_module.service.impl;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.service.IMailSenderService;

import java.nio.charset.StandardCharsets;
@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderServiceImpl implements IMailSenderService {
    private final JavaMailSender emailSender;
    private final JavaMailSenderImpl mailSender;

    // From lấy từ spring.mail.username
    @Value("${spring.mail.username}")
    private String from;


    private BaseResponse<String> doSendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            // true = multipart, UTF-8 để hiển thị tiếng Việt
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(new InternetAddress(from, "LC Store 💜"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = nội dung là HTML

            emailSender.send(message);

            String msg = "Đã gửi email tới: " + to;
            log.info(msg);
            return BaseResponse.success(msg, "Gửi email thành công");
        } catch (Exception e) {
            String errorMsg = "Gửi email tới " + to + " thất bại: " + e.getMessage();
            log.error(errorMsg, e);
            return BaseResponse.error(errorMsg);
        }
    }

    @Override
    public BaseResponse<String> sendMessageWithAttachment(String to, String subject, String text) {
        // Ở đây text đã là HTML, dùng chung hàm doSendHtmlMail
        return doSendHtmlMail(to, subject, text);
    }

    @Override
    public BaseResponse<String> sendActivationEmail(String to, String activationLink) {
        String subject = "Kích hoạt tài khoản LC-Store";

        String html = """
                <div style="background:#3a3243;padding:24px 0;font-family:Segoe UI,Tahoma,Geneva,Verdana,sans-serif;">
                  <div style="background:#594d65;border-radius:16px;
                              max-width:420px;margin:0 auto;padding:32px 24px;
                              text-align:center;border:1px solid #6b5d7a;
                              box-shadow:0 4px 16px rgba(0,0,0,0.3);">
                
                    <img src="https://i.ibb.co/8LfXbPdp/z7116621240566-b8a81aef05e5d43c0d6ee6c265d4dbf9.jpg"
                         alt="LC Store"
                         style="width:120px;border-radius:12px;border:1px solid #827193;margin-bottom:16px;" />
                
                    <h2 style="color:#f8f8fa;font-size:20px;margin:0 0 12px 0;">
                      Chào mừng bạn đến với LC-Store 💜
                    </h2>
                
                    <p style="color:#e8e6ee;font-size:14px;line-height:1.6;margin:0 0 24px 0;">
                      Bạn đã đăng ký tài khoản thành công.<br/>
                      Để kích hoạt tài khoản, vui lòng nhấn vào nút bên dưới.
                    </p>
                
                    <a href="{{activationLink}}" target="_blank"
                       style="display:inline-block;padding:12px 24px;
                              background:#a69ab8;color:#ffffff;text-decoration:none;
                              border-radius:999px;font-weight:600;font-size:13px;
                              letter-spacing:0.05em;text-transform:uppercase;">
                      Kích hoạt tài khoản
                    </a>
                
                    <p style="color:#bfb8cd;font-size:12px;margin-top:20px;">
                      Cảm ơn bạn đã tin tưởng LC-Store!
                    </p>
                  </div>
                </div>
                """.replace("{{activationLink}}", activationLink);


        return doSendHtmlMail(to, subject, html);
    }
}