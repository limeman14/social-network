package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;
    private SimpleMailMessage template;
    private String projectName;

    @Autowired
    public EmailServiceImpl(
            JavaMailSender emailSender,
            SimpleMailMessage template,
            @Value("${project.name}") String projectName
    ) {
        this.emailSender = emailSender;
        this.template = template;
        this.projectName = projectName;
    }

    @Override
    public void sendSimpleMessage(String to, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(projectName);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to, String... templateModel) {
        String text = String.format(template.getText(), templateModel);
        sendSimpleMessage(to, text);
    }

    @Override
    public void sendPasswordRecovery(String to, String name, String link) {
        String text = String.format(template.getText(), name, link);
        sendSimpleMessage(to, text);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }
}
