package ats.controllers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.aspectj.lang.annotation.Before;
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
import ats.utils.ThreadUtils;


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
	
	@Autowired
	private ThreadUtils threadUtils;
	
	// POST a new application
	@RequestMapping(value = "/{listingId}/application", method = RequestMethod.POST)
	public ResponseEntity<Object> createApplication(HttpServletRequest req, @PathVariable("username") String username, @PathVariable("listingId") int listingId){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			User user = userRepository.findByUsername(username);
			if(user != null) {
				Listing listing = listingRepository.findById(listingId);
				if(listing != null && listing.isActive()) {					
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req; 
					Map<String, MultipartFile> files = multiRequest.getFileMap(); 
					MultipartFile file = new ArrayList<Entry<String, MultipartFile>>(files.entrySet()).get(0).getValue(); 
					
					// Returns empty string if file does not have extension
					String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
					if(Constants.VALID_FILE_TYPES.contains(fileExtension)) {
						Application newApp = new Application();
						newApp.setUser(user);
						newApp.setListing(listing);
						applicationRepository.save(newApp);
						
						String filePath = s3utils.saveFileToLocal(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename(), appConfigUtils.getDevFilePath());
						threadUtils.scheduleThread(newApp, listing, filePath, s3utils, fileExtension);
//						s3utils.pushToS3(fileName, "all", listing.getLister().getCompany().getCompanyName(), listing.getId(), appConfigUtils.getDevFilePath());
						responseMap.put(Constants.APPLICATION, newApp);
						return new ResponseEntity<>(responseMap, HttpStatus.OK);
					}else {
						// Invalid file type
						responseMap.put(Constants.ERRORS, Errors.INVALID_FILE_UPLOAD);
						return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
					}			
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
