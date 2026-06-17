package vn.huuchuong.be_bee_store.auth_module.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {


    @Schema(description = "Thông tin password của người dùng, độ dài từ 6 đến 20 ký tự, phải có ít nhất 1 chữ hoa, 1 chữ thường và 1 số", required = true,example = "Password123")
    @Size(min = 6, max = 20, message = "Độ dài password chưa phù hợp ( 6- 20!) ")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,20}$",
            message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường và 1 số"
    )

    @NotNull(message = "Password not null")
    private String newPassword;



    @NotBlank(message = "Token không được để trống")
    private String token;



    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    @AssertTrue(message = "Mật khẩu xác nhận không khớp")
    public boolean isPasswordMatching() {
        if (newPassword == null || confirmPassword == null) {
            return false;
        }
        return newPassword.equals(confirmPassword);
    }
}
