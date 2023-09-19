package Ecommerce.sugar.plum.bazzar.Merchant;

import Ecommerce.sugar.plum.bazzar.Email.EmailDetails;
import Ecommerce.sugar.plum.bazzar.Email.EmailService;
import Ecommerce.sugar.plum.bazzar.Referral.ReferralCode;
import Ecommerce.sugar.plum.bazzar.Referral.VerificationToken;
import Ecommerce.sugar.plum.bazzar.Utils.Data;
import Ecommerce.sugar.plum.bazzar.Utils.Response;
import Ecommerce.sugar.plum.bazzar.Utils.ResponseUtils;
import Ecommerce.sugar.plum.bazzar.Utils.Roles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class MerchantServiceImpl implements MerchantService {
    private static final long EXPIRATION_DURATION_MINUTES = 10;
    private static final String DIGITS = "0123456789";

    private final MerchantRepository merchantRepository;
    private final EmailService emailService;
    private final ReferralCode referralCode;
    private final VerificationToken verificationToken;
    private final PasswordEncoder passwordEncoder;

    public MerchantServiceImpl(MerchantRepository merchantRepository, EmailService emailService, ReferralCode referralCode, VerificationToken verificationToken, PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.emailService = emailService;
        this.referralCode = referralCode;
        this.verificationToken = verificationToken;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Response createAdmin(MerchantRequest merchantRequest) {
        boolean existsByEmail = merchantRepository.existsByEmail(merchantRequest.getEmail());
        if (existsByEmail) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .build();
        }
        MerchantEntity create = MerchantEntity.builder()
                .email(merchantRequest.getEmail())
                .brandName(merchantRequest.getBrandName())
                .address(merchantRequest.getAddress())
                .age(merchantRequest.getAge())
                .password(passwordEncoder.encode(merchantRequest.getPassword()))
                .username(merchantRequest.getUsername())
                .phoneNumber(merchantRequest.getPhoneNumber())
                .profilePicture(merchantRequest.getProfilePicture())
                .roles(Roles.ROLE_ADMIN)
                .build();
        MerchantEntity register = merchantRepository.save(create);
        String emailMessage = "Welcome to Sugar_Plum_Bazzar! You have successfully created your account. Please find your account details below:\n"
                + "Your brand name is: " + register.getBrandName() + "\n"
                + "Your username is: " + register.getUsername() + "\n"
                + "Your referral code is: " + ReferralCode.generateReferralCode() + "\n"
                + "Please enter the following code to verify your account: " + "\n"
                +generateVerificationCode();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(register.getEmail())
                .subject("Account Creation And Verification")
                .messageBody(emailMessage)
                .build();
        emailService.sendSimpleEmail(emailDetails);


        return Response.builder()
                .responseCode(ResponseUtils.USER_CREATED_CODE)
                .responseMessage(ResponseUtils.USER_CREATED_MESSAGE)
                .data(Data.builder()
                        .name(merchantRequest.getBrandName())
                        .address(merchantRequest.getAddress())
                        .username(merchantRequest.getUsername())
                        .build())
                .build();
    }


    @Override
    public Response findByBrandName(String brandName) {
        boolean existsByName = merchantRepository.existsByBrandName(brandName);
        if (!existsByName) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .data(null)
                    .build();
        } else {
            MerchantEntity findByName = merchantRepository.findByBrandName(brandName).get();
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(Data.builder()
                            .address(findByName.getAddress())
                            .name(findByName.getBrandName())
                            .username(findByName.getUsername())
                            .build())
                    .build();

        }
    }

    @Override
    public Response forgetPassword(ResetPassword password) {
        boolean existsByEmail = merchantRepository.existsByEmail(password.getEmail());
        if (!existsByEmail) {
            return Response.builder()
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .data(null)
                    .build();
        }

        MerchantEntity admin = merchantRepository.findByEmail(password.getEmail()).orElse(null);
        if (admin == null) {
            return Response.builder()
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .data(null)
                    .build();
        }

        admin.setPassword(passwordEncoder.encode(password.getPassword()));
        admin.setUsername(password.getUsername());

        merchantRepository.save(admin);

        String mailMessage = "A request to change password was sent from your account. Please verify by inputing the code below   " + "\n" +
                "\n" + generatePasswordResetCode();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(admin.getEmail())
                .subject("Password Verification")
                .messageBody(mailMessage)
                .build();
        emailService.sendSimpleEmail(emailDetails);


        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFULLY_RESET_PASSWORD_CODE)
                .responseMessage(ResponseUtils.SUCCESSFULLY_RESET_PASSWORD_MESSAGE)
                .build();
    }


    @Override
    public Response update(MerchantRequest request) {
        boolean existsByEmail = merchantRepository.existsByEmail(request.getEmail());
        if (!existsByEmail) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_DOES_NOT_CODE)
                    .responseMessage(ResponseUtils.USER_DOES_NOT_MESSAGE)
                    .data(null)
                    .build();
        }
        MerchantEntity existingUser = merchantRepository.findByEmail(request.getEmail()).get();
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setAge(request.getAge());
        existingUser.setAddress(request.getAddress());
        existingUser.setEmail(request.getEmail());
        existingUser.setBrandName(request.getBrandName());
        existingUser.setGender(request.getGender());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setRoles(Roles.ROLE_ADMIN);

        MerchantEntity update = merchantRepository.save(existingUser);
        return Response.builder()
                .responseCode(ResponseUtils.UPDATE_ADMIN_CODE)
                .responseMessage(ResponseUtils.UPDATE_ADMIN_MESSAGE)
                .data(Data.builder()
                        .username(update.getUsername())
                        .name(update.getBrandName())
                        .address(update.getAddress())
                        .build())
                .build();


    }
// MUTABLE object
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






        public static String generateVerificationCode() {
            StringBuilder verificationCode = new StringBuilder();
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 4; i++) {
                char randomDigit = DIGITS.charAt(random.nextInt(DIGITS.length()));
                verificationCode.append(randomDigit);
            }

            return verificationCode.toString();
        }

        public static void main(String[] args) {
            String verificationCode = generateVerificationCode();
            System.out.println("Generated Verification Code: " + verificationCode);
            System.out.println("password");
            System.out.println(generatePasswordResetCode());
        }
    }







