package ats.controllers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import ats.constants.Constants;
import ats.constants.Errors;
import ats.database.models.Application;
import ats.database.models.Listing;
import ats.database.models.User;
import ats.database.repositories.ApplicationRepository;
import ats.database.repositories.ListingsRepository;
import ats.database.repositories.UsersRepository;
import ats.utils.AppConfigUtils;
import ats.utils.S3Utils;


@Controller
@RequestMapping("/users/{username}")
public class ApplicationsController {
	
	@Autowired
	private AppConfigUtils appConfigUtils;

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private ListingsRepository listingRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private S3Utils s3utils;
	
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
					
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req; 
					Map<String, MultipartFile> files = multiRequest.getFileMap(); 
					MultipartFile file = new ArrayList<Entry<String, MultipartFile>>(files.entrySet()).get(0).getValue(); // First file in file map
					String fileName = file.getOriginalFilename(); // First file's name
					ByteArrayInputStream fileByteData = new ByteArrayInputStream(file.getBytes()); // First file's data
					String filePath = s3utils.saveFileToLocal(fileByteData, fileName, appConfigUtils.getDevFilePath());
					System.out.println(filePath);
					System.out.println(newApp.getId());
					s3utils.pushToS3(fileName, "all", listing.getLister().getCompany().getCompanyName(), listing.getId(), appConfigUtils.getDevFilePath());
					
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
