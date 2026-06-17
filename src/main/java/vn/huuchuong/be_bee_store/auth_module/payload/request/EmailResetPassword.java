package vn.huuchuong.be_bee_store.auth_module.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailResetPassword {
    @Size(min = 6, max = 30,message = "Do dai email chua phu hop 6- 30!")
    @Email(message = "Incorrect Email")
    @NotBlank(message = "Email not null !")
    private String email;

    @Size(min = 6, max = 30,message = "Độ dài username chưa phù hợp 6- 30 !")
    @NotBlank(message = "Username not null !")
    private String username;
}
