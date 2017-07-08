package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.utils.ColorPalette;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class StudentRepository {

    private static Logger log = Logger.getLogger(StudentRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public List<Debt> getDebts(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("FINANZAS_DEUDASXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Debt> debts = new ArrayList<>();

            for(Map<String, Object> record : recordset) {

                Debt debt = new Debt();
                debt.setConcept((String)record.get("CONCEPTO"));
                debt.setExpiration(record.get("FECVENCIMIENTO")!=null?(String)record.get("FECVENCIMIENTO"):null);
                debt.setExpired((record.get("EXPIRADO")!=null && ((BigDecimal)record.get("EXPIRADO")).intValue() == 1));
                debt.setBalance((record.get("SALDO")!=null)?(String)record.get("SALDO"):null);
                debt.setArrears((record.get("MORA")!=null)?(String)record.get("MORA"):null);
                debts.add(debt);

            }

            log.info("debts: " + debts);

            return debts;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<Pay> getPays(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("FINANZAS_PAGOSXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Pay> pays = new ArrayList<>();

            for(Map<String, Object> record : recordset) {

                Pay pay = new Pay();
                pay.setConcept(record.get("DETALLE")!=null?(String)record.get("DETALLE"):null);
                pay.setDate(record.get("FECEMISION")!=null?(String)record.get("FECEMISION"):null);
                pay.setAmount(record.get("IMPORTE")!=null?(String)record.get("IMPORTE"):null);
                pay.setVoucher(record.get("COMPROBANTE")!=null?(String)record.get("COMPROBANTE"):null);
                pays.add(pay);
            }

            log.info("pays: " + pays);

            return pays;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public Credit getCredits(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("FINANZAS_CREDITOSXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_INFO_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            Credit credit = null;

            for(Map<String, Object> record : recordset) {

                credit = new Credit();
                credit.setStartdate(record.get("FECINICIO")!=null?(String)record.get("FECINICIO"):null);
                credit.setEnddate(record.get("FECFIN")!=null?(String)record.get("FECFIN"):null);

                credit.setAssigned(record.get("OTORGADO")!=null?(String)record.get("OTORGADO"):null);
                credit.setAjusted(record.get("AJUSTADO")!=null?(String)record.get("AJUSTADO"):null);
                credit.setPaid(record.get("TOTABONO")!=null?(String)record.get("TOTABONO"):null);
                credit.setBalance(record.get("SALDO")!=null?(String)record.get("SALDO"):null);

                if(record.get("FECINICIO")!=null && record.get("FECFIN")!=null && record.get("CUOTAMENSUAL")!=null)
                    credit.setExtrainfo("Para cancelar el pago a la fecha establecida debería abonar mensualmente: " + record.get("CUOTAMENSUAL"));

                break;
            }

            if(credit == null) {
                log.info("No existe información de crédito educativo");
                return null;
            }

            // Deudas Credito

            recordset = (ArrayList<Map<String, Object>>) out.get("S_C_DEUDAS_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Debt> debts = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Debt debt = new Debt();
                debt.setConcept(record.get("CONCEPTO")!=null?(String)record.get("CONCEPTO"):null);
                debt.setExpiration(record.get("FECVENCIMIENTO")!=null?(String)record.get("FECVENCIMIENTO"):null);
                debt.setExpired(record.get("EXPIRADO")!=null && ((BigDecimal)record.get("EXPIRADO")).intValue()==1);
                debt.setBalance(record.get("CARGO")!=null?(String)record.get("CARGO"):null);

                debts.add(debt);
            }

            credit.setDebts(debts);

            // Pagos Credito

            recordset = (ArrayList<Map<String, Object>>) out.get("S_C_PAGOS_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Pay> pays = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Pay pay = new Pay();
                pay.setConcept(record.get("CONCEPTO")!=null?(String)record.get("CONCEPTO"):null);
                pay.setDate(record.get("FECEMISION")!=null?(String)record.get("FECEMISION"):null);
                pay.setAmount(record.get("ABONO")!=null?(String)record.get("ABONO"):null);
                pay.setVoucher(record.get("NUMDOCCOMERCIAL")!=null?(String)record.get("NUMDOCCOMERCIAL"):null);

                pays.add(pay);
            }

            credit.setPays(pays);

            log.info("credit: " + credit);

            return credit;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    @Cacheable("events")    // Cache Enable: https://spring.io/guides/gs/caching/
    public List<Event> getEvents(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("HORARIOSXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id);

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
                event.setFrequency(record.get("tiposemana")!=null?(String)record.get("tiposemana"):null);
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

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOSXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id)
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

    public Score getScoreMaster(Integer id, Integer idcourse) throws Exception {
        log.info("id: "+id + " - idcourse: " + idcourse);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOS_NOTASXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id)
                    .addValue("E_C_CODCURSOEJEC", idcourse);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            Score score = null;

            for(Map<String, Object> record : recordset) {

                score = new Score();
                score.setCourse(record.get("nomcurso")!=null?(String)record.get("nomcurso"):null);
                score.setTerm(record.get("nomperiodo")!=null?(String)record.get("nomperiodo"):null);

                score.setTheoId(record.get("teocodigo")!=null?((BigDecimal)record.get("teocodigo")).intValue():null);
                score.setTheoTitle(record.get("teonombre")!=null?(String)record.get("teonombre"):null);
                score.setTheoScore(record.get("teopromedio")!=null?((BigDecimal)record.get("teopromedio")).doubleValue():null);
                score.setTheoWeight(record.get("teopeso")!=null?((BigDecimal)record.get("teopeso")).doubleValue():null);

                score.setLabId(record.get("labcodigo")!=null?((BigDecimal)record.get("labcodigo")).intValue():null);
                score.setLabTitle(record.get("labnombre")!=null?(String)record.get("labnombre"):null);
                score.setLabScore(record.get("labpromedio")!=null?((BigDecimal)record.get("labpromedio")).doubleValue():null);
                score.setLabWeight(record.get("labpeso")!=null?((BigDecimal)record.get("labpeso")).doubleValue():null);

                score.setWorkId(record.get("talcodigo")!=null?((BigDecimal)record.get("talcodigo")).intValue():null);
                score.setWorkTitle(record.get("talnombre")!=null?(String)record.get("talnombre"):null);
                score.setWorkScore(record.get("talpromedio")!=null?((BigDecimal)record.get("talpromedio")).doubleValue():null);
                score.setWorkWeight(record.get("talpeso")!=null?((BigDecimal)record.get("talpeso")).doubleValue():null);

                score.setPartialId(record.get("parcodigo")!=null?((BigDecimal)record.get("parcodigo")).intValue():null);
                score.setPartialTitle(record.get("parnombre")!=null?(String)record.get("parnombre"):null);
                score.setPartialScore(record.get("parpromedio")!=null?((BigDecimal)record.get("parpromedio")).doubleValue():null);
                score.setPartialWeight(record.get("parpeso")!=null?((BigDecimal)record.get("parpeso")).doubleValue():null);

                score.setFinalId(record.get("fincodigo")!=null?((BigDecimal)record.get("fincodigo")).intValue():null);
                score.setFinalTitle(record.get("finnombre")!=null?(String)record.get("finnombre"):null);
                score.setFinalScore(record.get("finpromedio")!=null?((BigDecimal)record.get("finpromedio")).doubleValue():null);
                score.setFinalWeight(record.get("finpeso")!=null?((BigDecimal)record.get("finpeso")).doubleValue():null);

                Double mobileScore = 0.0;

                if(score.getTheoId() != null && score.getTheoScore() != null){
                    mobileScore += score.getTheoScore() * score.getTheoWeight() / 100;
                }
                if(score.getLabId() != null && score.getLabScore() != null){
                    mobileScore += score.getLabScore() * score.getLabWeight() / 100;
                }
                if(score.getWorkId() != null && score.getWorkScore() != null){
                    mobileScore += score.getWorkScore() * score.getWorkWeight() / 100;
                }
                if(score.getPartialId() != null && score.getPartialScore() != null){
                    mobileScore += score.getPartialScore() * score.getPartialWeight() / 100;
                }
                if(score.getFinalId() != null && score.getFinalScore() != null){
                    mobileScore += score.getFinalScore() * score.getFinalWeight() / 100;
                }

                score.setScore(Math.round(mobileScore*100d)/100d);
                score.setFormula(record.get("formula")!=null?(String)record.get("formula"):null);

                // Theos
                List<Score.Item> theos = getScoreDetail(id, idcourse, 2324);
                score.setTheos(theos);

                // Labs
                List<Score.Item> labs = getScoreDetail(id, idcourse, 2326);
                score.setLabs(labs);

                // Works
                List<Score.Item> works = getScoreDetail(id, idcourse, 2325);
                score.setWorks(works);

                break;
            }

            log.info("score: " + score);

            return score;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    private List<Score.Item> getScoreDetail(Integer id, Integer idcourse, Integer idtype) throws Exception {
        log.info("id: "+id + " - idcourse: " + idcourse + " - idtype: " + idtype);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOS_NOTAS_LISTXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id)
                    .addValue("E_C_CODCURSOEJEC", idcourse)
                    .addValue("E_C_CODTIPOEVALUACION", idtype);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Score.Item> items = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Score.Item item = new Score.Item();
                item.setTitle(record.get("nro")!=null?"Práctica " + ((BigDecimal)record.get("nro")).intValue():null);
                item.setScore(record.get("nota")!=null?((BigDecimal)record.get("nota")).doubleValue():null);
                item.setWeight(record.get("peso")!=null?((BigDecimal)record.get("peso")).intValue():null);
                items.add(item);
            }

            log.info("items: " + items);

            return items;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public Attendance getAttendanceMaster(Integer id, Integer idcourse) throws Exception {
        log.info("id: "+id + " - idcourse: " + idcourse);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOS_ASISTENCIASXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id)
                    .addValue("E_C_CODCURSOEJEC", idcourse);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            Attendance attendance = null;

            for(Map<String, Object> record : recordset) {

                attendance = new Attendance();
                attendance.setCourse(record.get("nomcurso")!=null?(String)record.get("nomcurso"):null);
                attendance.setTerm(record.get("nomperiodo")!=null?(String)record.get("nomperiodo"):null);

                attendance.setTheoId(record.get("teocodigo")!=null?((BigDecimal)record.get("teocodigo")).intValue():null);
                attendance.setTheoTitle(record.get("teonombre")!=null?(String)record.get("teonombre"):null);
                attendance.setTheoAttendeds(record.get("teonasistencias")!=null?((BigDecimal)record.get("teonasistencias")).intValue():null);
                attendance.setTheoUnattendeds(record.get("teoninasistencias")!=null?((BigDecimal)record.get("teoninasistencias")).intValue():null);
                attendance.setTheoTardiness(record.get("teontardanzas")!=null?((BigDecimal)record.get("teontardanzas")).intValue():null);

                attendance.setLabId(record.get("labcodigo")!=null?((BigDecimal)record.get("labcodigo")).intValue():null);
                attendance.setLabTitle(record.get("labnombre")!=null?(String)record.get("labnombre"):null);
                attendance.setLabAttendeds(record.get("labnasistencias")!=null?((BigDecimal)record.get("labnasistencias")).intValue():null);
                attendance.setLabUnattendeds(record.get("labninasistencias")!=null?((BigDecimal)record.get("labninasistencias")).intValue():null);
                attendance.setLabTardiness(record.get("labntardanzas")!=null?((BigDecimal)record.get("labntardanzas")).intValue():null);

                attendance.setWorkId(record.get("talcodigo")!=null?((BigDecimal)record.get("talcodigo")).intValue():null);
                attendance.setWorkTitle(record.get("talnombre")!=null?(String)record.get("talnombre"):null);
                attendance.setWorkAttendeds(record.get("talnasistencias")!=null?((BigDecimal)record.get("talnasistencias")).intValue():null);
                attendance.setWorkUnattendeds(record.get("talninasistencias")!=null?((BigDecimal)record.get("talninasistencias")).intValue():null);
                attendance.setWorkTardiness(record.get("talntardanzas")!=null?((BigDecimal)record.get("talntardanzas")).intValue():null);

                attendance.setUnattendeds(record.get("porinasistencia")!=null?((BigDecimal)record.get("porinasistencia")).doubleValue():null);

                Boolean hasDI = false;
                if(attendance.getUnattendeds() != null && record.get("limiteporinasistencia")!=null){
                    hasDI = attendance.getUnattendeds() > ((BigDecimal)record.get("limiteporinasistencia")).doubleValue();
                }
                attendance.setHasDI(hasDI);

                // Theos
                List<Attendance.Item> theos = getAttendanceDetail(id, idcourse, 2324);
                attendance.setTheos(theos);

                // Labs
                List<Attendance.Item> labs = getAttendanceDetail(id, idcourse, 2326);
                attendance.setLabs(labs);

                // Works
                List<Attendance.Item> works = getAttendanceDetail(id, idcourse, 2325);
                attendance.setWorks(works);

                break;
            }

            log.info("attendance: " + attendance);

            return attendance;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    private List<Attendance.Item> getAttendanceDetail(Integer id, Integer idcourse, Integer idtype) throws Exception {
        log.info("id: "+id + " - idcourse: " + idcourse + " - idtype: " + idtype);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOS_ASISTENCIAS_LISTXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id)
                    .addValue("E_C_CODCURSOEJEC", idcourse)
                    .addValue("E_C_CODTIPOEVALUACION", idtype);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<Attendance.Item> items = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                Attendance.Item item = new Attendance.Item();
                item.setTitle(record.get("nro")!=null?"Sesión " + (String)record.get("nro"):null);
                item.setDate(record.get("fecha")!=null?new SimpleDateFormat("dd/MM/yyyy").format((Date)record.get("fecha")):null);
                item.setStatus(record.get("estado")!=null?((String)record.get("estado")):null);
                items.add(item);
            }

            log.info("items: " + items);

            return items;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public History getHistory(Integer id) throws Exception {
        log.info("id: "+id);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("HISTORIALXALUMNO");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("E_C_CODALUMNO", id);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_INFO_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            History history = null;

            for(Map<String, Object> record : recordset) {
                history = new History();
                history.setIdstudent(record.get("codalumno")!=null?((BigDecimal)record.get("codalumno")).longValue():null);
                history.setFullname(record.get("nomalumno")!=null?((String)record.get("nomalumno")):null);
                history.setIdcareer(record.get("codespecialidad")!=null?((BigDecimal)record.get("codespecialidad")).intValue():null);
                history.setCareer(record.get("nomespecialidad")!=null?((String)record.get("nomespecialidad")):null);
                history.setAverrage(record.get("promedio")!=null?((BigDecimal)record.get("promedio")).doubleValue():null);
                history.setRanking(record.get("puesto")!=null?((BigDecimal)record.get("puesto")).intValue():null);
                history.setStatus(record.get("ranking")!=null?((String)record.get("ranking")):null);

                break;
            }

            if(history == null)
                throw new Exception("No existe datos históricos");

            // Get Terms

            recordset = (ArrayList<Map<String, Object>>) out.get("S_C_SEMESTRES_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<History.Cycle> cycles = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                History.Cycle cycle = new History.Cycle();
                cycle.setName(record.get("ciclo")!=null?((String)record.get("ciclo")):null);
                cycle.setIdterm(record.get("codperiodo")!=null?((BigDecimal)record.get("codperiodo")).intValue():null);
                cycle.setTerm(record.get("nomperiodo")!=null?((String)record.get("nomperiodo")):null);
                cycle.setAverrage(record.get("promedio")!=null?((BigDecimal)record.get("promedio")).doubleValue():null);

                cycles.add(cycle);
            }

            history.setCycles(cycles);

            // Get Courses X Cycle

            for (History.Cycle cycle :  history.getCycles()){

                simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

                simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("CURSOSXALUMNO");

                in = new MapSqlParameterSource()
                        .addValue("E_C_CODALUMNO", id)
                        .addValue("E_C_CODPERIODO", cycle.getIdterm());

                out = simpleJdbcCall.execute(in);
                log.info(out);

                recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
                log.info("Length of retrieved batches from database = "+recordset);

                List<History.Course> courses = new ArrayList<>();

                for(Map<String, Object> record : recordset) {
                    History.Course course = new History.Course();
                    course.setName(record.get("nomcurso")!=null?((String)record.get("nomcurso")):null);
                    course.setScore(record.get("notapromedio")!=null?((BigDecimal)record.get("notapromedio")).doubleValue():null);
                    course.setStatus(record.get("estadopromedio")!=null?((BigDecimal)record.get("estadopromedio")).doubleValue()==1:null);

                    courses.add(course);
                }

                cycle.setCourses(courses);

            }

            log.info("history: " + history);

            return history;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public List<String> getDebtorInstancesDealy() throws Exception {
        log.info("");
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("DOCENCIA").withCatalogName("API").withProcedureName("FINANZAS_DEUDASXDIA");

            SqlParameterSource in = new MapSqlParameterSource();

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("S_C_RECORDSET");
            log.info("Length of retrieved batches from database = "+recordset);

            List<String> instances = new ArrayList<>();

            for(Map<String, Object> record : recordset) {
                instances.add( record.get("INSTANCEID")!=null?(String)record.get("INSTANCEID"):null );
            }

            log.info("instances: " + instances);

            return instances;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
