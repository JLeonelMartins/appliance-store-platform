package com.martins.jonathan.appliancestore.notification.email;

public interface EmailGateway {

    void send(EmailMessage message);
}