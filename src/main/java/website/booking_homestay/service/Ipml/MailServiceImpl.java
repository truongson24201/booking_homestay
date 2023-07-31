package website.booking_homestay.service.Ipml;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import website.booking_homestay.DTO.AccountInforSendMail;
import website.booking_homestay.service.IMailService;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {
    private final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final SpringTemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;


    @Override
    public void sendMailRegister(AccountInforSendMail account) {
        log.debug("Request send mail to: " + account.getEmail());
        Locale locale = Locale.forLanguageTag("vn");
        Context context = new Context(locale);
        context.setVariable("account", account);
        String content = templateEngine.process("email/register-account", context);
        this.sendEmail("[BOOKING] Register account successfully!", content, account.getEmail());
    }

    @Async
    void sendEmail(String subject, String content, String email) {
        log.debug("Send email to '{}' with subject '{}' and content={}", email, subject, content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content, true);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to: " + email);
        } catch (MailException | MessagingException e) {
            log.debug("Fail to send mail to: " + email);
        }
    }
}
