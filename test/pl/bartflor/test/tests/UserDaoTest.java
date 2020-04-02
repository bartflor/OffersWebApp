package pl.bartflor.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import pl.bartflor.dao.User;
import pl.bartflor.dao.UserDao;

@Transactional
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:pl/bartflor/config/security-context.xml",
		"classpath:pl/bartflor/test/config/dataSource.xml" })
@ExtendWith(SpringExtension.class)
@Rollback(false)
public class UserDaoTest {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private SessionFactory sessionFactory;
	private Session session;
	private JdbcTemplate jdbc;
	private NamedParameterJdbcTemplate namedJdbc;

	@BeforeEach
	public void init() throws SQLException {
		session = sessionFactory.getCurrentSession();
		jdbc = new JdbcTemplate(dataSource);
		jdbc.execute("DELETE FROM offer");
		jdbc.execute("DELETE FROM users");
		namedJdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	@Test
	public void createaccount_getsUserObject_shouldCreateNewDatabaseRows() {
		User createdUser = new User("TESTusername", "tname", "tpassword", "t@mail.tt", "ROLE_USER", true);
		userDao.createaccount(createdUser);
		session.flush();
		User dbRecivedUser = namedJdbc.queryForObject("SELECT * FROM users WHERE username=:username",
				new MapSqlParameterSource("username", createdUser.getUsername()), new UserRowMapper());

		assertEquals("User obj recived from DB should be equal to created one.", createdUser, dbRecivedUser);

	}

	@Test
	public void exist_getUserObject_shouldReturnTrueWhenUserIsInDatabase() {
		jdbc.update("INSERT INTO users(username, name, password, enabled, email) "
				+ "VALUES ('TESTusername', 'tuser','password', '1', 'tuser@email.tt')");
		assertTrue("Should return true if obj exists in DB", userDao.exists("TESTusername"));
	}

	@Test
	public void getUserList_shouldReturnListOfUserInDatabase() {
		User createdUser1 = new User("TESTusername1", "tname1", "tpassword", "t@mail.tt", "ROLE_USER", true);
		User createdUser2 = new User("TESTusername2", "tname2", "tpassword", "t@mail.tt", "ROLE_USER", true);
		User createdUser3 = new User("TESTusername3", "tname3", "tpassword", "t@mail.tt", "ROLE_USER", true);
		User createdUser4 = new User("TESTusername4", "tname4", "tpassword", "t@mail.tt", "ROLE_USER", true);
		List<User> insertedUserList = Arrays.asList(createdUser1, createdUser2, createdUser3, createdUser4);
		namedJdbc.update(
				"INSERT INTO users(username, name, password, enabled, email, authority) "
						+ "VALUES (:username, :name, :password, :enabled, :email, :authority)",
				new BeanPropertySqlParameterSource(createdUser1));
		namedJdbc.update(
				"INSERT INTO users(username, name, password, enabled, email, authority) "
						+ "VALUES (:username, :name, :password, :enabled, :email, :authority)",
				new BeanPropertySqlParameterSource(createdUser2));
		namedJdbc.update(
				"INSERT INTO users(username, name, password, enabled, email, authority) "
						+ "VALUES (:username, :name, :password, :enabled, :email, :authority)",
				new BeanPropertySqlParameterSource(createdUser3));
		namedJdbc.update(
				"INSERT INTO users(username, name, password, enabled, email, authority) "
						+ "VALUES (:username, :name, :password, :enabled, :email, :authority)",
				new BeanPropertySqlParameterSource(createdUser4));
		int numberOfInserts = jdbc.queryForObject("SELECT COUNT(username) FROM users", Integer.class);
		List<User> userList = userDao.getUserList();
		System.out.println(userList);
		System.out.println(insertedUserList);

		assertEquals("Recived list size should be equal to nuber of inserted users", numberOfInserts, userList.size());
		assertTrue("Should contain all inserted object", userList.containsAll(insertedUserList));
	}

}
