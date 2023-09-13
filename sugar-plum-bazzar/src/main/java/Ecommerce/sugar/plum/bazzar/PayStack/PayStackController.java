package Ecommerce.sugar.plum.bazzar.PayStack;

import Ecommerce.sugar.plum.bazzar.Users.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor

public class PayStackController {
    private final PayStackService paystackService;
    @PostMapping("/initializePayment")
    @ResponseStatus(HttpStatus.OK)
    public InitializePaymentResponse initializePayment(@Validated @RequestBody InitializePaymentDto initializePaymentDto) throws Throwable {
        return paystackService.initializePayment(initializePaymentDto);
    }

    @GetMapping("/verifyPayment/{reference}")
    public PaymentVerificationResponse paymentVerification(@PathVariable(value = "reference") String reference) throws Exception {
        return paystackService.paymentVerification(reference);
    }
}
