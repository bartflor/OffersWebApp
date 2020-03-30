package pl.bartflor.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.bartflor.dao.User;
import pl.bartflor.dao.UserDao;
@Transactional
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:pl/bartflor/config/dao-context.xml",
		"classpath:pl/bartflor/config/security-context.xml", "classpath:pl/bartflor/test/config/dataSource.xml" })
@ExtendWith(SpringExtension.class)
public class UserDaoTest {

	@Autowired
	public DataSource jdbcDataSource;
	@Autowired
	public UserDao userDao;
	@Autowired
	public SessionFactory sessionFactory;
	public Session session;
	public JdbcTemplate jdbc;

	@BeforeEach
	public void init() throws SQLException {
		session = sessionFactory.getCurrentSession();
		jdbc = new JdbcTemplate(jdbcDataSource);
		jdbc.execute("DELETE FROM offer");
		jdbc.execute("DELETE FROM users");

	}

	@Test
	public void createaccount_getsUserObject_shouldCreateNewDatabaseRows() {
		User createdUser = new User("cusername", "tname", "tpassword", "t@mail.tt", "ROLE_USER", true);
		userDao.createaccount(createdUser);
//		session.save(createdUser);
//		session.flush();
//		session.close();
//		jdbc.update("INSERT INTO users(username, name, password, enabled, email) "
//				+ "VALUES ('cusername', 'tname','tpassword', '1', 'tuser@email.tt')");
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(jdbc);
//		User dbRecivedUser = njdbc.queryForObject("SELECT * FROM users", new MapSqlParameterSource("username", createdUser.getUsername()), new RowMapper<User>() {
//
//			@Override
//			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//				User user = new User();
//				user.setAuthority(rs.getNString("authority"));
//				user.setEmail(rs.getNString("email"));
//				user.setEnabled(rs.getBoolean("enabled"));
//				user.setName(rs.getNString("name"));
//				user.setUsername(rs.getNString("username"));
//				return null;
//			}
//		});

		User dbRecivedUser = session.createQuery("select u from User u where u.username like :username", User.class)
				.setParameter("username", "cusername").getSingleResult();
		System.out.println(createdUser);
		System.out.println(dbRecivedUser);
		assertEquals("User obj recived from DB should be equal to created one.", createdUser, dbRecivedUser);

	}
	
	@Test
	public void exist_getUserObject_shouldReturnTrueWhenUserIsInDataBase() {
		jdbc.update("INSERT INTO users(username, name, password, enabled, email) "
				+ "VALUES ('eusername', 'tuser','password', '1', 'tuser@email.tt')");
		assertTrue("Should return true if obj exists in DB", userDao.exists("eusername"));
	}

}
