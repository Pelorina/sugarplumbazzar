package Ecommerce.sugar.plum.bazzar.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleEmail(NotificationDetails notificationDetails) {

        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(notificationDetails.getRecipient());
            mailMessage.setSubject(notificationDetails.getSubject());
            mailMessage.setText(notificationDetails.getMessageBody());
            javaMailSender.send(mailMessage);
            return "mail successfully sent";

        }catch (MailException e){
            throw new RuntimeException(e);
        }
    }
}
