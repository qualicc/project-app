package pl.kamil.bak.project.auctionsite.domain.user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.kamil.bak.project.auctionsite.domain.token.service.TokenService;
import pl.kamil.bak.project.auctionsite.model.tokenEntity.Token;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final TokenService tokenService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public EmailService(TokenService tokenService, TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.tokenService = tokenService;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendHtmlMail(User user) throws MessagingException{
        Token token = tokenService.findByUser(user);
        if (token != null){
            String getToken = token.getToken();
            Context context = new Context();
            context.setVariable("title", "Verify your address");
            context.setVariable("link","http://localhost:8080/activation?token="+getToken);

            String body = templateEngine.process("verification", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("email address verification");
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);

        }
    }
}
