package Ecommerce.sugar.plum.bazzar.Product;

import Ecommerce.sugar.plum.bazzar.Utils.Response;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Response createProduct(ProductRequest request);
    List<ProductData> searchProduct(String category,String brandName, Double price, String productName);
//    List<ProductData> searchProduct(Optional<String> category, java.util.Optional<String> brandName, Optional<String> price, Optional<String> productName
    Response updateProduct(UpdateProduct product);
    public String deleteProduct(Long id);
    public ProductEntity getProductById(Long id);
}
