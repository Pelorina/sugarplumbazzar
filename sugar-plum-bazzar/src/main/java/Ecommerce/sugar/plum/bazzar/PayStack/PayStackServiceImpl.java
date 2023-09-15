package Ecommerce.sugar.plum.bazzar.PayStack;

import Ecommerce.sugar.plum.bazzar.Carts.Cart;
import Ecommerce.sugar.plum.bazzar.Carts.CartItem;
import Ecommerce.sugar.plum.bazzar.Carts.CartItemRepository;
import Ecommerce.sugar.plum.bazzar.Carts.CartRepository;
import Ecommerce.sugar.plum.bazzar.Email.NotificationDetails;
import Ecommerce.sugar.plum.bazzar.Email.NotificationService;
import Ecommerce.sugar.plum.bazzar.Merchant.MerchantEntity;
import Ecommerce.sugar.plum.bazzar.Product.ProductEntity;
import Ecommerce.sugar.plum.bazzar.Product.ProductRepository;
import Ecommerce.sugar.plum.bazzar.Users.Entity;
import Ecommerce.sugar.plum.bazzar.Users.UserRepository;
import Ecommerce.sugar.plum.bazzar.Utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.net.http.HttpClient;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PayStackServiceImpl implements  PayStackService {
    private final WebClient webClient;
    private final PayStackPaymentRepository paystackPaymentRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Value("${applyforme.paystack.public.key}")
    private String payStackSecretKey;

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) {
        InitializePaymentResponse response = webClient.post()
                .uri(ResponseUtils.PAYSTACK_INITIALIZE_PAY)
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + payStackSecretKey)
                .body(BodyInserters.fromValue(initializePaymentDto))
                .retrieve()
                .bodyToMono(InitializePaymentResponse.class)
                .block();

        if (response != null && response.getStatus().equals(true)) {
            PaymentEntity payment = PaymentEntity.builder()
                    .amount(initializePaymentDto.getAmount())
                    .ipAddress(response.getData().getAuthorization_url())
                    .reference(response.getData().getReference())
                    .currency("NGN")
                    .build();
            paystackPaymentRepository.save(payment);


            return response;
        } else {
            return InitializePaymentResponse.builder()
                    .message("Paystack is unable to initialize payment at the moment")
                    .build();
        }


//    private void clearCurrentUserCart() {
//        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//        Entity currentUser = userRepository.findByUsername(currentUsername); // Assuming you have a method to find users by username
//
//        if (currentUser != null && currentUser.getCart() != null) {
//            currentUser.getCart().getItems().clear();
//            cartRepository.save(currentUser.getCart());
//        }
//    }
//
//    private void reduceStockAfterSuccessfulPayment(ProductEntity product, int quantity) {
//        int currentStock = product.getQuantity();
//        int updatedStock = currentStock - quantity;
//        product.setQuantity(updatedStock);
//        productRepository.save(product);
//    }
//
//    private String getProductOrderSummary(ProductEntity product, int quantity) {
//        Locale nigeriaLocale = new Locale("en", "NG");
//        NumberFormat nairaFormat = NumberFormat.getCurrencyInstance(nigeriaLocale);
//
//        String formattedPrice = nairaFormat.format(product.getPrice());
//        String formattedTotalPrice = nairaFormat.format(product.getPrice() * quantity);
//
//        return String.format(
//                "Product: %s (Qty: %d, Price: %s, Total: %s)",
//                product.getProductName(),
//                quantity,
//                formattedPrice,
//                formattedTotalPrice
//        );
//    }
    }


    @Override
    public PaymentVerificationResponse paymentVerification(String reference) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse;


        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(ResponseUtils.PAYSTACK_VERIFY + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + payStackSecretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == ResponseUtils.STATUS_CODE_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;

                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to verify payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            paymentVerificationResponse = mapper.readValue(result.toString(), PaymentVerificationResponse.class);

            if (paymentVerificationResponse == null || paymentVerificationResponse.getStatus().equals("false")) {
                throw new Exception("An error");
            } else if (paymentVerificationResponse.getData().getStatus().equals("success")) {


                Entity user = userRepository.findByEmail(paymentVerificationResponse.getData().getCustomer().getEmail()).get();
                PaymentEntity paymentPaystack = PaymentEntity.builder()
                        .userId(user.getId())
                        .name(user.getFirstname() + " " + user.getLastname())
                        .email(user.getEmail())
                        .reference(paymentVerificationResponse.getData().getReference())
                        .amount(paymentVerificationResponse.getData().getAmount())
                        .gatewayResponse(paymentVerificationResponse.getData().getGateway_response())
                        .paidAt(paymentVerificationResponse.getData().getPaidAt())
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .channel(paymentVerificationResponse.getData().getChannel())
                        .currency(paymentVerificationResponse.getData().getCurrency())
                        .ipAddress(paymentVerificationResponse.getData().getIp_address())
                        .createdOn(new Date())
                        .build();
                if (paymentPaystack != null) {
                    paystackPaymentRepository.save(paymentPaystack);
                }

                clearCurrentUserCart(user);

                for (CartItem cartItem : user.getCart().getItems()) {
                    ProductEntity product = cartItem.getProduct();
                    MerchantEntity merchant = product.getMerchant();

                    if (merchant != null && merchant.getEmail() != null) {
                        NotificationDetails notificationDetails = new NotificationDetails();
                        notificationDetails.setRecipient(merchant.getEmail());
                        notificationDetails.setSubject("New order notification");
                        notificationDetails.setMessageBody(getProductOrderSummary(product, cartItem.getQuantity()));

                        notificationService.sendSimpleEmail(notificationDetails);
                    }

                    reduceStockAfterSuccessfulPayment(product, cartItem.getQuantity());
                }
                return paymentVerificationResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return paymentVerificationResponse;

    }
    private void clearCurrentUserCart(Entity user) {
        if (user != null && user.getCart() != null) {
            for (CartItem cartItem : user.getCart().getItems()) {
                cartItem.reset(); // Reset cart item properties
            }
            cartRepository.save(user.getCart()); // Save the cleared cart
        }
    }

    private boolean reduceStockAfterSuccessfulPayment(ProductEntity product, int quantity) {
        int currentStock = product.getQuantity();
        if (quantity > currentStock) {
            return false;
        }

        int updatedStock = currentStock - quantity;
        product.setQuantity(updatedStock);
        productRepository.save(product);
        return true;
    }


    private String getProductOrderSummary(ProductEntity product, int quantity) {
        Locale nigeriaLocale = new Locale("en", "NG");
        NumberFormat nairaFormat = NumberFormat.getCurrencyInstance(nigeriaLocale);

        String formattedPrice = nairaFormat.format(product.getPrice());
        String formattedTotalPrice = nairaFormat.format(product.getPrice() * quantity);

        return String.format(
                "Product: %s\nQty: %d\nPrice: %s\nTotal: %s",
                product.getProductName(),
                quantity,
                formattedPrice,
                formattedTotalPrice
        );
    }
}









