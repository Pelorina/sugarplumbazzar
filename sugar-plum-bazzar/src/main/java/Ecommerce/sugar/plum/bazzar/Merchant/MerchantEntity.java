package Ecommerce.sugar.plum.bazzar.Merchant;

import Ecommerce.sugar.plum.bazzar.Product.ProductEntity;
import Ecommerce.sugar.plum.bazzar.Utils.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class MerchantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @Size(min=3, max=10, message="invalid firstname")
    private String brandName;
    //    @Size(min=3, max=10,
    //    @Size(min=7, max=10, message="invalid username")
    private String username;
    //    @Size(min=3, max=10, message="password not strong enough")
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String gender;
    private String profilePicture;
    private String verificationToken;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Roles roles;
    @OneToMany
    @JoinColumn(name = "merchant_id")
    private List<ProductEntity> products;

}
