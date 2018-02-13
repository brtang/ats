package ats.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import ats.database.models.Application;
import ats.database.models.Listing;
import ats.utils.S3Utils;

public class FileWorker implements Runnable{
	
	private Application application;
	
	private Listing listing;
	
	private String filePath;
	
	private S3Utils s3Util;
	
	public FileWorker(Application application, Listing listing, String filePath, S3Utils s3Util) {
		this.application = application;
		this.listing = listing;
		this.filePath = filePath;
		this.s3Util = s3Util;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("BRIANS THREAD!!!");
		System.out.println("Running....");
		try {
			boolean hasKeywords = false;
			List<String> keyWords = listing.getKeyWords();
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {
				String line = para.getText();
				System.out.println(line);
				for(String keyWord : keyWords) {
					if(StringUtils.containsIgnoreCase(line, keyWord)) {
						System.out.println("Line encountered keyword: " + keyWord + " " + line);
						hasKeywords = true;
					}
				}
			}
			fis.close();
			
			if(hasKeywords) {
				s3Util.pushToS3(file.getName(), "keywords", listing.getLister().getCompany().getCompanyName(), listing.getId(), filePath);
			}else {
				s3Util.pushToS3(file.getName(), "all", listing.getLister().getCompany().getCompanyName(), listing.getId(), filePath);
			}
			
			
			// Need to delete file from local repo 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
