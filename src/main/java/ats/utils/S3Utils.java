package ats.utils;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ats.constants.Errors;
import io.minio.MinioClient;

@Service
public class S3Utils {
	
	private static Logger logger = LoggerFactory.getLogger(S3Utils.class);
	
	@Autowired
	private AppConfigUtils appConfigUtils;
	
	public String pushToS3(String fileName, String location, String companyName, int listingId, String path) {
		try {
			String s3Path = companyName + "/" + listingId + "/" + location + "/" + fileName;
			logger.info("Attempting to connect to S3 client.");
			MinioClient minioClient = new MinioClient(appConfigUtils.getS3Port(), appConfigUtils.getS3AccessKeyID(), appConfigUtils.getS3SecretAccessKey());
			logger.info("Successfully connected to S3 client.");
			boolean isExist = minioClient.bucketExists(appConfigUtils.getS3Bucket());
			if(!isExist) {
				minioClient.makeBucket(appConfigUtils.getS3Bucket());
				logger.info(appConfigUtils.getS3Bucket() + " created.");
			}
			minioClient.putObject(appConfigUtils.getS3Bucket(), s3Path, path);	
			logger.info("Successfully pushed " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3 bucket.");
			return s3Path;
		}catch(Exception e) {
			// TO DO: Implement a retry mechanism
			e.printStackTrace();
			logger.error("Exception caught while pushing " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3");
			return Errors.FAILED_TO_UPLOAD_TO_S3;
		}
	}
	
	public String saveFileToLocal(ByteArrayInputStream fileData, String fileName, String path) {
		try {
			String filePath = path + "/" + fileName;
			File directory = new File(path);
			File fileCsv = new File(filePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			FileUtils.copyInputStreamToFile(fileData, fileCsv);
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public boolean moveDirectory(String location, String newLocation) {
		try {	
			logger.info("Attempting to connect to S3 client.");
			MinioClient minioClient = new MinioClient(appConfigUtils.getS3Port(), appConfigUtils.getS3AccessKeyID(), appConfigUtils.getS3SecretAccessKey());
			logger.info("Successfully connected to S3 client.");
			boolean isExist = minioClient.bucketExists(appConfigUtils.getS3Bucket());
			if(!isExist) {
				minioClient.makeBucket(appConfigUtils.getS3Bucket());
				logger.info(appConfigUtils.getS3Bucket() + " created.");
			}
			minioClient.copyObject(appConfigUtils.getS3Bucket(), location, appConfigUtils.getS3Bucket(), newLocation);
			minioClient.removeObject(appConfigUtils.getS3Bucket(), location);
			logger.info("Successfully copied object from: " + location + " to: " + newLocation);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
