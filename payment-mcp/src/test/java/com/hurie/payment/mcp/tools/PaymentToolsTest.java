package com.hurie.payment.mcp.tools;

import com.hurie.payment.mcp.dto.PaymentCallback;
import com.hurie.payment.mcp.dto.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PaymentToolsTest {

    private static final String BASE_URL = "http://localhost:8080";

    private MockRestServiceServer server;
    private PaymentTools paymentTools;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        paymentTools = new PaymentTools(builder.build());
    }

    @Test
    void getSystemHealthCallsActuatorHealth() {

        server.expect(requestTo(BASE_URL + "/actuator/health"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("UP", MediaType.TEXT_PLAIN));

        String health = paymentTools.getSystemHealth();

        assertThat(health).isEqualTo("UP");
        server.verify();
    }

    @Test
    void getPaymentStatusCallsPaymentByOrderId() {

        server.expect(requestTo(BASE_URL + "/api/payments/RC-123"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"orderId\":\"RC-123\",\"amount\":100000,\"status\":\"PAID\",\"channel\":\"QRIS\"}",
                        MediaType.APPLICATION_JSON));

        PaymentResponse response = paymentTools.getPaymentStatus("RC-123");

        assertThat(response.orderId()).isEqualTo("RC-123");
        assertThat(response.status()).isEqualTo("PAID");
        server.verify();
    }

    @Test
    void getCallbackHistoryCallsCallbacksPath() {

        server.expect(requestTo(BASE_URL + "/api/payments/RC-123/callbacks"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        List<PaymentCallback> callbacks = paymentTools.getCallbackHistory("RC-123");

        assertThat(callbacks).isEmpty();
        server.verify();
    }

    @Test
    void countFailedTransactionCallsFailedCountPath() {

        server.expect(requestTo(BASE_URL + "/api/payments/failed/count"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("1", MediaType.APPLICATION_JSON));

        Long count = paymentTools.countFailedTransaction();

        assertThat(count).isEqualTo(1L);
        server.verify();
    }

    @Test
    void getPaymentInfoCallsPaymentInfoPath() {

        server.expect(requestTo(BASE_URL + "/api/payments/payment/info"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"orderId\":\"RC-123\",\"amount\":100000,\"status\":\"PAID\",\"channel\":\"QRIS\"}]",
                        MediaType.APPLICATION_JSON));

        List<PaymentResponse> info = paymentTools.getPaymentInfo();

        assertThat(info).hasSize(1);
        assertThat(info.get(0).orderId()).isEqualTo("RC-123");
        server.verify();
    }
}
