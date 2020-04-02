package pl.bartflor.test.tests;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;
import javax.transaction.Transactional;

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

import pl.bartflor.dao.Offer;
import pl.bartflor.dao.OffersDao;
import pl.bartflor.dao.User;
@Transactional
@ActiveProfiles("test")
@ContextConfiguration(locations = { 
		"classpath:pl/bartflor/config/security-context.xml" , "classpath:pl/bartflor/test/config/dataSource.xml" })
@ExtendWith(SpringExtension.class)
@Rollback(false)

public class OffersDaoTest {
	@Autowired
	private OffersDao offersDao;
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbc;
	
	@BeforeEach
	private void init() {
		jdbc = new JdbcTemplate(this.dataSource);
		jdbc.execute("DELETE FROM offer");
		jdbc.execute("DELETE FROM users");
	}
	@Test
	public void createOffer_gestOfferObject_shouldcreateNewDatabaseRow() {
		
		User createdUser = new User("testusername", "testname1", "testpassword", "test@mail.tt", "ROLE_USER", true);
		BeanPropertySqlParameterSource userParamSource = new BeanPropertySqlParameterSource(createdUser);
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(this.jdbc);
		njdbc.update(
				"INSERT INTO users(username, name, password, enabled, email, authority) VALUES (:username, :name, :password, :enabled, :email, :authority)",
				userParamSource);
		Offer createdOffer = new Offer(createdUser, "test >>>> offer text");
		offersDao.createOffer(createdOffer);
		
		Offer recivedOffer = jdbc.queryForObject("SELECT * FROM offer, users WHERE offer.username=users.username", new OfferRowMapper());
		assertEquals("Created Offer object should equal to object recived from DB", createdOffer, recivedOffer);
	}

	@Test
	public void getOffer_getsOfferId_shouldReciveNewlyCreatedOffer() {
		User createdUser = new User("tusername1","tname1", "tpassword", "t@mail.tt", "ROLE_USER", true);
		Offer createdOffer = new Offer(createdUser, "test new offer text");
		insertOfferIntoDatabase(createdOffer);
		int createdOfferId = jdbc.queryForObject("SELECT offer_id FROM offer", Integer.class);
		createdOffer.setOffer_id(createdOfferId);
		
		Offer recivedOffer = offersDao.getOffer(createdOfferId);
		assertEquals("Offer object recived from DB, should equal to new Offer object added to DB", createdOffer, recivedOffer);
	}
	
	@Test
	public void deleteOffer_getsOfferId_shouldDeleteExactEntityFromDatabase() {
		User createdUser1 = new User("tusername1","tname1", "tpassword", "t@mail.tt", "ROLE_USER", true);
		Offer createdOffer1 = new Offer(createdUser1, "test new offer text");
		User createdUser2 = new User("tusername2","tname2", "tpassword", "t@mail.tt", "ROLE_USER", true);
		Offer createdOffer2 = new Offer(createdUser2, "test new offer text");
		insertOfferIntoDatabase(createdOffer1);
		insertOfferIntoDatabase(createdOffer2);
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(this.jdbc);
		int offerIdToDelete = njdbc.queryForObject("SELECT offer_id FROM offer WHERE username = :username", new MapSqlParameterSource("username", createdUser1.getUsername()), Integer.class);
		
		offersDao.deleteOffer(offerIdToDelete);
		int count = njdbc.queryForObject("SELECT COUNT(*) FROM offer WHERE offer_id=:offer_id", new MapSqlParameterSource("offer_id", offerIdToDelete), Integer.class);
		assertEquals("Should count zero objects with deleted offer_id", 0, count);
	}
	
	private void insertOfferIntoDatabase(Offer offer) {
		NamedParameterJdbcTemplate njdbc = new NamedParameterJdbcTemplate(this.jdbc);
		BeanPropertySqlParameterSource userParamSource = new BeanPropertySqlParameterSource(offer.getUser());
		njdbc.update("INSERT INTO users(username, name, password, enabled, email, authority)"
				+ " VALUES (:username, :name, :password, :enabled, :email, :authority)", userParamSource);
		BeanPropertySqlParameterSource offerParamSource = new BeanPropertySqlParameterSource(offer);
		njdbc.update("INSERT INTO offer(username, text) VALUES (:username, :text)", offerParamSource);
	}
}
