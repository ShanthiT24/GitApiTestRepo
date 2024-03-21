package org.github.tests;


import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.github.utils.*;
import org.techArk.responsePOJO.StatusResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.base.APIHelper;
import com.github.base.BaseTest;

public class ValidateGetRepo_Functionality extends BaseTest {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance();
    APIHelper apiHelper;
       
    @BeforeClass
    public void beforeClass() {
        apiHelper = new APIHelper();
    }

   /* @Test(priority = 0, description = "validate login functionality with valid credentials")
    public void validateLoginWithValidCredentials() {
        Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_CREATED,"error occured with login");
        report.logTestInfo("successfull login witrh statuscode 201");
        JsonSchemaValidate.validateSchemaInClassPath(login, "ExpectedJsonSchema/LoginResponseSchema.json");
        report.logTestInfo("LoginResponse is validated against expected schema successfully");
         
    }

    

    @Test(priority = 1, description = "validate login functionality with invalid credentials")
    public void validateLoginWithInValidCredentials() {
        Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), "password");
        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "Login is not returning proper status code with invalid credentials.");
        StatusResponse statusResponse = login.as(StatusResponse.class);
        Assert.assertEquals(statusResponse.getStatus(), TestDataUtils.getProperty("invalidCredentialsMessage"), "Status message is not returning as expected");
    }*/
    
    @Test
    public void validateGetSingleRepo()
    {
    	Response getRepoInfo= apiHelper.getSingleRepo();
    	Assert.assertEquals(getRepoInfo.getStatusCode(), HttpStatus.SC_OK, "Not able to get the repository info");
    	getRepoInfo.prettyPrint();
    	
    }
    
    @Test
    public void validateGetRepo_InvalidRepo()
    {
    	Response getInvalidRepoInfo= apiHelper.getNonExistantRepo();
    	Assert.assertEquals(getInvalidRepoInfo.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Response invalid when trying to get info non existent Repository");
    	getInvalidRepoInfo.prettyPrint();
    	
    }
    
    @Test
    public void validateGetAllRepositories()
    {
    	Response getAllRepoInfo= apiHelper.getAllRepositories();
    	Assert.assertEquals(getAllRepoInfo.getStatusCode(), HttpStatus.SC_OK, "Error when trying to get info of all repositories");
    	getAllRepoInfo.prettyPrint();
    	JsonSchemaValidate.validateSchema(getAllRepoInfo.asPrettyString(), "GetAllUserRepos.json");
    	
    }

}
