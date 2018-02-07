package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import ats.database.models.User;
import ats.database.repositories.UsersRepository;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UsersRepository userRepository;
	
	// POST a new user
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> createUser(HttpServletRequest req, @RequestBody User newUser){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			if(!userRepository.checkUsernameOrEmailExists(newUser.getUsername(), newUser.getEmail())) {
				userRepository.save(newUser);
				responseMap.put(Constants.USER, newUser);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);	
			}else {
				// Username or email already exists
				responseMap.put(Constants.ERRORS, "Username or Email already exists");
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);		
			}	
		}catch(Exception e) {
			e.printStackTrace();
			responseMap.put(Constants.ERRORS, Errors.INTERNAL_ERROR);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	// GET a user 
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(HttpServletRequest req, @PathVariable("username") String username){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			User user = userRepository.findByUsername(username);
			if(user != null) {
				responseMap.put(Constants.USER, user);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);
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
