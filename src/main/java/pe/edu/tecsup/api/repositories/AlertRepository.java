package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AlertRepository {

    private static Logger log = Logger.getLogger(AlertRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public List<Alert> listByStudent(Integer id) throws Exception {
        log.info("listByStudent("+id+")");
        try {

            String sql = "select a.id, general.nombrecliente(a.senderid) sender, a.content, to_char(a.senddate, 'dd-mm-yyyy hh24:mi') senddate, v.viewed \n" +
                    "from api_alerts a\n" +
                    "inner join api_alerts_viewers v on v.alertid=a.id\n" +
                    "where a.deleted=0 and v.userid=?\n" +
                    "order by a.senddate desc";

            List<Alert> alerts = jdbcTemplate.query(sql, new RowMapper<Alert>() {
                public Alert mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Alert alert = new Alert();
                    alert.setId(rs.getLong("id"));
                    alert.setFrom(rs.getString("sender"));
                    alert.setContent(rs.getString("content"));
                    alert.setDate(rs.getString("senddate"));
                    alert.setViewed(rs.getInt("viewed")==1);
                    return alert;
                }
            }, id);

            log.info("alerts: " + alerts);

            return alerts;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Alert> listBySender(Integer id) throws Exception {
        log.info("listBySender("+id+")");
        try {

            String sql = "select a.id, general.nombrecliente(a.senderid) sender, receiver, a.content, to_char(a.senddate, 'dd-mm-yyyy hh24:mi') senddate\n" +
                    "from api_alerts a\n" +
                    "where a.deleted=0 and a.senderid=?\n" +
                    "order by a.senddate desc";

            List<Alert> alerts = jdbcTemplate.query(sql, new RowMapper<Alert>() {
                public Alert mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Alert alert = new Alert();
                    alert.setId(rs.getLong("id"));
                    alert.setFrom(rs.getString("sender"));
                    alert.setTo(rs.getString("receiver"));
                    alert.setContent(rs.getString("content"));
                    alert.setDate(rs.getString("senddate"));
                    return alert;
                }
            }, id);

            log.info("alerts: " + alerts);

            return alerts;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Student> save(Integer senderid, String content, String sede, Integer formacion, Integer ciclo, Integer seccion) throws Exception {
        log.info("save("+senderid+":"+content+", "+sede+", "+formacion+", "+ciclo+", "+seccion+")");
        try {

            String sql = "select (select descripcion from GENERAL.GEN_V_SEDE where codsede=?) nomsede, \n" +
                    "docencia.nomespecialidad(?) as nomespecialidad, \n" +
                    "decode(?, 1, '1ro', 2, '2do', 3, '3ro', 4, '4to', 5, '5to', 6, '6to', 7, '7mo', 8, '8vo', 9, '9no', 10, '10mo') nomciclo,\n" +
                    "decode(?, 1, 'A', 2, 'B', 3, 'C', 4, 'D', 5, 'E', 6, 'F', 7, 'G', 8, 'H', 9, 'I', 10, 'J') as nomseccion\n" +
                    "from dual";

            String fromlabel = jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    StringBuffer sb = new StringBuffer("");
                    if(rs.getString("nomsede") != null)
                        sb.append(rs.getString("nomsede")).append(" ");
                    if(rs.getString("nomespecialidad") != null)
                        sb.append("C").append(rs.getString("nomespecialidad")).append(" ");
                    if(rs.getString("nomciclo") != null)
                        sb.append(rs.getString("nomciclo")).append(" ");
                    if(rs.getString("nomseccion") != null)
                        sb.append(rs.getString("nomseccion")).append(" ");
                    return sb.toString();
                }
            }, sede, formacion, ciclo, seccion);

            sql = "insert into api_alerts (id, senderid, content, receiver, senddate) values(seq_alerts.nextval, ?, ?, ?, sysdate)";
            
            int inserteds = jdbcTemplate.update(sql, senderid, content, fromlabel);
            log.info("Instances: Rows inserteds api_alerts: " + inserteds);

            sql = "select distinct ca.codalumno, docencia.carnet(ca.codalumno) as carnet, general.nombrecliente(ca.codalumno) as nombres, (select correo from docencia.doc_alumno where codalumno=ca.codalumno) as correo, I.INSTANCEID\n" +
                    "from evaluacion.eva_v_curso_alumno ca\n" +
                    "inner join evaluacion.eva_v_cursos c on c.codcursoejec=ca.codcursoejec and c.situacionregistro='A'\n" +
                    "inner join evaluacion.eva_curso_periodo cp on cp.codcursoejec=c.codcursoejec\n" +
                    "inner join evaluacion.eva_v_periodo p on p.codperiodo=cp.codperiodo and p.situacionregistro='A'\n" +
                    "inner join evaluacion.eva_v_seccion s on s.codseccion=ca.seccion\n" +
                    "LEFT JOIN API_INSTANCES I ON I.LASTUSERID=CA.CODALUMNO AND I.APP='TECSUP' AND I.STATUS='1'\n" +
                    "where 1=1\n" +
                    "and (? is null or c.sede=?)\n" +
                    "and (? is null or ca.codespecialidad=?)\n" +
                    "and (? is null or ca.codciclo=?)\n" +
                    "and (? is null or s.codigo=?)\n";// +
//                    "union\n" +
//                    "select 46694, '123456', 'Test User', 'ebenites@tecsup.edu.pe', 'foOs3EBDKbU:APA91bHQ8gYQCzCGoOVoE6-A5V5pdFosheZ3BH2xb3dlAr2DZAap93tx8lHU-198eYDQup24L88N6fN8W1dkgZw_FcEBCyHqokcJ4H56ixQpiizzoFxZN79ZdQzW9Py_EX1s2VZMoWdk' FROM DUAL\n";

            List<Student> students = jdbcTemplate.query(sql, new RowMapper<Student>() {
                public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Student student = new Student();
                    student.setId(rs.getInt("codalumno"));
                    student.setCarnet(rs.getString("carnet"));
                    student.setNombres(rs.getString("nombres"));
                    student.setCorreo(rs.getString("correo"));
                    student.setInstanceid(rs.getString("INSTANCEID"));
                    return student;
                }
            }, sede, sede, formacion, formacion, ciclo, ciclo, seccion, seccion);

            log.info("students: " + students);

            Set<Integer> studentids = new HashSet<>();  // Eliminar ids repetidos con Set collections
            for (Student student : students) {
                log.info("student:" + student);
                studentids.add(student.getId());
            }

            log.info("studentids: " + studentids);

            for (Integer studentid : studentids) {
                log.info("studentid:" + studentid);

                sql = "insert into api_alerts_viewers (alertid, userid) values(seq_alerts.currval, ?)";

                inserteds = jdbcTemplate.update(sql, studentid);
                log.info("Instances: Rows inserteds api_alerts_viewers: " + inserteds);

            }

            return students;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void delete(Integer id) throws Exception {
        log.info("delete("+id+")");
        try {

            String sql = "update api_alerts set deleted=1 where id=?";
            jdbcTemplate.update(sql, id);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void edit(Integer id, String content) throws Exception {
        log.info("edit("+id+", "+content+")");
        try {

            String sql = "update api_alerts set content=? where id=?";
            jdbcTemplate.update(sql, content, id);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
