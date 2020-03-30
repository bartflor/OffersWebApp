package pl.bartflor.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Entity
@Table(name = "users")
public class User {
	@NotBlank(groups = {PersistenceConstraints.class, FormConstraints.class})
	@Size(min=4, max=20, groups = {PersistenceConstraints.class, FormConstraints.class})
	@Id
	private String username;
	@NotBlank(groups = {PersistenceConstraints.class, FormConstraints.class})
	@Size(min=4, max=20, groups = {PersistenceConstraints.class, FormConstraints.class})
	
	private String name;
	
	@NotBlank(groups = {PersistenceConstraints.class, FormConstraints.class})
	@Size(min=5, max=20, groups = {FormConstraints.class})
	private String password;
	@NotBlank(groups = {PersistenceConstraints.class, FormConstraints.class})
	@Email(groups = {PersistenceConstraints.class, FormConstraints.class})
	private String email;
	private String authority;
	private boolean enabled = false;

	public User() {
	}

	public User(String username, String name, String password, String email, String athority, boolean enable) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.email = email;
		this.authority = athority;
		this.enabled = enable;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String athority) {
		this.authority = athority;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", name=" + name + ", email=" + email + ", authority=" + authority
				+ ", enabled=" + enabled + "]";
	}
	



}
