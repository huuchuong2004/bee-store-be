package vn.huuchuong.be_bee_store.auth_module.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import vn.huuchuong.be_bee_store.auth_module.entity.Role;

import java.util.UUID;

@Data
@Schema(name = "UserLoginResponse", description = "Đối tượng phản hồi sau khi người dùng đăng nhập thành công, chứa thông tin về người dùng và token truy cập.")
public class UserLoginResponse {

    @Schema(description = "ID của user")
    private UUID id;

    @Schema(description = "Username của user")
    private String username;

    @Schema(description = "ROLE của user")
    private Role role;

    @Schema(description = "firstName của user")
    private String firstName;

    @Schema(description = "lastName của user")
    private String lastName;

    @Schema(description = "user agent của client khi đăng nhập")
    private String userAgent;

    @Schema(description = "Token truy cập sau khi đăng nhập thành công, được sử dụng để xác thực các yêu cầu tiếp theo của người dùng.")
    private String token;

}
