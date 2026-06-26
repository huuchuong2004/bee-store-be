package vn.huuchuong.be_bee_store.analys_module.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatsResponse {
    private String period;
    private BigDecimal revenue;
}