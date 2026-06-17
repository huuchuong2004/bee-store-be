package vn.huuchuong.be_bee_store.auth_module.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import vn.huuchuong.be_bee_store.auth_module.entity.Role;

import java.util.UUID;
@Schema(description = "Đây là lớp phản hồi cho việc tạo người dùng mới, chứa thông tin chi tiết về người dùng đã được tạo ra.")
public class CreateUserResponse {
    @Schema(description = "ID duy nhất của người dùng, được sử dụng để xác định và truy cập thông tin người dùng trong hệ thống.")
    private UUID id;

    @Schema(description = "Tên đăng nhập của người dùng, được sử dụng để đăng nhập vào hệ thống và xác định danh tính của người dùng.")
    private String username;

    @Schema
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isActive;
    private Role role;
}
