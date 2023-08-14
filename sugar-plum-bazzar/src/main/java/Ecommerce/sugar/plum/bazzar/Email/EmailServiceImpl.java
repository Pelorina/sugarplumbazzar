package Ecommerce.sugar.plum.bazzar.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    @Override
    public String sendSimpleEmail(EmailDetails emailDetails) {

            try {

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(sender);
                mailMessage.setTo(emailDetails.getRecipient());
                mailMessage.setSubject(emailDetails.getSubject());
                mailMessage.setText(emailDetails.getMessageBody());
                javaMailSender.send(mailMessage);
                return "mail successfully sent";

            }catch (MailException e){
                throw new RuntimeException(e);
            }


        }
    }

