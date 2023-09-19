package Ecommerce.sugar.plum.bazzar.Security;

import Ecommerce.sugar.plum.bazzar.Merchant.MerchantEntity;
import Ecommerce.sugar.plum.bazzar.Merchant.MerchantRepository;
import Ecommerce.sugar.plum.bazzar.Users.Entity;
import Ecommerce.sugar.plum.bazzar.Users.UserRepository;
import Ecommerce.sugar.plum.bazzar.Utils.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class CustomerDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;

    public CustomerDetailService(UserRepository userRepository, MerchantRepository merchantRepository) {
        this.userRepository = userRepository;
        this.merchantRepository = merchantRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Entity user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElse(null);
        MerchantEntity merchant = merchantRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElse(null);

        if (user == null && merchant == null) {
            throw new UsernameNotFoundException("User with provided credentials not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (user != null) {
            authorities.add(new SimpleGrantedAuthority(Roles.ROLE_USERS.name()));
        }

        if (merchant != null) {
            authorities.add(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.name()));
        }
//   tenary operator
        String password = (user != null) ? user.getPassword() : merchant.getPassword();

        // Use UserDetails interface instead of concrete User class
        return User.withUsername((user != null) ? user.getUsername() : merchant.getUsername())
                .password(password)
                .authorities(authorities)
                .build();
    }

}
