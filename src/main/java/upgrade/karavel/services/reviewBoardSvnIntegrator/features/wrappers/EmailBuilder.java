package upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;

@RequiredArgsConstructor
public class EmailBuilder {

    private final SimpleMailMessage simpleMail;

    public static EmailBuilder builder() {
        return new EmailBuilder(new SimpleMailMessage());
    }

    public EmailBuilder from(String from) {
        simpleMail.setFrom(from);
        return this;
    }

    public EmailBuilder to(String to) {
        simpleMail.setTo(to);
        return this;
    }

    public EmailBuilder subject(String subject) {
        simpleMail.setSubject(subject);
        return this;
    }

    public EmailBuilder text(String text) {
        simpleMail.setText(text);
        return this;
    }

    public SimpleMailMessage build() {
        return simpleMail;
    }

}
