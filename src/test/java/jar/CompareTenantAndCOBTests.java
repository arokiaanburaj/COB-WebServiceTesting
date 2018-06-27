package jar;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import jar.dataprovider.*;
import jar.util.*;

public class CompareTenantAndCOBTests {

	@Test
	public void test_ScenarioRetrieveCustomerFromTenant_ShouldBeEqualInCOB() {
	        
	    // First, retrieve the circuit ID for the first circuit of the 2017 season
	    String customerId = given().
	    when().
	        get("http://tenant.com/api/f1/2017/customer.json").
	    then().
	        extract().
	        path("MRData.CustomerTable.customerId[0]");
	        
	    // Then, retrieve the information known for that circuit and verify it is located in Australia
	    given().
	        pathParam("customerId",customerId).
	    when().
	        get("http://cob.com/api/f1/customers/{customerId}.json").
	    then().
	        assertThat().
	        body("MRData.CustomerTable.customerId[0].addressZipcode",equalTo("54324"));
	}
	
}
