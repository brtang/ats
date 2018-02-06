package ats.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ats.constants.Constants;
import ats.constants.Errors;
import ats.database.models.Company;
import ats.database.models.Lister;
import ats.database.models.Listing;
import ats.database.repositories.CompaniesRepository;
import ats.database.repositories.ListersRepository;
import ats.database.repositories.ListingsRepository;

@Controller
@RequestMapping("/{companyName}")
public class ListingsController {
	
	private static Logger logger = LoggerFactory.getLogger(ListingsController.class);
	
	@Autowired
	private ListingsRepository listingsRepository;
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	@Autowired
	private ListersRepository listerRepository;
	
	// TO DO: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// A lot of repetitive code that can be placed in an interceptor?
	
	// POST a new listing for an existing company and valid lister
	@RequestMapping(value = "/{username}/listing/{createCode}", method = RequestMethod.POST)
	public ResponseEntity<Object> createListing(HttpServletRequest req, @PathVariable("companyName") String companyName,  @PathVariable("username") String username, @PathVariable("createCode") String createCode, @RequestBody Listing newListing){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				logger.error("Invalid secretKey. Unauthorized attempt by IP: " + req.getLocalAddr());	
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}
		
			Company company = companyRepository.findByCompanyName(companyName);
			if(company != null) {
				if(company.getCreateCode().equals(createCode)) {
					if(company.getNumListingsRemaining() > 0) {
						Lister lister = listerRepository.findByCompanyNameAndUsername(companyName, username);
						if(lister != null) {
							if(lister.isCanList()) {
								newListing.setLister(lister);
								listingsRepository.save(newListing);
								responseMap.put(Constants.LISTING, newListing);
								return new ResponseEntity<>(responseMap, HttpStatus.OK);
							}else {
								// Lister cannot list 
								responseMap.put(Constants.ERRORS, "Lister cannot list");
								return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
							}					
						}else {
							// Lister not found
							responseMap.put(Constants.ERRORS, "Lister not found");
							return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
						}
					}else {
						// Not enough listings remaining
						responseMap.put(Constants.ERRORS, "Company does not have any listings left");
						return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
					}				
				}else {
					// Create code does not match
					responseMap.put(Constants.ERRORS, "Invalid create code");
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
				}	
			}else {
				// Company does not exist
				responseMap.put(Constants.ERRORS,"Company not found");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}			
		}catch(Exception e) {
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// GET a listing for a lister of a company
	@RequestMapping(value = "/{username}/listing", method = RequestMethod.GET)
	public ResponseEntity<Object> getListing(HttpServletRequest req, @PathVariable("companyName") String companyName, @PathVariable("username") String username){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}
			
			Company company = companyRepository.findByCompanyName(companyName);
			if(company != null) {
				Lister lister = listerRepository.findByCompanyNameAndUsername(companyName, username);
				if(lister != null) {
					List<Listing> listings = listingsRepository.findByListerUsernameAndCompanyName(username, companyName);
					responseMap.put(Constants.LISTING, listings);
					return new ResponseEntity<>(responseMap, HttpStatus.OK);
				}else {
					// Lister not found
					responseMap.put(Constants.ERRORS, Errors.LISTER_NOT_FOUND);
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);	
				}			
			}else {
				// Company does not exist
				responseMap.put(Constants.ERRORS, Errors.COMPANY_NOT_FOUND);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);	
			}	
		}catch(Exception e) {
			e.printStackTrace();
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// GET all listings by companyName
	@RequestMapping(value = "/listing", method = RequestMethod.GET)
	public ResponseEntity<Object> getListingByCompany(HttpServletRequest req, @PathVariable("companyName") String companyName){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}
			
			Company company = companyRepository.findByCompanyName(companyName);
			if(company != null) {
				List<Listing> listings = listingsRepository.findByCompany(companyName);
				responseMap.put(Constants.LISTING, listings);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);			
			}else {
				// Company does not exist
				responseMap.put(Constants.ERRORS, Errors.COMPANY_NOT_FOUND);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);	
			}						
		}catch(Exception e) {
			e.printStackTrace();
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
