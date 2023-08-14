package Ecommerce.sugar.plum.bazzar.Utils;

import Ecommerce.sugar.plum.bazzar.Product.ProductData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String responseCode;
    private String responseMessage;
    private Data data;
    private ProductData productData;
}
