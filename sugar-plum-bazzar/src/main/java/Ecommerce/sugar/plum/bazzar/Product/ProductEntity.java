package Ecommerce.sugar.plum.bazzar.Product;

import Ecommerce.sugar.plum.bazzar.Merchant.MerchantEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Setter
@Getter
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String brandName;
    private String category;
    private Double price;
    private String url;
    private Integer quantity;
    private String uniqueIdentifier;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private MerchantEntity merchant;

}
