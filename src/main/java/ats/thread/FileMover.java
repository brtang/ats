package ats.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ats.constants.AppLocation;
import ats.database.models.Application;
import ats.database.repositories.ApplicationRepository;
import ats.utils.BeanUtil;
import ats.utils.S3Utils;

public class FileMover implements Runnable{
	
	private static Logger logger = LoggerFactory.getLogger(FileMover.class);
	
	private S3Utils s3Util;
	
	private AppLocation appLocation;
	
	private Application application;
	
	public FileMover(S3Utils s3Util, AppLocation appLocation, Application application) {
		this.s3Util = s3Util;
		this.appLocation = appLocation;
		this.application = application;
	}
	
	private String formatLocation(String location, String newDirectory) {
		String[] locationParts = location.split("/");
		return locationParts[0] + "/" + locationParts[1] + "/" + newDirectory + "/" + locationParts[locationParts.length - 1];
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("FileMover Thread");
		try {
			String newLocation = null;
			// To DO: probably dont need this switch statement?
			// TO DO: Need to update Application's resumePath and save back into DB
			switch(appLocation) {			
				case FAVORITE:
					newLocation = formatLocation(application.getResumePath(), appLocation.FAVORITE.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(application.getResumePath(), newLocation);
					break;
				case REJECT:
					newLocation = formatLocation(application.getResumePath(), appLocation.REJECT.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(application.getResumePath(), newLocation);
					break;
				case SAVE_FOR_LATER:		
					newLocation = formatLocation(application.getResumePath(), appLocation.SAVE_FOR_LATER.toString().toLowerCase());
					System.out.println("New location: " + newLocation);
					s3Util.moveDirectory(application.getResumePath(), newLocation);	
					break;
			}
			ApplicationRepository applicationRepository = BeanUtil.getBean(ApplicationRepository.class);
			application.setResumePath(newLocation);	
			applicationRepository.save(application);
			logger.info("Saved application: " + application.getId() + " to new directory: " + newLocation);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
