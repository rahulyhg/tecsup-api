package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.utils.ColorPalette;
import pe.edu.tecsup.api.utils.Constant;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TeacherRepository {

    private static Logger log = Logger.getLogger(TeacherRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//@Cacheable(cacheNames="events", key="#id")    // Cache Enable: https://spring.io/guides/gs/caching/
    public List<Event> getEvents(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API_DOCENTES").withProcedureName("HORARIOSXDOCENTE");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODDOCENTE", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Event> events = new ArrayList<>();
            ColorPalette palette = new ColorPalette();

            for(Map<String, Object> record : recordset) {

                Event event = new Event();
                event.setId(record.get("codcursoejec")!=null?((BigDecimal)record.get("codcursoejec")).longValue():null);
                event.setCourse(record.get("nomcurso")!=null?(String)record.get("nomcurso"):null);
                event.setYear(record.get("anio")!=null?((BigDecimal)record.get("anio")).intValue():null);
                event.setMonth(record.get("mes")!=null?((BigDecimal)record.get("mes")).intValue():null);
                event.setDay(record.get("dia")!=null?((BigDecimal)record.get("dia")).intValue():null);
                event.setStartTime(record.get("hora_inicio")!=null?(String)record.get("hora_inicio"):null);
                event.setEndTime(record.get("hora_fin")!=null?(String)record.get("hora_fin"):null);

                event.setRoom(record.get("codamb")!=null?(String)record.get("codamb"):null);
                event.setTeacher(record.get("nomevaluador")!=null?(String)record.get("nomevaluador"):null);
                event.setSection(record.get("nomseccion")!=null?(String)record.get("nomseccion"):null);
                event.setType(record.get("tiposesion")!=null?(String)record.get("tiposesion"):null);
//                event.setFrequency(record.get("tiposemana")!=null?(String)record.get("tiposemana"):null);
                event.setTopic(record.get("unidad")!=null?(String)record.get("unidad"):null);
                event.setColor(palette.getColor(record.get("codcurso")!=null?((BigDecimal)record.get("codcurso")).intValue():null));

                events.add(event);
            }

            log.info("events: " + events);

            return events;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Course> getCourses(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API_DOCENTES").withProcedureName("CURSOSXDOCENTE");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODDOCENTE", id)
                    .addValue("E_C_CODPERIODO", null);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Course> courses = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Course course = new Course();
                course.setId(record.get("codcursoejec")!=null?((BigDecimal)record.get("codcursoejec")).longValue():null);
                course.setCourse(record.get("nomcurso")!=null?(String)record.get("nomcurso"):null);
                course.setSection(record.get("nomseccion")!=null?(String)record.get("nomseccion"):null);
                course.setIdteacher(record.get("codevaluador")!=null?((BigDecimal)record.get("codevaluador")).intValue():null);
                course.setTeacher(record.get("nomevaluador")!=null?(String)record.get("nomevaluador"):null);
                course.setPeriodo(record.get("nomperiodo")!=null?(String)record.get("nomperiodo"):null);
                courses.add(course);
            }

            log.info("courses: " + courses);

            return courses;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public Course getSections(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API_DOCENTES").withProcedureName("SECCIONESXCURSO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODCURSOEJEC", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_CURSO_RECORSET");
            log.info("Length of retrieved batches from database = "+recordset);

            Course course = null;

            for(Map<String, Object> record : recordset) {
                course = new Course();
                course.setId(record.get("codcursoejec")!=null?((BigDecimal)record.get("codcursoejec")).longValue():null);
                course.setCourse(record.get("nomcurso")!=null?(String)record.get("nomcurso"):null);
                course.setSection(record.get("nomseccion")!=null?(String)record.get("nomseccion"):null);
                course.setIdteacher(record.get("codevaluador")!=null?((BigDecimal)record.get("codevaluador")).intValue():null);
                course.setTeacher(record.get("nomevaluador")!=null?(String)record.get("nomevaluador"):null);
                course.setPeriodo(record.get("nomperiodo")!=null?(String)record.get("nomperiodo"):null);
            }

            if(course == null)
                throw new Exception("No existe curso");

            recordset = (ArrayList<Map<String, Object>>) out.get("S_C_SECCIONES_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Section> sections = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Section section = new Section();
                section.setId(record.get("codseccion")!=null?((BigDecimal)record.get("codseccion")).intValue():null);
                section.setName(record.get("nomseccion")!=null?(String)record.get("nomseccion"):null);
                sections.add(section);
            }

            course.setSections(sections);

            log.info("course: " + course);

            return course;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public CourseDashboard getMetrics(Integer codcursoejec, Integer codseccion) throws Exception {
        log.info("codcursoejec: "+codcursoejec+" - codseccion:"+codseccion);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API_DOCENTES").withProcedureName("METRICASXCURSOSECCION");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODCURSOEJEC", codcursoejec)
                    .addValue("E_C_CODSECCION", codseccion);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_PRACTICAS_RECORSET");
            log.info("Length of retrieved batches from database = "+recordset);

            CourseDashboard courseDashboard = new CourseDashboard();

            List<CourseDashboard.Score> scores = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                CourseDashboard.Score score = new CourseDashboard.Score();
                score.setDate(record.get("FECHA")!=null?(String)record.get("FECHA"):null);
                score.setCodtype(record.get("CODTIPOEVALUACION")!=null?((BigDecimal)record.get("CODTIPOEVALUACION")).intValue():null);
                score.setNumber(record.get("NUMPRACTICA")!=null?((BigDecimal)record.get("NUMPRACTICA")).intValue():null);
                score.setScore(record.get("NOTA")!=null?((BigDecimal)record.get("NOTA")).doubleValue():null);
                score.setTotal(record.get("NUMESTUDIANTES")!=null?((BigDecimal)record.get("NUMESTUDIANTES")).intValue():null);
                scores.add(score);
            }

            courseDashboard.setScores(scores);

            recordset = (ArrayList<Map<String, Object>>) out.get("S_C_ASISTENCIAS_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<CourseDashboard.Attendance> attendances = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                CourseDashboard.Attendance attendance = new CourseDashboard.Attendance();
                attendance.setDate(record.get("FECHA")!=null?(String)record.get("FECHA"):null);
                attendance.setCodtype(record.get("CODTIPOSESION")!=null?((BigDecimal)record.get("CODTIPOSESION")).intValue():null);
                attendance.setNumber(record.get("NUMSESION")!=null?((BigDecimal)record.get("NUMSESION")).intValue():null);
                attendance.setAttendeds(record.get("NUMASISTENCIAS")!=null?((BigDecimal)record.get("NUMASISTENCIAS")).intValue():null);
                attendance.setUnattendeds(record.get("NUMINASISTENCIAS")!=null?((BigDecimal)record.get("NUMINASISTENCIAS")).intValue():null);
                attendance.setTardiness(record.get("NUMTARDANZAS")!=null?((BigDecimal)record.get("NUMTARDANZAS")).intValue():null);
                attendance.setTotal(record.get("NUMESTUDIANTES")!=null?((BigDecimal)record.get("NUMESTUDIANTES")).intValue():null);
                attendances.add(attendance);
            }

            courseDashboard.setAttendances(attendances);

            log.info("courseDashboard: " + courseDashboard);

            return courseDashboard;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Seat> getSeatsWithLocations() throws Exception {
        log.info("getSeatsWithLocations");
        try {

            String sql = "select distinct s.codsede, s.descripcion, l.latitude, l.longitude\n" +
                    "from GENERAL.GEN_V_SEDE s \n" +
                    "inner join api_sedes l on l.codsede=s.codsede";

            List<Seat> seats = jdbcTemplate.query(sql, new RowMapper<Seat>() {
                public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Seat seat = new Seat();
                    seat.setId(rs.getString("codsede"));
                    seat.setName(rs.getString("descripcion"));
                    seat.setLatitude(rs.getDouble("latitude"));
                    seat.setLongitude(rs.getDouble("longitude"));
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

    public PhoneNumber getPhoneNumber(String instanceid) throws Exception {
        log.info("getPhoneNumber: "+instanceid);
        try{

            String sql = "SELECT ID, COUNTRYCODE, PHONENUMBER, ACTIVATED \n" +
                    "FROM API_PHONES \n" +
                    "WHERE INSTANCEID=? \n" +
                    "AND STATUS='1'";

            PhoneNumber phoneNumber = jdbcTemplate.queryForObject(sql, new RowMapper<PhoneNumber>() {
                public PhoneNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setId(rs.getLong("ID"));
                    phoneNumber.setCountryCode(rs.getString("COUNTRYCODE"));
                    phoneNumber.setPhoneNumber(rs.getString("PHONENUMBER"));
                    phoneNumber.setActivated("1".equals(rs.getString("ACTIVATED")));
                    return phoneNumber;
                }
            }, instanceid);

            log.info("phoneNumber: " + phoneNumber);

            return phoneNumber;
        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }
        return null;
    }

    public PhoneNumber getPhoneNumber(Long id) throws Exception {
        log.info("getPhoneNumber: "+id);
        try{

            String sql = "SELECT ID, INSTANCEID, COUNTRYCODE, PHONENUMBER, ACTIVATED \n" +
                    "FROM API_PHONES \n" +
                    "WHERE ID=? \n" +
                    "AND STATUS='1'";

            PhoneNumber phoneNumber = jdbcTemplate.queryForObject(sql, new RowMapper<PhoneNumber>() {
                public PhoneNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setId(rs.getLong("ID"));
                    phoneNumber.setInstanceid(rs.getString("INSTANCEID"));
                    phoneNumber.setCountryCode(rs.getString("COUNTRYCODE"));
                    phoneNumber.setPhoneNumber(rs.getString("PHONENUMBER"));
                    phoneNumber.setActivated("1".equals(rs.getString("ACTIVATED")));
                    return phoneNumber;
                }
            }, id);

            log.info("phoneNumber: " + phoneNumber);

            return phoneNumber;
        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }
        return null;
    }

    public void insertPhoneNumber(String instanceid, String countrycode, String phonenumber) throws Exception {
        log.info("insertPhoneNumber: instanceid:" + instanceid + " - countrycode:" + " - phonenumber:" + phonenumber);
        try {

            String sql = "UPDATE API_PHONES SET DELETED=SYSDATE, STATUS='0' WHERE INSTANCEID=?";

            int updateds = jdbcTemplate.update(sql, instanceid);
            log.info("API_PHONES: Rows updateds: " + updateds);

            sql = "INSERT INTO API_PHONES (ID, INSTANCEID, COUNTRYCODE, PHONENUMBER)\n" +
                    "VALUES (SEQ_PHONES.NEXTVAL, ?, ?, ?)";

            int inserteds = jdbcTemplate.update(sql, instanceid, countrycode, phonenumber);
            log.info("API_PHONES: Rows inserteds: " + inserteds);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void activatePhoneNumber(Long id) throws Exception {
        log.info("activatePhoneNumber: id:" + id);
        try {

            String sql = "UPDATE API_PHONES SET ACTIVATED='1' WHERE ID=?";

            int updateds = jdbcTemplate.update(sql, id);
            log.info("API_PHONES: Rows updateds: " + updateds);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public PhoneNumber getActivedPhoneNumber(String instanceid) throws Exception {
        log.info("getActivedPhoneNumber: "+instanceid);
        try{

            String sql = "SELECT ID, INSTANCEID, COUNTRYCODE, PHONENUMBER, ACTIVATED " +
                    "FROM API_PHONES WHERE INSTANCEID=? AND STATUS='1' AND ACTIVATED='1' AND ROWNUM=1";

            PhoneNumber phoneNumber = jdbcTemplate.queryForObject(sql, new RowMapper<PhoneNumber>() {
                public PhoneNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setId(rs.getLong("ID"));
                    phoneNumber.setInstanceid(rs.getString("INSTANCEID"));
                    phoneNumber.setCountryCode(rs.getString("COUNTRYCODE"));
                    phoneNumber.setPhoneNumber(rs.getString("PHONENUMBER"));
                    phoneNumber.setActivated("1".equals(rs.getString("ACTIVATED")));
                    return phoneNumber;
                }
            }, instanceid);

            log.info("phoneNumber: " + phoneNumber);

            return phoneNumber;
        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }
        return null;
    }

    public Integer saveIncident(Integer customerid, String phone, String sede, String location) throws Exception {
        log.info("saveIncident: customerid:" + customerid + ", phone:" + phone + ", sede:" + sede + ", location:" + location);
        try {

            String sql = "INSERT INTO API_INCIDENTS (ID, CUSTOMERID, PHONE, SEDE, LOCATION, CREATED, STATUS)\n" +
                    "VALUES(SEQ_INCIDENTS.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?)";

            int inserteds = jdbcTemplate.update(sql, customerid, phone, sede, location, Constant.INCIDENT_STATUS_PENDIENT);
            log.info("API_INCIDENTS: Rows inserteds: " + inserteds);

            sql = "SELECT SEQ_INCIDENTS.CURRVAL FROM DUAL";

            return jdbcTemplate.queryForObject(sql, Integer.class);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public Incident getIncident(Integer id) throws Exception {
        log.info("getIncident: "+id);
        try{

            String sql = "SELECT ID, CUSTOMERID, GENERAL.NOMBRECLIENTE(CUSTOMERID) AS CUSTOMERNAME, PHONE, SEDE, LOCATION, COMMENTS, CREATED, UPDATED, \n" +
                    "TECHNICALID, DECODE(TECHNICALID, NULL, NULL, GENERAL.NOMBRECLIENTE(TECHNICALID)) AS TECHNICALNAME, STATUS\n" +
                    "FROM API_INCIDENTS\n" +
                    "WHERE ID=?";

            Incident incident = jdbcTemplate.queryForObject(sql, new RowMapper<Incident>() {
                public Incident mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Incident incident = new Incident();
                    incident.setId(rs.getInt("ID"));
                    incident.setCustomerid(rs.getInt("CUSTOMERID"));
                    incident.setCustomer(rs.getString("CUSTOMERNAME"));
                    incident.setPhone(rs.getString("PHONE"));
                    incident.setSede(rs.getString("SEDE"));
                    incident.setLocation(rs.getString("LOCATION"));
                    incident.setComments(rs.getString("COMMENTS"));
                    incident.setCreated(rs.getTimestamp("CREATED")!=null?new Date(rs.getTimestamp("CREATED").getTime()):null);
                    incident.setUpdated(rs.getTimestamp("UPDATED")!=null?new Date(rs.getTimestamp("UPDATED").getTime()):null);
                    incident.setTechnicalid(rs.getInt("TECHNICALID"));
                    if(rs.wasNull()) incident.setTechnicalid(null);
                    incident.setTechnical(rs.getString("TECHNICALNAME"));
                    incident.setStatus(rs.getString("STATUS"));
                    return incident;
                }
            }, id);

            log.info("incident: " + incident);

            return incident;
        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }
        return null;
    }

    public List<Technical> getTechnicalForNotification(String sede) throws Exception {
        log.info("getTechnicalForNotification: " + sede);
        try {

            String sql = "SELECT T.ID, GENERAL.NOMBRECLIENTE(T.ID) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(T.ID) AS NAME, \n" +
                    "(SELECT TRIM(CORREO)||'tecsup.edu.pe' FROM PERSONAL.PER_EMPLEADO WHERE CODEMPLEADO=T.ID) AS CORREO, I.INSTANCEID\n" +
                    "FROM API_TECHNICALS T\n" +
                    "LEFT JOIN MOVIL.API_INSTANCES I ON I.LASTUSERID=T.ID AND I.APP='TECSUP' AND I.STATUS='1'\n" +
                    "WHERE T.SEDE=? AND T.STATUS='1'";

            List<Technical> technicals = jdbcTemplate.query(sql, new RowMapper<Technical>() {
                public Technical mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Technical technical = new Technical();
                    technical.setId(rs.getInt("ID"));
                    technical.setFullname(rs.getString("FULLNAME"));
                    technical.setName(rs.getString("NAME"));
                    technical.setEmail(rs.getString("CORREO"));
                    technical.setInstanceid(rs.getString("INSTANCEID"));
                    return technical;
                }
            }, sede);

            log.info("technicals: " + technicals);

            return technicals;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Incident> getIncidents(String sede, String status) throws Exception {
        log.info("getIncidents: "+sede+" - status:"+status);
        try{

            String sql = "SELECT ID, CUSTOMERID, GENERAL.NOMBRECLIENTE(CUSTOMERID) AS CUSTOMERNAME, PHONE, SEDE, LOCATION, COMMENTS, CREATED, UPDATED, \n" +
                    "TECHNICALID, DECODE(TECHNICALID, NULL, NULL, GENERAL.NOMBRECLIENTE(TECHNICALID)) AS TECHNICALNAME, STATUS\n" +
                    "FROM API_INCIDENTS\n" +
                    "WHERE SEDE=? AND STATUS=?\n" +
                    "ORDER BY ID DESC";

            List<Incident> incidents = jdbcTemplate.query(sql, new RowMapper<Incident>() {
                public Incident mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Incident incident = new Incident();
                    incident.setId(rs.getInt("ID"));
                    incident.setCustomerid(rs.getInt("CUSTOMERID"));
                    incident.setCustomer(rs.getString("CUSTOMERNAME"));
                    incident.setPhone(rs.getString("PHONE"));
                    incident.setSede(rs.getString("SEDE"));
                    incident.setLocation(rs.getString("LOCATION"));
                    incident.setComments(rs.getString("COMMENTS"));
                    incident.setCreated(rs.getTimestamp("CREATED")!=null?new Date(rs.getTimestamp("CREATED").getTime()):null);
                    incident.setUpdated(rs.getTimestamp("UPDATED")!=null?new Date(rs.getTimestamp("UPDATED").getTime()):null);
                    incident.setTechnicalid(rs.getInt("TECHNICALID"));
                    if(rs.wasNull()) incident.setTechnicalid(null);
                    incident.setTechnical(rs.getString("TECHNICALNAME"));
                    incident.setStatus(rs.getString("STATUS"));
                    return incident;
                }
            }, sede, status);

            log.info("incident: " + incidents);

            return incidents;

        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }
        return null;
    }

}
