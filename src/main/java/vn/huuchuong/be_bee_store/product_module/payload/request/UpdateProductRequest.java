package vn.huuchuong.be_bee_store.product_module.payload.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {


    private Integer categoryId;
    private String name;
    private String description;
    private BigDecimal baseprice;
}