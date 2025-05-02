package es.samfc.learning.backend.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String fromMail;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(fromMail);
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }
}
