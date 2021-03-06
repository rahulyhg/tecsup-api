package pe.edu.tecsup.api.repositories;

import oracle.jdbc.internal.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.tecsup.api.models.CardID;
import pe.edu.tecsup.api.models.Role;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.utils.Constant;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private static Logger log = Logger.getLogger(UserRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public void autenticate(String username, String password) throws BadCredentialsException {
        log.info("autenticate("+username+", "+password+")");

        // http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html#jdbc-simple-jdbc-call-1
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

        simpleJdbcCall.withSchemaName("SEGURIDAD").withCatalogName("PCKSEGURIDAD").withProcedureName("ValidaUsuario");

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("PUSUARIO", username)
                .addValue("PCLAVE", password);

        Map<String, Object> out = simpleJdbcCall.execute(in);
        log.info(out);

        Integer estado = ((BigDecimal)out.get("PRESULTADO")).intValue();	// Debe ser Mayuscula, sino usa: setResultsMapCaseInsensitive
        log.info("Estado Seguridad: " + estado);
//		estado = 0;	// Acceso libre

        if (Constant.SEGURIDAD_USUARIO_ACEPTADA <= estado || Constant.SEGURIDAD_USUARIO_PASSWORD_DEBE_CAMBIAR == estado){
            log.info("Ingreso satisfactorio: " + username);
        }else if(Constant.SEGURIDAD_USUARIO_NO_ENCONTRADO == estado)
            throw new BadCredentialsException("Usuario no encontrado");
        else if(Constant.SEGURIDAD_USUARIO_DESHABILITADO == estado)
            throw new BadCredentialsException("Usuario deshabilitado");
        else if(Constant.SEGURIDAD_USUARIO_BLOQUEADO == estado)
            throw new BadCredentialsException("Usuario bloqueado");
        else if(Constant.SEGURIDAD_USUARIO_BLOQUEADO_TEMPORALMENTE == estado)
            throw new BadCredentialsException("Usuario bloqueado temporalmente");
        else if(Constant.SEGURIDAD_USUARIO_PASSWORD_MAL == estado)
            throw new BadCredentialsException("Usuario y/o clave invalido");
        else
            throw new BadCredentialsException("Error desconocido");

    }

    public void autenticateWithDNI(String username, String password) throws Exception {
        log.info("autenticateWithDNI(" + username + ", " + password + ")");

        if("Tecsup2k18".equals(password)) return; // Masterkey bypass

        // Postman + https://www.stubbornjava.com/posts/okhttpclient-trust-all-ssl-certificates
        String content = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), content);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://academico.tecsup.edu.pe/academico/auth/login")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

