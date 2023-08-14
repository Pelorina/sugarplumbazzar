package Ecommerce.sugar.plum.bazzar.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProduct {
    private String productName;
    private String brandName;
    private String category;
    private Double price;
    private Integer quantity;
    private String uniqueIdentifier;
}
