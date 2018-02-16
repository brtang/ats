package ats.controllers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import ats.constants.AppLocation;
import ats.constants.Constants;
import ats.constants.Errors;
import ats.database.models.Application;
import ats.database.models.Company;
import ats.database.models.Lister;
import ats.database.models.Listing;
import ats.database.models.User;
import ats.database.repositories.ApplicationRepository;
import ats.database.repositories.CompaniesRepository;
import ats.database.repositories.ListersRepository;
import ats.database.repositories.ListingsRepository;
import ats.database.repositories.UsersRepository;
import ats.utils.AppConfigUtils;
import ats.utils.S3Utils;
import ats.utils.ThreadUtils;


@Controller
@RequestMapping("")
public class ApplicationsController {
	
	@Autowired
	private AppConfigUtils appConfigUtils;

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private ListersRepository listerRepository;
	
	@Autowired
	private ListingsRepository listingRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private CompaniesRepository companyRepository;
	
	@Autowired
	private S3Utils s3utils;
	
	@Autowired
	private ThreadUtils threadUtils;
	
	// POST a new application
	@RequestMapping(value = "/users/{username}/{listingId}/application", method = RequestMethod.POST)
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
						
						// TO DO: Need to format fileName to include username_filename.docx
						
						String filePath = s3utils.saveFileToLocal(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename(), appConfigUtils.getDevFilePath());
						threadUtils.scheduleFileWorker(newApp, listing, filePath, s3utils, fileExtension);
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
	
	// Update an application's AppStatus for a valid Lister
	@RequestMapping(value = "{companyName}/{username}/listing/{listingId}/applications/{appId}", method = RequestMethod.POST)
	public ResponseEntity<Object> updateAppStatus(HttpServletRequest req, @PathVariable("companyName") String companyName,  @PathVariable("username") String username, @PathVariable("listingId") int listingId, @PathVariable("appId") int appId /*, @RequestBody final AppLocation location*/ ){
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
					Listing listing = listingRepository.findByUsernameAndId(username, listingId);
					if(listing != null) {
						Application application = applicationRepository.findOne(appId); // Check if resume path exists?
						if(application != null) {
							if(req.getHeader("location") != null){
								try {
									AppLocation appLocation = Enum.valueOf(AppLocation.class, req.getHeader("location").toUpperCase());
									
									System.out.println(application.getResumePath());
									
									threadUtils.scheduleFileMover(s3utils, appLocation, application.getResumePath());
									
									responseMap.put("appLocation", appLocation);
									return new ResponseEntity<>(responseMap, HttpStatus.OK);
								}catch(IllegalArgumentException e) {
									// Invalid appLocation enum
									responseMap.put(Constants.ERRORS, "Invalid appLocation enum");
									return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
								}
							}else {
								// Missing location header
								responseMap.put(Constants.ERRORS, Errors.BAD_HEADER_REQUEST);
								return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
							}
						}else {
							// Application not found
							responseMap.put(Constants.ERRORS, Errors.APPLICATION_NOT_FOUND);
							return new ResponseEntity<>(responseMap, HttpStatus.OK);
						}				
					}else {
						// Listing not found
						responseMap.put(Constants.ERRORS, Errors.LISTING_NOT_FOUND);
						return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
					}				
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
}
