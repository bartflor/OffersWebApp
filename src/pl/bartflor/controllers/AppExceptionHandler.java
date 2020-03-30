package pl.bartflor.controllers;

import java.security.Principal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class AppExceptionHandler {
	protected static final Logger logger = LogManager.getLogger(AppExceptionHandler.class);

	
	@ExceptionHandler(DataAccessException.class)
	public String dataBaseErrorHandler(DataAccessException exeption) {
		logger.error("DataAccessException ocure:"+exeption+"\n"+exeption.getStackTrace());
		return "error";
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public String dataBaseErrorHandler(AccessDeniedException exeption, Principal principal) {
		logger.info("Unauthorized admin panel acces attempt by: "+principal.getName());
		logger.error("AccessDeniedException ocure:"+exeption);
		return "noaccess";
	}
	
}
	