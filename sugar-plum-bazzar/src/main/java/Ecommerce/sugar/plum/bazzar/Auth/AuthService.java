package Ecommerce.sugar.plum.bazzar.Auth;


import Ecommerce.sugar.plum.bazzar.Users.Entity;

public interface AuthService {
    AuthResponse login (LoginDto loginDto);

}
