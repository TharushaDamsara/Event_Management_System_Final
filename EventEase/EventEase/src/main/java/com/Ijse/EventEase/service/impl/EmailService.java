package com.Ijse.EventEase.service.impl;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendQrCodeEmail(String to, String subject, String text, String qrCodePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            // Attach QR Code
            FileSystemResource file = new FileSystemResource(new File(qrCodePath));
            helper.addAttachment("Ticket-QR.png", file);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with QR Code", e);
        }
    }
}

