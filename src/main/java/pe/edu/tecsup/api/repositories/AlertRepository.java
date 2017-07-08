package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

            String sql = "select a.id, general.nombrecliente(a.senderid) sender, a.content, to_char(a.senddate, 'dd-mm-yyyy hh24:mi') senddate\n" +
                    "from api_alerts a\n" +
                    "where a.deleted=0 and a.senderid=?\n" +
                    "order by a.senddate desc";

            List<Alert> alerts = jdbcTemplate.query(sql, new RowMapper<Alert>() {
                public Alert mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Alert alert = new Alert();
                    alert.setId(rs.getLong("id"));
                    alert.setFrom(rs.getString("sender"));
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

    public void save(Integer senderid, String content) throws Exception {
        log.info("save("+senderid+":"+content+")");
        try {

            String sql = "insert into api_alerts (senderid, content, senddate) values(?, ?, sysdate)";

            int inserteds = jdbcTemplate.update(sql, senderid, content);

            log.info("Instances: Rows inserteds: " + inserteds);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
