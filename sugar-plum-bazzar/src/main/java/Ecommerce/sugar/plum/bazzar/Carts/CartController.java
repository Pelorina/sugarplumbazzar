package Ecommerce.sugar.plum.bazzar.Carts;

import Ecommerce.sugar.plum.bazzar.Users.Entity;
import Ecommerce.sugar.plum.bazzar.Users.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
        private final CartService cartService;
        private final Service userService;
        public CartController(CartService cartService, Service userService) {
            this.cartService = cartService;
            this.userService = userService;
        }

        @PostMapping("/add")
        public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
            Entity user = userService.getCurrentUser();
            cartService.addToCart(user, request.getProductId(), request.getQuantity());

            return ResponseEntity.ok("Product added to cart");
        }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody RemoveFromCartRequest request) {
        Entity user = userService.getCurrentUser();
        ResponseEntity<String> response = cartService.removeFromCart(user, request.getProductId(), request.getQuantity());

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }

        return ResponseEntity.ok("Product removed from cart.");
    }




}


