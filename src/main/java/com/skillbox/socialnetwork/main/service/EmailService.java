package com.skillbox.socialnetwork.main.service;

public interface EmailService {

    void sendSimpleMessage(
            String to,
            String subject,
            String text
    );

    void sendPasswordRecovery(
            String to,
            String subject,
            String name,
            String link
    );

    void sendSimpleMessageUsingTemplate(
            String to,
            String subject,
            String ...templateModel
    );
    void sendMessageWithAttachment(
            String to,
            String subject,
            String text,
            String pathToAttachment
    );
}
