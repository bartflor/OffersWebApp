package pl.bartflor.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
@Repository

public class UserDao {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public void createaccount(User user) {
		
		user.setEnabled(true);
		user.setAuthority("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		getSession().save(user);
	}

	public boolean exists(String username) {
		return getSession().get(User.class, username) != null;
	}

	public List<User> getUserList() {
		return getSession().createQuery("from User", User.class).list();
		
	}

}
