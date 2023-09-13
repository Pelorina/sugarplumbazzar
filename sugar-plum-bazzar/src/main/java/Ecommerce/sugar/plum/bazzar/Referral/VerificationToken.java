package Ecommerce.sugar.plum.bazzar.Referral;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
@Service
public class VerificationToken {
    public  String generateVerificationToken() {
        int tokenLength = 20; // Adjust the token length as needed
        byte[] randomBytes = new byte[tokenLength];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public static void main(String[] args) {

    }
}
