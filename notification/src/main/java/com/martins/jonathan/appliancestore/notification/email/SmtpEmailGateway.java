package com.martins.jonathan.appliancestore.notification.email;

import com.martins.jonathan.appliancestore.notification.exception.EmailDeliveryException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailGateway implements EmailGateway {

    private final JavaMailSender mailSender;
    private final NotificationEmailProperties properties;

    public SmtpEmailGateway(JavaMailSender mailSender, NotificationEmailProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    @Override
    public void send(EmailMessage emailMessage) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(properties.from());
        message.setTo(emailMessage.recipient());
        message.setSubject(emailMessage.subject());
        message.setText(emailMessage.body());

        try {
            mailSender.send(message);

        } catch (MailException exception) {
            throw new EmailDeliveryException(
                    emailMessage.recipient(),
                    exception
            );
        }
    }
}