package vn.huuchuong.be_bee_store.product_module.map;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import vn.huuchuong.be_bee_store.product_module.entity.Product;
import vn.huuchuong.be_bee_store.product_module.entity.ProductImage;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductListResponse;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductResponse;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductVariantResponse;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "images", target = "imageUrls", qualifiedByName = "mapImagesToUrls")
    ProductResponse toProductResponse(Product product);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "images", target = "imageUrls", qualifiedByName = "mapImagesToUrls")
    ProductListResponse toProductListResponse(Product product);

    ProductVariantResponse toProductVariantResponse(ProductVariant variant);

    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(ProductImage::getImageURL)
                .filter(url -> url != null)
                .collect(Collectors.toList());
    }
}