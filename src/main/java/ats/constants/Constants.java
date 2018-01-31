package ats.constants;

import java.util.HashSet;
import java.util.Set;

public class Constants {
	// Prevents creation of objects. Only static items can be used
	private Constants() {
		
	}
	
	public static final Set<String> VALID_IPS = new HashSet<String>() {{
		add("0:0:0:0:0:0:0:1");
	}};
	
	public static final String ERRORS = "Errors";
	
	
	
}
