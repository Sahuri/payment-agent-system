package com.hurie.payment.agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PaymentAgentService {

    private final ChatClient chatClient;

    public PaymentAgentService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String question) {

        return chatClient.prompt("""
                You are Payment Operations Assistant.

                Rules:
                - Answer in Indonesian
                - Use MCP tools for payment information
                - Never invent transaction status

                User Question:
                %s
                """.formatted(question))
                .call()
                .content();
    }
}
