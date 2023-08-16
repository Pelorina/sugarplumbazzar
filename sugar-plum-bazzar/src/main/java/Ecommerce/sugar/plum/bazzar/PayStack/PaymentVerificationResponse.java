package Ecommerce.sugar.plum.bazzar.PayStack;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentVerificationResponse {
    private String status;
    private String message;
    private PaymentData data;
}
