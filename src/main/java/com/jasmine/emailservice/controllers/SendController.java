package com.jasmine.emailservice.controllers;

import com.jasmine.emailservice.models.Email;
import com.jasmine.emailservice.services.SendService;
import com.sendgrid.*;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
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

    @ExceptionHandler(IllegalArgumentException.class)
    void handleBadRequests(HttpServletResponse response, IllegalArgumentException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(method = POST, consumes = "application/json")
    public void send(@RequestBody Email email) throws IOException {

        logger.info("Sending email: " + email.toString());
        if(email.getTo().length == 0) {
            throw new IllegalArgumentException("Please specify at least one email in the 'to' array.");
        }

        Response response = service.sendRequest(createRequest(email));

        logger.info("Status Code: " + response.getStatusCode());
        logger.info("Body: " + response.getBody());
        logger.info("Headers: " + response.getHeaders());
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
