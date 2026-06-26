package vn.huuchuong.be_bee_store.product_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.product_module.payload.request.*;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductListResponse;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest req);

    Page<ProductListResponse> findAll(Pageable pageable);

    ProductResponse getProductDetail(Integer productId);

    ProductResponse updateProduct(Integer id, UpdateProductRequest req);

    void deleteProduct(Integer id);

    ProductResponse createpv(Integer productId, CreateProductVariantRequest req);

    ProductResponse updateVariant(Integer productId, Integer variantId, UpdateProductVariantRequest req);

    void deleteVariant(Integer productId, Integer variantId);

    ProductResponse addImages(Integer productId, AddProductImagesRequest req);

    void deleteImage(Integer productId, Integer imageId);

    Page search(ProductFilter productFilter, Pageable pageable);

    Page<ProductListResponse> getProductByCategpgys(Integer categoryId, Pageable pageable);

    void deleteImageByUrl(Integer productId, String imageUrl);

    ProductResponse restoreProduct(Integer productId);

    Page<ProductListResponse> findAllForAdmin(Pageable pageable);

    ProductResponse getProductDetailForAdmin(Integer productId);
}
