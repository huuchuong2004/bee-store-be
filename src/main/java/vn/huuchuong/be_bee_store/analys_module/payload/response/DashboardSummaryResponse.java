package vn.huuchuong.be_bee_store.analys_module.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {
    private Integer totalOrders;
    private Integer deliveredOrders;
    private BigDecimal totalRevenue;
}