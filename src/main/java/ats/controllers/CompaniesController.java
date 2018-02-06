package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import ats.database.repositories.CompaniesRepository;

@Controller
@RequestMapping("/{companyName}")
public class CompaniesController {
	
	private static Logger logger = LoggerFactory.getLogger(CompaniesController.class);
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	// POST to create a new company using headers
//	@RequestMapping(value = "/{companyName}", method = RequestMethod.POST)
//	public ResponseEntity<Object> saveCompany(HttpServletRequest req, @PathVariable("companyName") String companyName){
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		try {	
//			logger.debug("/POST company: Creating new entry for: " + companyName);		
//			if(!companyRepository.exists(companyName)) {
//				Company newCompany = new Company(companyName);
//				if(req.getHeader("createCode") != null) {
//					newCompany.setCreateCode(req.getHeader("createCode"));
//					if(req.getHeader("address") != null) {
//						newCompany.setAddress(req.getHeader("address"));
//					}
//					if(req.getHeader("zipCode") != null && req.getHeader("zipCode").matches("[0-9]+")) {
//						newCompany.setZipCode(req.getIntHeader("zipCode"));
//					}
//					
//					companyRepository.save(newCompany);
//					logger.debug("/POST company: Successfully for name: " + companyName);
//					responseMap.put(Constants.COMPANY, newCompany);
//					return new ResponseEntity<>(responseMap, HttpStatus.OK);
//				}else {
//					responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
//					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
//				}
//			}else {
//				logger.debug("/POST comapny: Company already exists for name: " + companyName);
//				responseMap.put(Constants.ERRORS, "Company with name " + companyName + " already exists.");
//				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
//			}			
//		}catch(Exception e) {
//			logger.error("/POST company: Caught exception for name: " + companyName + " " + ExceptionUtils.getStackTrace(e));
//			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
//			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
//		}			
//	}
	
	// POST to create a new company using Json
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCompany(HttpServletRequest req, @PathVariable("companyName") String companyName, @RequestBody Company newCompany){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {	
			logger.debug("/POST company: Creating new entry for: " + companyName);		
			if(!companyRepository.exists(companyName)) {
				newCompany.setCompanyName(companyName);
				companyRepository.save(newCompany);
				logger.debug("/POST company: Successfully for name: " + companyName);
				responseMap.put(Constants.COMPANY, newCompany);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}else {
				// Company name already exists
				logger.debug("/POST comapny: Company already exists for name: " + companyName);
				responseMap.put(Constants.ERRORS, "Company with name " + companyName + " already exists.");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}			
		}catch(Exception e) {
			logger.error("/POST company: Caught exception for name: " + companyName + " " + ExceptionUtils.getStackTrace(e));
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	// GET to fetch a company
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> getCompany(HttpServletRequest req, @PathVariable("companyName") String companyName){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Company company = companyRepository.findByCompanyName(companyName);
			if(company != null) {
				responseMap.put(Constants.COMPANY, company);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}else {
				// Company not found
				responseMap.put(Constants.ERRORS, "Company with name " + companyName + " not found.");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}						
		}catch(Exception e) {
			logger.error("/GET company: Caught exception for name: " + companyName + " " + ExceptionUtils.getStackTrace(e));
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
}
