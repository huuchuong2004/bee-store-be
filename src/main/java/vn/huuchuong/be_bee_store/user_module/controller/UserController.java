package vn.huuchuong.be_bee_store.user_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.request.*;
import vn.huuchuong.be_bee_store.user_module.payload.response.LoadUserResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserResponse;
import vn.huuchuong.be_bee_store.user_module.service.UserService;

import java.util.List;
import java.util.UUID;
@Tag(
        name = "Quản lý người dùng",
        description = "Nhóm API quản lý người dùng, hồ sơ cá nhân, phân quyền và thống kê tài khoản trong hệ thống."
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Lấy danh sách người dùng",
            description = "API dành cho ADMIN hoặc STAFF để lấy danh sách người dùng. Hỗ trợ phân trang và sắp xếp theo username."
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<List<User>>> getUsers(
            @Parameter(description = "Thông tin phân trang. Mặc định size = 10, sort = username ASC")
            @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                new BaseResponse<>(userService.getUsers(pageable), "Lấy danh sách người dùng thành công")
        );
    }

    @Operation(
            summary = "Lấy thông tin người dùng theo username",
            description = "Người dùng được xem thông tin của chính mình. ADMIN và STAFF được phép xem thông tin người dùng khác."
    )
    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.name or hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<LoadUserResponse>> loadUser(
            @Parameter(description = "Tên đăng nhập của người dùng", example = "nguyenvana")
            @P("username") @PathVariable String username
    ) {
        return ResponseEntity.ok(
                new BaseResponse<>(userService.loadUser(username), "Lấy thông tin người dùng thành công")
        );
    }

    @Operation(
            summary = "Xóa người dùng theo username",
            description = "Người dùng có thể tự xóa tài khoản của mình. ADMIN có thể xóa tài khoản của người dùng khác."
    )
    @DeleteMapping("/{username}")
    @Transactional
    @PreAuthorize("#username == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> deleteUser(
            @Parameter(description = "Tên đăng nhập của người dùng cần xóa", example = "nguyenvana")
            @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.deleteByUsername(username));
    }

    @Operation(
            summary = "Cập nhật thông tin người dùng theo username",
            description = "API dành cho ADMIN để cập nhật thông tin người dùng như họ tên, email, số điện thoại, trạng thái hoạt động hoặc vai trò."
    )
    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse> updateUser(
            @Parameter(description = "Tên đăng nhập của người dùng cần cập nhật", example = "nguyenvana")
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest user
    ) {
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    @Operation(
            summary = "Cập nhật hồ sơ cá nhân",
            description = "API cập nhật một phần thông tin hồ sơ người dùng theo id. Người dùng chỉ được cập nhật hồ sơ của chính mình, ADMIN được cập nhật hồ sơ người khác."
    )
    @PatchMapping("/{id}")
    @Transactional
    @PreAuthorize("@authz.isSelf(#id, authentication) || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Void>> patch(
            @Parameter(description = "ID của người dùng", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody ProfileUpdateRequest req
    ) {
        userService.patch(id, req);
        return ResponseEntity.ok(new BaseResponse<>(null, "Cập nhật thông tin thành công"));
    }

    @Operation(
            summary = "Gán quyền ADMIN cho người dùng",
            description = "API dành cho ADMIN để gán vai trò quản trị viên cho một tài khoản người dùng."
    )
    @PostMapping("/set-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse> setAdmin(
            @Valid @RequestBody SetAdminRequest req
    ) {
        return ResponseEntity.ok(userService.setRoleAdmin(req));
    }

    @Operation(
            summary = "Gán quyền STAFF cho người dùng",
            description = "API dành cho ADMIN để gán vai trò nhân viên cho một tài khoản người dùng."
    )
    @PostMapping("/set-staff")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<BaseResponse> setStaff(
            @Valid @RequestBody SetStaffRequest req
    ) {
        return ResponseEntity.ok(userService.setRoleStaff(req));
    }

    @Operation(
            summary = "Tìm kiếm và lọc người dùng",
            description = "API dành cho ADMIN hoặc STAFF để tìm kiếm người dùng theo username, email, số điện thoại, họ hoặc tên. Kết quả hỗ trợ phân trang."
    )
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<Page<User>>> search(
            @Valid @ModelAttribute UserFilterRequest req,
            @Parameter(description = "Thông tin phân trang. Mặc định size = 10, sort = username ASC")
            @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<User> page = userService.search(req, pageable);
        return ResponseEntity.ok(new BaseResponse<>(page, "Lấy danh sách thành công"));
    }

    @Operation(
            summary = "Đếm số lượng người dùng theo vai trò",
            description = "API dành cho ADMIN hoặc STAFF để thống kê số lượng người dùng theo vai trò trong hệ thống."
    )
    @GetMapping("/count-by-role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<Integer>> countUserByRole() {
        return ResponseEntity.ok(
                new BaseResponse<>(userService.countUserByRole(), "Đếm số lượng user theo role thành công")
        );
    }
}
