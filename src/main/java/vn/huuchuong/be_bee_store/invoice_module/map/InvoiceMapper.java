package vn.huuchuong.be_bee_store.invoice_module.map;



import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.invoice_module.entity.Invoice;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.InvoiceDTO;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.OrderDTO;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.OrderItemDTO;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.UserDTO;
import vn.huuchuong.be_bee_store.order_module.entity.Order;
import vn.huuchuong.be_bee_store.order_module.entity.OrderItem;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceMapper {

    public static InvoiceDTO toDto(Invoice invoice) {
        if (invoice == null) return null;

        return InvoiceDTO.builder()
                .id(invoice.getInvoiceId())
                .invoiceDate(invoice.getInvoiceDate())
                .totalAmount(invoice.getTotalAmount())
                .order(toDto(invoice.getOrder()))
                .build();
    }

    public static OrderDTO toDto(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .user(toDto(order.getUser()))
                .items(
                        order.getItems() == null
                                ? List.of()
                                : order.getItems().stream()
                                .map(InvoiceMapper::toDto)
                                .toList()
                )
                .build();
    }

    public static OrderItemDTO toDto(OrderItem item) {
        if (item == null) return null;

        ProductVariant v = item.getProductVariant();

        BigDecimal discount = item.getDiscountAmount() != null
                ? item.getDiscountAmount()
                : BigDecimal.ZERO;

        BigDecimal lineTotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .subtract(discount);

        return OrderItemDTO.builder()
                .orderItemId(item.getOrderItemId())
                .productVariantId(v.getProductVariantId())
                .productName(v.getProduct().getName())
                .size(v.getSize())
                .color(v.getColor())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discountAmount(item.getDiscountAmount())

                .build();
    }

    public static UserDTO toDto(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
