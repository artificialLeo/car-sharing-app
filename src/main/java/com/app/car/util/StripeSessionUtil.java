package com.app.car.util;

import com.app.car.exception.payment.PaymentSessionCreationException;
import com.app.car.model.enums.PaymentType;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeSessionUtil {

    @Value("${stripe.secret}")
    private String stripeSecretKey;

    public String createPaymentSession(
            String successUrl, String cancelUrl,
            Long rentalId,
            PaymentType paymentType,
            BigDecimal unitAmount
    ) throws PaymentSessionCreationException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> sessionParams = new HashMap<>();
        sessionParams.put("payment_method_types", List.of("card"));
        sessionParams.put("line_items", createLineItems(rentalId, paymentType, unitAmount));
        sessionParams.put("payment_intent_data", createPaymentIntentData());
        sessionParams.put("success_url", successUrl);
        sessionParams.put("cancel_url", cancelUrl);

        try {
            Session session = Session.create(sessionParams);
            return session.getUrl();
        } catch (Exception e) {
            throw new PaymentSessionCreationException(e);
        }
    }

    private List<Map<String, Object>> createLineItems(
            Long rentalId,
            PaymentType paymentType,
            BigDecimal unitAmount
    ) {
        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("name", "Car Rental");
        lineItem.put("amount", unitAmount.multiply(BigDecimal.valueOf(100)).intValue());
        lineItem.put("currency", "usd");
        lineItem.put("quantity", 1);

        List<Map<String, Object>> lineItems = new ArrayList<>();
        lineItems.add(lineItem);

        return lineItems;
    }

    private Map<String, Object> createPaymentIntentData() {
        Map<String, Object> paymentIntentData = new HashMap<>();
        paymentIntentData.put("description", "Car Rental Payment");

        return paymentIntentData;
    }
}


