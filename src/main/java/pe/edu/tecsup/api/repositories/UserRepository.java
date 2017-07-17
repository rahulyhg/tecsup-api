package pe.edu.tecsup.api.repositories;

import oracle.jdbc.internal.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
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

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+username);
        try {

            String sql = "SELECT U.CODSUJETO AS ID, TRIM(U.USUARIO) AS USUARIO, GENERAL.NOMBRECLIENTE(U.CODSUJETO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(U.CODSUJETO) AS NAME, TRIM(P.CORREO)||'@tecsup.edu.pe' AS CORREO, P.LUGAR AS SEDE \n" +
                        "FROM SEGURIDAD.SEG_USUARIO U \n" +
                        "INNER JOIN PERSONAL.PER_EMPLEADO P ON P.CODEMPLEADO=U.CODSUJETO \n" +
                        "WHERE EXISTS(SELECT * FROM SEGURIDAD.SEG_USUARIO_ROL WHERE USUARIO=U.USUARIO /*AND CODROL IN (132, 134, 143, 146)*/) \n" + // Admin, Secdoc, Docente, Jefe
                        "AND U.USUARIO = ? \n" +
                        "UNION ALL \n" +
                        "SELECT CODALUMNO AS ID, TRIM(CODCARNET) AS USUARIO, GENERAL.NOMBRECLIENTE(CODALUMNO) AS FULLNAME, GENERAL.NOMBRECORTOCLIENTE(CODALUMNO) AS NAME, A.CORREO, (SELECT SEDE FROM GENERAL.GEN_PERIODO WHERE CODIGO=A.CODPERULTMATRICULA) AS SEDE \n" +
                        "FROM DOCENCIA.DOC_ALUMNO A \n" +
                        "WHERE A.CONDICION NOT IN ('P', 'X') \n" +
                        "AND A.CORREO = ?||'@tecsup.edu.pe'    OR CODCARNET = ?"; // OR CODCARNET = ? hack!

            User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setUsername(rs.getString("USUARIO"));
                    user.setId(rs.getInt("ID"));
                    user.setFullname(rs.getString("FULLNAME"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("CORREO"));
                    user.setSede(rs.getString("SEDE"));
                    return user;
                }
            }, new SqlParameterValue(OracleTypes.FIXED_CHAR, username), username,    new SqlParameterValue(OracleTypes.FIXED_CHAR, username));

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
                    }
                    return role;
                }
            }, new SqlParameterValue(OracleTypes.FIXED_CHAR, username));

            user.setAuthorities(roles);

            // Default role
            if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DOCENTE)))
                user.setRole(Constant.ROLE_SEVA_DOCENTE);
            else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE)) || user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE_ANTIGUO)))
                user.setRole(Constant.ROLE_SEVA_ESTUDIANTE);

            log.info("User found: " + user);

            return user;

        }catch (EmptyResultDataAccessException e){
            log.error(e, e);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
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

    @Cacheable("picture")
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

            int width = 256;
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

    public void saveAccess(Integer userid, String instanceid, String token, String deviceid, String manufacturer, String model, String device, String kernel, String version, Integer sdk) throws Exception {
        log.info("saveAccess: userid:" + userid + " - instanceid:" + instanceid + " - token:" + token+" - deviceid:"+deviceid+" - manufacturer:"+manufacturer+" - model:"+model+" - device:"+device+" - kernel:"+kernel+" - version:"+version+" - sdk:"+sdk);
        try {
            /**
             * Instances
             */
            // Update instance if exists
            String sql = "UPDATE API_INSTANCES SET APP=?, LASTUSERID=?, LASTDATE=SYSDATE, STATUS=1, DEVICEID=?, MANUFACTURER=?, MODEL=?, DEVICE=?, KERNEL=?, VERSION=?, SDK=? WHERE INSTANCEID=?";

            int updateds = jdbcTemplate.update(sql, "TECSUP", userid, deviceid, manufacturer, model, device, kernel, version, sdk, instanceid);
            log.info("Instances: Rows updateds: " + updateds);

            if(updateds == 0){
                // Insert a new instance
                sql = "INSERT INTO API_INSTANCES (INSTANCEID, APP, LASTUSERID, LASTDATE, STATUS, DEVICEID, MANUFACTURER, MODEL, DEVICE, KERNEL, VERSION, SDK) " +
                        "VALUES(?, ?, ?, SYSDATE, 1, ?, ?, ?, ?, ?, ?, ?)";

                int inserteds = jdbcTemplate.update(sql, instanceid, "TECSUP", userid, deviceid, manufacturer, model, device, kernel, version, sdk);
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

}
