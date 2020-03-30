package pl.bartflor.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.bartflor.dao.FormConstraints;
import pl.bartflor.dao.User;
import pl.bartflor.service.UserService;

@Controller
public class LoginController {
	protected static final Logger logger = LogManager.getLogger(LoginController.class);
	
	@Autowired
	private UserService userservice;

	@RequestMapping("/loginform")
	public String showLogin() {
		return "loginform";
	}
	
	@RequestMapping("/loggedout")
	public String showLogout() {
		return "loggedout";
	}
	
	@RequestMapping(value = "/createaccount", method = RequestMethod.POST )
	public String showCreateAccount(@Validated(FormConstraints.class) User user, BindingResult userResult, @RequestParam(value="confirm_password",required=false) String confirm_password ) {

		if(userResult.hasErrors()) {
			return "newaccount";
		}
		else if(userservice.existsUserWithUsername(user.getUsername())) {
			userResult.rejectValue("username", "DuplicateKey");
			return "newaccount";
		}
		else if(((String)user.getPassword()).equals(confirm_password)==false) {
			userResult.rejectValue("password", "MissmatchPassword");
			return "newaccount";
		}
		userservice.createaccount(user);
		logger.info("New account created. Username:"+user.getUsername());
		return "accountcreated";
	}
	
	@RequestMapping("/newaccount")
	public String showNewAccount(Model model) {
		model.addAttribute("user", new User());
		return "newaccount";
		
	}
}
