package Ecommerce.sugar.plum.bazzar.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String name;
    private String username;
    private String address;
}
