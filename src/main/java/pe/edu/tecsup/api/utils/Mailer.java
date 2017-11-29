package pe.edu.tecsup.api.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.api.models.Incident;
import pe.edu.tecsup.api.models.User;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;

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

    @Async
    public void sendMailByIncident(String[] to, User customer, Incident incident){
        log.info("sendMailByIncident: " + "t:" + to + "c:" + customer + "i:" + incident);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
            mailMsg.setFrom(Constant.EMAIL_FROM);
            mailMsg.setTo(to);
            //mailMsg.setCc(customer.getEmail());
            mailMsg.setSubject(SUBJECT + "Nueva solicitud de atención");

            String text = "<p><h3>Solicitud de atención a incidente generada</h3></p>" +
                    "<p><b>Número:</b> " + String.format("%05d", incident.getId()) + "<br/>" +
                    "<b>Docente:</b> " + customer.getFullname() + "<br/>" +
                    "<b>Ambiente:</b> " + incident.getLocation() + "<br/>" +
                    "<b>Fecha:</b> " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(incident.getCreated())) + "<br/></p>" +
                    "<p>Soporte de TI</p>";

            mailMsg.setText(text, true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            log.error(e, e);
        }
    }

}
