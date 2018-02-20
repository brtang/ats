package ats.constants;

public class Errors {
	private Errors() {
		
	}
	
	public static final String INTERNAL_ERROR = "Internal Server error occured.";
	public static final Object COMPANY_NOT_FOUND = "Company not found.";
	public static final Object BAD_HEADER_REQUEST = "Incomplete headers.";
	public static final Object LISTER_NOT_FOUND = "Lister not found";
	public static final Object INVALID_CREATE_CODE = "Invalid create code";
	public static final Object USER_ALREADY_EXISTS = "Username already exists";
	public static final Object USER_NOT_FOUND = "User not found";
	public static final Object LISTING_NOT_FOUND = "Listing not found";
	public static final Object INVALID_FILE_UPLOAD = "Invalid file upload";
	public static final String FAILED_TO_UPLOAD_TO_S3 = "Failed to upload to S3";
	public static final Object APPLICATION_NOT_FOUND = "Application not found";
	public static final Object APPLICATION_ALREADY_EXISTS = "Application already exists";

}
