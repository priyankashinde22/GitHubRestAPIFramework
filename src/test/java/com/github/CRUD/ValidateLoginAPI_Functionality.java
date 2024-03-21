package com.github.CRUD;

import org.apache.http.HttpStatus;
import com.github.utils.EnvironmentDetails;
import com.github.utils.ExtentReportsUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.base.APIHelper;
import com.github.base.BaseTest;

import io.restassured.response.Response;


public class ValidateLoginAPI_Functionality extends BaseTest {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance();
	
    APIHelper apiHelper;

    @BeforeClass
    public void beforeClass() {
        apiHelper = new APIHelper();
       
    }

    @Test(priority = 0, description = "validate login functionality with valid credentials")
    public void validateLoginWithValidCredentials() {
        Response login = apiHelper.login(EnvironmentDetails.getProperty("bearer_Token"));

       Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_OK,"error occured with login");
        report.logTestInfo("successfull login with statuscode 200");
       
        //JsonSchemaValidate.validateSchemaInClassPath(login, "ExpectedJsonSchema/LoginResponseSchema.json");
       // report.logTestInfo("LoginResponse is validated against expected schema successfully");
         
    }

    

//    @Test(priority = 1, description = "validate login functionality with invalid credentials")
//    public void validateLoginWithInValidCredentials() {
//        Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), "password");
//        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "Login is not returning proper status code with invalid credentials.");
//        StatusResponse statusResponse = login.as(StatusResponse.class);
//        Assert.assertEquals(statusResponse.getStatus(), TestDataUtils.getProperty("invalidCredentialsMessage"), "Status message is not returning as expected");
//    }

}



