package vn.huuchuong.be_bee_store.order_module.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder

public class UserOrderResponse {

    private Integer orderId;
    private UUID userId;
    private String username;
    private String fisrtName;
    private String lastName;
    private String email;
    private String phone;
}
