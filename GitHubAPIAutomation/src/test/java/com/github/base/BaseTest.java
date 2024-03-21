package com.github.base;

import org.github.utils.EnvironmentDetails;
import org.github.utils.TestDataUtils;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    @BeforeSuite
    public void beforeSuite() {
        EnvironmentDetails.loadProperties();
        TestDataUtils.loadProperties();
    }
}
