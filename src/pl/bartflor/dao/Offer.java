package pl.bartflor.dao;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
@Entity
@Table(name="offer")
public class Offer {
	@Id
	@Generated(value = { "GenerationTime.ALWAYS" })
	private int offer_id;
	
	@ManyToOne
	@JoinColumn(name="username")
	private User user;
	
	
	@Size(min=20, max=255, groups = {PersistenceConstraints.class, FormConstraints.class})
	private String text;
	
	public Offer() {
		this.user = new User();
	}

	public Offer(User user, String text) {
		super();
		this.user = user;
		this.text = text;
	}

	public Offer(int id, User user, String text) {
		super();
		this.offer_id = id;
		this.user = user;
		this.text = text;
	}

	public int getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offer_id;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Offer other = (Offer) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Offer [offer_id=" + offer_id + ", user=" + user + ", text=" + text + "]";
	}


}
