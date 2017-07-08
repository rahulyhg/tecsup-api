package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.edu.tecsup.api.models.New;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.NewsService;
import pe.edu.tecsup.api.utils.Constant;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/api/news")
public class NewsController {

	private static final Logger log = Logger.getLogger(NewsController.class);

	@Autowired
	private NewsService newsService;

    @GetMapping
        public ResponseEntity<?> getNews(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getNews: user:" + user);
        try {

//            List<New> news = new ArrayList<>();
//
//            New neu = new New();
//            neu.setId(1L);
//            neu.setTitle("Alumnos de Tecsup Realizan Visita Técnica a la Unión Nacional de Cementeras - UNACEM");
//            neu.setSummary("Nuestros alumnos de Mantenimiento de Maquinaria de Planta de  IV ciclo realizaron una visita técnica a la Unión Nacional de Cementeras – UNACEM, ex Cementos Lima, en su planta de Atocongo ubicada en Villa María del Triunfo, con el objetivo de conocer los  procesos y la gestión del mantenimiento.");
//            neu.setPicture(true);
//            neu.setPublished("12/05/2017");
//            news.add(neu);
//
//            neu = new New();
//            neu.setId(2L);
//            neu.setTitle("#UnaSolaFuerza por la Educación: Tecsup se sumó a la campaña con la entrega de kits escolares");
//            neu.setSummary("Tecsup se sumó a la campaña nacional “#UnaSolaFuerza por la Educación” con la entrega 396 kits escolares que se logró recaudar gracias a la solidaridad de los colaboradores y alumnos ");
//            neu.setPicture(true);
//            neu.setPublished("07/05/2017");
//            news.add(neu);

            List<New> news = newsService.listAllPublished();

            log.info("news: " + news);

            return ResponseEntity.ok(news);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNew(@AuthenticationPrincipal User user,  @PathVariable Long id) throws Exception{
        log.info("call getNew: user:" + user + " - id:" + id);
        try {

//            New neu = new New();
//
//            if(id == 1){
//
//                neu.setId(1L);
//                neu.setTitle("Alumnos de Tecsup Realizan Visita Técnica a la Unión Nacional de Cementeras - UNACEM");
//                neu.setSummary("Nuestros alumnos de Mantenimiento de Maquinaria de Planta de  IV ciclo realizaron una visita técnica a la Unión Nacional de Cementeras – UNACEM, ex Cementos Lima, en su planta de Atocongo ubicada en Villa María del Triunfo, con el objetivo de conocer los  procesos y la gestión del mantenimiento.");
////                neu.setPicture(true);
////                neu.setPublished("12/05/2017");
//                neu.setContent("<div><div style=\"text-align:justify\"><span style=\"font-size:14px\"><span style=\"font-family:Arial,Helvetica,sans-serif\"><span style=\"color:black\">Nuestros alumnos de Mantenimiento de Maquinaria de Planta de &nbsp;IV ciclo realizaron una visita t&eacute;cnica a la Uni&oacute;n Nacional de Cementeras &ndash; UNACEM, ex Cementos Lima, en su planta de Atocongo ubicada en Villa Mar&iacute;a del Triunfo, con el objetivo de conocer los &nbsp;procesos y la gesti&oacute;n del mantenimiento.</span></span></span></div> <br /> <div style=\"text-align:justify\"><span style=\"color:black; font-family:Arial,Helvetica,sans-serif; font-size:14px\">La visita se realiz&oacute; el &uacute;ltimo martes 3 de mayo, y fue gestionada por Luis Villarreal, docente del departamento de Mec&aacute;nica y Aviaci&oacute;n.</span><br /> &nbsp; <div style=\"text-align:center\"><img alt=\"\" src=\"http://app.tecsup.edu.pe/file/intranet/noticias/1494000543590_6098.png\" width=\"100%\" /></div> &nbsp; <div style=\"text-align:center\"><img alt=\"\" src=\"http://app.tecsup.edu.pe/file/intranet/noticias/1494000596641_8672.png\" width=\"100%\" /></div> &nbsp; <div style=\"text-align:center\"><img alt=\"\" src=\"http://app.tecsup.edu.pe/file/intranet/noticias/1494000610092_5017.png\" width=\"100%\" /></div> </div> </div></div>");
//
//            }else{
//
//                neu.setId(2L);
//                neu.setTitle("#UnaSolaFuerza por la Educación: Tecsup se sumó a la campaña con la entrega de kits escolares");
//                neu.setSummary("Tecsup se sumó a la campaña nacional “#UnaSolaFuerza por la Educación” con la entrega 396 kits escolares que se logró recaudar gracias a la solidaridad de los colaboradores y alumnos ");
////                neu.setPicture(true);
////                neu.setPublished("07/05/2017");
//                neu.setContent("<span style=\"font-size:11.0pt\"><span style=\"font-family:&quot;Arial&quot;,sans-serif\">Tecsup</span></span> <span style=\"font-size:11.0pt\"><span style=\"font-family:&quot;Arial&quot;,sans-serif\">se sum&oacute; a la campa&ntilde;a nacional &ldquo;#UnaSolaFuerza por la Educaci&oacute;n&rdquo; con la entrega 396 kits escolares que se logr&oacute; recaudar gracias a la solidaridad de los colaboradores y alumnos</span></span> <br /> <p style=\"text-align:justify\"><span style=\"font-size:11.0pt\"><span style=\"font-family:&quot;Arial&quot;,sans-serif\">La campa&ntilde;a &ldquo;#UnaSolaFuerza por la Educaci&oacute;n&rdquo; tiene como meta lograr la donaci&oacute;n de 20000 kits de &uacute;tiles escolares que ser&aacute;n entregados a las distintas instituciones educativas a nivel nacional que se han visto afectadas por los &uacute;ltimos desastres ocasionados por &ldquo; El Ni&ntilde;o Costero&rdquo;. </span></span></p> <p style=\"text-align:start\"><span style=\"font-size:11.0pt\"><span style=\"font-family:&quot;Arial&quot;,sans-serif\">La instituci&oacute;n est&aacute; comprometida con la educaci&oacute;n y la sociedad, por ello desde la quincena de marzo se lanz&oacute; una campa&ntilde;a interna y se logr&oacute; recolectar los kits de &uacute;tiles escolares para educaci&oacute;n inicial; despu&eacute;s varios alumnos empaquetaron los kits y los colocaron en cajas para luego ser llevadas al centro de recolecci&oacute;n. </span></span></p> <p style=\"text-align:start\"><span style=\"font-size:11.0pt\"><span style=\"font-family:&quot;Arial&quot;,sans-serif\">La entrega oficial a los representantes de la campa&ntilde;a se realiz&oacute; el d&iacute;a lunes 24 de abril.</span></span><br /> <br /> &nbsp;</p> <div style=\"text-align:center\"><img alt=\"\" src=\"http://app.tecsup.edu.pe/file/intranet/noticias/1493740075264_1808.png\" width=\"100%\" /></div>");
//
//            }

            New neu = newsService.findOne(id);

            log.info("new: " + neu);

            return ResponseEntity.ok(neu);
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

            if(!(file.exists() || file.isReadable()))
                throw new FileNotFoundException("Could not read file: " + filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                    .body(file);

        }catch (Throwable e){
            log.error(e, e);
//            throw e;
            return ResponseEntity.notFound().build();
        }
    }

}
