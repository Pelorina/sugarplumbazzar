package Ecommerce.sugar.plum.bazzar.Product;

import Ecommerce.sugar.plum.bazzar.Merchant.MerchantEntity;
import Ecommerce.sugar.plum.bazzar.Merchant.MerchantRepository;
import Ecommerce.sugar.plum.bazzar.Utils.Response;
import Ecommerce.sugar.plum.bazzar.Utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    public ProductServiceImpl(ProductRepository productRepository, MerchantRepository merchantRepository) {
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
    }

//Must be a merchant
    @Override
    public Response createProduct(ProductRequest request) {
        Optional<MerchantEntity> optionalMerchant = merchantRepository.findById(request.getMerchant_id());
        if (optionalMerchant.isEmpty()) {
            throw new IllegalArgumentException("Merchant not found with ID: " + request.getMerchant_id());
        }
        String uniqueIdentifier = UUID.randomUUID().toString();
        boolean existsByUniqueIdentifier = productRepository.existsByUniqueIdentifier(uniqueIdentifier);

        if (existsByUniqueIdentifier) {
            return Response.builder()
                    .responseCode(ResponseUtils.PRODUCT_EXISTS_CODE)
                    .responseMessage(ResponseUtils.PRODUCT_EXISTS_MESSAGE)
                    .data(null)
                    .build();
        }

        ProductEntity product = ProductEntity.builder()
                .uniqueIdentifier(uniqueIdentifier) // Use the generated uniqueIdentifier
                .productName(request.getProductName())
                .brandName(request.getBrandName())
                .price(request.getPrice())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .merchant(optionalMerchant.get())
                .build();

        ProductEntity create = productRepository.save(product);

        return Response.builder()
                .responseCode(ResponseUtils.PRODUCT_SUCCESS_CODE)
                .responseMessage(ResponseUtils.PRODUCT_SUCCESS_MESSAGE)
                .productData(ProductData.builder()
                        .productName(create.getProductName())
                        .category(create.getCategory())
                        .brandName(create.getBrandName())
                        .build())
                .build();
    }

    @Override
    public List<ProductData> searchProduct(String category, String brandName, Double price, String productName) {
        logger.info("Search called with category: {0}, brandName: {1}, price: {2}, productName: {3}", category, brandName, price, productName);
        List<ProductData> productDataList = new ArrayList<>();

        if (category != null) {
            List<ProductEntity> productsByCategory = productRepository.findByCategory(category);
            productDataList.addAll(convertToProductDataList(productsByCategory));
        } else if (brandName != null) {
            List<ProductEntity> productsByBrandName = productRepository.findByBrandName(brandName);
            productDataList.addAll(convertToProductDataList(productsByBrandName));
        } else if (price != null) {
            List<ProductEntity> productsByPrice = productRepository.findByPrice(price);
            productDataList.addAll(convertToProductDataList(productsByPrice));
        } else if (productName != null) {
            List<ProductEntity> productsByProductName = productRepository.findByProductName(productName);
            productDataList.addAll(convertToProductDataList(productsByProductName));
        }
        return productDataList;
    }


    private List<ProductData> convertToProductDataList(List<ProductEntity> products) {
        return products.stream()
                .map(product -> ProductData.builder()
                        .category(product.getCategory())
                        .brandName(product.getBrandName())
                        .price(String.valueOf(product.getPrice()))
                        .productName(product.getProductName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Response updateProduct(UpdateProduct product) {
        Optional<ProductEntity> optionalProduct = productRepository.findByUniqueIdentifier(product.getUniqueIdentifier());
        if (optionalProduct.isEmpty()) {
            return Response.builder()
                    .responseCode(ResponseUtils.PRODUCT_NOT_CODE)
                    .responseMessage(ResponseUtils.PRODUCT_NOT_MESSAGE)
                    .data(null)
                    .build();
        }

        ProductEntity existingProduct = optionalProduct.get();
        existingProduct.setProductName(product.getProductName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrandName(product.getBrandName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());

        ProductEntity update = productRepository.save(existingProduct);
        return Response.builder()
                .responseCode(ResponseUtils.PRODUCT_SUCCESS_CODE)
                .responseMessage(ResponseUtils.PRODUCT_SUCCESS_UPDATE_MESSAGE)
                .productData(ProductData.builder()
                        .brandName(update.getBrandName())
                        .category(update.getCategory())
                        .productName(update.getProductName())
                        .build())
                .build();
    }


    @Override
    public String deleteProduct(Long id) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return "Product not found with ID: " + id;
        }

        productRepository.deleteById(id);

        return "Product with ID: " + id + " has been permanently deleted.";
    }
    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}




