package com.jasmine.emailservice.controllers;

import com.sendgrid.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/send")
public class SendController {

    @ResponseBody
    @RequestMapping(method = POST, produces = "application/json")
    public String send(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam(value = "body") String body,
            @RequestParam(value = "CCs", required = false) String[] ccs,
            @RequestParam(value = "BCCs", required = false) String[] bccs
    ) {
        Email fromEmail = new Email("test@example.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Personalization ccsPersonalisation = addCcs(ccs);
        mail.addPersonalization(ccsPersonalisation);

        Personalization bccsPersonalisation = addBccs(bccs);
        mail.addPersonalization(bccsPersonalisation);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
            //throw ex;
        }
        return "{}";
    }

    private Personalization addCcs(String[] ccs) {
        Personalization p = new Personalization();
        Arrays.stream(ccs).forEach(cc -> p.addCc(new Email(cc)));
        return p;
    }

    private Personalization addBccs(String[] bccs) {
        Personalization p = new Personalization();
        Arrays.stream(bccs).forEach(bcc -> p.addBcc(new Email(bcc)));
        return p;
    }

}
