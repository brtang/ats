package ats.utils;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.apache.commons.io.FileUtils;
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
			minioClient.putObject(appConfigUtils.getS3Bucket(), companyName + "/" + listingId + "/" + location + "/" + fileName, path + "/" + fileName);	
			logger.info("Successfully pushed " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3 bucket.");
			return true;
		}catch(Exception e) {
			// TO DO: Implement a retry mechanism
			e.printStackTrace();
			logger.error("Exception caught while pushing " + companyName + "/" + listingId + "/" + location + "/" + fileName + " to S3");
			return false;
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
	
	
	

}
