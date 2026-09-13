package com.hurie.payment.mcp.tools;

import com.hurie.payment.mcp.dto.PaymentCallback;
import com.hurie.payment.mcp.dto.PaymentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class PaymentTools {

    private final RestClient paymentRestClient;

    public PaymentTools(RestClient paymentRestClient) {
        this.paymentRestClient = paymentRestClient;
    }

    @Tool(
            description = "Check payment service health")
    public String getSystemHealth() {

        return paymentRestClient
                .get()
                .uri("/api/health")
                .retrieve()
                .body(String.class);
    }

    @Tool(
            description =
                    "Get payment status by order id")
    public PaymentResponse getPaymentStatus(
            String orderId) {

        return paymentRestClient
                .get()
                .uri("/api/payments/{orderId}",
                        orderId)
                .retrieve()
                .body(PaymentResponse.class);
    }

    @Tool(
            description =
                    "Get payment callback history")
    public List<PaymentCallback>
    getCallbackHistory(
            String orderId) {

        return paymentRestClient
                .get()
                .uri(
                        "/api/payments/{orderId}/callbacks",
                        orderId)
                .retrieve()
                .body(List.class);
    }

    @Tool(
            description =
                    "Count failed transactions")
    public Long
    countFailedTransaction() {

        return paymentRestClient
                .get()
                .uri("/api/payments/failed/count")
                .retrieve()
                .body(Long.class);
    }
    @Tool(
            name = "get_payment_information",
            description = "get all payment information"
    )
    public List<PaymentResponse> getPaymentInfo() {
        try {
            return paymentRestClient
                    .get()
                    .uri("/api/payments/payment/info")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<PaymentResponse>>() {});
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Gagal mengambil data payment: " + e.getMessage(), e);
        }
    }
}