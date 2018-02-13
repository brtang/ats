package ats.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import ats.database.models.Application;
import ats.database.models.Listing;

public class FileWorker implements Runnable{
	
	private Application application;
	
	private Listing listing;
	
	private String filePath;
	
	public FileWorker(Application application, Listing listing, String filePath) {
		this.application = application;
		this.listing = listing;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("BRIANS THREAD!!!");
		System.out.println("Running....");
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {
				System.out.println(para.getText());
			}
			fis.close();
			Thread.sleep(10000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
