package Ecommerce.sugar.plum.bazzar.Users;

import Ecommerce.sugar.plum.bazzar.Carts.Cart;
import Ecommerce.sugar.plum.bazzar.Carts.CartRepository;
import Ecommerce.sugar.plum.bazzar.Email.EmailDetails;
import Ecommerce.sugar.plum.bazzar.Email.EmailService;
import Ecommerce.sugar.plum.bazzar.Referral.ReferralCode;
import Ecommerce.sugar.plum.bazzar.Referral.VerificationToken;
import Ecommerce.sugar.plum.bazzar.Utils.Data;
import Ecommerce.sugar.plum.bazzar.Utils.Response;
import Ecommerce.sugar.plum.bazzar.Utils.ResponseUtils;
import Ecommerce.sugar.plum.bazzar.Utils.Roles;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
    private static final long EXPIRATION_DURATION_MINUTES = 10;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ReferralCode referralCode;
    private final VerificationToken verificationToken;

    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public ServiceImpl(UserRepository userRepository, EmailService emailService, ReferralCode referralCode,
                       VerificationToken verificationToken, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.referralCode = referralCode;
        this.verificationToken = verificationToken;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Override
    public Response create(Request request) {
        boolean findByEmail = userRepository.existsByEmail(request.getEmail());
//        Entity register;
        if (findByEmail) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_CREATED_MESSAGE)
                    .data(null)
                    .build();
        }

        // Generate the verification token during user registration
        String verificationToken1 = verificationToken.generateVerificationToken();

        Entity register;
        try {
            // Your existing code for entity creation and saving
            Entity create = Entity.builder()
                    .address(request.getAddress())
                    .age(request.getAge())
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Roles.ROLE_USERS)
                    .phoneNumber(request.getPhoneNumber())
                    .username(request.getUsername())
                    .verificationToken(verificationToken1) // Save the generated verification token
                    .build();
            register = userRepository.save(create);

            // Send the verification email with the verification token as a link
//            String verificationLink = "https://localhost:8080/verify?token=" + verificationToken1;
//            String emailSubject = "Account Verification";
//            String emailBody = "Welcome to Sugar_Plum_Bazzar,You have successfully created your " +
//                    "account. Please click the following link to verify your account: " + verificationLink;
            String emailMessage = "Welcome to Sugar_Plum_Bazzar,\n\n"
                    + "You have successfully created your account. Please find your account details below:\n\n"
                    + "Your account name is: " + register.getFirstname() + " " + register.getLastname() + "\n"
                    + "Your username is: " + register.getUsername() + "\n"
                    + "Your referral code is: " + ReferralCode.generateReferralCode() + "\n\n"
                    + "Please click the following link to verify your account:\n"
                    + "http://localhost:8080/api/verifyAccount/" + verificationToken1 + "\n\n"
                    + "Thank you for joining us!\n\n"
                    + "Best regards,\n"
                    + "Sugar_Plum_Bazzar";

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(register.getEmail())
                    .subject("Account verification")
                    .messageBody(emailMessage)
                    .build();
            emailService.sendSimpleEmail(emailDetails);
            Cart cart = new Cart();
            cart.setUser(register); // Associate the cart with the registered user
            cartRepository.save(cart);
        } catch (MailException e) {
            // Handle email sending failure
            // e.g., log the error or return an appropriate error response
        }

        // Return the response after user registration
        return Response.builder()
                .responseCode(ResponseUtils.USER_CREATED_CODE)
                .responseMessage(ResponseUtils.USER_CREATED_MESSAGE)
                .data(Data.builder()
                        .name(request.getFirstname() + " " + request.getLastname())
                        .address(request.getAddress())
                        .username(request.getUsername())
                        .build())
                .build();

    }


    @Override
    public String VerifyToken(String verificationToken1) {
        Optional<Entity> optionalUser = userRepository.findByVerificationToken(verificationToken1);
        if (optionalUser.isPresent()) {
            Entity user = optionalUser.get();
//            if (!isTokenExpired(user.getCreatedAt())) {
            // Mark the user's account as verified
            user.setVerificationToken(String.valueOf(true));
            userRepository.save(user);
            return "SUCCESS";
        } else
            return "UNSUCCESSFUL";
    }


//    public boolean isTokenExpired(LocalDateTime tokenCreationTime) {
//        LocalDateTime now = LocalDateTime.now();
//        long durationSinceTokenCreation = now.until(tokenCreationTime, TimeUnit.MINUTES.toChronoUnit());
//        return durationSinceTokenCreation >= EXPIRATION_DURATION_MINUTES;
//    }


    @Override
    public Response findBYId(Long id) {
        boolean existsById = userRepository.existsById(id);
        if (!userRepository.existsById(id)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .data(null)
                    .build();

        } else {
            Entity findById = userRepository.findById(id).get();
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(Data.builder()
                            .username(findById.getUsername())
                            .name(findById.getFirstname() + " " + findById.getLastname())
                            .address(findById.getAddress())
                            .build())
                    .build();
        }
    }

    @Override
    public List<Entity> getAll(int pageNo, int pageSize) {
        return userRepository.findAll().stream().skip(pageNo - 1).limit(pageSize).toList();
    }

    @Override
    public Response resetPassword(ResetDto resetDto) {
        boolean existsByEmail = userRepository.existsByEmail(resetDto.getEmail());
        if (!existsByEmail) {
            return Response.builder()
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .data(null)
                    .build();
        }
        Entity user = userRepository.findByEmail(resetDto.getEmail()).orElse(null);
        if (user == null) {
            return Response.builder()
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .data(null)
                    .build();
        }
        user.setPassword(passwordEncoder.encode(resetDto.getPassword()));
        String mailMessage = "A request to change password was sent from your account. Please verify by inputing the code below to " +
                "\n" + "\n" + generatePasswordResetCode();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Password Verification")
                .messageBody(mailMessage)
                .build();
        emailService.sendSimpleEmail(emailDetails);



        userRepository.save(user);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFULLY_RESET_PASSWORD_CODE)
                .responseMessage(ResponseUtils.SUCCESSFULLY_RESET_PASSWORD_MESSAGE)
                .build();
    }

    public Entity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Assuming you have a method to find a user by username in your repository
        return userRepository.findByUsername(username);
    }


    public static String generatePasswordResetCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        // Generate a 4-digit code
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            codeBuilder.append(digit);
        }

        return codeBuilder.toString();
    }
}







