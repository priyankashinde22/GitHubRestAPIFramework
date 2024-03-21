package com.github.base;

import org.testng.annotations.BeforeSuite;

import com.github.utils.EnvironmentDetails;
import com.github.utils.TestDataUtils;

public class BaseTest {
	@BeforeSuite
    public void beforeSuite() {
        EnvironmentDetails.loadProperties();
        TestDataUtils.loadProperties();
    }

}
