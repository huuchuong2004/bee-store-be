package vn.huuchuong.be_bee_store.category_module.payload.request;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;

    private String description;


    private Integer parent;
}
