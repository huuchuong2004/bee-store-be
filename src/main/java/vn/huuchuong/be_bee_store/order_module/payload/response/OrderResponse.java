package vn.huuchuong.be_bee_store.order_module.payload.response;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Integer orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String status;

    private String couponCode;
    private BigDecimal discountValue;
    private List<OrderItemResponse> items;
}

