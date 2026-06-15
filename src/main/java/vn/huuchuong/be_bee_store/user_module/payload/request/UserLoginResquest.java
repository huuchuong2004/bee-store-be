package vn.huuchuong.be_bee_store.user_module.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vn.huuchuong.be_bee_store.user_module.entity.Role;

import java.util.UUID;

@Data
@Schema(description = "Thông tin đăng nhập người dùng")
public class UserLoginResquest {


    @Size(min = 6, max = 30, message = "Độ dài username chưa phù hợp ( 6- 30) !")
    @NotBlank(message = "Username not null !")
    @Schema(description = "Thông tin username của người dùng, độ dài từ 6 đến 30 ký tự", required = true,example = "huuchuong123")
    private String username;

    @Schema(description = "Thông tin password của người dùng, độ dài từ 6 đến 20 ký tự, phải có ít nhất 1 chữ hoa, 1 chữ thường và 1 số", required = true,example = "Password123")
    @Size(min = 6, max = 20, message = "Độ dài password chưa phù hợp ( 6- 20!) ")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,20}$",
            message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường và 1 số"
    )

    @NotNull(message = "Password not null")
    private String password;
}

