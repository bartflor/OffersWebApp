package pl.bartflor.test.tests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import pl.bartflor.dao.Offer;
import pl.bartflor.dao.User;

public class OfferRowMapper implements RowMapper<Offer> {

	@Override
	public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Offer offer = new Offer();
		offer.setOffer_id(rs.getInt("offer_id"));
		offer.setText(rs.getNString("text"));
		User user = new User();
		user.setAuthority(rs.getNString("authority"));
		user.setEmail(rs.getNString("email"));
		user.setEnabled(true);
		user.setName(rs.getNString("name"));
		user.setUsername(rs.getNString("username"));
		offer.setUser(user);	
		return offer;
	}

}
