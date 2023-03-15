package upgrade.karavel.services.reviewBoardSvnIntegrator.features;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers.EmailBuilder;
import upgrade.karavel.services.reviewBoardSvnIntegrator.services.EmailService;

@Service
@PropertySource("classpath:features.properties")
@RequiredArgsConstructor
public class EmailNotifier {

    @Value("${reviewboard.email.invalid-commit.subject}")
    private String invalidCommitSubject;
    @Value("${reviewboard.email.company-suffix}")
    private String companySuffix;

    private final EmailService emailService;

    public void notifyInvalidCommit(SvnCommit commit) {
        emailService.sendMail(
                EmailBuilder.builder()
                        .to(StringUtils.join(commit.getCommiterName(),companySuffix))
                        .subject(invalidCommitSubject)
                        .text(StringUtils.join(
                                "Commit invalide -> le commentaire du commit doit contenir un ID de JIRA (exemple: SR-2564). ",
                                commit.toString()
                        )).build()
        );
    }

}
