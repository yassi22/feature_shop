package com.example.todoappdeel3.controller;

import com.example.todoappdeel3.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendGiftCard")
    public String sendGiftCardEmail(@RequestParam String to, @RequestParam String giftCardCode) {
        emailService.sendGiftCardEmail(to, giftCardCode);
        return "Email sent successfully!";
    }
}
