package Ecommerce.sugar.plum.bazzar.Carts;

import Ecommerce.sugar.plum.bazzar.Product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

//    @OneToOne
//    @JoinColumn(name = "entity_id")
//    private Ecommerce.sugar.plum.bazzar.Users.Entity user;
    private Integer quantity;
    private String productName;
    private double price;
    private String brandName;
    private double totalAmount;


//    public void nullifyAllFields() {
//        // Nullify all fields that need to be cleared
//        this.product = null;
//        this.quantity = null;
//        this.cart = null;
//        this.productName = null;
//        this.id = null;
//    }
}

