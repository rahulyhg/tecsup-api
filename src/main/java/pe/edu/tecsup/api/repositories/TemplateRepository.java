package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TemplateRepository {

	protected static Logger log = Logger.getLogger(TemplateRepository.class);
	
	//@Autowired
	//private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplateWeb;

	public List<String> listar(){
		log.info("calling listar: ");
		
		String sql = "select general.nombrecliente(codempleado) as nombres from personal.per_empleado where codarea=328 and esactivo='S' order by 1";
		
		List<String> lista = jdbcTemplateWeb.query(sql, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException{
            	String nombre = new String(rs.getString("nombres"));
                return nombre;
            }
        });
		
		log.info(lista);
		
		return lista;
	}

}
