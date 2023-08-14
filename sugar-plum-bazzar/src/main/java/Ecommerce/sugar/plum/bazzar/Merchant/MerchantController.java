package Ecommerce.sugar.plum.bazzar.Merchant;

import Ecommerce.sugar.plum.bazzar.Utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    @PostMapping("/create")
    public Response createAdmin(@RequestBody MerchantRequest request){
        return merchantService.createAdmin(request);
    }

    @GetMapping("/{brandName}")
    public Response getByName(@PathVariable("brandName") String brandName ){
        return merchantService.findByBrandName(brandName);
    }
    @PutMapping("/update")
        public Response update (@RequestBody MerchantRequest merchantRequest){
        return  merchantService.update(merchantRequest);
    }
    @PostMapping("/reset")
    public Response resetPassword(@RequestBody ResetPassword password){
        return merchantService.forgetPassword(password);
    }

}
