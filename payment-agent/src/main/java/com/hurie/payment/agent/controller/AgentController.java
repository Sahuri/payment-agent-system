package com.hurie.payment.agent.controller;

import com.hurie.payment.agent.service.PaymentAgentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final PaymentAgentService service;

    public AgentController(PaymentAgentService service) {
        this.service = service;
    }

    @GetMapping
    public String ask(
            @RequestParam String q) {

        return service.ask(q);
    }
}
