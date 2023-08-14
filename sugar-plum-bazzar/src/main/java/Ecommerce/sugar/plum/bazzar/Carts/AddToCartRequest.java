package Ecommerce.sugar.plum.bazzar.Carts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private Long productId;
    private int quantity;
    private String productName;
    private String brandName;

    }

