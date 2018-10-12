package com.jasmine.emailservice.models;

import com.sendgrid.Content;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;

import java.util.Arrays;

public class Email {

    private String to;
    private String subject;
    private String body;
    private String[] ccs;
    private String[] bccs;

    public Email(String to, String subject, String body, String[] ccs, String [] bccs) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.ccs = ccs;
        this.bccs = bccs;
    }

    public Email(){}

    public Mail toMail() {
        Content content = new Content("text/html", body);

        com.sendgrid.Email fromEmail = new com.sendgrid.Email("test@example.com");
        com.sendgrid.Email toEmail = new com.sendgrid.Email(this.to);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Personalization ccsPersonalisation = addCcs(ccs);
        mail.addPersonalization(ccsPersonalisation);

        Personalization bccsPersonalisation = addBccs(bccs);
        mail.addPersonalization(bccsPersonalisation);

        return mail;
    }

    private Personalization addCcs(String[] ccs) {
        Personalization p = new Personalization();
        Arrays.stream(ccs).forEach(cc -> p.addCc(new com.sendgrid.Email(cc)));
        return p;
    }

    private Personalization addBccs(String[] bccs) {
        Personalization p = new Personalization();
        Arrays.stream(bccs).forEach(bcc -> p.addBcc(new com.sendgrid.Email(bcc)));
        return p;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCcs(String[] ccs) {
        this.ccs = ccs;
    }

    public void setBccs(String[] bccs) {
        this.bccs = bccs;
    }

    @Override
    public String toString() {
        return "SimpleEmail{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", ccs=" + Arrays.toString(ccs) +
                ", bccs=" + Arrays.toString(bccs) +
                '}';
    }
}
