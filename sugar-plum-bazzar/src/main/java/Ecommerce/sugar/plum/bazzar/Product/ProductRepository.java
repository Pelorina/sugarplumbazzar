package Ecommerce.sugar.plum.bazzar.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    boolean existsByUniqueIdentifier(String UniqueIdentifier);
   boolean existsByProductName(String productName);
    Optional<ProductEntity> findById(Long id);
    Optional<ProductEntity>findByUniqueIdentifier(String quantity);



 Optional<ProductEntity> findByMerchantId( Long merchant_id);

//    boolean existsByCategoryOrBrandNameOrPriceOrProductName(String category, String brandName,
//                                                            String price,String productName);
//

    List<ProductEntity> findByProductName(String productName);
    List<ProductEntity> findByBrandName( String brandName);
    List<ProductEntity> findByPrice( double price);
    List<ProductEntity> findByCategory( String category);

//    List<ProductEntity>findByPriceOrCategoryOrBrandNameOrProductName(String category, String brandName,
//                                                                            String price,String productName);

}
