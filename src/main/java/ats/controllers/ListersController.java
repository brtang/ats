package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/lister")
public class ListersController {
	
	@Autowired
	private ListersRepository listerRepository;
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	
	// Think of a way for this to be more intuitive. We dont want companies to be able to create unlimited number of listers. We want to restrict the number of listers.
	// Maybe use a generated code to verify and have a field in companies for numListersLeft ? Set numListersLeft to a default int and decrement whenever a lister is created
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> createLister(HttpServletRequest req){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			// To Do: Store secret key in properties
			if(req.getHeader("secretKey") == null || !req.getHeader("secretKey").equals("this")) {
				// Log this attempt
				System.out.println("WRONG SECRET KEY");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
						
			}
			
			if(req.getHeader("firstName") == null || req.getHeader("lastName") == null || req.getHeader("email") == null || req.getHeader("company") == null) {
				responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
			
			String companyName = req.getHeader("company");
			Company company = companyRepository.findByCompanyName(companyName);
			if(company != null) {
				Lister newLister = new Lister();
				ListerId i = new ListerId(req.getHeader("firstName"), req.getHeader("lastName"), req.getHeader("email"));
				return new ResponseEntity<>(responseMap, HttpStatus.OK);			
			}else {
				responseMap.put(Constants.ERRORS, Errors.COMPANY_NOT_FOUND);
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
				
		}catch(Exception e) {
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
}
