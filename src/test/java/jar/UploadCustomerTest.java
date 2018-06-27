package jar;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import jar.dataprovider.*;
import jar.util.*;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.apache.commons.io.IOUtils;
import io.restassured.path.json.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.withArgs;
import static io.restassured.config.MultiPartConfig.multiPartConfig;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UploadCustomerTest {
	public TemporaryFolder folder = new TemporaryFolder();
	 @Test public void
	    file_uploading_works()throws IOException {
	        File file = folder.newFile("something");
	        IOUtils.write("Something21", new FileOutputStream(file));

	        RestAssuredMockMvc.given().
	                multiPart(file).
	        when().
	                post("/fileUpload").
	        then().
	                body("size", greaterThan(10)).
	                body("name", equalTo("file"));
	    }
}
