package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ats.constants.Constants;
import ats.constants.Errors;
import ats.database.models.Application;
import ats.database.models.Listing;
import ats.database.models.User;
import ats.database.repositories.ApplicationRepository;
import ats.database.repositories.ListingsRepository;
import ats.database.repositories.UsersRepository;


@Controller
@RequestMapping("/users/{username}")
public class ApplicationsController {

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private ListingsRepository listingRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	// POST a new application
	@RequestMapping(value = "/{listingId}/application", method = RequestMethod.POST)
	public ResponseEntity<Object> createApplication(HttpServletRequest req, @PathVariable("username") String username, @PathVariable("listingId") int listingId){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			User user = userRepository.findByUsername(username);
			if(user != null) {
				Listing listing = listingRepository.findById(listingId);
				if(listing != null && listing.isActive()) {
					Application newApp = new Application();
					newApp.setUser(user);
					newApp.setListing(listing);
					applicationRepository.save(newApp);
					System.out.println(newApp.getId());
					responseMap.put(Constants.APPLICATION, newApp);
					return new ResponseEntity<>(responseMap, HttpStatus.OK);
				}else {
					// Listing not found or is not active
					responseMap.put(Constants.ERRORS, Errors.LISTING_NOT_FOUND);
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
				}
			}else {
				// User not found
				responseMap.put(Constants.ERRORS, Errors.USER_NOT_FOUND);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}	
		}catch(Exception e) {
			e.printStackTrace();
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
