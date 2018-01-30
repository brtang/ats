package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ats.database.models.Company;
import ats.database.repositories.CompaniesRepository;

@Controller
@RequestMapping("/company")
public class CompaniesController {
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	// POST to create a new company
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCompany(HttpServletRequest req){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String name = req.getHeader("name");
			if(StringUtils.isBlank(name)) {
				responseMap.put("Error", "Need to specify a name");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
			Company result = companyRepository.findByCompanyName(name);
			if(result == null) {
				Company newCompany = new Company(name);
				companyRepository.save(newCompany);
				responseMap.put("Company", newCompany);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}else {
				responseMap.put("Error", "Company with name " + name + " already exists.");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			responseMap.put("Exception", e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
}
