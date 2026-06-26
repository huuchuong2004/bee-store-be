package vn.huuchuong.be_bee_store.product_module.payload.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CreateProductRequest {
    private Integer categoryId;

    private String name;

    private String description;

    private BigDecimal baseprice;

    private List<CreateProductVariantRequest> variants;

    private List<String> imageUrls;
}
