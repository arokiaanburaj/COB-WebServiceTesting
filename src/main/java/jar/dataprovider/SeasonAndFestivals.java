package jar.dataprovider;

import org.testng.annotations.DataProvider;

public class SeasonAndFestivals {
	@DataProvider(name="seasonsAndNumberOfRaces")
	public Object[][] createTestDataRecords() {
	    return new Object[][] {
	        {"2017",20},
	        {"2016",21},
	        {"1966",9}
	    };
	}
}
