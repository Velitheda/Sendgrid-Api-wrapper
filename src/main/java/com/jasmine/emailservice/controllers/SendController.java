package com.jasmine.emailservice.controllers;

import com.jasmine.emailservice.models.Email;
import com.jasmine.emailservice.services.SendService;
import com.sendgrid.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/send")
public class SendController {

    private final SendService service;

    public SendController(SendService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(method = POST, produces = "application/json")
    public HttpStatus send(@RequestBody Email email) throws IOException {
        System.out.println("Email: " + email.toString());

        Response response = service.sendRequest(createRequest(email));

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());

        return HttpStatus.OK;
    }

    private Request createRequest(Email email) throws IOException {
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(email.toMail().build());

        return request;
    }

}
