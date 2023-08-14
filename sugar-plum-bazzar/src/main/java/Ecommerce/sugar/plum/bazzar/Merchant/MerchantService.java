package Ecommerce.sugar.plum.bazzar.Merchant;

import Ecommerce.sugar.plum.bazzar.Utils.Response;

public interface MerchantService {
     Response createAdmin(MerchantRequest merchantRequest);
     Response findByBrandName(String BrandName);
     Response forgetPassword (ResetPassword password);

     Response update(MerchantRequest request);


}
