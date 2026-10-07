package com.hurie.payment.agent.controller;

import com.hurie.payment.agent.service.PaymentAgentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgentController.class)
class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentAgentService service;

    @Test
    void getAgentReturnsServiceAnswer() throws Exception {

        when(service.ask("halo")).thenReturn("ok");

        mockMvc.perform(get("/agent").param("q", "halo"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void getAgentWithoutQuestionReturnsBadRequest() throws Exception {

        mockMvc.perform(get("/agent"))
                .andExpect(status().isBadRequest());
    }
}
