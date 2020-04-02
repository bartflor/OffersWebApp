package pl.bartflor.test.tests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import pl.bartflor.dao.User;

public class UserRowMapper implements RowMapper<User> {
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setAuthority(rs.getNString("authority"));
		user.setEmail(rs.getNString("email"));
		user.setEnabled(rs.getBoolean("enabled"));
		user.setName(rs.getNString("name"));
		user.setUsername(rs.getNString("username"));
		return user;
	}
}
