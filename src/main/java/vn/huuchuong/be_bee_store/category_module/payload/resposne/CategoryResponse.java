package vn.huuchuong.be_bee_store.category_module.payload.resposne;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CategoryResponse {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;

    private Integer parentId;
    private String parentName;
}
