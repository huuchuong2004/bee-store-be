package vn.huuchuong.be_bee_store.user_module.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Trả  về thông tin sau khi đăng nhập thành công")
public class AuthResponse {
    @Schema(description = "Trả  về thông tin accessToken sau khi đăng nhập thành công")
    private String accessToken;

    @Schema(description = "Trả  về thông tin refreshToken sau khi đăng nhập thành công")
    private String refreshToken;
}
