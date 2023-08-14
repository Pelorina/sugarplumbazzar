package Ecommerce.sugar.plum.bazzar.Carts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveFromCartRequest {
    private int quantity;
    private Long productId;

}
