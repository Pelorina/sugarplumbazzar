package Ecommerce.sugar.plum.bazzar.PayStack;

import Ecommerce.sugar.plum.bazzar.Users.Entity;

public interface PayStackService {
    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
    PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}
