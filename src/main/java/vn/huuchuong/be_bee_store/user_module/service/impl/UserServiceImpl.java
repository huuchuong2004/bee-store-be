package vn.huuchuong.be_bee_store.user_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.huuchuong.be_bee_store.auth_module.entity.Role;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.auth_module.repository.UserRepository;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.user_module.payload.request.*;
import vn.huuchuong.be_bee_store.user_module.payload.response.CountByRole;
import vn.huuchuong.be_bee_store.user_module.payload.response.LoadUserResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UpdateUserResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserResponse;
import vn.huuchuong.be_bee_store.user_module.service.UserService;
import vn.huuchuong.be_bee_store.user_module.spec.UserSpectification;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    @Override
    public LoadUserResponse loadUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        return modelMapper.map(user, LoadUserResponse.class);
    }

    @Override
    public BaseResponse deleteByUsername(String username) {
        User user =userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));
       user.setIsActive(false);
       userRepository.save(user);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(null);
        baseResponse.setMessage("User locked successfully");
        return baseResponse;
    }

    @Override
    public BaseResponse<UpdateUserResponse> updateUser(String username, UpdateUserRequest req) {
        // 1) Tìm user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        // 2) Cập nhật các trường có giá trị (partial update)
        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            user.setFirstName(req.getFirstName().trim());
        }
        if (req.getLastName() != null && !req.getLastName().isBlank()) {
            user.setLastName(req.getLastName().trim());
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            String email = req.getEmail().trim();
            // (tuỳ chọn) Check trùng email nếu bạn có hàm repo tương ứng
            // if (userRepository.existsByEmailAndIdNot(email, user.getId())) {
            //     throw new BusinessException("Email already in use");
            // }
            user.setEmail(email);
        }
        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            user.setPhone(req.getPhone().trim());
        }



        //  isActive (Boolean) – chỉ cập nhật khi không null
        if (req.getIsActive() != null) {
            user.setIsActive(req.getIsActive());
        }

        // role (Enum) – CHỈ cho phép ADMIN cập nhật
        if (req.getRole() != null) {
            boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getAuthorities()
                    .stream()
                    .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
            if (!isAdmin) {
                throw new org.springframework.security.access.AccessDeniedException("Only ADMIN can update role");
            }
            user.setRole(req.getRole());
        }

        // 3) Lưu DB
        userRepository.save(user);

        // 4) Map sang response DTO
        UpdateUserResponse response = new UpdateUserResponse();
        modelMapper.map(user, response);

        // 5) Đóng gói BaseResponse
        BaseResponse<UpdateUserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(response);
        baseResponse.setMessage("User updated successfully");
        return baseResponse;
    }

    @Override
    public BaseResponse setRoleAdmin(SetAdminRequest req) {
        return setUserRole(req.getUsername(), Role.ADMIN);
    }

    @Override
    public BaseResponse setRoleStaff(SetStaffRequest req) {
        return setUserRole(req.getUsername(), Role.STAFF);
    }

    private BaseResponse<String> setUserRole(String username, Role role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (user.getRole() == role) {
            throw new BusinessException("User is already a " + role.name().toLowerCase());
        }

        user.setRole(role);
        userRepository.save(user);

        BaseResponse<String> response = new BaseResponse<>();
        response.setData(null);
        response.setMessage("Role " + role.name().toLowerCase() + " set successfully");
        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> search(UserFilterRequest req, Pageable pageable) {
        String username  = req.getUsername();
        String email     = req.getEmail();
        String phone     = req.getPhone();
        String firstName = req.getFirstName();
        String lastName  = req.getLastName();

        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (StringUtils.hasText(username)) {
            spec = spec.and(UserSpectification.hasUsername(username)); // sửa typo: UserSpecification
        }
        if (StringUtils.hasText(email)) {
            spec = spec.and(UserSpectification.hasEmail(email));
        }
        if (StringUtils.hasText(phone)) {
            spec = spec.and(UserSpectification.hasPhone(phone));
        }
        if (StringUtils.hasText(firstName)) {
            spec = spec.and(UserSpectification.hasFirstName(firstName));
        }
        if (StringUtils.hasText(lastName)) {
            spec = spec.and(UserSpectification.hasLastName(lastName));
        }

        return userRepository.findAll(spec, pageable);
    }

    @Override
    public void patch(UUID id, ProfileUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User không tồn tại"));

        if (StringUtils.hasText(req.getFirstName())) {
            user.setFirstName(req.getFirstName().trim());
        }
        if (StringUtils.hasText(req.getLastName())) {
            user.setLastName(req.getLastName().trim());
        }
        if (StringUtils.hasText(req.getEmail())) {
            user.setEmail(req.getEmail().trim());
        }
        if (StringUtils.hasText(req.getPhone())) {
            user.setPhone(req.getPhone().trim());
        }

        userRepository.save(user);
    }



    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public CountByRole countUserByRole() {
        List<User> users = userRepository.findAll();
        int countAdmin = 0 ;
        int countStaff = 0 ;
        int  countUser = 0 ;
        for (User user : users) {
            if (user.getRole() == Role.ADMIN) {
                countAdmin++;
            }else if (user.getRole() == Role.STAFF) {
                countStaff++;
            } else if (user.getRole() == Role.USER) {
                countUser++;

            }



        }
        CountByRole countByRole = new CountByRole();
        countByRole.setCount_user(countUser);
        countByRole.setCount_admin(countAdmin);
        countByRole.setCount_staff(countStaff);
        return countByRole;
    }


}
