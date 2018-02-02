package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/listing")
public class ListingsController {
	
	@Autowired
	private ListingsRepository listingsRepository;
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	@Autowired
	private ListersRepository listerRepository;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> createListing(HttpServletRequest req, @RequestBody Listing listing){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}
			
			Lister lister = listerRepository.findByListerIdEmail(listing.getLister().getListerId().getEmail());
			if(lister.isCanList()) {
				// Create new Listing 
				if(listing.getId() == null) {
						Company company = companyRepository.findByCompanyName(listing.getCompany().getCompanyName());
						if(company.getNumListingsRemaining() > 0) {
							company.setNumListingsRemaining(company.getNumListingsRemaining() - 1);
							companyRepository.save(company);
													
							listingsRepository.save(listing);
							responseMap.put(Constants.LISTING, listing);
							return new ResponseEntity<>(responseMap, HttpStatus.OK);
						}else {
							// Return no listings left error
							responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
							return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
						}				
				}else{
					// Update a listing
					if(listingsRepository.exists(listing.getId())){
						listingsRepository.save(listing);
						responseMap.put(Constants.LISTING, listing);
						return new ResponseEntity<>(responseMap, HttpStatus.OK);
					}else {
						// Return id not found error
						responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
						return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
					}
						
				}
					
			}else {
				// Return Lister is not valid to post listings error
				responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
								
		}catch(Exception e) {
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
