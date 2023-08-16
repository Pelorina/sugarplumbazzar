package Ecommerce.sugar.plum.bazzar.PayStack;

public interface PayStackService {
    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
    PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}
