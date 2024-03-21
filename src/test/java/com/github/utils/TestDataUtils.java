package com.github.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class TestDataUtils {
	
	static Properties prop = new Properties();

    public static void loadProperties() {
        try {
            File propertiesFile = new File(Constants.TESTDATA_PROPERTIES_FILE);
            prop.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

}
