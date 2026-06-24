package vn.huuchuong.be_bee_store.user_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserResponse;
import vn.huuchuong.be_bee_store.user_module.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "API này sẽ trả về danh sách tất cả người dùng trong hệ thống dưới dạng phân trang, cần cần truyền số trang")
    public ResponseEntity<BaseResponse<Page<UserResponse>>> getAllUsers( @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(BaseResponse.success(users,"Lấy danh sách người dùng thành công"));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa người dùng theo ID", description = "API này sẽ xóa người dùng dựa trên ID được cung cấp. Nếu người dùng tồn tại, nó sẽ bị xóa khỏi hệ thống và trả về thông báo thành công. Nếu người dùng không tồn tại, nó sẽ trả về thông báo lỗi.")
    public ResponseEntity<BaseResponse<String>> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(BaseResponse.success("Xóa người dùng thành công"));
    }


}
