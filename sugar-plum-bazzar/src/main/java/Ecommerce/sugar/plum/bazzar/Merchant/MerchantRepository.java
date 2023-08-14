package Ecommerce.sugar.plum.bazzar.Merchant;

import Ecommerce.sugar.plum.bazzar.Users.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<MerchantEntity,Long> {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    boolean existsByBrandName( String brandName);
    Optional<MerchantEntity> findByBrandName(String brandName);
    Optional<MerchantEntity> findByEmailOrUsername(String email,String username);

    Optional<MerchantEntity> findByEmail(String email);
    Optional <MerchantEntity> findByVerificationToken (String verificationToken);
}
