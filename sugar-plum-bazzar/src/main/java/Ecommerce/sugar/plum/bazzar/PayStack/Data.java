package Ecommerce.sugar.plum.bazzar.PayStack;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String authorization_url;
    private String access_code;
    private String reference;
}
