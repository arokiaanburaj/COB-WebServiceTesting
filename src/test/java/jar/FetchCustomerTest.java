package jar;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import jar.dataprovider.*;
import jar.util.*;

public class FetchCustomerTest {
	@Test
	public void test_NumberOfConfigViews_ShouldBe20() {
	        
	    given().
	    when().
	        get("http://cob.com/api/config.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.viewId",hasSize(20));
	}
	

		@Test
	public void test_ResponseHeaderData_ShouldBeCorrect() {
	        
	    given().
	    when().
	        get("http://cob.com/api/f1/2017/viewTenant.json").
	    then().
	        assertThat().
	        statusCode(200).
	    and().
	        contentType(ContentType.JSON).
	    and().
	        header("Content-Length",equalTo("4567"));
	}
	
	@Test
	public void test_Md5CheckSumForTest_ShouldBe098f6bcd4621d373cade4e832627b4f6() {
	    
	    String originalText = "test";
	    String expectedMd5CheckSum = "098f6bcd4621d373cade4e832627b4f6";
	        
	    given().
	        param("text",originalText).
	    when().
	        get("http://md5.jsontest.com").
	    then().
	        assertThat().
	        body("md5",equalTo(expectedMd5CheckSum));
	}
	
	@Test
	public void test_NumberOfCircuits_ShouldBe20_Parameterized() {
	        
	    String season = "2017";
	    int numberOfRaces = 20;
	        
	    given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://cob.com/api/f1/{raceSeason}/viewCustomer.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.customerId",hasSize(numberOfRaces));
	}
	
	@Test(dataProvider="seasonsAndNumberOfRaces")
	public void test_NumberOfCircuits_ShouldBe_DataDriven(String season, int numberOfRaces) {
	                
	    given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://cob.com/api/f1/{raceSeason}/view.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.viewId",hasSize(numberOfRaces));
	}
	
	@Test
	public void test_APIWithBasicAuthentication_ShouldBeGivenAccess() {
	        
	    given().
	        auth().
	        preemptive().
	        basic("username", "password").
	    when().
	        get("http://path.to/basic/secured/api").
	    then().
	        assertThat().
	        statusCode(200);
	}
	
	@Test
	public void test_APIWithOAuth2Authentication_ShouldBeGivenAccess() {
	        
	    given().
	        auth().
	        oauth2(Constants.AUTHENTICATION_TOKEN).
	    when().
	        get("http://path.to/oath2/secured/api").
	    then().
	        assertThat().
	        statusCode(200);
	}
	
	
}
