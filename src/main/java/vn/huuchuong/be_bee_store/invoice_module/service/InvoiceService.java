package vn.huuchuong.be_bee_store.invoice_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.invoice_module.entity.Invoice;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.InvoiceDTO;
import vn.huuchuong.be_bee_store.order_module.entity.Order;

import java.util.List;

public interface InvoiceService {
    public Invoice createInvoiceForOrder(Order order);
    Invoice createInvoiceForOrderIfNotExists(Order order);

    List<InvoiceDTO> getMyInvoices();

    InvoiceDTO getInvoiceById(Integer invoiceId);

    Page<InvoiceDTO> getInvoices(Pageable pageable);
}
