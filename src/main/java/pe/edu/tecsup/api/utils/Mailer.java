package pe.edu.tecsup.api.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class Mailer {

    private static final Logger log = Logger.getLogger(Mailer.class);

    private static final String SUBJECT = "Tecsup App: ";

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailToAdministrator(String subject, String message){
        log.info("sendMailToAdministrator: " + "s:" + subject + "m:" + message);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
            mailMsg.setFrom(Constant.EMAIL_FROM);
            mailMsg.setTo(Constant.EMAIL_ADMINISTRATOR);
            mailMsg.setSubject(SUBJECT + subject);
            mailMsg.setText(message);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error(e, e);
        }
    }

    @Async // http://therealdanvega.com/blog/2016/01/13/sending-async-emails-in-spring
    public void sendMailByNotification(String[] to, String message){
        log.info("sendMailByNotification: " + "t:" + to + "m:" + message);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
            mailMsg.setFrom(Constant.EMAIL_FROM);
            mailMsg.setTo(to);
            mailMsg.setSubject(SUBJECT + "Nueva notificación enviada");
            mailMsg.setText(message);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error(e, e);
        }
    }

}
