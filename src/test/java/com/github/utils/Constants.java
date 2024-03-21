package com.github.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	
	public final static String PROJECT_HOME = System.getProperty("user.dir");
    public final static String MAIN_RESOURCES = PROJECT_HOME + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    public final static String TEST_RESOURCES = PROJECT_HOME + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    
    public final static String ENVIRONMENT_DETAILS_PROPERTIES_FILE = TEST_RESOURCES + File.separator + "EnvironmentDetails" + File.separator + "GithubTest.properties";
  //  public final static String ENVIRONMENT_DETAILS_PROPERTIES_FILE1= TEST_RESOURCES + File.separator + "EnvironmentDetails" + File.separator + "Reponame.properties";
    public final static String TESTDATA_PROPERTIES_FILE = TEST_RESOURCES + File.separator + "Testdata" + File.separator + "Testdata.properties";

    public final static String EXTENT_REPORT_FILE = PROJECT_HOME + File.separator + "NewExtentReports" + File.separator
            + "CompleteReport" + new SimpleDateFormat("dd-MM-yyyy HH-mm").format(new Date()) + ".html";


}
