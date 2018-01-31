package hello;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import ats.Application;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@WebAppConfiguration
public class CompaniesTests {
	private static boolean setUpIsDone = false;
	
	
	@Before()
	public void setUp() {
		if(setUpIsDone) {
			return;
		}
		SpringApplication.run(Application.class);
		setUpIsDone = true;
	}
	
	@Test
	public void testCompaniesAPI() throws UnirestException {
		// Test for POST /company
		String testName = "test";
		HttpResponse<com.mashape.unirest.http.JsonNode> resp = Unirest.post("http://localhost:3000/company")
				.header("name", testName)
				.asJson();
		JSONObject obj = (JSONObject) resp.getBody().getObject().get("Company");
		String expectedName = (String) obj.get("companyName");
		assertEquals("test",expectedName);
		
		
	}

}
