package Ecommerce.sugar.plum.bazzar.PayStack;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitializePaymentDto {
    private BigDecimal amount;
    private String email;
    private Long id;
}
