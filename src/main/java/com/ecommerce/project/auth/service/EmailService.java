package com.ecommerce.project.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🎉 Welcome to SB E-Commerce");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f6f8;
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: auto;
                            background: #ffffff;
                            padding: 30px;
                            border-radius: 8px;
                            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                        }
                        .logo {
                            text-align: center;
                            font-size: 28px;
                            font-weight: bold;
                            color: #2563eb;
                        }
                        .content {
                            margin-top: 20px;
                            color: #333333;
                            line-height: 1.6;
                        }
                        .button {
                            display: inline-block;
                            margin-top: 25px;
                            padding: 12px 24px;
                            background-color: #2563eb;
                            color: #ffffff;
                            text-decoration: none;
                            border-radius: 6px;
                            font-weight: bold;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888888;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="logo">
                            SB E-Commerce
                        </div>

                        <div class="content">
                            <p>Dear <strong>%s</strong>,</p>

                            <p>
                                Thank you for registering with <strong>SB E-Commerce</strong>.
                                Your account has been successfully created.
                            </p>

                            <p>
                                You can now log in and start exploring our products.
                            </p>


                            <p style="margin-top: 20px;">
                                If you have any questions, feel free to contact our support team.
                            </p>
                        </div>

                        <div class="footer">
                            © 2026 SB E-Commerce. All rights reserved.
                        </div>
                    </div>
                </body>
                </html>
            """.formatted(name);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
