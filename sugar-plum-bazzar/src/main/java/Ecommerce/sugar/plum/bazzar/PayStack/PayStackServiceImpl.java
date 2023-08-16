package Ecommerce.sugar.plum.bazzar.PayStack;

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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.net.http.HttpClient;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PayStackServiceImpl implements PayStackService {
    private final WebClient webClient;
    private final PayStackPaymentRepository paystackPaymentRepository;
    private final UserRepository userRepository;

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
            PaymentEntity user = PaymentEntity.builder()
                    .amount(initializePaymentDto.getAmount())
                    .ipAddress(response.getData().getAuthorization_url())
                    .reference(response.getData().getReference())
                    .currency("NGN")
                    .build();
            paystackPaymentRepository.save(user);
            return response;
        } else {
            return InitializePaymentResponse.builder()
                    .message("Paystack is unable to initialize payment at the moment")
                    .build();
        }
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

//                AppUser appUser = appUserRepository.getReferenceById(id);
//                PricingPlanType pricingPlanType = PricingPlanType.valueOf(plan.toUpperCase());

//                CustomAttendeePaymentResponse user = new UserRequest(paymentVerificationResponse.getData().getCustomer().getEmail());
//                Entity user



               Entity user= userRepository.findByEmail( paymentVerificationResponse.getData().getCustomer().getEmail()).get();
                PaymentEntity paymentPaystack = PaymentEntity.builder()
                        .userId(user.getId())
                        .name(user.getFirstname()+ " " +user.getLastname())
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
                return paymentVerificationResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return paymentVerificationResponse;
    }


    }

