package Ecommerce.sugar.plum.bazzar.Auth;

import Ecommerce.sugar.plum.bazzar.Merchant.MerchantRepository;
import Ecommerce.sugar.plum.bazzar.Users.Service;
import Ecommerce.sugar.plum.bazzar.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthService authService;
    @Autowired
    Service customerService;
//    @PostMapping("/signup")
//    public Response signup(@RequestBody Dto dto) {
//        return customerService.signup(dto);
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto){
        return  ResponseEntity.ok(authService.login(loginDto));
    }
}
