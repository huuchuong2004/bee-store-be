package vn.huuchuong.be_bee_store.product_module.payload.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ProductListResponse {
    private Integer productId;

    private String name;
    private String description;
    private BigDecimal baseprice;

    private Integer categoryId;
    private String categoryName;

    private List<String> imageUrls;
    private boolean deleted;
    private LocalDateTime deletedAt;
}
