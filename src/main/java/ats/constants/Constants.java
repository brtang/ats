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
	
	public static final Set<String> VALID_FILE_TYPES = new HashSet<String>() {{
		add("docx");
		add("doc");
	}};
	
	public static final String ERRORS = "Errors";

	public static final String LISTING = "Listing";
	
	public static final String COMPANY = "Company";

	public static final String LISTER = "Lister";

	public static final String USER = "User";

	public static final String APPLICATION = "Application";	
	
	public static final String ALL = "all";
	
	public static final String KEYWORD = "keyword";
	
	public static final String EMPTY_STRING = "";
	
}
