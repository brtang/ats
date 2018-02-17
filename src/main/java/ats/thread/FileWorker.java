package ats.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import ats.constants.Constants;
import ats.database.models.Application;
import ats.database.models.Listing;
import ats.database.repositories.ApplicationRepository;
import ats.utils.BeanUtil;
import ats.utils.S3Utils;

public class FileWorker implements Runnable{
	
	private static Logger logger = LoggerFactory.getLogger(FileWorker.class);
	
	private Application application;
	
	private Listing listing;
	
	private String filePath;
	
	private S3Utils s3Util;
	
	private String fileExtension;
	
	public FileWorker(Application application, Listing listing, String filePath, S3Utils s3Util, String fileExtension) {
		this.application = application;
		this.listing = listing;
		this.filePath = filePath;
		this.s3Util = s3Util;
		this.fileExtension = fileExtension;
	}
	
	public boolean parseFile(File file, List<String> keyWords) throws IOException {
		boolean hasKeywords = false;
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		if(fileExtension.equals("docx")) {
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {
				String line = para.getText();
				System.out.println(line);
				for(String keyWord : keyWords) {
					if(StringUtils.containsIgnoreCase(line, keyWord)) {
						hasKeywords = true;
					}
				}
			}
			document.close();
		}else if(fileExtension.equals("doc")) {
			HWPFDocument document = new HWPFDocument(fis);
			WordExtractor we = new WordExtractor(document);
			String[] lines = we.getParagraphText();
			for(String line : lines) {
				System.out.println(line);
				for(String keyWord : keyWords) {
					if(StringUtils.containsIgnoreCase(line, keyWord)) {
						hasKeywords = true;
					}
				}
			}
			we.close();			
		}		
		fis.close();	
		return hasKeywords;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("FileWorker Thread");
		System.out.println("Running....");
		try {
			File file = new File(filePath);
			String s3Path;
			if(parseFile(file, listing.getKeyWords())) {
				s3Path = s3Util.pushToS3(file.getName(), Constants.KEYWORD, listing.getLister().getCompany().getCompanyName(), listing.getId(), filePath);
				application.setResumePath(s3Path);
			}else {
				s3Path = s3Util.pushToS3(file.getName(), Constants.ALL, listing.getLister().getCompany().getCompanyName(), listing.getId(), filePath);	
			}			
			ApplicationRepository applicationRepository = BeanUtil.getBean(ApplicationRepository.class);
			application.setResumePath(s3Path);	
			applicationRepository.save(application);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

}
