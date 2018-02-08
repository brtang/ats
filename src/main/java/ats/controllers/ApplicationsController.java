package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ats.constants.Constants;
import ats.constants.Errors;


@Controller
@RequestMapping("/users/{username}")
public class ApplicationsController {

	// POST a new application
//	@RequestMapping(value = "/{listingId}/application", method = RequestMethod.POST)
//	public ResponseEntity<Object> createApplication(HttpServletRequest req, @PathVariable("username") String username, @PathVariable("listingId") int listingId){
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		try {
//			
//			
//			
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
//			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//	}
	
	
}
