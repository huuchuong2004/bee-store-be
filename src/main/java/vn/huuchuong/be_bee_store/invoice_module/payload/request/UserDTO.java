package vn.huuchuong.be_bee_store.invoice_module.payload.request;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
