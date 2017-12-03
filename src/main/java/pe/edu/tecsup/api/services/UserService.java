package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.Role;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.repositories.UserRepository;
import pe.edu.tecsup.api.utils.Constant;

import javax.transaction.Transactional;

@Service
public class UserService {

	private static Logger log = Logger.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userDao;

	public void autenticate(String username, String password) throws BadCredentialsException {
		log.info("calling autenticate: " + username + " - " + password);
		userDao.autenticate(username, password);
	}

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("calling loadUserByUsername: " + username);
        User user = userDao.loadUserByUsername(username);

        // Default role
        if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ADMINISTRADOR)))
            user.setRole(Constant.ROLE_SEVA_ADMINISTRADOR);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_PORTAL_SOPORTE)))
            user.setRole(Constant.ROLE_PORTAL_SOPORTE);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_SECRETARIA)))
            user.setRole(Constant.ROLE_SEVA_SECRETARIA);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DIRECTOR)))
            user.setRole(Constant.ROLE_SEVA_DIRECTOR);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_JEFE_DEPARTAMENTO)))
            user.setRole(Constant.ROLE_SEVA_JEFE_DEPARTAMENTO);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DOCENTE)))
            user.setRole(Constant.ROLE_SEVA_DOCENTE);
        else if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE)) || user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE_ANTIGUO)))
            user.setRole(Constant.ROLE_SEVA_ESTUDIANTE);

        return user;
    }

    public User loadUserByUsername(String username, String app) throws UsernameNotFoundException {
        log.info("calling loadUserByUsername: " + username + " - app:" + app);
        User user = userDao.loadUserByUsername(username);

        // Default role
        if(Constant.APP_TECSUP.equals(app)){
            if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE)) || user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE_ANTIGUO)))
                user.setRole(Constant.ROLE_SEVA_ESTUDIANTE);
            else
                throw new UsernameNotFoundException("No tiene el rol de estudiante");
        }else if(Constant.APP_TECSUP_DOCENTE.equals(app)){
            if(user.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DOCENTE)))
                user.setRole(Constant.ROLE_SEVA_DOCENTE);
            else
                throw new UsernameNotFoundException("No tiene el rol de docente");
        }else if(Constant.APP_TECSUP_SOPORTE.equals(app)){
            if(user.getAuthorities().contains(new Role(Constant.ROLE_PORTAL_SOPORTE)))
                user.setRole(Constant.ROLE_PORTAL_SOPORTE);
            else
                throw new UsernameNotFoundException("No tiene el rol de soporte");
        }else{
            throw new UsernameNotFoundException("Aplicación no registrada");
        }

        log.info("Default Role: " + user.getRole());

        return user;
    }

	public byte[] loadUserPicture(Integer id) throws Exception {
        log.info("calling loadUserPicture: " + id);
        return userDao.loadUserPicture(id);
    }

    public byte[] loadThumbedUserPicture(Integer id) throws Exception {
        log.info("calling loadThumbedUserPicture: " + id);
        return userDao.loadThumbedUserPicture(id);
    }

    @Transactional
    public void saveAccess(String app, Integer userid, String instanceid, String token, String deviceid, String manufacturer, String model, String device, String kernel, String version, Integer sdk) throws Exception {
		log.info("calling saveAccess: " + userid + " - instanceid:" + instanceid + " - token:" + token);
		userDao.saveAccess(app, userid, instanceid, token, deviceid, manufacturer, model, device, kernel, version, sdk);
    }

    public void destroyToken(String token) throws Exception {
        log.info("calling destroyToken: " + token);
        userDao.destroyToken(token);
    }

    public void updateToken(String token) {
        log.info("calling updateToken: " + token);
        userDao.updateToken(token);
    }

    public void validateAdmin(String email) throws Exception {
        log.info("calling validateAdmin: " + email);
        userDao.validateAdmin(email);
    }

}
