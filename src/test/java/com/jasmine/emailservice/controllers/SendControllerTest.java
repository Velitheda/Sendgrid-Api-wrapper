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

    private String testAddress = "test@example.com";
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldSendRequestToMailService() throws Exception {
        Email email = new Email(
                testAddress,
                "The subject",
                "The body",
                new String[]{ testAddress },
                new String[]{}
        );
        String json = mapper.writeValueAsString(email);

        when(service.sendRequest(any(Request.class))).thenReturn(new Response());
        this.mockMvc.perform(post("/send", json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldHandleNoCcsAndBccs() throws Exception {
        Email email = new Email(
                testAddress,
                "The subject",
                "The body"
        );

        String json = mapper.writeValueAsString(email);

        when(service.sendRequest(any(Request.class))).thenReturn(new Response());
        this.mockMvc.perform(post("/send", json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }
}
