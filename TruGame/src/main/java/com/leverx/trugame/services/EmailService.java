package com.leverx.trugame.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async("emailExecutor")
    public void sendHtml(String to, String subject, String html) {
        MimeMessage mime = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            this.mailSender.send(mime);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Async("emailExecutor")
    public void sendConfirmationEmailTemplate(String to, String subject, String link, String name, String message) {
        Context ctx = new Context();
        ctx.setVariable("link", link);
        ctx.setVariable("greeting", "Hello, " + name + "!");
        ctx.setVariable("message", message);

        String html = this.templateEngine.process("email", ctx);
        this.sendHtml(to, subject, html);
    }
}
