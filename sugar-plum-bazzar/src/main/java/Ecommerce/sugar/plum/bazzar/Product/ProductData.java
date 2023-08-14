package Ecommerce.sugar.plum.bazzar.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private String brandName;
    private String productName;
    private String category;
    private String price;
}
