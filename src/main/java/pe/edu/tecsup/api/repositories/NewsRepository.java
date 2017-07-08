package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.CrmDeuda;
import pe.edu.tecsup.api.models.New;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface NewsRepository extends PagingAndSortingRepository<New, Long> {

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 ORDER BY n.published DESC")
    Page<New> listAll(Pageable pageable);

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 ORDER BY n.published DESC")
    List<New> listAll();

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 and n.activated = 1 ORDER BY n.published DESC")
    List<New> listAllPublished();

}
