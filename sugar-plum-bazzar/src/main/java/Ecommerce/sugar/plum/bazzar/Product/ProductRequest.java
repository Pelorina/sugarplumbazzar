package Ecommerce.sugar.plum.bazzar.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String category;
    private String brandName;
    private Double price;
    private String productName;
    private Integer quantity;
    private String uniqueIdentifier;
    private Long merchant_id;
}
