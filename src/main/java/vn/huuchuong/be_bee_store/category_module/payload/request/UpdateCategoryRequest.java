package vn.huuchuong.be_bee_store.category_module.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequest {

    private String name;

    private String description;

    private Integer parentId;

    private Boolean isActive;
}
