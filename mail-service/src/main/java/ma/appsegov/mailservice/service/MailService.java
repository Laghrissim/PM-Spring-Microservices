package ma.appsegov.mailservice.service;

import lombok.RequiredArgsConstructor;
import ma.appsegov.mailservice.dto.UserDTO;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    private final Environment environment;

    private final String SITE_URL = "http://localhost:4200";

    public UserDTO sendPasswordResetEmail(UserDTO user) throws MessagingException {
        String toAddress = user.getEmail();
        String subject = "Veuillez vérifier votre accès ";
        String content = environment.getProperty("check.mail");

        if (content != null) {
            LocalTime currentTime = LocalTime.now();
            String greeting = currentTime.isBefore(LocalTime.of(18, 0)) ? "Bonjour " : "Bonsoir ";
            String resetUrl = SITE_URL + "/password/reset?code=" + RandomString.make(64);
            content = content.replace("{{now}}", greeting).replace("{{username}}", user.getUsername()).replace("{{url}}", resetUrl);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("azakariaachour@gmail.com", "jhbrkgadadiortzf");
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("azakariaachour@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(content, "utf-8", "html");

            Transport.send(message);
        } else {
            System.err.println("Warning: Email content is null.");
        }

        return user;
    }
}