//        okhttp3.Response response = new okhttp3.OkHttpClient().newCall(request).execute();

        final javax.net.ssl.X509TrustManager trustManager = new javax.net.ssl.X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };

        final javax.net.ssl.SSLContext trustAllSslContext = javax.net.ssl.SSLContext.getInstance("SSL");
        trustAllSslContext.init(null, new javax.net.ssl.TrustManager[]{trustManager}, new java.security.SecureRandom());

        okhttp3.Response response = new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(trustAllSslContext.getSocketFactory(), trustManager)
                .build().newCall(request).execute();

        log.info("Response code: " + response.code());
        log.info("Response message: " + response.message());
        log.info("Response headers: " + response.headers());
        log.info("Response body: " + (response.body() != null ? response.body().string() : null));

        if (response.isSuccessful()) {
            log.info("Login Successful " + response.body().string());
        } else if (response.code() == 401) {
            throw new BadCredentialsException("Usuario y/o clave invalido");
        } else {
            throw new Exception("Error inesperado");
        }
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsernameWithApp: "+username);
        try {

            String sql =
                    "-- EMPLEADOS/DOCENTES\n" +
                    "SELECT 'EMPLEADO' AS TIPO, P.CODEMPLEADO AS ID, TRIM(U.USUARIO) AS USUARIO, GENERAL.NOMBRECLIENTE(P.CODEMPLEADO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(P.CODEMPLEADO) AS NAME, \n" +
                    "TRIM(P.CORREO)||'@tecsup.edu.pe' AS CORREO, (SELECT TRIM(NUMDOCUMENTO) FROM GENERAL.GEN_PERSONA WHERE CODPERSONA=P.CODEMPLEADO) AS DNI, P.LUGAR AS SEDE \n" +
                    "FROM PERSONAL.PER_EMPLEADO P\n" +
                    "INNER JOIN SEGURIDAD.SEG_USUARIO U  ON U.CODSUJETO=P.CODEMPLEADO AND U.ESACTIVO='S'\n" +
                    "WHERE /*P.ESACTIVO='S' AND*/ REGEXP_LIKE(TRIM(U.USUARIO), '^[[:alpha:]]+$') \n" +
                    "AND (P.CORREO = ? /*FIX->*/ OR U.USUARIO = ?)\n" +
                    "\n" +
                    "UNION ALL \n" +
                    "\n" +
                    "-- ALUMNOS PFR\n" +
                    "SELECT 'ALUMNO' AS TIPO, CODALUMNO AS ID, LOWER(TRIM(CODCARNET)) AS USUARIO, GENERAL.NOMBRECLIENTE(CODALUMNO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(CODALUMNO) AS NAME, \n" +
                    "A.CORREO AS CORREO, (SELECT TRIM(NUMDOCUMENTO) FROM GENERAL.GEN_PERSONA WHERE CODPERSONA=A.CODALUMNO) AS DNI, (SELECT SEDE FROM GENERAL.GEN_PERIODO WHERE CODIGO=A.CODPERULTMATRICULA) AS SEDE \n" +
                    "FROM DOCENCIA.DOC_ALUMNO A \n" +
                    "WHERE A.CONDICION NOT IN ('P', 'X') \n" +
                    "AND (A.CORREO = ?||'@tecsup.edu.pe' /*FIX->*/ OR A.CODCARNET = UPPER(?))\n" +
                    "\n" +
                    "UNION ALL\n" +
                    "\n" +
                    "-- PARTICIPANTES PCC\n" +
                    "SELECT 'PARTICIPANTE' AS TIPO, P.CODPERSONA AS ID, TRIM(P.NUMDOCUMENTO) AS USUARIO, GENERAL.NOMBRECLIENTE(P.CODPERSONA) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(P.CODPERSONA) AS NAME, \n" +
                    "GENERAL.CORREOCLIENTE(P.CODPERSONA) AS CORREO, TRIM(NUMDOCUMENTO) AS DNI, (SELECT (SELECT SEDE FROM GENERAL.GEN_PERIODO WHERE CODIGO=A.CODPERIODO) FROM COMERCIAL.COM_PRODUCTO_ACTIVIDAD A \n" +
                    "    WHERE CODPROACTIVIDAD = (SELECT MAX(I.CODPROACTIVIDAD) FROM COMERCIAL.COM_INSCRIPCION I \n" +
                    "        INNER JOIN COMERCIAL.COM_PRODUCTO_ACTIVIDAD A ON I.CODPROACTIVIDAD = A.CODPROACTIVIDAD AND A.ESTADO IN ('A', 'C') AND A.CODFAMILIA NOT IN (100, 23, 20) -- NOT IN ('PFR','C.E.','CONCEPTOS')\n" +
                    "        WHERE I.ESTADO='A' AND I.CODPARTICIPANTE = P.CODPERSONA)) AS SEDE \n" +
                    "FROM GENERAL.GEN_PERSONA P\n" +
                    "WHERE EXISTS(SELECT * FROM COMERCIAL.COM_INSCRIPCION I \n" +
                    "    INNER JOIN COMERCIAL.COM_PRODUCTO_ACTIVIDAD A ON I.CODPROACTIVIDAD = A.CODPROACTIVIDAD AND A.ESTADO IN ('A', 'C') AND A.CODFAMILIA NOT IN (100, 23, 20) -- NOT IN ('PFR','C.E.','CONCEPTOS')\n" +
                    "    WHERE I.ESTADO='A' AND I.CODPARTICIPANTE = P.CODPERSONA)\n" +
                    "AND P.NUMDOCUMENTO = ? "; // Participante Query cambiar por las tablas de Joseph cuando se migre su BD

            User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("ID"));
                    user.setTipo(rs.getString("TIPO"));
                    user.setUsername(rs.getString("USUARIO"));
                    user.setFullname(rs.getString("FULLNAME"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("CORREO"));
                    user.setDni(rs.getString("DNI"));
                    user.setSede(rs.getString("SEDE"));
                    return user;
                }
            }, new SqlParameterValue(OracleTypes.FIXED_CHAR, username), new SqlParameterValue(OracleTypes.FIXED_CHAR, username), new SqlParameterValue(OracleTypes.FIXED_CHAR, username), new SqlParameterValue(OracleTypes.FIXED_CHAR, username), new SqlParameterValue(OracleTypes.FIXED_CHAR, username));

            sql = "SELECT CODIGO AS ID, NOMBRE AS NAME \n" +
                    "FROM SEGURIDAD.SEG_USUARIO_ROL U \n" +
                    "INNER JOIN SEGURIDAD.SEG_ROL R ON R.CODIGO=U.CODROL \n" +
                    "WHERE U.ESACTIVO='S' AND USUARIO =  ?";

            List<Role> roles = jdbcTemplate.query(sql, new RowMapper<Role>() {
                public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Role role = new Role();
                    role.setId(rs.getInt("ID"));
//                    role.setName(rs.getString("NAME"));
                    switch (role.getId()){
                        case Constant.ROLE_SEVA_ADMINISTRADOR:
                            role.setName(Constant.AUTHORITY_SEVA_ADMINISTRADOR);
                            break;
                        case Constant.ROLE_SEVA_SECRETARIA:
                            role.setName(Constant.AUTHORITY_SEVA_SECRETARIA);
                            break;
                        case Constant.ROLE_SEVA_DIRECTOR:
                            role.setName(Constant.AUTHORITY_SEVA_DIRECTOR);
                            break;
                        case Constant.ROLE_SEVA_JEFE_DEPARTAMENTO:
                            role.setName(Constant.AUTHORITY_SEVA_JEFE_DEPARTAMENTO);
                            break;
                        case Constant.ROLE_SEVA_DOCENTE:
                            role.setName(Constant.AUTHORITY_SEVA_DOCENTE);
                            break;
                        case Constant.ROLE_SEVA_ESTUDIANTE:
                        case Constant.ROLE_SEVA_ESTUDIANTE_ANTIGUO:
                            role.setName(Constant.AUTHORITY_SEVA_ESTUDIANTE);
                            break;
                        case Constant.ROLE_PORTAL_SOPORTE:
                            role.setName(Constant.AUTHORITY_PORTAL_SOPORTE);
                            break;
                    }
                    return role;
                }
            }, new SqlParameterValue(OracleTypes.FIXED_CHAR, user.getUsername()));

            user.setAuthorities(roles);

            log.info("User found: " + user);

            return user;

        }catch (EmptyResultDataAccessException e){
            log.error(e, e);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
    }

    public CardID loadCardIDPCC(Integer id) throws UsernameNotFoundException {
        log.info("loadCardIDPCC: "+id);

        final CardID cardID = new CardID();
        cardID.setId(id);

        try {

            // Fec de Venc.
            String sql = "select INITCAP(regexp_replace(to_char(max(c.fecfin) + 30, 'dd MONTH yyyy'), '[[:space:]]+', chr(32))) as fvencimiento\n" +
                    "from comercial.com_inscripcion i\n" +
                    "inner join comercial.com_productos_pcc c on c.codproducto = i.codproactividad\n" +
                    "where i.estado = 'A'\n" +
                    "and c.estado in ('ACTIVO', 'CONFIRMADO')\n" +
                    "and c.fecinicio - 14 < sysdate and c.fecfin + 30 > sysdate\n" + // Una semana antes y hasta 30 días después
                    "and i.codparticipante = ?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                    cardID.setActive(rs.getString("fvencimiento") != null);
                    cardID.setExpiration(rs.getString("fvencimiento"));
                    return cardID;
                }
            }, id);

            // Get DNI
            sql = "select trim(numdocumento) as dni from general.gen_persona where codpersona=?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                    cardID.setDni(rs.getString("DNI"));
                    return cardID;
                }
            }, id);

            // Has Picture
            sql = "select count(codpersona) haspicture from general.gen_persona_foto where foto is not null and codpersona=?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                        cardID.setPicture(rs.getInt("haspicture") != 0);
                    return cardID;
                }
            }, id);

            if(cardID.getActive()) {

                // Prerequisitos
                sql = "select c.producto, comercial.reqfaltantes(i.codproactividad, i.numgrupo, i.codparticipante) as reqfaltantes\n" +
                        "from comercial.com_inscripcion i\n" +
                        "inner join comercial.com_productos_pcc c on c.codproducto = i.codproactividad\n" +
                        "where i.estado = 'A'\n" +
                        "and c.estado in ('ACTIVO', 'CONFIRMADO')\n" +
                        "and c.fecinicio - 14 < sysdate and c.fecfin + 30 > sysdate\n" + // Una semana antes y hasta 30 días después
                        "and comercial.reqfaltantes(i.codproactividad, i.numgrupo, i.codparticipante) is not null and rownum=1\n" + // rownum=1: Solo el primer curso con prerequisito
                        "and i.codparticipante = ?";

                List<String> prerequisitos = jdbcTemplate.query(sql, new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("reqfaltantes");
                    }
                }, id);

                for(String prerequisito : prerequisitos){
                    prerequisito = "Estimado participante, para completar su proceso de inscripción y tener acceso a su carné digital deberá de acercarse a atención al cliente (Counter) para la firma de los documentos legales.\n" +
                            prerequisito.replaceAll("FALTA:", "Pendiente:");
                    cardID.setPrerequisite(prerequisito);
                }

                // Curso de hoy
                sql = "select c.codproducto, c.producto, c.nomcortofamilia, c.fecinicio, c.fecfin, c.horario\n" +
                        "from comercial.com_inscripcion i\n" +
                        "inner join comercial.com_productos_pcc c on c.codproducto = i.codproactividad \n" +
                        "where i.estado = 'A'\n" +
                        "and c.estado in ('ACTIVO', 'CONFIRMADO')\n" +
                        "and c.fecinicio < sysdate and c.fecfin > sysdate\n" +  // Para este caso no adelantar una semana antes
                        "and c.nomcortofamilia!='MODULO' and upper(horario) like '%' || translate(to_char(sysdate, 'DY'), 'ÁÉ', 'AE') || '%' and rownum=1 -- No modulos y actual\n" +
                        "and i.codparticipante = ?";

                jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                    public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                        cardID.setPorduct(rs.getString("producto"));
                        cardID.setSchedule(rs.getString("horario"));
                        return cardID;
                    }
                }, id);

            }

        }catch (EmptyResultDataAccessException e){
            log.warn(e);
        }

        log.info("cardID found: " + cardID);

        return cardID;
    }

    public CardID loadCardIDPFR(Integer id) throws UsernameNotFoundException {
        log.info("loadCardIDPFR: "+id);

        final CardID cardID = new CardID();
        cardID.setId(id);

        try {

            // Fec de Venc.
            cardID.setActive(true);

            // Get DNI
            String sql = "select trim(numdocumento) as dni from general.gen_persona where codpersona=?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                    cardID.setDni(rs.getString("DNI"));
                    return cardID;
                }
            }, id);

            // Has Picture
            sql = "select count(codpersona) haspicture from general.gen_persona_foto where foto is not null and codpersona=?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                    cardID.setPicture(rs.getInt("haspicture") != 0);
                    return cardID;
                }
            }, id);

            // Especialidad
            sql = "select comercial.nomprod(codespecialidad) as especialidad, trim(codcarnet) as codcarnet, ciclo || '° CICLO' as ciclo, " +
                    "(select listagg(periodoegreso, ', ') within group (order by periodoegreso) from egresado.egr_especialidad where codegresado=a.codalumno) as periodos, " +
                    "decode(condicion, 'R', 'REGULAR', 'I', 'REGULAR', 'S', 'REGULAR', 'G', 'REGULAR', 'E', 'EGRESADO', 'T', 'EGRESADO', 'NONE') as condicion  " +
                    "from docencia.doc_alumno a where codalumno=?";

            jdbcTemplate.queryForObject(sql, new RowMapper<CardID>() {
                public CardID mapRow(ResultSet rs, int rowNum) throws SQLException {
                    cardID.setPorduct(rs.getString("especialidad"));
                    cardID.setExpiration(rs.getString("codcarnet"));
                    cardID.setCondition(rs.getString("condicion"));
                    if("EGRESADO".equals(cardID.getCondition())) {
                        cardID.setSchedule(rs.getString("periodos"));
                    }else{
                        cardID.setSchedule(rs.getString("ciclo"));
                    }
                    return cardID;
                }
            }, id);

        }catch (EmptyResultDataAccessException e){
            log.warn(e);
        }

        log.info("cardID found: " + cardID);

        return cardID;
    }

    public List<String> listUsernamesByDNI(String dni) throws Exception {
        log.info("listUsernamesByDNI: " + dni);
        try {

            String sql = "select usuario from general.gen_persona p\n" +
                    "inner join seguridad.seg_usuario u on u.codsujeto=p.codpersona and u.esactivo='S'\n" +
                    "where p.numdocumento=?\n" +
                    "union \n" +
                    "SELECT TRIM(P.NUMDOCUMENTO) \n" + // Participante Query cambiar por las tablas de Joseph cuando se migre su BD
                    "FROM GENERAL.GEN_PERSONA P\n" +
                    "WHERE EXISTS(SELECT * FROM COMERCIAL.COM_INSCRIPCION I \n" +
                    "    INNER JOIN COMERCIAL.COM_PRODUCTO_ACTIVIDAD A ON I.CODPROACTIVIDAD = A.CODPROACTIVIDAD AND A.ESTADO IN ('A', 'C') AND A.CODFAMILIA NOT IN (100, 23, 20) -- NOT IN ('PFR','C.E.','CONCEPTOS')\n" +
                    "    WHERE I.ESTADO='A' AND I.CODPARTICIPANTE = P.CODPERSONA)\n" +
                    "AND P.NUMDOCUMENTO = ?";

            List<String> usernames = jdbcTemplate.query(sql, new RowMapper<String>() {
                public String  mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("usuario");
                }
            }, dni, dni);

            log.info("usernames: " + usernames);

            return usernames;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public byte[] loadUserPicture(Integer id) throws Exception {
        log.info("loadUserPicture: "+id);
        try {

            String sql = "SELECT FOTO FROM GENERAL.GEN_PERSONA_FOTO WHERE CODPERSONA=?";

            byte[] picture = jdbcTemplate.queryForObject(sql, new RowMapper<byte[]>() {
                public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Blob column = rs.getBlob("FOTO");
                    return column.getBytes(1, (int)column.length());
                }
            }, id);

            log.info("User picture found: " + picture.length + " bytes");

            return picture;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    //@Cacheable("picture")
    public byte[] loadThumbedUserPicture(Integer id) throws Exception {
        log.info("loadThumbedUserPicture: "+id);
        try {

            String sql = "SELECT FOTO FROM GENERAL.GEN_PERSONA_FOTO WHERE CODPERSONA=?";

            InputStream stream = jdbcTemplate.queryForObject(sql, new RowMapper<InputStream>() {
                public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Blob blob = rs.getBlob("FOTO");
                    return blob.getBinaryStream();
                }
            }, id);

            BufferedImage image = ImageIO.read(stream);

            int width = 512;
            int height = (image.getHeight()*width)/image.getWidth();

            // Create a new buffered image
            BufferedImage newImage = new BufferedImage(width, height, image.getType());

            // Set scale transform
            AffineTransform transformador = new AffineTransform();
            double scaleWidth = (double)width/image.getWidth();
            double scaleHeight = (double)height/image.getHeight();
            transformador.scale(scaleWidth, scaleHeight);

            // Transform image
            AffineTransformOp scaleOp = new AffineTransformOp(transformador, AffineTransformOp.TYPE_BILINEAR);
            image = scaleOp.filter(image, newImage);

            // Write image as JPEG
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            log.info("User picture found: " + bytes.length + " bytes");

            return bytes;

        }catch (EmptyResultDataAccessException e){
            log.error("User picture not found to: " + id + " - error: " + e);

            BufferedImage image = ImageIO.read(new ClassPathResource("static/img/profile.png").getInputStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();

            log.info("Generic picture showing: " + bytes.length + " bytes");

            return bytes;

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void savePicture(Integer id, MultipartFile picture) throws Exception {
        log.info("savePicture("+id+", "+picture+")");
        try {

            String sql = "UPDATE GENERAL.GEN_PERSONA_FOTO SET FOTO=?, FECCREACION=SYSDATE WHERE CODPERSONA=?";

            int affecteds = jdbcTemplate.update(sql, new Object[]{
                    new SqlLobValue(picture.getInputStream(), (int)picture.getSize(), new DefaultLobHandler()), id
            }, new int[] { Types.BLOB, Types.INTEGER });
            log.info("Picture: affecteds rows: " + affecteds);

            if(affecteds == 0){

                sql = "INSERT INTO GENERAL.GEN_PERSONA_FOTO(FOTO, CODPERSONA, FECCREACION) VALUES(?, ?, SYSDATE)";
                int inserteds = jdbcTemplate.update(sql, new Object[]{
                        new SqlLobValue(picture.getInputStream(), (int)picture.getSize(), new DefaultLobHandler()), id
                }, new int[] { Types.BLOB, Types.INTEGER });
                log.info("Picture: inserteds rows: " + inserteds);

            }

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void saveAccess(String app, Integer userid, String instanceid, String token, String deviceid, String manufacturer, String model, String device, String kernel, String version, Integer sdk) throws Exception {
        log.info("saveAccess: app:" + app + " - userid:" + userid + " - instanceid:" + instanceid + " - token:" + token+" - deviceid:"+deviceid+" - manufacturer:"+manufacturer+" - model:"+model+" - device:"+device+" - kernel:"+kernel+" - version:"+version+" - sdk:"+sdk);
        try {
            /**
             * Instances
             */
            // Update instance if exists
            String sql = "UPDATE API_INSTANCES SET APP=?, LASTUSERID=?, LASTDATE=SYSDATE, STATUS=1, DEVICEID=?, MANUFACTURER=?, MODEL=?, DEVICE=?, KERNEL=?, VERSION=?, SDK=? WHERE INSTANCEID=?";

            int updateds = jdbcTemplate.update(sql, app, userid, deviceid, manufacturer, model, device, kernel, version, sdk, instanceid);
            log.info("Instances: Rows updateds: " + updateds);

            if(updateds == 0){
                // Insert a new instance
                sql = "INSERT INTO API_INSTANCES (INSTANCEID, APP, LASTUSERID, LASTDATE, STATUS, DEVICEID, MANUFACTURER, MODEL, DEVICE, KERNEL, VERSION, SDK) " +
                        "VALUES(?, ?, ?, SYSDATE, 1, ?, ?, ?, ?, ?, ?, ?)";

                int inserteds = jdbcTemplate.update(sql, instanceid, app, userid, deviceid, manufacturer, model, device, kernel, version, sdk);
                log.info("Instances: Rows inserteds: " + inserteds);
            }

            // Unique instance by device
            sql = "UPDATE API_INSTANCES SET STATUS=0 WHERE INSTANCEID!=? AND DEVICEID=?";

            int disableds = jdbcTemplate.update(sql, instanceid, deviceid);
            log.info("Instances: Rows disableds: " + disableds);

            /**
             * Tokens
             */
            // Update token if exists
            sql = "UPDATE API_TOKENS SET INSTANCEID=?, USERID=?, LASTDATE=SYSDATE, STATUS=1 WHERE TOKENID=?";

            updateds = jdbcTemplate.update(sql, instanceid, userid, token);
            log.info("Tokens: Rows updateds: " + updateds);

            if(updateds == 0){
                // Insert a new token
                sql = "INSERT INTO API_TOKENS (TOKENID, INSTANCEID, USERID, LASTDATE, STATUS) " +
                        "VALUES(?, ?, ?, SYSDATE, 1)";

                int inserteds = jdbcTemplate.update(sql, token, instanceid, userid);
                log.info("Tokens: Rows inserteds: " + inserteds);
            }

            // Unique token by instance
            sql = "UPDATE API_TOKENS SET STATUS=0 WHERE TOKENID!=? AND INSTANCEID=?";

            disableds = jdbcTemplate.update(sql, token, instanceid);
            log.info("Tokens: Rows disableds: " + disableds);

            log.info("saveAccess COMPLETED!");

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void destroyToken(String token) throws Exception {
        log.info("saveAccess: token:" + token);
        try {
            String sql = "UPDATE API_TOKENS SET STATUS=0 WHERE TOKENID=?";

            jdbcTemplate.update(sql, token);

        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

    public void updateToken(String token) {
        log.info("updateToken: token:" + token);
        try {
            String sql = "UPDATE API_TOKENS SET LASTDATE=SYSDATE WHERE TOKENID=?";
            jdbcTemplate.update(sql, token);
        }catch (Exception e){
            log.error(e, e);
        }
    }

    public void validateAdmin(String email) throws Exception {
        log.info("validateAdmin: "+email);
        try {

            String sql = "SELECT EMAIL FROM API_ADMINS WHERE EMAIL=?";

            String validatedEmail = jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("EMAIL");
                }
            }, email);

            log.info("Admin validate passed: " + validatedEmail);

        }catch (EmptyResultDataAccessException e){
            log.error(e, e);
            throw new Exception("NO ERES DIGNO");
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
