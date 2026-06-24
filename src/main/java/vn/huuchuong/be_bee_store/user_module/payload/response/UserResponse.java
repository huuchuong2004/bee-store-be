package vn.huuchuong.be_bee_store.user_module.payload.response;



import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Boolean isActive;

    private String role;
}