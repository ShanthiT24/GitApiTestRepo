package org.github.tests;

import com.github.base.APIHelper;
import com.github.base.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

import org.apache.http.HttpStatus;
import org.github.requestPOJO.AddDataRequest;
//import org.github.requestPOJO.DeleteDataRequest;
import org.github.requestPOJO.UpdateDataRequest;
import org.github.responsePOJO.ResponsePOJO;
import org.github.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
public class ValidateAddUpdateDeleteRepo_Functionality extends BaseTest {
    APIHelper apiHelper;
    ExtentReportsUtility report=ExtentReportsUtility.getInstance();
    //String name,description,homepage;
	boolean privateVal;
    //private Faker faker;
    String dataId = "";
    
    @BeforeClass
    public void beforeClass() {
        //faker = new Faker();
        apiHelper = new APIHelper();
            
    }

    
    public String validateCreateRepoFunctionality() {
    /*	{"name":"Hello-World","description":"This is your first
    		repo!","homepage":"https://github.com","private":false,"} */
        //name = EnvironmentDetails.getProperty("newRepoName");
    	
    	String newRepoName;
    	Faker faker = new Faker();
    	newRepoName = faker.name().firstName();
        String name =  newRepoName;
        String description = "This is your first repo!";
        String homepage = "https://github.com";
        //privateVal=false;
        AddDataRequest createRepoRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage).build();
        Response response = apiHelper.addData(createRepoRequest);
        response.prettyPrint();
       
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Add data functionality is not working as expected.");
        log.info("name of repo created :" +name);
        report.logTestInfo("name of repo created:" +name);
              
        ResponsePOJO createRepoInfo = response.getBody().as(new TypeRef<ResponsePOJO>() {});
     
        String loginName = createRepoInfo.getOwner().getLogin();
        log.info("login Name :" + loginName);
        report.logTestInfo("login Name :" + loginName);
        		
        String type = createRepoInfo.getOwner().getType();
        log.info("type :" + type);
        report.logTestInfo("type :" + type);
        newRepoName = createRepoInfo.getName();
        String repoName=newRepoName;
        EnvironmentDetails.setProperty(repoName);
        return newRepoName;
   
     }
  
    @Test (priority = 1, description = "validate create repo functionality")
    public void validateCreateRepoFunctionality1() 
    {
    	validateCreateRepoFunctionality();
    }
    
    @Test(priority = 1, description = "validate add repo funct with existing repo name" )//, dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateCreateRepoFunc_ExistingRepoName() {
        /*	{"name":"Hello-World","description":"This is your first
        		repo!","homepage":"https://github.com","private":false,"} */
            //name = EnvironmentDetails.getProperty("newRepoName");
    		
    		String newRepoName = validateCreateRepoFunctionality();
            String name = newRepoName;
            String description = "This is your first repo!";
            String homepage = "https://github.com";
            AddDataRequest createRepoRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage).build();
            Response response = apiHelper.addData(createRepoRequest);
            log.info("response for createRepo with existing repo name:");
            log.info(response.asPrettyString());
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY, "Status code incorrect when adding a repo with an existing repo name");
            log.info("validating schema for status response");
            report.logTestInfo("validating schema for status response");
            JsonSchemaValidate.validateSchema(response.asPrettyString(), "/StatusResponseSchema.json");
            log.info("*****************Schema Validation Success *************************************");
     
           // ResponsePOJO abc =  response.getBody().as(ResponsePOJO.class);
            //String errmsg= abc.getErrors().get(0).getMsg().toString();
            //System.out.println("error msg: " + errMSg);
            ResponsePOJO responseInfo = response.getBody().as(new TypeRef<ResponsePOJO>() {});
            String errmsg = responseInfo.getErrors().get(0).getMsg().toString();
            log.info("Error msg on adding repo with existing repo name:" + errmsg );
            report.logTestInfo("Error msg on adding repo with existing repo name:" + errmsg );
            Assert.assertEquals(TestDataUtils.getProperty("errMsgExistingRepo"), errmsg,"proper err msg not displayed on creating repo with existing repo name");
        }
    
   
    
   @Test(priority = 1, description = "update repo func" ) //, dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateUpdateRepoFunctionality() {
        /*	{
		"name": "name",
		"description": "my repository created using apis after update",
		"private": "False"
		
		} */
	   		String newRepoName = validateCreateRepoFunctionality();
	   		String oldRepoName = newRepoName;
	   		Faker faker = new Faker();
    	    String updateRepoName = faker.animal().name();
            String name = updateRepoName;
            String description = "This is your first repo!";
            UpdateDataRequest updateRepoRequest = UpdateDataRequest.builder().name(name).description(description).build();
            Response response = apiHelper.putData(updateRepoRequest,oldRepoName);
            response.prettyPrint();
            log.info(response.prettyPrint());
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "update repo not working as expected. old repo name:"+oldRepoName+" .New Repo name:"+name);
            ResponsePOJO updateInfo = response.getBody().as(new TypeRef<ResponsePOJO>() {});
            String updatedName = updateInfo.getName();
            log.info("repo name updated to: "+ updatedName);
            report.logTestInfo("repo name updated to: "+ updatedName);
            Assert.assertEquals(updatedName,name, "updated Repo Name incorrect ");
  
    } 
    
    @Test(priority = 1, description = "delete repo func" )//,dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateDeleteRepo() throws InterruptedException {
    	String newRepoName = validateCreateRepoFunctionality();
    	Thread.sleep(5000);
   		String delRepoName = newRepoName;
   	    Response data = apiHelper.deleteRepo(delRepoName);
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Delete Repo functionality is not working as expected.");
        String msg1=data.getBody().prettyPrint();
        log.info("return body should be empty. body info seen : "+ msg1);
        report.logTestInfo("return body should be empty. body info seen : "+ msg1);
        Assert.assertEquals(msg1,"","Body of the response should be empty but it shows : "+msg1);
     }
    
    @Test
    public void validateDeleteRepo_NonExistentRepo() {
        Response data = apiHelper.deleteRepo_NonExistentRepo();
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Error on deleting a non existent repo");
        //JsonPath content = data.jsonPath();
        //String msg = content.get("message");
        ResponsePOJO deleteErrInfo = data.getBody().as(new TypeRef<ResponsePOJO>() {});
        String msg = deleteErrInfo.getMessage();
        log.info("msg after deleting the repo: "+msg );
        report.logTestInfo("msg after deleting the repo: "+msg );
        Assert.assertEquals(msg, "Not Found","Msg displayed on trying to delete a non existent repo is not as expected");
      }
    
  
}
