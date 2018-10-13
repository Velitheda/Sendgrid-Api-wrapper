package com.jasmine.emailservice.models;

import com.sendgrid.Mail;
import com.sendgrid.Personalization;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmailTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorWhenNoToEmails() {
        Email email = new Email(
                new String[]{},
                "The subject",
                "The body"
        );
        email.toMail();
    }

    @Test
    public void shouldLoadFromEmail() {
        Email email = new Email(
                new String[]{ "to@example.com" },
                "The subject",
                "The body"
        );
        String fromEmail = email.toMail().getFrom().getEmail();
        assertEquals(fromEmail, "from@example.com");
    }

    @Test
    public void shouldSetBody() {
        Email email = new Email(
                new String[]{ "to@example.com" },
                "The subject",
                "The body"
        );
        Mail mail = email.toMail();
        assertEquals(mail.getContent().get(0).getValue(), email.getBody());
        assertEquals(mail.getContent().size(), 1);
    }

    @Test
    public void shouldSetSingleToRecipient() {
        Email email = new Email(
                new String[]{ "to@example.com" },
                "The subject",
                "The body"
        );
        Mail mail = email.toMail();

        List<Personalization> personalizations = mail.getPersonalization();
        assertEquals(personalizations.get(0).getTos().get(0).getEmail(), "to@example.com");
    }

    @Test
    public void shouldSetMultipleToRecipients() {
        Email email = new Email(
                new String[]{ "first-to@example.com", "second-to@example.com" },
                "The subject",
                "The body"
        );
        Mail mail = email.toMail();

        List<Personalization> personalizations = mail.getPersonalization();
        assertEquals(personalizations.get(0).getTos().get(0).getEmail(), "first-to@example.com");
        assertEquals(personalizations.get(1).getTos().get(1).getEmail(), "second-to@example.com");
    }

    @Test
    public void shouldSetCcAndBcc() throws Exception {
        Email email = new Email(
                new String[]{ "to@example.com" },
                "The subject",
                "The body",
                new String[]{ "cc@example.com" },
                new String[]{ "bcc@example.com"}
        );
        Mail mail = email.toMail();

        List<Personalization> personalizations = mail.getPersonalization();
        assertEquals(personalizations.get(1).getCcs().get(0).getEmail(), "cc@example.com");
        assertEquals(personalizations.get(1).getBccs().get(0).getEmail(), "bcc@example.com");
    }
}
