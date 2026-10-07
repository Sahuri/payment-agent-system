package com.hurie.payment.api.controller;

import com.hurie.payment.api.service.PaymentService;
import com.hurie.payment.api.store.PaymentStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import({PaymentService.class, PaymentStore.class})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPaymentReturnsAmountAndStatus() throws Exception {

        mockMvc.perform(get("/api/payments/RC-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100000))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void getUnknownPaymentReturnsNotFound() throws Exception {

        mockMvc.perform(get("/api/payments/RC-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Payment not found"))
                .andExpect(jsonPath("$.orderId").value("RC-999"));
    }

    @Test
    void getFailedPaymentReturnsFailedStatus() throws Exception {

        mockMvc.perform(get("/api/payments/RC-124"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void getCallbacksForUnknownPaymentReturnsEmptyList() throws Exception {

        mockMvc.perform(get("/api/payments/UNKNOWN/callbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void countFailedTransactionReturnsOne() throws Exception {

        mockMvc.perform(get("/api/payments/failed/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void getCallbacksHasSizeTwo() throws Exception {

        mockMvc.perform(get("/api/payments/RC-123/callbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getPaymentInfoHasSizeThree() throws Exception {

        mockMvc.perform(get("/api/payments/payment/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
