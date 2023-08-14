package Ecommerce.sugar.plum.bazzar.Merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPassword {
    private String username;
    private String password;
    private String email;
    private String verificationToken2;
}
