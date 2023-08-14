package Ecommerce.sugar.plum.bazzar.Users;

import Ecommerce.sugar.plum.bazzar.Utils.Response;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class Controller {
    public final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/verifyAccount/{verificationToken1}")
    public String verifyAccount(@PathVariable("verificationToken1") String verificationToken1) {
        String isVerified = service.VerifyToken(verificationToken1);

        if (isVerified.equalsIgnoreCase("success")) {
            return "Your account has been successfully verified.";
        } else {
            return "Invalid verification token or the token has expired.";
        }

    }

    @PostMapping("/register")
    public Response create(@RequestBody Request request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public Response findById(@PathVariable("id") Long id) {
        return service.findBYId(id);
    }

    @PutMapping("/resetPassword")
    public Response reset(@RequestBody ResetDto resetDto) {
        return service.resetPassword(resetDto);
    }


}
