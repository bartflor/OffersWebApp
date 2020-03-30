package pl.bartflor.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.bartflor.dao.Offer;
import pl.bartflor.service.OfferService;

@Controller
public class HomeController {
	@Autowired
	private OfferService offerService;
    protected static final Logger mainLogger = LogManager.getLogger("HomeController");
	@RequestMapping("/")
	public String showHome(Model model, Principal principal) {
		List<Offer> offersList = offerService.getCurrent();
		model.addAttribute("offersList", offersList);
		if(principal!=null) {
			model.addAttribute("hasOffer", offerService.userHasOffer(principal.getName()));
		}
		else {
			model.addAttribute("hasOffer", false);
		}
		return "home";
	}
}
