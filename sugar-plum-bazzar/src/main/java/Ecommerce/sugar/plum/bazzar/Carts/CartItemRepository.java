package Ecommerce.sugar.plum.bazzar.Carts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartAndProductId(Cart cart, Long productId);
}
