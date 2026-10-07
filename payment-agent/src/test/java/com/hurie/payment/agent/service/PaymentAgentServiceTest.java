package com.hurie.payment.agent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentAgentServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private PaymentAgentService service;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        service = new PaymentAgentService(chatClient);

        when(chatClient.prompt(anyString()).call().content())
                .thenReturn("jawaban uji");

        clearInvocations(chatClient);
    }

    @Test
    void askReturnsContentFromChatClient() {

        assertThat(service.ask("status RC-123")).isEqualTo("jawaban uji");
    }

    @Test
    void promptSentContainsUserQuestion() {

        service.ask("status RC-123");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(chatClient).prompt(captor.capture());

        assertThat(captor.getValue()).contains("status RC-123");
    }

    @Test
    void promptSentContainsGuardrails() {

        service.ask("status RC-123");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(chatClient).prompt(captor.capture());

        assertThat(captor.getValue())
                .contains("Answer in Indonesian")
                .contains("Never invent transaction status");
    }
}
