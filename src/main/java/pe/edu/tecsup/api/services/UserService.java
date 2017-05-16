package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.repositories.UserRepository;

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

}
