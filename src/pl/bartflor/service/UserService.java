package pl.bartflor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartflor.dao.User;
import pl.bartflor.dao.UserDao;
@Service
@Transactional

public class UserService {
	@Autowired
	private UserDao userdao;
	public void createaccount(User user) {
		userdao.createaccount(user);

	}
	public boolean existsUserWithUsername(String username) {
		return userdao.exists(username);
	}
	@Secured("ROLE_ADMIN")
	public List<User> getAllUsers() {
		return userdao.getUserList();
	}

}
