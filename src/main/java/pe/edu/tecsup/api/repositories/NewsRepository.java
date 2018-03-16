package pe.edu.tecsup.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.New;

import java.util.List;

@Repository
public interface NewsRepository extends PagingAndSortingRepository<New, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 ORDER BY n.published DESC")
    Page<New> listAll(Pageable pageable);

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 ORDER BY n.published DESC")
    List<New> listAll();

    @Query("SELECT n FROM New as n WHERE n.deleted = 0 and n.activated = 1 and (n.sede is null or n.sede = ?1) ORDER BY n.published DESC")
    List<New> listAllPublishedBySede(String sede);

}
