package pl.bartflor.test.tests;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.bartflor.dao.Offer;
import pl.bartflor.dao.OffersDao;
import pl.bartflor.dao.User;
import pl.bartflor.service.OfferService;

@ContextConfiguration(locations = { "classpath:pl/bartflor/config/dao-context.xml",
		"classpath:pl/bartflor/config/security-context.xml", "classpath:pl/bartflor/test/config/dataSource.xml" })
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional
public class OffersDaoTest {

	@Autowired
	OffersDao offersDao;
	@Autowired
	DataSource jdbcDataSource;
	JdbcTemplate jdbc;

	@BeforeEach
	private void init() {
		jdbc = new JdbcTemplate(this.jdbcDataSource);
		jdbc.execute("DELETE FROM offer");
		jdbc.execute("DELETE FROM users");
	}

	@Test
	void createOffer_gestOfferObject_shouldcreateNewDatabaseRow() {
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(this.jdbc);
		User createdUser = new User("tusername1", "tname1", "tpassword", "t@mail.tt", "ROLE_USER", true);
		BeanPropertySqlParameterSource userParamSource = new BeanPropertySqlParameterSource(createdUser);
		njdbc.update(
				"INSERT INTO users(username, name, password, enabled, email) VALUES (:username, :name, :password, :enabled, :email)",
				userParamSource);

		Offer createdOffer = new Offer(createdUser, "test >>>> offer text");
//		List<Offer> ul = jdbc.query("Select * from offer", new RowMapper<Offer>() {
//
//			@Override
//			public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Offer u = new Offer();
//				u.setOffer_id(rs.getInt("offer_id"));
//				u.setText(rs.getNString("text"));
//				return u;
//			}
//		});
//		System.out.println(">>>>foreach");
//		for (Offer u : ul) {
//			System.out.println(u);
//		}
		offersDao.createOffer(createdOffer);
		@SuppressWarnings("unchecked")
		Offer recivedOffer = njdbc.queryForObject("SELECT * FROM offer", new HashMap(), new RowMapper<Offer>() {

			@Override
			public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Offer u = new Offer();
				u.setOffer_id(rs.getInt("offer_id"));
				u.setText(rs.getNString("text"));
				return u;
			}
		});
		System.out.println(createdOffer);
		System.out.println(recivedOffer);
		assertEquals("Created Offer object should equal to object recived from DB", createdOffer, recivedOffer);
	}

	
	@Test
	void getOffer_getsOffer_id_shouldReciveNewlyCreatedOffer() {
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(this.jdbc);
		User createdUser = new User("tusername1","tname1", "tpassword", "t@mail.tt", "ROLE_USER", true);
		BeanPropertySqlParameterSource userParamSource = new BeanPropertySqlParameterSource(createdUser);
		njdbc.update("INSERT INTO users(username, name, password, enabled, email, authority)"
				+ " VALUES (:username, :name, :password, :enabled, :email, :authority)", userParamSource);
		Offer createdOffer = new Offer(createdUser, "test new offer text");
		BeanPropertySqlParameterSource offerParamSource = new BeanPropertySqlParameterSource(createdOffer);
		njdbc.update("INSERT INTO offer(username, text) VALUES (:username, :text)", offerParamSource);
		int createdOfferId = jdbc.queryForObject("SELECT offer_id FROM offer", Integer.class);
		createdOffer.setOffer_id(createdOfferId);
		
		Offer recivedOffer = offersDao.getOffer(createdOfferId);
//		System.out.println(recivedOffer);
		assertEquals("Offer object recived from DB, should equal to new Offer object added to DB", createdOffer, recivedOffer);
	}
}
