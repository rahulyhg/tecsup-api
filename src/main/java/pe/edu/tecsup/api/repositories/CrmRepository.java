package pe.edu.tecsup.api.repositories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.api.models.CrmDeuda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class CrmRepository {

    private static Logger log = Logger.getLogger(CrmRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public List<CrmDeuda>  deudas(String numdocumento) throws Exception {
        log.info("deudas: "+numdocumento);
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

            simpleJdbcCall.withSchemaName("COMERCIAL").withCatalogName("SCOB_PROCESO").withProcedureName("CONSULTA_DEUDA_CRM");

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("PNUMDOCUMENTO", numdocumento);

            Map<String, Object> out = simpleJdbcCall.execute(in);
            log.info(out);

            List<Map<String, Object>> recordset = (ArrayList<Map<String, Object>>) out.get("SREGISTROS");
            log.info("Length of retrieved batches from database = "+recordset);

            List<CrmDeuda> deudas = new ArrayList<>();

            for(Map<String, Object> record : recordset) {

                CrmDeuda deuda = new CrmDeuda();
                deuda.setProducto((String)record.get("PRODUCTO"));
                deuda.setMonto((String) record.get("TOTAL"));
                deuda.setFecha(new SimpleDateFormat("dd/MM/yyyy").format((Date) record.get("FECVENCIMIENTO")));

                deudas.add(deuda);
            }

            log.info("Deudas: " + deudas);

            return deudas;
        }catch (Exception e){
            log.error(e, e);
            throw e;
        }
    }

}
