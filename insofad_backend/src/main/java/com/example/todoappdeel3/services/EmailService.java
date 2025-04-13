package com.example.todoappdeel3.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public void sendGiftCardEmail(String to, String giftCardCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your Gift Card Code");
            message.setText(
                    "Thanks for shopping at Gaming Webshop!\n\n" +
                            "Order created successfully.\n\n" +
                            "Your Giftcard code: " + giftCardCode + "\n\n" +
                            "Thank you for your purchase!"
            );

            emailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Error sending email to {}", to, e);
        }
    }
}
