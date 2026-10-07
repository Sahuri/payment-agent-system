package com.hurie.payment.api.service;

import com.hurie.payment.api.exception.PaymentNotFoundException;
import com.hurie.payment.api.model.Payment;
import com.hurie.payment.api.model.PaymentCallback;
import com.hurie.payment.api.store.PaymentStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentServiceTest {

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(new PaymentStore());
    }

    @Test
    void getPaymentReturnsMatchingPayment() {

        Payment payment = paymentService.getPayment("RC-123");

        assertThat(payment.amount())
                .isEqualByComparingTo(BigDecimal.valueOf(100000));
        assertThat(payment.status()).isEqualTo("PAID");
    }

    @Test
    void getPaymentThrowsForUnknownOrderId() {

        assertThatThrownBy(() -> paymentService.getPayment("UNKNOWN"))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessage("Payment not found for order id: UNKNOWN");
    }

    @Test
    void getCallbacksForRc123HasTwoEntries() {

        List<PaymentCallback> callbacks = paymentService.getCallbacks("RC-123");

        assertThat(callbacks).hasSize(2);
    }

    @Test
    void getCallbacksForRc124HasOneEntry() {

        assertThat(paymentService.getCallbacks("RC-124")).hasSize(1);
    }

    @Test
    void getCallbacksForUnknownIsEmpty() {

        assertThat(paymentService.getCallbacks("UNKNOWN")).isEmpty();
    }

    @Test
    void countFailedTransactionReturnsOne() {

        assertThat(paymentService.countFailedTransaction()).isEqualTo(1);
    }

    @Test
    void getPaymentInfoReturnsThreeInOrder() {

        List<Payment> info = paymentService.getPaymentInfo();

        assertThat(info).hasSize(3);
        assertThat(info)
                .extracting(Payment::orderId)
                .containsExactly("RC-123", "RC-124", "RC-125");
    }
}
