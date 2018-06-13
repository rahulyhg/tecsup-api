package pe.edu.tecsup.api.controllers.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.tecsup.api.models.New;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.NewsService;
import pe.edu.tecsup.api.utils.Constant;
import pe.edu.tecsup.api.utils.Notifier;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * https://spring.io/guides/gs/uploading-files/
 * http://www.mkyong.com/spring-boot/spring-boot-file-upload-example/
 * http://www.heroesdelcodigo.com/crud-usando-spring-boot-spring-data-jpa-thymeleaf/
 * https://github.com/Zianwar/springboot-crud-demo
 */
@Controller
@RequestMapping("/admin/news")
public class NewsManagerController {

	private static final Logger log = Logger.getLogger(NewsManagerController.class);

    @Autowired
    private ServletContext context;

    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(required = false, defaultValue = "1") int number) throws Exception {
        log.info("calling index");
        try{
            PageRequest request = new PageRequest(number-1, Constant.PAGINATION_LIMIT);
            Page<New> newPage = newsService.listAll(request);
            log.info("news: " + newPage.getContent());

            model.addAttribute("news", newPage.getContent());
            model.addAttribute("page", newPage);

            return "admin/news/index";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) throws Exception {
        log.info("calling view: " + id);
        try{

            New neu = newsService.findOne(id);
            log.info("new: " + neu);

            model.addAttribute("neu", neu);

            return "admin/news/view";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/create")
    public String create(Model model) throws Exception {
        log.info("calling create");
        try{
            model.addAttribute("neu", new New());
            return "admin/news/create";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @Autowired
    @Qualifier("mvcValidator")
    private Validator validator;

    @Autowired
    private Notifier notifier;

    @PostMapping("/store")
    public String store(/*@Valid */@ModelAttribute("neu") New neu, /*BindingResult result, */Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user) throws Exception {
        log.info("calling store: " + neu);
        try{

            if (!file.isEmpty()) {
                log.info("Save with filename " + file.getOriginalFilename());

                String filename = propareRepositoryFilename(file.getOriginalFilename());

                // set filename to save
                neu.setPicture(filename);
            }

            neu.setPublished(new Date());
            neu.setActivated(1);
            neu.setDeleted(0);
            neu.setCreatedby(user.getUsername());
            neu.setCreatedat(new Date());
            neu.setUpdatedby(user.getUsername());
            neu.setUpdatedat(new Date());

            ValidationUtils.invokeValidator(validator, neu, errors);
            if (errors.hasErrors()) {
                return "admin/news/create";
            }

            log.info("New Before: " + neu);
            newsService.save(neu);
            log.info("New After: " + neu);

            if (!file.isEmpty()) {
                uploadFile(file, neu.getPicture());
            }

            // Notification
            notifier.notifyNews(neu.getSede(), neu.getTitle());

            redirectAttrs.addFlashAttribute("message", "Registro guardado correctamente");
            return "redirect:/admin/news/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) throws Exception {
        log.info("calling edit: " + id);
        try{

            New neu = newsService.findOne(id);
            log.info("new: " + neu);

            model.addAttribute("neu", neu);

            return "admin/news/edit";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("neu") New neu, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user) throws Exception {
        log.info("calling update: " + neu);
        try{

            New neuOriginal = newsService.findOne(neu.getId());

            neuOriginal.setTitle(neu.getTitle());
            neuOriginal.setSummary(neu.getSummary());
            neuOriginal.setContent(neu.getContent());
            neuOriginal.setSede(neu.getSede());

            if (!file.isEmpty()) {
                log.info("Save with filename " + file.getOriginalFilename());

                String filename = propareRepositoryFilename(file.getOriginalFilename());

                // set filename to save
                neuOriginal.setPicture(filename);
            }

            neuOriginal.setUpdatedby(user.getUsername());
            neuOriginal.setUpdatedat(new Date());

            ValidationUtils.invokeValidator(validator, neuOriginal, errors);
            if (errors.hasErrors()) {
                return "admin/news/edit";
            }

            log.info("New Before: " + neuOriginal);
            newsService.save(neuOriginal);
            log.info("New After: " + neuOriginal);

            if (!file.isEmpty()) {
                uploadFile(file, neuOriginal.getPicture());
            }

            redirectAttrs.addFlashAttribute("message", "Registro guardado correctamente");
            return "redirect:/admin/news/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user) throws Exception {
        log.info("calling delete: " + id);
        try{

            New neu = newsService.findOne(id);
            neu.setDeleted(1);
            neu.setUpdatedby(user.getUsername());
            neu.setUpdatedat(new Date());
            newsService.save(neu);

            redirectAttrs.addFlashAttribute("message", "Registro eliminado correctamente");
            return "redirect:/admin/news/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/activate/{id}/{status}")
    public @ResponseBody ResponseEntity<?> activate(@PathVariable Long id, @PathVariable Integer status, @AuthenticationPrincipal User user) throws Exception {
        log.info("calling activate: " + id + " - status: " + status);
        try{

            New neu = newsService.findOne(id);
            neu.setActivated(status);
            neu.setUpdatedby(user.getUsername());
            neu.setUpdatedat(new Date());
            newsService.save(neu);

            return ResponseEntity.ok("Estado actualizado");
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("upload") MultipartFile file, @RequestHeader(value="Origin") String origin,
                               @RequestParam(required = false) String responseType, @RequestParam(required = false) Integer CKEditorFuncNum) throws Exception {
        log.info("calling upload");
        try{

            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String filename = propareRepositoryFilename(file.getOriginalFilename());

            uploadFile(file, filename);

            if(responseType != null){
                Map<String, Object> response = new HashMap<>();
                response.put("fileName", filename);
                response.put("uploaded", 1);
                response.put("url", origin + context.getContextPath() + "/api/news/files/" + filename);
                return ResponseEntity.ok(response);
            }
            if(CKEditorFuncNum != null) {
                Integer funcNum = CKEditorFuncNum;
                String url =  origin + context.getContextPath() + "/api/news/files/" + filename;
                String message = "Carga completada";
                return ResponseEntity.ok("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("+funcNum+", '"+url+"', '"+message+"');</script>");
            }

            return ResponseEntity.ok("OK");
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/files/{filename:.+}")
    public @ResponseBody ResponseEntity<Resource> files(@PathVariable String filename) throws Exception{
        log.info("calling files: " + filename);
        try{

            Resource file = new UrlResource(Paths.get(Constant.PATH_NEWS).resolve(filename).toUri());
            log.info("Resource: " + file);

            if(!(file.exists() || file.isReadable())) {
                throw new FileNotFoundException("Could not read file: " + filename);
            }

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                    .body(file);

        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    private String propareRepositoryFilename(String originalFilename) throws Exception{
        File directory = new File(Constant.PATH_NEWS);
        if (!directory.exists()){
            if(!directory.mkdirs())
                throw new Exception("Failed to mkdirs " + directory);
        }

        originalFilename = originalFilename.toLowerCase()
                .replaceAll(" ", "-")
                .replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i")
                .replaceAll("ó", "o").replaceAll("ú", "u").replaceAll("á", "a")
                .replaceAll("[^A-Za-z0-9.\\s-]", "");    // Elimina caracteres no alfanuméricos excepto espacios

        int indexOf = originalFilename.lastIndexOf(".");
        if(indexOf == -1)
            indexOf = originalFilename.length();
        String cleanFilename = new StringBuffer(originalFilename).insert(indexOf, "-"+System.currentTimeMillis()).toString();
        log.info("Save with clean filename " + cleanFilename);

        return cleanFilename;
    }

    // Resize: https://memorynotfound.com/java-resize-image-fixed-width-height-example/
    private static void uploadFile(MultipartFile file, String filename) throws Exception{

        final float maxSize = 614400;
        final int maxWidth = 1080;

        BufferedImage image = ImageIO.read(file.getInputStream());

        if(file.getSize() > maxSize || image.getWidth() > maxWidth) {

            final int maxHeight = (image.getHeight()* maxWidth)/image.getWidth();
            int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
            BufferedImage resized = new BufferedImage(maxWidth, maxHeight, type);
            Image tmp = image.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            //return resized;

            // if filename ends with png then generate as png, otherside jpg
            String extension = "jpg";
            if(filename.toLowerCase().endsWith(".png")){
                extension = "png";
            }

            ImageIO.write(resized, extension, new File(Constant.PATH_NEWS, filename));
        }else {
            Files.copy(file.getInputStream(), Paths.get(Constant.PATH_NEWS).resolve(filename));
        }
    }

}
