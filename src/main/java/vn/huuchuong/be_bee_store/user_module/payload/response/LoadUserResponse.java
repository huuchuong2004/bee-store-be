package vn.huuchuong.be_bee_store.user_module.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.huuchuong.be_bee_store.auth_module.entity.Role;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoadUserResponse {


    private String username;


    private String email;


    private String firstName;


    private String lastName;


    private String phone;


    private BigDecimal amount;


    private Boolean isActive;


    private Role role;
}
