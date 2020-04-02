package pl.bartflor.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository("offersDAO")
public class OffersDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public List<Offer> getOffers() {
		return getSession().createQuery("from Offer", Offer.class).list();
	
	}

	public Offer getOffer(int id) {
		return getSession().createQuery("select o from Offer o where o.offer_id = :id", Offer.class)
		.setParameter("id", id)
		.getSingleResult();
	}
	
	public List<Offer> getOffers(String username) {
		return getSession().createQuery("select offer from Offer offer where offer.user.username = :username", Offer.class)
					.setParameter("username", username)
					.list();
	}

	public void deleteOffer(int id) {
		getSession().createQuery("delete from Offer offer where offer.offer_id=:id")
					.setParameter("id", id)
					.executeUpdate();
	}

	public void createOffer(Offer offer) {
		getSession().save(offer);
	}

	public void updateOffer(Offer newOffer) {
		getSession().createQuery("update Offer offer set offer.text=:text where offer.offer_id=:id")
					.setParameter("text", newOffer.getText())
					.setParameter("id", newOffer.getOffer_id())
					.executeUpdate();
	}

}
