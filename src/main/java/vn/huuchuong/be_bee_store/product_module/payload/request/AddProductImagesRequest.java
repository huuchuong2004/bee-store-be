package vn.huuchuong.be_bee_store.product_module.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class AddProductImagesRequest {

    private List<String> imageUrls;
}