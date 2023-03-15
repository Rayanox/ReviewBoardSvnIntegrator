package upgrade.karavel.services.reviewBoardSvnIntegrator.services;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:features.properties")
@RequiredArgsConstructor
public class EmailService {

    @Value("${reviewboard.email.from}")
    private String from;

    @Value("${reviewboard.email.disable-mail-sending}")
    private boolean disableMailSending;

    private final MailSender mailSender;

    public void sendMail(SimpleMailMessage simpleMailMessage) {
        if(disableMailSending)
            return;

        simpleMailMessage.setFrom(from);
        mailSender.send(simpleMailMessage);
    }

}
