package pe.edu.tecsup.api.utils;

import com.liferay.mobile.fcm.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.api.models.Incident;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ebenites on 16/07/2017.
 */
@Component
public class Notifier {

    private static final Logger log = Logger.getLogger(Notifier.class);

    @Value("${fcm.serverkey}")
    private String FCMSERVERKEY;

    @Async
    public void notifyNews(String title) {
        log.info("notifyNews: " + title);
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title("¡Noticias Tecsup!")
                    .body(title)
                    .clickAction(".activities.MainActivity")    // https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase#42279260
                    .icon("in_news")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_NEWS);

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(new Topic(Constant.FIREBASE_TOPIC_NEWS))
                    .data(payload)
                    .notification(notification)
                    .build();

            // Send a Message:
            Status status = sender.send(message);

            // Show status:
            log.info("Response: " + status);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void notifyPayment(List<String> registrationIds) {
        log.info("notifyPayment: " + registrationIds);
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title("¡Hoy vence tu deuda!")
                    .body("Evita las moras, paga a tiempo")
                    .clickAction(".activities.MainActivity")    // https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase#42279260
                    .icon("in_payment")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_DEBT);

            // Up to 1000 tokens are allowed for a multicast message
            int size = registrationIds.size();
            int pages = size/1000;
            if(size % 1000 > 0) pages++;

            int curpage = 0, first, last;

            do{
                first = curpage * 1000;
                last = ((curpage + 1 ) * 1000);
                last = (last > size) ? size : last;
                curpage++;

                log.info("f:"+first+" - l:" +last);
                List<String> subRegistrationIds = registrationIds.subList(first, last);

                // Compose a Message:
                Message message = new Message.Builder()
                        .to(subRegistrationIds)
                        .data(payload)
                        .notification(notification)
                        .build();

                // Send a Message:
                Status status = sender.send(message);

                // Show status:
                log.info("Response: " + status);

            }while (curpage < pages);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void notifyIncident(List<String> registrationIds, String customer, String location) {
        log.info("notifyIncident: " + registrationIds);
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title("Nueva solicitud de atención")
                    .body(customer + " en el " + location)
                    .clickAction(".activities.MainActivity")
                    .icon("in_support")
//                    .color("")
                    .sound("default")
                    .build();

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(registrationIds)
                    .notification(notification)
                    .build();

            // Send a Message:
            Status status = sender.send(message);

            // Show status:
            log.info("Response: " + status);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void notifyIncidentAttention(List<String> registrationIds, Incident incident) {
        log.info("notifyIncidentAttention: " + registrationIds);
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            String title = "";
            String body = "";
            if(Constant.INCIDENT_STATUS_ATENTION.equals(incident.getStatus())) {
                title = "Solicitud de atención aceptada";
                body = "Será contactado a la brevedad";
            }else if(Constant.INCIDENT_STATUS_CLOSED.equals(incident.getStatus())) {
                title = "Solicitud de atención cerrada";
                body = "Gracias por contar con nuestro servicio";
            }

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title(title)
                    .body(body)
//                    .clickAction(".activities.MainActivity")
                    .icon("in_support")
//                    .color("")
                    .sound("default")
                    .build();

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(registrationIds)
                    .notification(notification)
                    .build();

            // Send a Message:
            Status status = sender.send(message);

            // Show status:
            log.info("Response: " + status);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Async
    public void notifyAlert(List<String> registrationIds, String from, String content) {
        log.info("notifyAlert: f:" + from + " - c:" + content + " - i:" + registrationIds);
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title("¡Nueva notificación recibida!")
                    .body(from + " dice: '" + content)
                    .clickAction(".activities.MainActivity")    // https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase#42279260
                    .icon("in_notification")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_ALERTS);

            // Up to 1000 tokens are allowed for a multicast message
            int size = registrationIds.size();
            int pages = size/1000;
            if(size % 1000 > 0) pages++;

            int curpage = 0, first, last;

            do{
                first = curpage * 1000;
                last = ((curpage + 1 ) * 1000);
                last = (last > size) ? size : last;
                curpage++;

                log.info("f:"+first+" - l:" +last);
                List<String> subRegistrationIds = registrationIds.subList(first, last);

                // Compose a Message:
                Message message = new Message.Builder()
                        .to(subRegistrationIds)
                        .data(payload)
                        .notification(notification)
                        .build();

                // Send a Message:
                Status status = sender.send(message);

                // Show status:
                log.info("Response: " + status);

            }while (curpage < pages);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

}