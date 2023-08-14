package Ecommerce.sugar.plum.bazzar.Users;

import Ecommerce.sugar.plum.bazzar.Carts.Cart;
import Ecommerce.sugar.plum.bazzar.Utils.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
//import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Entity
@Builder
@Setter
@Getter
@Table
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Size(min=3, max=10, message="invalid firstname")
    private String firstname;
//    @Size(min=3, max=10, message="invalid lastname")
    private String lastname;
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
    @OneToOne(mappedBy = "user")
    private Cart cart;
}
