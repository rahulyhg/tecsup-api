package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.tecsup.api.tasks.ScheduledTask;

@Controller
public class TestController {

	private static final Logger log = Logger.getLogger(TestController.class);

	@Autowired
	private ScheduledTask scheduledTask;

	@Value("${fcm.serverkey}")
	private String FCMSERVERKEY;

	@GetMapping("/test")
	public ResponseEntity<?> test(Model model) throws Exception {
		log.info("calling test");

//        scheduledTask.processingDealyDebts();
//        scheduledTask.processingWeatherUpdating();

        /*
		Sender sender = new Sender(FCMSERVERKEY);

		// Build Notification Payload
		Notification notification = new Notification.Builder()
				.title("¡Noticias Tecsup!")
				.body("Test")
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
*/

/*

        // Build Notification Payload
         notification = new Notification.Builder()
                .title("¡Nueva notificación recibida!")
                .body("Test controller")
                .clickAction(".activities.MainActivity")    // https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase#42279260
                .icon("in_notification")
//                    .color("")
                .sound("default")
                .build();

        payload = new HashMap<>();
        payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_ALERTS);

        // Compose a Message:
        message = new Message.Builder()
                .to("cN4KhGU3-wo:APA91bEVd9YqAdTGId6GiCOn68AjNmgeFKUyjJebBpiNSFgoTjU1yuPTLclR_d3k_VUvweW5uMEeB_mw220FgFY5PY-yKh4bjLqSi-WGQvrxSNsS12fSDJTI1ewtwfCXgJZ_RR6g18vA")
                .data(payload)
                .notification(notification)
                .build();

        // Send a Message:
        new Sender(FCMSERVERKEY).send(message);

*/

        /*for (int i=0; i<20; i++){
            try {
                log.info(task.generateHash(i));
            }catch (Exception e){
                log.error(e);
            }
            try {
                log.info(task.generateHash(i));
            }catch (Exception e){
                log.error(e);
            }
            try {
                log.info(task.generateHash(i));
            }catch (Exception e){
                log.error(e);
            }
                log.info(TOKEN);

        }*/

		return ResponseEntity.ok("OK");
	}

//	@Autowired
//    private Task task;
//
//    private static int TOKEN = 1;
//
//	@Component
//    class Task{
//
//        @Cacheable(cacheNames="generateHash", key="#id", unless="#result == null") // las excepciones no se cachean
//        public String generateHash(Integer id) throws Exception{
//            Thread.sleep(3000L);
//            if(id == 10)
//                return null;
//            if(TOKEN == 15)
//                throw new Exception("HORROR");
//            return "ID: " + id + " - HASH: " + (TOKEN++);
//        }
//
//    }

}
