package ats.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;

@Service
public class S3Utils {
	
	private static Logger logger = LoggerFactory.getLogger(S3Utils.class);
	
	@Autowired
	private AppConfigUtils appConfigUtils;
	
	public boolean pushToS3(String fileName, String location, String companyName, int listingId, String path) {
		try {
			logger.info("Attempting to connect to S3 client.");
			MinioClient minioClient = new MinioClient(appConfigUtils.getS3Port(), appConfigUtils.getS3AccessKeyID(), appConfigUtils.getS3SecretAccessKey());
			logger.info("Successfully connected to S3 client.");
			boolean isExist = minioClient.bucketExists(appConfigUtils.getS3Bucket());
			if(!isExist) {
				minioClient.makeBucket(appConfigUtils.getS3Bucket());
				logger.info(appConfigUtils.getS3Bucket() + " created.");
			}
			minioClient.putObject(appConfigUtils.getS3Bucket(), companyName + "/" + listingId + "/" + location + "/" + fileName, path);	
			logger.info("Successfully pushed " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3 bucket.");
			return true;
		}catch(Exception e) {
			logger.error("Exception caught while pushing " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3");
			return false;
		}
	}
	
	

}
