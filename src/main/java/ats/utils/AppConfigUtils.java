package ats.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"file:src/main/java/resources/appConfig.properties"})
public class AppConfigUtils {

	// s3 configuration properties
	@Value("${s3.port}")
	private String s3Port;
	@Value("${s3.bucket}")
	private String s3Bucket;
	@Value("${s3.accessKeyID}")
	private String s3AccessKeyID;
	@Value("${s3.secretAccessKey}")
	private String s3SecretAccessKey;
	
	public String getS3Port() {
		return s3Port;
	}
	public String getS3Bucket() {
		return s3Bucket;
	}
	public String getS3AccessKeyID() {
		return s3AccessKeyID;
	}
	public String getS3SecretAccessKey() {
		return s3SecretAccessKey;
	}
	
}
