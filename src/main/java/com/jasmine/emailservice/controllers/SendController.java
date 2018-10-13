package com.jasmine.emailservice.controllers;

import com.jasmine.emailservice.models.Email;
import com.jasmine.emailservice.services.SendService;
import com.sendgrid.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/v1/send")
public class SendController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SendService service;

    public SendController(SendService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(method = POST, produces = "application/json")
    public Response send(@RequestBody Email email) throws IOException {

        logger.info("Sending email: " + email.toString());

        Response response = service.sendRequest(createRequest(email));

        logger.info("Status Code: " + response.getStatusCode());
        logger.info("Body: " + response.getBody());
        logger.info("Headers: " + response.getHeaders());

        return response;
    }

    private Request createRequest(Email email) throws IOException {
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");

        Mail mail = email.toMail();

        try {
            request.setBody(mail.build());
        } catch(IOException e) {
            logger.error("Illegal values in email: ", e.getMessage());
            throw e;
        }
        return request;
    }

}
