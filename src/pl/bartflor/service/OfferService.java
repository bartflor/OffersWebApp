package pl.bartflor.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.bartflor.dao.Offer;
import pl.bartflor.dao.OffersDao;
@Transactional
@Service("offersService")
public class OfferService {
	
	private OffersDao offersDao;
	
	@Autowired
	public void setOffersDao(OffersDao offersDao) {
		this.offersDao = offersDao;
	}


	public List<Offer> getCurrent(){
		return offersDao.getOffers();
	}

	public void setOffer(Offer offer) {
		offersDao.createOffer(offer);	
	}

	public boolean userHasOffer(String username) {
		List<Offer> userOffers = offersDao.getOffers(username);
		return !userOffers.isEmpty();
	}


	public Offer getOffer(String username) {
		List<Offer> userOffers = offersDao.getOffers(username);
		return userOffers.get(0);
	}


	public void updateOffer(Offer offerUpdate) {
		offersDao.updateOffer(offerUpdate);
		
	}


	public void deleteOffer(int offer_id) {
		offersDao.deleteOffer(offer_id);
	}
	
	
}
