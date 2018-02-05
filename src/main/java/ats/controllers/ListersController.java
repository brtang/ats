package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ats.constants.Constants;
import ats.constants.Errors;
import ats.database.models.Company;
import ats.database.models.Lister;
import ats.database.models.ids.ListerId;
import ats.database.repositories.CompaniesRepository;
import ats.database.repositories.ListersRepository;

@Controller
@RequestMapping("")
public class ListersController {
	
	private static Logger logger = LoggerFactory.getLogger(ListersController.class);
	
	@Autowired
	private ListersRepository listerRepository;
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	
	// POST to create a new Lister for a Company
	@RequestMapping(value = "/{companyName}", method = RequestMethod.POST)
	public ResponseEntity<Object> createLister(HttpServletRequest req, @PathVariable("companyName") String companyName){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				logger.error("Invalid secretKey. Unauthorized attempt by IP: " + req.getLocalAddr());
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);			
			}		
			String email = req.getHeader("email");
			if(req.getHeader("createCode") != null && companyRepository.exists(companyName) && email!= null && !listerRepository.exists(email)){
				Company company = companyRepository.findByCompanyName(companyName);
				if(company.getCreateCode().equals(req.getHeader("createCode"))) {
					Lister newLister = new Lister(company, email);
					if(req.getHeader("firstName") != null) {
						newLister.setFirstName(req.getHeader("firstName"));
					}
					if(req.getHeader("lastName") != null) {
						newLister.setLastName(req.getHeader("lastName"));
					}		
					listerRepository.save(newLister);
					responseMap.put(Constants.LISTER, newLister);
					return new ResponseEntity<>(responseMap, HttpStatus.OK);					
				}else{
					responseMap.put(Constants.ERRORS, "Invalid create code");
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);			
				}			
			}else{
				responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);			
			}
		}catch(Exception e) {
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	// GET a Lister by email and Company
	@RequestMapping(value = "/{companyName}", method = RequestMethod.GET)
	public ResponseEntity<Object> getLister(HttpServletRequest req, @PathVariable("companyName") String companyName){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				logger.error("Invalid secretKey. Unauthorized attempt by IP: " + req.getLocalAddr());
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);			
			}		
			String email = req.getHeader("email");
			if(companyRepository.exists(companyName) && email!= null){
				Lister lister = listerRepository.findByEmail(email);
				if(lister != null && lister.getCompany().getCompanyName().equals(companyName)) {
					responseMap.put(Constants.LISTER, lister);
					return new ResponseEntity<>(responseMap, HttpStatus.OK);
				}else{
					responseMap.put(Constants.ERRORS, Errors.LISTER_NOT_FOUND);
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
				}
			}else {
				responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);			
			}	
		}catch(Exception e) {
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
}
