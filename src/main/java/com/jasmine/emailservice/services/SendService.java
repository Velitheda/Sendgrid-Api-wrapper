package com.jasmine.emailservice.services;


import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendService {
    public Response sendRequest(Request request) throws IOException {
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        return sg.api(request);
    }
}
