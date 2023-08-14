package Ecommerce.sugar.plum.bazzar.Merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantRequest {
    private String brandName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String gender;
    private String profilePicture;
    private String verificationToken;
}
