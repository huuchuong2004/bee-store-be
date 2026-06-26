package vn.huuchuong.be_bee_store.invoice_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.invoice_module.payload.request.InvoiceDTO;
import vn.huuchuong.be_bee_store.invoice_module.service.InvoiceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoice", description = "API quản lý hóa đơn")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Lấy danh sách hóa đơn",
            description = "API dành cho ADMIN để lấy danh sách tất cả hóa đơn có phân trang"
    )
    public ResponseEntity<BaseResponse<Page<InvoiceDTO>>> getInvoices(Pageable pageable) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        invoiceService.getInvoices(pageable),
                        "Lấy danh sách hóa đơn thành công"
                )
        );
    }

    @GetMapping("/my")
    @Operation(
            summary = "Lấy danh sách hóa đơn của người dùng hiện tại",
            description = "API lấy danh sách hóa đơn của tài khoản đang đăng nhập"
    )
    public ResponseEntity<BaseResponse<List<InvoiceDTO>>> getMyInvoices() {
        return ResponseEntity.ok(
                BaseResponse.success(
                        invoiceService.getMyInvoices(),
                        "Lấy danh sách hóa đơn của bạn thành công"
                )
        );
    }

    @GetMapping("/{invoiceId}")
    @Operation(
            summary = "Lấy chi tiết hóa đơn",
            description = "API lấy thông tin chi tiết của một hóa đơn theo ID"
    )
    public ResponseEntity<BaseResponse<InvoiceDTO>> getInvoiceById(
            @PathVariable Integer invoiceId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        invoiceService.getInvoiceById(invoiceId),
                        "Lấy hóa đơn thành công"
                )
        );
    }
}