package vn.huuchuong.be_bee_store.product_module.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.huuchuong.be_bee_store.category_module.entity.Category;
import vn.huuchuong.be_bee_store.category_module.repository.CategoryRepository;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.product_module.entity.Inventory;
import vn.huuchuong.be_bee_store.product_module.entity.Product;
import vn.huuchuong.be_bee_store.product_module.entity.ProductImage;
import vn.huuchuong.be_bee_store.product_module.entity.ProductVariant;
import vn.huuchuong.be_bee_store.product_module.map.ProductMapper;
import vn.huuchuong.be_bee_store.product_module.payload.request.*;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductListResponse;
import vn.huuchuong.be_bee_store.product_module.payload.response.ProductResponse;
import vn.huuchuong.be_bee_store.product_module.repository.IProductVariantRepository;
import vn.huuchuong.be_bee_store.product_module.repository.InventoryRepository;
import vn.huuchuong.be_bee_store.product_module.repository.ProductImageRepository;
import vn.huuchuong.be_bee_store.product_module.repository.ProductRepository;
import vn.huuchuong.be_bee_store.product_module.service.ProductService;
import vn.huuchuong.be_bee_store.product_module.spec.ProductSpectification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final IProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;
    private final InventoryRepository inventoryRepository;

    // ======================== HELPER ============================

    private Product getActiveProduct(Integer productId) {
        return productRepository.findByProductIdAndDeletedFalse(productId)
                .orElseThrow(() -> new BusinessException("Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    // ======================== CREATE PRODUCT ============================

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest req) {

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new BusinessException("Danh mục không tồn tại"));

        Product product = new Product();
        product.setCategory(category);
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setBaseprice(req.getBaseprice());
        product.setDeleted(false);
        product.setDeletedAt(null);

        product.setVariants(new ArrayList<>());
        product.setImages(new ArrayList<>());

        // Tạo variants
        if (req.getVariants() != null) {
            for (CreateProductVariantRequest vReq : req.getVariants()) {

                ProductVariant variant = new ProductVariant();
                variant.setProduct(product);
                variant.setSize(vReq.getSize());
                variant.setColor(vReq.getColor());
                variant.setPrice(vReq.getPrice());
                variant.setQuantityInStock(vReq.getQuantityInStock());

                String sku = vReq.getSku();
                if (sku == null || sku.isBlank()) {
                    sku = generateUniqueSku(product, vReq);
                }
                variant.setSku(sku);

                product.getVariants().add(variant);
            }
        }

        // Tạo images
        if (req.getImageUrls() != null) {
            for (String url : req.getImageUrls()) {
                if (url == null || url.isBlank()) continue;
                ProductImage img = new ProductImage();
                img.setProduct(product);
                img.setImageURL(url.trim());
                product.getImages().add(img);
            }
        }

        Product saved = productRepository.save(product);

        // Tạo inventory cho từng variant
        for (ProductVariant v : saved.getVariants()) {
            Inventory inv = new Inventory();
            inv.setProductVariant(v);
            inv.setCurrentStockLevel(v.getQuantityInStock());
            inv.setLastUpdate(LocalDate.now());
            inventoryRepository.save(inv);
        }

        return productMapper.toProductResponse(saved);
    }

    // ======================== GET ALL ============================

    @Override
    @Transactional
    public Page<ProductListResponse> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findByDeletedFalse(pageable);
        return page.map(productMapper::toProductListResponse);
    }

    // ======================== GET DETAIL ============================

    @Override
    @Transactional
    public ProductResponse getProductDetail(Integer productId) {
        Product product = getActiveProduct(productId);
        return productMapper.toProductResponse(product);
    }

    // ======================== UPDATE PRODUCT ============================

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, UpdateProductRequest req) {

        Product product = getActiveProduct(id);

        // category
        if (req.getCategoryId() != null && req.getCategoryId() > 0) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new BusinessException("Danh mục không tồn tại"));
            product.setCategory(category);
        }

        // name
        if (req.getName() != null && !req.getName().isBlank()) {
            String cleaned = req.getName().trim();
            if (cleaned.length() < 3) {
                throw new BusinessException("Tên sản phẩm phải có ít nhất 3 ký tự");
            }
            product.setName(cleaned);
        }

        // description
        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            product.setDescription(req.getDescription().trim());
        }

        // baseprice
        if (req.getBaseprice() != null) {
            if (req.getBaseprice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Giá sản phẩm phải lớn hơn 0");
            }
            product.setBaseprice(req.getBaseprice());
        }

        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    // ======================== SOFT DELETE PRODUCT ============================

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {

        Product product = getActiveProduct(productId);

        List<ProductVariant> variants = product.getVariants();

        if (variants != null) {
            for (ProductVariant variant : variants) {

                Inventory inv = inventoryRepository.findByProductVariant(variant)
                        .orElse(null);

                if (inv != null && inv.getCurrentStockLevel() != null && inv.getCurrentStockLevel() > 0) {
                    throw new BusinessException(
                            "Không thể xóa sản phẩm vì biến thể " + variant.getSku()
                                    + " còn tồn kho: " + inv.getCurrentStockLevel()
                    );
                }
            }
        }

        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    // ======================== RESTORE PRODUCT ============================

    @Override
    @Transactional
    public ProductResponse restoreProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Sản phẩm không tồn tại"));

        if (Boolean.FALSE.equals(product.getDeleted())) {
            throw new BusinessException("Sản phẩm chưa bị xóa");
        }

        product.setDeleted(false);
        product.setDeletedAt(null);

        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    // ======================== VARIANT: CREATE ============================

    @Override
    @Transactional
    public ProductResponse createpv(Integer productId, CreateProductVariantRequest req) {

        Product product = getActiveProduct(productId);

        if (product.getVariants() == null) {
            product.setVariants(new ArrayList<>());
        }

        // Kiểm tra trùng size + color
        boolean exists = product.getVariants().stream()
                .anyMatch(v -> v.getSize().equalsIgnoreCase(req.getSize())
                        && v.getColor().equalsIgnoreCase(req.getColor()));
        if (exists) {
            throw new BusinessException("Biến thể này đã tồn tại");
        }

        // Tạo variant mới
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSize(req.getSize());
        variant.setColor(req.getColor());
        variant.setPrice(req.getPrice());
        variant.setQuantityInStock(req.getQuantityInStock());

        String sku = req.getSku();
        if (sku == null || sku.isBlank()) {
            sku = generateUniqueSku(product, req);
        }
        variant.setSku(sku);

        ProductVariant savedVariant = productVariantRepository.save(variant);
        product.getVariants().add(savedVariant);

        Inventory inventory = new Inventory();
        inventory.setProductVariant(savedVariant);
        inventory.setCurrentStockLevel(savedVariant.getQuantityInStock());
        inventory.setLastUpdate(LocalDate.now());

        inventoryRepository.save(inventory);
        productRepository.save(product);

        Product productLatest = getActiveProduct(productId);
        return productMapper.toProductResponse(productLatest);
    }

    // ======================== VARIANT: UPDATE ============================

    @Override
    @Transactional
    public ProductResponse updateVariant(Integer productId, Integer variantId, UpdateProductVariantRequest req) {

        Product product = getActiveProduct(productId);

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new BusinessException("Biến thể không tồn tại"));

        if (!variant.getProduct().getProductId().equals(productId)) {
            throw new BusinessException("Biến thể không thuộc về sản phẩm này");
        }

        // Validate size + color
        String newSize = (req.getSize() != null && !req.getSize().isBlank())
                ? req.getSize().trim()
                : variant.getSize();

        String newColor = (req.getColor() != null && !req.getColor().isBlank())
                ? req.getColor().trim()
                : variant.getColor();

        boolean duplicated = product.getVariants().stream()
                .anyMatch(v -> !v.getProductVariantId().equals(variantId)
                        && v.getSize().equalsIgnoreCase(newSize)
                        && v.getColor().equalsIgnoreCase(newColor));

        if (duplicated) {
            throw new BusinessException("Biến thể size " + newSize + " - màu " + newColor + " đã tồn tại");
        }

        // Update basic info
        if (req.getSize() != null) variant.setSize(newSize);
        if (req.getColor() != null) variant.setColor(newColor);
        if (req.getPrice() != null) variant.setPrice(req.getPrice());

        if (req.getQuantityInStock() != null) {
            if (req.getQuantityInStock() < 0) {
                throw new BusinessException("Tồn kho không được âm");
            }

            variant.setQuantityInStock(req.getQuantityInStock());

            Inventory inv = inventoryRepository.findByProductVariant(variant)
                    .orElseThrow(() -> new BusinessException("Không tìm thấy inventory"));

            inv.setCurrentStockLevel(req.getQuantityInStock());
            inv.setLastUpdate(LocalDate.now());
            inventoryRepository.save(inv);
        }

        productVariantRepository.save(variant);

        Product latestProduct = getActiveProduct(productId);
        return productMapper.toProductResponse(latestProduct);
    }

    // ======================== VARIANT: DELETE ============================

    @Override
    @Transactional
    public void deleteVariant(Integer productId, Integer variantId) {

        Product product = getActiveProduct(productId);

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new BusinessException("Biến thể không tồn tại"));

        if (!variant.getProduct().getProductId().equals(productId)) {
            throw new BusinessException("Biến thể không thuộc về sản phẩm này");
        }

        Inventory inventory = inventoryRepository.findByProductVariant(variant)
                .orElse(null);

        if (inventory != null && inventory.getCurrentStockLevel() != null
                && inventory.getCurrentStockLevel() > 0) {
            throw new BusinessException(
                    "Không thể xóa biến thể vì còn tồn kho: " + inventory.getCurrentStockLevel()
            );
        }

        if (inventory != null) {
            inventoryRepository.delete(inventory);
        }

        if (product.getVariants() != null) {
            product.getVariants().removeIf(v -> v.getProductVariantId().equals(variantId));
        }

        productVariantRepository.delete(variant);
    }

    // ======================== IMAGES: CREATE ============================

    @Override
    @Transactional
    public ProductResponse addImages(Integer productId, AddProductImagesRequest req) {

        Product product = getActiveProduct(productId);

        if (req.getImageUrls() == null || req.getImageUrls().isEmpty()) {
            throw new BusinessException("Danh sách imageUrls không được rỗng");
        }

        if (product.getImages() == null) {
            product.setImages(new ArrayList<>());
        }

        for (String url : req.getImageUrls()) {
            if (url == null || url.isBlank()) continue;
            ProductImage img = new ProductImage();
            img.setProduct(product);
            img.setImageURL(url.trim());
            product.getImages().add(img);
        }

        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    // ======================== IMAGES: DELETE BY ID ============================

    @Override
    @Transactional
    public void deleteImage(Integer productId, Integer imageId) {

        Product product = getActiveProduct(productId);

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException("Ảnh không tồn tại"));

        if (!image.getProduct().getProductId().equals(productId)) {
            throw new BusinessException("Ảnh không thuộc về sản phẩm này");
        }

        if (product.getImages() != null) {
            product.getImages().removeIf(img -> img.getProductImageId().equals(imageId));
        }

        productImageRepository.delete(image);
    }

    // ======================== IMAGES: DELETE BY URL ============================

    @Override
    @Transactional
    public void deleteImageByUrl(Integer productId, String imageUrl) {

        Product product = getActiveProduct(productId);

        ProductImage imageToDelete = null;

        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                if (img.getImageURL() != null && img.getImageURL().equals(imageUrl)) {
                    imageToDelete = img;
                    break;
                }
            }
        }

        if (imageToDelete == null) {
            throw new BusinessException("Ảnh không tồn tại hoặc không thuộc về sản phẩm này");
        }

        product.getImages().remove(imageToDelete);
        productImageRepository.delete(imageToDelete);
    }

    // ======================== SEARCH ============================

    @Override
    public Page<ProductListResponse> search(ProductFilter req, Pageable pageable) {
        Specification<Product> spec = ProductSpectification.notDeleted();

        if (req.getName() != null && !req.getName().isBlank()) {
            spec = spec.and(ProductSpectification.hasName(req.getName()));
        }

        if (req.getCategoryId() != null && req.getCategoryId() > 0) {
            spec = spec.and(ProductSpectification.hasCategory(req.getCategoryId()));
        }

        if (req.getMinPrice() != null) {
            spec = spec.and(ProductSpectification.hasMinPrice(req.getMinPrice()));
        }

        if (req.getMaxPrice() != null) {
            spec = spec.and(ProductSpectification.hasMaxPrice(req.getMaxPrice()));
        }

        Page<Product> page = productRepository.findAll(spec, pageable);

        return page.map(productMapper::toProductListResponse);
    }

    // ======================== GET BY CATEGORY ============================

    @Override
    public Page<ProductListResponse> getProductByCategpgys(Integer categoryId, Pageable pageable) {
        Category root = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException("Danh mục không tồn tại"));

        List<Integer> list = getAllCategoryIdsIteractive(root);
        Page<Product> find = productRepository.findByCategory_IdInAndDeletedFalse(list, pageable);
        return find.map(productMapper::toProductListResponse);
    }

    // ======================== SKU HELPER ============================

    private String generateUniqueSku(Product product, CreateProductVariantRequest req) {
        String base = "P" +
                (product.getProductId() == null ? "NEW" : product.getProductId()) +
                "-" + normalize(req.getSize()) +
                "-" + normalize(req.getColor());

        String sku = base;
        int counter = 1;

        while (productVariantRepository.existsBySku(sku)) {
            counter++;
            sku = base + "-" + counter;
        }

        return sku;
    }

    // ======================== ADMIN: GET ALL INCLUDING DELETED ============================

    @Override
    @Transactional
    public Page<ProductListResponse> findAllForAdmin(Pageable pageable) {
        Page<Product> page = productRepository.findAllBy(pageable);
        return page.map(productMapper::toProductListResponse);
    }

// ======================== ADMIN: GET DETAIL INCLUDING DELETED ============================

    @Override
    @Transactional
    public ProductResponse getProductDetailForAdmin(Integer productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("Sản phẩm không tồn tại"));

        return productMapper.toProductResponse(product);
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("\\s+", "-").toUpperCase();
    }

    private List<Integer> getAllCategoryIdsIteractive(Category root) {
        List<Integer> categoryIds = new ArrayList<>();
        Queue<Category> categoryQueue = new LinkedList<>();

        categoryQueue.add(root);
        while (!categoryQueue.isEmpty()) {
            Category category = categoryQueue.poll();
            categoryIds.add(category.getId());
            if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                categoryQueue.addAll(category.getChildren());
            }
        }
        return categoryIds;
    }
}
