package ats.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class DataFilesController {
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> testAPI(@PathVariable("id") String id){
		System.out.println(id);
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("test", id);
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
	
	
	
}
