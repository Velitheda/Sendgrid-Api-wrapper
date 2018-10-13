package com.jasmine.emailservice.models;

import com.sendgrid.Content;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;

import java.util.Arrays;
import java.util.ResourceBundle;

public class Email {

    private String[] to;
    private String subject;
    private String body;
    private String[] cc = new String[]{};
    private String[] bcc = new String[]{};

    public Email(String[] to, String subject, String body, String[] cc, String [] bcc) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.cc = cc;
        this.bcc = bcc;
    }

    public Email(String[] to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.cc = new String[]{};
        this.bcc = new String[]{};
    }

    public Email(){}

    public Mail toMail() {
        Content content = new Content("text/html", body);

        com.sendgrid.Email fromEmail = new com.sendgrid.Email(getFromEmail());

        if(to.length == 0){
            throw new IllegalArgumentException("To array must not be empty");
        }
        com.sendgrid.Email firstTo = new com.sendgrid.Email(to[0]);
        Mail mail = new Mail(fromEmail, subject, firstTo, content);

        Personalization p = createPersonalization();
        mail.addPersonalization(p);

        return mail;
    }

    private String getFromEmail() {
        ResourceBundle rb = ResourceBundle.getBundle("application");
        return rb.getString("from_email");
    }

    private Personalization createPersonalization() {
        Personalization p = new Personalization();

        // SendGrid de-dupes the to array on its servers, so we won't send the email twice to the first 'to'.
        Arrays.stream(to).forEach(to -> p.addTo(new com.sendgrid.Email(to)));
        Arrays.stream(cc).forEach(cc -> p.addCc(new com.sendgrid.Email(cc)));
        Arrays.stream(bcc).forEach(bcc -> p.addBcc(new com.sendgrid.Email(bcc)));
        return p;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String[] getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String[] getCc() {
        return cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    @Override
    public String toString() {
        return "SimpleEmail{" +
                "to='" + Arrays.toString(to) + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                '}';
    }
}
