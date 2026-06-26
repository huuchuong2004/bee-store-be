package vn.huuchuong.be_bee_store.analys_module;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.huuchuong.be_bee_store.analys_module.payload.response.BestSellerResponse;
import vn.huuchuong.be_bee_store.analys_module.payload.response.DashboardSummaryResponse;
import vn.huuchuong.be_bee_store.analys_module.payload.response.RevenueStatsResponse;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.order_module.service.OrderService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "API thống kê và tổng quan dashboard cho quản trị viên")
public class AdminDashboardController {

    private final OrderService orderService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Lấy tổng quan dashboard",
            description = "API lấy thông tin tổng quan dashboard gồm doanh thu, đơn hàng và các chỉ số thống kê chính"
    )
    public ResponseEntity<BaseResponse<DashboardSummaryResponse>> summary() {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getDashboardSummary(),
                        "Lấy tổng quan dashboard thành công"
                )
        );
    }

    @GetMapping("/best-sellers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Lấy danh sách sản phẩm bán chạy",
            description = "API thống kê danh sách sản phẩm bán chạy trong khoảng thời gian được chọn"
    )
    public ResponseEntity<BaseResponse<List<BestSellerResponse>>> bestSellers(
            @Parameter(description = "Ngày bắt đầu thống kê", example = "2025-01-01")
            @RequestParam LocalDate from,

            @Parameter(description = "Ngày kết thúc thống kê", example = "2025-01-31")
            @RequestParam LocalDate to,

            @Parameter(description = "Số lượng sản phẩm bán chạy cần lấy", example = "10")
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getBestSellers(from, to, limit),
                        "Lấy thống kê best seller thành công"
                )
        );
    }

    @GetMapping("/revenue/daily")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Thống kê doanh thu theo ngày",
            description = "API lấy thống kê doanh thu từng ngày trong khoảng thời gian được chọn"
    )
    public ResponseEntity<BaseResponse<List<RevenueStatsResponse>>> revenueByDay(
            @Parameter(description = "Ngày bắt đầu thống kê", example = "2025-01-01")
            @RequestParam LocalDate from,

            @Parameter(description = "Ngày kết thúc thống kê", example = "2025-01-31")
            @RequestParam LocalDate to
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getRevenueByDay(from, to),
                        "Lấy thống kê doanh thu theo ngày thành công"
                )
        );
    }

    @GetMapping("/revenue/monthly")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ADMIN','STAFF')")
    @Operation(
            summary = "Thống kê doanh thu theo tháng",
            description = "API lấy thống kê doanh thu theo từng tháng trong khoảng thời gian được chọn"
    )
    public ResponseEntity<BaseResponse<List<RevenueStatsResponse>>> revenueByMonth(
            @Parameter(description = "Ngày bắt đầu thống kê", example = "2025-01-01")
            @RequestParam LocalDate from,

            @Parameter(description = "Ngày kết thúc thống kê", example = "2025-12-31")
            @RequestParam LocalDate to
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        orderService.getRevenueByMonth(from, to),
                        "Lấy thống kê doanh thu theo tháng thành công"
                )
        );
    }
}