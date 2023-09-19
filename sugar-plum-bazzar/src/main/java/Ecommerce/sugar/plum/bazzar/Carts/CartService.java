package Ecommerce.sugar.plum.bazzar.Carts;

import Ecommerce.sugar.plum.bazzar.Product.ProductEntity;
import Ecommerce.sugar.plum.bazzar.Product.ProductService;
import Ecommerce.sugar.plum.bazzar.Users.Entity;
import Ecommerce.sugar.plum.bazzar.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(UserRepository userRepository, ProductService productService, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    public void addToCart(Entity user, Long productId, int quantity) {
        ProductEntity product = productService.getProductById(productId);

        Cart cart = user.getCart();
        CartItem cartItem = findCartItemByProduct(cart, product);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);
            cartItem.setTotalAmount(cartItem.getPrice() * newQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setProductName(product.getProductName());
            cartItem.setPrice(product.getPrice());
            cartItem.setBrandName(product.getBrandName());
            cartItem.setTotalAmount(product.getPrice() * quantity);


            // Save the cart item to generate the ID
            cartItemRepository.save(cartItem);
        }
        cart.getItems().add(cartItem);


        userRepository.save(user);

    }

    public ResponseEntity<String> removeFromCart(Entity user, Long productId, Integer quantity) {
        Cart cart = user.getCart();
        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, productId);

        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }

        if (cartItem.getQuantity() > quantity) {
            // Calculate the price per unit of the product
            double pricePerUnit = cartItem.getProduct().getPrice();

            // Update the quantity and total price
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItem.setTotalAmount(pricePerUnit * cartItem.getQuantity());
        } else {
            // Remove the item from the cart and update the total price
            cart.getItems().remove(cartItem);
            cartItem.setCart(null);
        }

        userRepository.save(user);

        return ResponseEntity.ok("Product removed from cart.");
    }




    private CartItem findCartItemByProduct (Cart cart, ProductEntity product){
            return cart.getItems().stream()
                    .filter(item -> item.getProduct().equals(product))
                    .findFirst()
                    .orElse(null);
        }


    }


