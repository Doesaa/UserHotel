package bt.edu.gcit.usermicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("12220054.gcit@rub.edu.bt");  // Sender's email address
        message.setTo(toEmail);  // Recipient's email address
        message.setSubject(subject);
        message.setText(body);
        
        // Send email
        javaMailSender.send(message);
    }
}

