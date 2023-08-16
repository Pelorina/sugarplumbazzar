package Ecommerce.sugar.plum.bazzar.PayStack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitializePaymentResponse {

    private Boolean status;
    private String message;
    private Data data;
}
