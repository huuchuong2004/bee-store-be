package vn.huuchuong.be_bee_store.invoice_module.payload.request;


import lombok.*;
import vn.huuchuong.be_bee_store.order_module.Enum.OrderStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Integer orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;


    private List<OrderItemDTO> items;

    private UserDTO user;
}
