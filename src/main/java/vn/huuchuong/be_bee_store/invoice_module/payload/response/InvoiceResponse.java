package vn.huuchuong.be_bee_store.invoice_module.payload.response;


import lombok.Data;
import vn.huuchuong.be_bee_store.invoice_module.entity.InvoiceItem;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceResponse {


    private Integer invoiceId;



    private Order order;


    private LocalDate invoiceDate;


    private BigDecimal totalAmount;



    private String buyerName;


    private String buyerAddress;


    private String buyerEmail;


    private String buyerPhone;


    private List<InvoiceItem> items;
}
