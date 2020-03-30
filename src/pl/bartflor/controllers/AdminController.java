package pl.bartflor.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.bartflor.dao.User;
import pl.bartflor.service.UserService;

@Controller
public class AdminController {
	protected static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	UserService userservice;
	
	@RequestMapping("/panel")
	@Secured("ROLE_ADMIN")
	public String showPanel(Model model, Principal principal) {
		logger.info("Admin panel acces by: "+principal.getName()); 
		List<User> users = userservice.getAllUsers();
		model.addAttribute("users", users);
		return "panel";
	}

	@RequestMapping("/noaccess")
	public String showAccesDeny(Principal principal) {
		logger.info("Unauthorized admin panel acces by: "+principal.getName());
		return "noaccess";
	}

}
