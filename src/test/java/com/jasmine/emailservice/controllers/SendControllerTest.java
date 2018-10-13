package com.jasmine.emailservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasmine.emailservice.models.Email;
import com.jasmine.emailservice.services.SendService;
import com.sendgrid.Request;
import com.sendgrid.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SendController.class)
public class SendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendService service;

    private String toAddress = "to@example.com";
    private String ccAddress = "cc@example.com";
    private ObjectMapper mapper = new ObjectMapper();

    private Email email = new Email(
            new String[]{ toAddress },
            "The subject",
            "The body"
    );
    private Email ccEmail = new Email(
            new String[]{ toAddress },
            "The subject",
            "The body",
            new String[]{ ccAddress },
            new String[]{}
    );

    @Test
    public void shouldSendRequestToMailService() throws Exception {
        String json = mapper.writeValueAsString(ccEmail);

        when(service.sendRequest(any(Request.class))).thenReturn(new Response());
        this.mockMvc.perform(post("/v1/send", json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldHandleNullCcsAndBccs() throws Exception {
        String json = mapper.writeValueAsString(email);

        when(service.sendRequest(any(Request.class))).thenReturn(new Response());
        this.mockMvc.perform(post("/v1/send", json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldHandleCcsAndNullBccs() throws Exception {
        email.setCc(new String[]{ ccAddress });

        String json = mapper.writeValueAsString(email);

        when(service.sendRequest(any(Request.class))).thenReturn(new Response());
        this.mockMvc.perform(post("/v1/send", json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }
}
