package ats.thread;

import ats.constants.AppLocation;
import ats.database.models.Application;
import ats.utils.S3Utils;

public class FileMover implements Runnable{
	
	private S3Utils s3Util;
	
	private AppLocation appLocation;
	
	private String location;
	
	public FileMover(S3Utils s3Util, AppLocation appLocation, String location) {
		this.s3Util = s3Util;
		this.appLocation = appLocation;
		this.location = location;
	}
	
	private String formatLocation(String location, String newDirectory) {
		String[] locationParts = location.split("/");
		return locationParts[0] + "/" + locationParts[1] + "/" + newDirectory + "/" + locationParts[locationParts.length - 1];
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("FileMover Thread");
		try {
			String newLocation;
			// To DO: probably dont need this switch statement?
			// TO DO: Need to update Application's resumePath and save back into DB
			switch(appLocation) {			
				case FAVORITE:
					newLocation = formatLocation(location, appLocation.FAVORITE.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(location, newLocation);
					break;
				case REJECT:
					newLocation = formatLocation(location, appLocation.REJECT.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(location, newLocation);
					break;
				case SAVE_FOR_LATER:		
					newLocation = formatLocation(location, appLocation.SAVE_FOR_LATER.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(location, newLocation);	
					break;
			}
				
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
