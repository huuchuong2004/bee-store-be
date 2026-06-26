package vn.huuchuong.be_bee_store.invoice_module.payload.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceDTO {

    private Integer id;
    private LocalDateTime invoiceDate;
    private BigDecimal totalAmount;

    private OrderDTO order;
}
