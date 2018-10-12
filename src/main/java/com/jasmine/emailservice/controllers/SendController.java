package com.jasmine.emailservice.controllers;

import com.jasmine.emailservice.models.Email;
import com.sendgrid.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/send")
public class SendController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(method = POST, produces = "application/json")
    public HttpStatus send(@RequestBody Email email) throws IOException {
        System.out.println("Email: " + email.toString());

        logger.debug(email.toString());
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(email.toMail().build());
        Response response = sg.api(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());

        return HttpStatus.OK;
    }
}
