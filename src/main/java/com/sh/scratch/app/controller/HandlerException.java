package com.sh.scratch.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

	private static Logger LOGGER = LoggerFactory.getLogger(HandlerException.class);
	
	@ExceptionHandler(value = Exception.class)
	public  ResponseEntity<?> handler(Exception ex){
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("status", "500");
		map.add("message", ex.getMessage());
		LOGGER.error("Error: ",ex);
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
