package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.New;
import pe.edu.tecsup.api.repositories.NewsRepository;

import java.util.List;

@Service
public class NewsService {

	private static Logger log = Logger.getLogger(NewsService.class);
	
	@Autowired
	private NewsRepository newsRepository;

	public Page<New> listAll(Pageable pageable) throws Exception {
        log.info("calling listAll: ");
        return newsRepository.listAll(pageable);
    }

	public List<New> listAll() throws Exception {
        log.info("calling listAll: ");
        return newsRepository.listAll();
    }

    public List<New> listAllPublished() throws Exception {
        log.info("calling listAllPublished: ");
        return newsRepository.listAllPublished();
    }

    public New findOne(Long id) throws Exception {
        log.info("calling findOne: ");
        return newsRepository.findOne(id);
    }

    public New save(New neu) throws Exception {
        log.info("calling save: ");
        return newsRepository.save(neu);
    }

    public void delete(Long id) throws Exception {
        log.info("calling delete: ");
        newsRepository.delete(id);
    }

}
