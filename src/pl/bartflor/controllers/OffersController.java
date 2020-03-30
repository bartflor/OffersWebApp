package pl.bartflor.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.bartflor.dao.FormConstraints;
import pl.bartflor.dao.Offer;
import pl.bartflor.service.OfferService;

@Controller
public class OffersController {
	protected static final Logger logger = LogManager.getLogger(OffersController.class);
	private OfferService offerService;

	@Autowired
	public void setOfferService(OfferService offerService) {
		this.offerService = offerService;
	}

	@RequestMapping("/offers")
	public String showOffers(Model model) {
		logger.info("loading offers page");

		List<Offer> offersList = offerService.getCurrent();
		model.addAttribute("offersList", offersList);
		return "offers";
	}

	@RequestMapping("/createoffer")
	public String createOffer(Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			if (offerService.userHasOffer(username)) {
				Offer offerToEdit = offerService.getOffer(username);
				model.addAttribute("offer", offerToEdit);
				return "createoffer";
			}
		}
		model.addAttribute("offer", new Offer());
		return "createoffer";
	}

	@RequestMapping(value = "/docreate", method = RequestMethod.POST)
	public String doCreate(@Validated(FormConstraints.class) Offer offer, BindingResult result, Principal principal,
			@RequestParam(value = "delete", required = false) String delete) {
		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				logger.debug("creating offer validation error: " + error.getDefaultMessage());
			}
			return "createoffer";
		}
		String creatorUsername = principal.getName();
		if (delete == null) {
			offer.getUser().setUsername(creatorUsername);
			if (offerService.userHasOffer(creatorUsername)) {
				logger.info("Update offer: id = " + offer.getOffer_id() + ", by user: " + creatorUsername);
				offerService.updateOffer(offer);
			} else {
				logger.info(
						"Create offer by user: " + creatorUsername);
				offerService.setOffer(offer);
			}
			return "offercreated";
		}
		else {
			logger.info("Delete offer: id = " + offer.getOffer_id() + ", by user: " + creatorUsername);
			offerService.deleteOffer(offer.getOffer_id());
			return "offerdeleted";
		}
		
	}
}
