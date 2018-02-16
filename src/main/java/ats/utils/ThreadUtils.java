package ats.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import ats.constants.AppLocation;
import ats.database.models.Application;
import ats.database.models.Listing;
import ats.thread.FileMover;
import ats.thread.FileWorker;

@Service
public class ThreadUtils {
	
	private static ExecutorService threadExecutor;
	
	@PostConstruct
	public void init() {
		System.out.println("BEFOREEEEEEEEEEEEEEEEEEEE!!!!!!!!!!!!!!!!!!!");
		threadExecutor = Executors.newFixedThreadPool(15);
	}
	
	public void scheduleFileMover(S3Utils s3Util, AppLocation appLocation, String location) {
		Runnable mover = new FileMover(s3Util, appLocation, location);
		threadExecutor.execute(mover);
	}
	
	public void scheduleFileWorker(Application application, Listing listing, String filePath, S3Utils s3Util, String fileExtension) {
		Runnable worker = new FileWorker(application, listing, filePath, s3Util, fileExtension);
		threadExecutor.execute(worker);
	}
}
