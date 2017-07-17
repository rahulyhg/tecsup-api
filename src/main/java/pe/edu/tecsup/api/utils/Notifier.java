package pe.edu.tecsup.api.utils;

import com.liferay.mobile.fcm.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
                    .icon("ic_news")
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
                    .icon("ic_payment")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_DEBT);

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(registrationIds)
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
                    .icon("ic_notification")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_ALERTS);

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(registrationIds)
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

}