package Ecommerce.sugar.plum.bazzar.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetDto {
    private String username;
    private String password;
    private String email;

}
