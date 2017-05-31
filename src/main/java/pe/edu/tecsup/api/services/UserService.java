package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.repositories.UserRepository;

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
		return userDao.loadUserByUsername(username); 
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
    public void saveAccess(Integer userid, String instanceid, String token, String deviceid, String manufacturer, String model, String device, String kernel, String version, Integer sdk) throws Exception {
		log.info("calling saveAccess: " + userid + " - instanceid:" + instanceid + " - token:" + token);
		userDao.saveAccess(userid, instanceid, token, deviceid, manufacturer, model, device, kernel, version, sdk);
    }

    public void destroyToken(String token) throws Exception {
        log.info("calling destroyToken: " + token);
        userDao.destroyToken(token);
    }

    public void updateToken(String token) throws Exception {
        log.info("calling updateToken: " + token);
        userDao.updateToken(token);
    }

}
