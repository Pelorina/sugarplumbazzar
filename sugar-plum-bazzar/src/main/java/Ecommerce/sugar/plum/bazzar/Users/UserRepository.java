package Ecommerce.sugar.plum.bazzar.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Entity,Long> {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<Entity> findById(Long id);
    Optional<Entity> findByEmail(String email);
   Optional <Entity> findByVerificationToken (String verificationToken);
    Optional<Entity> findByEmailOrUsername(String email,String username);
    Entity findByUsername(String username);

}
