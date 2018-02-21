package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.utils.ColorPalette;
import pe.edu.tecsup.api.utils.Constant;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                course.setMinscore(record.get("notaminima")!=null?((BigDecimal)record.get("notaminima")).doubleValue():null);
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
        }catch (Exception e){
            log.error(e, e);
            throw e;
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
        }catch (Exception e){
            log.error(e, e);
            throw e;
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
        }catch (Exception e){
            log.error(e, e);
            throw e;
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
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
        return null;
    }

    public List<Technical> getTechnicalForNotification(String sede) throws Exception {
        log.info("getTechnicalForNotification: " + sede);
        try {

            String sql = "SELECT P.CODEMPLEADO AS ID, GENERAL.NOMBRECLIENTE(P.CODEMPLEADO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(P.CODEMPLEADO) AS NAME, TRIM(CORREO)||'@tecsup.edu.pe' AS CORREO, I.INSTANCEID\n" +
                    "FROM PERSONAL.PER_EMPLEADO P\n" +
                    "LEFT JOIN MOVIL.API_INSTANCES I ON I.LASTUSERID=P.CODEMPLEADO AND I.APP='TECSUP-SOPORTE' AND I.STATUS='1'\n" +
                    "WHERE P.LUGAR=? AND P.ESACTIVO='S' \n" +
                    "AND EXISTS(SELECT * FROM SEGURIDAD.SEG_USUARIO U INNER JOIN SEGURIDAD.SEG_USUARIO_ROL R ON R.USUARIO=U.USUARIO AND R.CODROL=214 AND R.ESACTIVO='S' WHERE U.CODSUJETO=P.CODEMPLEADO AND U.ESACTIVO='S')";

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

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Incident> getIncidentsByTechnical(Integer userid, String status) throws Exception {
        log.info("getIncidentsByTechnical: userid:"+userid+" - status:"+status);
        try{

            String sql = "SELECT ID, CUSTOMERID, GENERAL.NOMBRECLIENTE(CUSTOMERID) AS CUSTOMERNAME, PHONE, SEDE, LOCATION, COMMENTS, CREATED, UPDATED, \n" +
                    "TECHNICALID, DECODE(TECHNICALID, NULL, NULL, GENERAL.NOMBRECLIENTE(TECHNICALID)) AS TECHNICALNAME, STATUS\n" +
                    "FROM API_INCIDENTS\n" +
                    "WHERE TECHNICALID=? AND STATUS=?\n" +
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
            }, userid, status);

            log.info("incident: " + incidents);

            return incidents;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Incident> getAllIncidents() throws Exception {
        log.info("getAllIncidents:");
        try{

            String sql = "SELECT ID, CUSTOMERID, GENERAL.NOMBRECLIENTE(CUSTOMERID) AS CUSTOMERNAME, PHONE, SEDE, LOCATION, COMMENTS, CREATED, UPDATED, \n" +
                    "TECHNICALID, DECODE(TECHNICALID, NULL, NULL, GENERAL.NOMBRECLIENTE(TECHNICALID)) AS TECHNICALNAME, STATUS\n" +
                    "FROM API_INCIDENTS\n" +
                    "WHERE STATUS!='C'\n" +
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
            });

            log.info("incident: " + incidents);

            return incidents;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void updateIncident(Integer id, Integer technicalid, String status) throws Exception {
        log.info("updateIncident: id:"+id+", technicalid:" + technicalid + ", status:" + status);
        try {

            String sql = "UPDATE API_INCIDENTS  SET UPDATED=SYSDATE, TECHNICALID=?, STATUS=?\n" +
                    "WHERE ID=?";

            int inserteds = jdbcTemplate.update(sql, technicalid, status, id);
            log.info("API_INCIDENTS: Rows updateds: " + inserteds);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public Customer getCustomer(Integer id) throws Exception {
        log.info("getCustomer: " + id);
        try {

            String sql = "SELECT P.CODEMPLEADO, GENERAL.NOMBRECLIENTE(P.CODEMPLEADO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(P.CODEMPLEADO) AS NAME, TRIM(P.CORREO)||'@tecsup.edu.pe' AS CORREO, P.LUGAR AS SEDE\n" +
                    "FROM PERSONAL.PER_EMPLEADO P\n" +
                    "WHERE P.ESACTIVO='S' AND P.CODEMPLEADO=?";

            Customer customer = jdbcTemplate.queryForObject(sql, new RowMapper<Customer>() {
                public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("CODEMPLEADO"));
                    customer.setFullname(rs.getString("FULLNAME"));
                    customer.setName(rs.getString("NAME"));
                    customer.setEmail(rs.getString("CORREO"));
                    customer.setSede(rs.getString("SEDE"));
                    return customer;
                }
            }, id);

            sql = "SELECT I.INSTANCEID\n" +
                    "FROM API_INSTANCES I\n" +
                    "WHERE I.LASTUSERID=? AND I.APP='TECSUP-SOPORTE' AND I.STATUS=1";

            List<String> instancesid = jdbcTemplate.query(sql, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("INSTANCEID");
                }
            }, id);

            customer.setInstancesid(instancesid);

            log.info("customer: " + customer);

            return customer;
        }catch (EmptyResultDataAccessException e){
            log.warn(e, e);
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
        return null;
    }

    public List<Student> getStudentsbyCourse(Integer codcursoejec) throws Exception {
        log.info("getStudentsbyCourse: "+codcursoejec);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API_DOCENTES").withProcedureName("ESTUDIANTESXCURSO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODCURSOEJEC", codcursoejec);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Student> students = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Student student = new Student();
                student.setCodcursoejec(record.get("CODCURSOEJEC")!=null?((BigDecimal)record.get("CODCURSOEJEC")).intValue():null);
                student.setCodseccion(record.get("CODSECCION")!=null?((BigDecimal)record.get("CODSECCION")).intValue():null);
                student.setNomseccion(record.get("NOMSECCION")!=null?(String)record.get("NOMSECCION"):null);
                student.setId(record.get("CODALUMNO")!=null?((BigDecimal)record.get("CODALUMNO")).intValue():null);
                student.setNombres(record.get("NOMALUMNO")!=null?(String)record.get("NOMALUMNO"):null);
                students.add(student);
            }

            log.info("students: " + students);

            return students;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Section> getSectionsByTeacher(Integer id) throws Exception {
        log.info("getSectionsByTeacher("+id+")");
        try {

            // Query SEVA
//            String sql = "SELECT DISTINCT NP.CODSECCION, C.CODCURSOEJEC, C.CODCURSO, P.CODIGO AS CODPERIODO, \n" +
//                    "'[' || P.SEDE || '-' || REPLACE(P.NOMBRE, ' ', '') || '] ' AS NOMPERIODO, C.DESCRIPCION AS NOMCURSO,\n" +
//                    "(SELECT UPPER(NOMCORTO) FROM COMERCIAL.COM_PRODUCTO WHERE CODIGO=C.CODCURSO) AS NOMCURSOCORTO,\n" +
//                    "TRIM(S.DESCRIPCION) || ' - ' || C.CODCICLO AS NOMSECCION\n" +
//                    "FROM EVALUACION.EVA_DEF_NRO_EVAL_PARCIALES NP\n" +
//                    "INNER JOIN EVALUACION.EVA_V_CURSOS C ON C.CODCURSOEJEC=NP.CODCURSOEJEC AND C.SITUACIONREGISTRO='A'\n" +
//                    "INNER JOIN EVALUACION.EVA_CURSO_PERIODO CP ON CP.CODCURSOEJEC=C.CODCURSOEJEC\n" +
//                    "INNER JOIN GENERAL.GEN_PERIODO P ON P.CODIGO = CP.CODPERIODO AND P.ESTADO = 1 -- EVA_V_PERIODO los profes abren y cierra en cualquier momento\n" +
//                    "INNER JOIN EVALUACION.EVA_V_SECCION S ON S.CODSECCION=NP.CODSECCION\n" +
//                    "WHERE NP.CODEVALUADOR = ?\n" +
//                    "ORDER BY NOMPERIODO, NOMCURSO, NOMSECCION";

            // Query Docencia
            String sql = "SELECT DISTINCT H.CODSECCION, NULL AS CODCURSOEJEC, H.CODIGOCUR AS CODCURSO, P.CODIGO AS CODPERIODO, \n" +
                    "'[' || P.SEDE || '-' || REPLACE(P.NOMBRE, ' ', '') || '] ' AS NOMPERIODO, \n" +
                    "C.NOMBRE AS NOMCURSO, UPPER(NOMCORTO) AS NOMCURSOCORTO,\n" +
                    "DOCENCIA.NOMCORTOESPECI(H.CODESPECIALIDAD) || ' ' || H.CODGRP || ' - ' || H.NUMCIC AS NOMSECCION\n" +
                    "FROM DOCENCIA.HORDEF H\n" +
                    "INNER JOIN GENERAL.GEN_PERIODO P ON P.CODIGO = H.CODPERIODO AND P.ESTADO = 1\n" +
                    "INNER JOIN COMERCIAL.COM_PRODUCTO C ON C.CODIGO=H.CODIGOCUR\n" +
                    "WHERE CODEVALUADOR = ?\n" +
                    "ORDER BY NOMPERIODO, NOMCURSO, NOMSECCION";

            List<Section> sections = jdbcTemplate.query(sql, new RowMapper<Section>() {
                public Section mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Section section = new Section();
                    section.setId(rs.getInt("CODSECCION"));
                    section.setName(rs.getString("NOMPERIODO") + " " + rs.getString("NOMSECCION") + "\n" + rs.getString("NOMCURSOCORTO") );
                    return section;
                }
            }, id);

            log.info("sections: " + sections);

            return sections;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Student> saveAlert(Integer senderid, String content, Integer[] codsecciones) throws Exception {
        log.info("saveAlert("+senderid+":"+content+", "+codsecciones+")");
        try {

            String sql = "SELECT DISTINCT --NP.CODSECCION, C.CODCURSOEJEC, C.CODCURSO, P.CODIGO AS CODPERIODO, \n" +
                    "--'[' || P.SEDE || '-' || REPLACE(P.NOMBRE, ' ', '') || '] ' AS NOMPERIODO, C.DESCRIPCION AS NOMCURSO,\n" +
                    "--(SELECT UPPER(NOMCORTO) FROM COMERCIAL.COM_PRODUCTO WHERE CODIGO=C.CODCURSO) AS NOMCURSOCORTO,\n" +
                    "TRIM(S.DESCRIPCION) || ' - ' || C.CODCICLO AS NOMSECCION\n" +
                    "FROM EVALUACION.EVA_DEF_NRO_EVAL_PARCIALES NP\n" +
                    "INNER JOIN EVALUACION.EVA_V_CURSOS C ON C.CODCURSOEJEC=NP.CODCURSOEJEC AND C.SITUACIONREGISTRO='A'\n" +
                    "INNER JOIN EVALUACION.EVA_CURSO_PERIODO CP ON CP.CODCURSOEJEC=C.CODCURSOEJEC\n" +
                    "INNER JOIN GENERAL.GEN_PERIODO P ON P.CODIGO = CP.CODPERIODO --AND P.ESTADO = 1 -- EVA_V_PERIODO los profes abren y cierra en cualquier momento\n" +
                    "INNER JOIN EVALUACION.EVA_V_SECCION S ON S.CODSECCION=NP.CODSECCION\n" +
                    "WHERE NP.CODSECCION in (:codsecciones)\n" +
                    "ORDER BY 1";

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("codsecciones", Arrays.asList(codsecciones));

            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

            List<String> secciones = namedParameterJdbcTemplate.query(sql, parameters, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("NOMSECCION");
                }
            });

            String fromlabel = StringUtils.join(secciones, ",");
            log.info("fromlabel: " + fromlabel);

            sql = "insert into api_alerts (id, senderid, content, receiver, senddate) values(seq_alerts.nextval, ?, ?, ?, sysdate)";

            int inserteds = jdbcTemplate.update(sql, senderid, content, fromlabel);
            log.info("Instances: Rows inserteds api_alerts: " + inserteds);

            sql = "select distinct ca.codalumno, docencia.carnet(ca.codalumno) as carnet, general.nombrecliente(ca.codalumno) as nombres, (select correo from docencia.doc_alumno where codalumno=ca.codalumno) as correo, I.INSTANCEID\n" +
                    "from evaluacion.eva_v_curso_alumno ca\n" +
                    "LEFT JOIN MOVIL.API_INSTANCES I ON I.LASTUSERID=CA.CODALUMNO AND I.APP='TECSUP' AND I.STATUS='1'\n" +
                    "where ca.seccion in (:codsecciones)";// +
//                    "union\n" +
//                    "select 46694, '123456', 'Test User', 'ebenites@tecsup.edu.pe', 'foOs3EBDKbU:APA91bHQ8gYQCzCGoOVoE6-A5V5pdFosheZ3BH2xb3dlAr2DZAap93tx8lHU-198eYDQup24L88N6fN8W1dkgZw_FcEBCyHqokcJ4H56ixQpiizzoFxZN79ZdQzW9Py_EX1s2VZMoWdk' FROM DUAL\n";

            parameters = new MapSqlParameterSource();
            parameters.addValue("codsecciones", Arrays.asList(codsecciones));

            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

            List<Student> students = namedParameterJdbcTemplate.query(sql, parameters, new RowMapper<Student>() {
                public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Student student = new Student();
                    student.setId(rs.getInt("codalumno"));
                    student.setCarnet(rs.getString("carnet"));
                    student.setNombres(rs.getString("nombres"));
                    student.setCorreo(rs.getString("correo"));
                    student.setInstanceid(rs.getString("INSTANCEID"));
                    return student;
                }
            });

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

}
