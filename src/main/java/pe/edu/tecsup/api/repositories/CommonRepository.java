package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CommonRepository {

    private static Logger log = Logger.getLogger(CommonRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public List<Seat> listSeats() throws Exception {
        log.info("listSeats()");
        try {

            String sql = "select distinct s.codsede, s.descripcion\n" +
                        "from GENERAL.GEN_V_SEDE s \n" +
                        "inner join evaluacion.eva_v_periodo p on p.sede=s.codsede and p.situacionregistro='A'";

            List<Seat> seats = jdbcTemplate.query(sql, new RowMapper<Seat>() {
                public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Seat seat = new Seat();
                    seat.setId(rs.getString("codsede"));
                    seat.setName(rs.getString("descripcion"));
                    return seat;
                }
            });

            log.info("seats: " + seats);

            return seats;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Major> listMajors(String sede) throws Exception {
        log.info("listMajors("+sede+")");
        try {

            String sql = "select distinct codespecialidad, comercial.nomprod(codespecialidad) || ' ' || 'C'||docencia.nomespecialidad(codespecialidad) as nomespecialidad \n" +
                    "from doc_estado_alumno a\n" +
                    "inner join evaluacion.eva_v_periodo p on p.codperiodo=a.codperiodo and p.situacionregistro='A'\n" +
                    "where ? is null or sede=? \n" +
                    "order by 2";

            List<Major> majors = jdbcTemplate.query(sql, new RowMapper<Major>() {
                public Major mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Major major = new Major();
                    major.setId(rs.getInt("codespecialidad"));
                    major.setName(rs.getString("nomespecialidad"));
                    return major;
                }
            }, sede, sede);

            log.info("majors: " + majors);

            return majors;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Cicle> listCicles(String sede, Integer codespecialidad) throws Exception {
        log.info("listCicles("+sede+", "+codespecialidad+")");
        try {

            String sql = "select distinct c.codciclo, decode(c.codciclo, 1, '1ro', 2, '2do', 3, '3ro', 4, '4to', 5, '5to', 6, '6to', 7, '7mo', 8, '8vo', 9, '9no', 10, '10mo', c.codciclo||'') nomciclo\n" +
                    "from evaluacion.eva_v_curso_alumno ca\n" +
                    "inner join evaluacion.eva_v_cursos c on c.codcursoejec=ca.codcursoejec and c.situacionregistro='A'\n" +
                    "inner join evaluacion.eva_curso_periodo cp on cp.codcursoejec=c.codcursoejec\n" +
                    "inner join evaluacion.eva_v_periodo p on p.codperiodo=cp.codperiodo and p.situacionregistro='A'\n" +
                    "where 1=1\n" +
                    "and (? is null or c.sede=?)\n" +
                    "and (? is null or ca.codespecialidad=?)\n" +
                    "order by 1";

            List<Cicle> cicles = jdbcTemplate.query(sql, new RowMapper<Cicle>() {
                public Cicle mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Cicle ciclo = new Cicle();
                    ciclo.setId(rs.getInt("codciclo"));
                    ciclo.setName(rs.getString("nomciclo"));
                    return ciclo;
                }
            }, sede, sede, codespecialidad, codespecialidad);

            log.info("cicles: " + cicles);

            return cicles;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Section> listSections(String sede, Integer codespecialidad, Integer codcicle) throws Exception {
        log.info("listSections("+sede+", "+codespecialidad+", "+codcicle+")");
        try {

            String sql = "select distinct s.codigo as codseccion, decode(s.codigo, 1, 'A', 2, 'B', 3, 'C', 4, 'D', 5, 'E', 6, 'F', 7, 'G', 8, 'H', 9, 'I', 10, 'J', 'X') as nomseccion\n" +
                    "from evaluacion.eva_v_curso_alumno ca\n" +
                    "inner join evaluacion.eva_v_cursos c on c.codcursoejec=ca.codcursoejec and c.situacionregistro='A'\n" +
                    "inner join evaluacion.eva_curso_periodo cp on cp.codcursoejec=c.codcursoejec\n" +
                    "inner join evaluacion.eva_v_periodo p on p.codperiodo=cp.codperiodo and p.situacionregistro='A'\n" +
                    "inner join evaluacion.eva_v_seccion s on s.codseccion=ca.seccion\n" +
                    "where 1=1\n" +
                    "and (? is null or c.sede=?)\n" +
                    "and (? is null or ca.codespecialidad=?)\n" +
                    "and (? is null or ca.codciclo=?)\n" +
                    "order by 1";

            List<Section> sections = jdbcTemplate.query(sql, new RowMapper<Section>() {
                public Section mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Section section = new Section();
                    section.setId(rs.getInt("codseccion"));
                    section.setName(rs.getString("nomseccion"));
                    return section;
                }
            }, sede, sede, codespecialidad, codespecialidad, codcicle, codcicle);

            log.info("sections: " + sections);

            return sections;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
