package org.github.tests;

import com.github.base.APIHelper;
import com.github.base.BaseTest;
import com.github.javafaker.Faker;

import io.restassured.common.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.github.requestPOJO.AddDataRequest;
//import org.github.requestPOJO.DeleteDataRequest;
import org.github.requestPOJO.UpdateDataRequest;
import org.github.utils.*;
import org.techArk.responsePOJO.AddDataResponse;
import org.techArk.responsePOJO.GetDataResponse;
import org.techArk.responsePOJO.LoginResponse;
import org.techArk.responsePOJO.StatusResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class ValidateAddUpdateDeleteRepo_Functionality extends BaseTest {
    APIHelper apiHelper;
    //String userId, accountNo, departmentNo, salary, pincode;
    String name,description,homepage,privateVal;
    private Faker faker;
    String dataId = "";

    @BeforeClass
    public void beforeClass() {
        faker = new Faker();
        apiHelper = new APIHelper();
       // Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
        //userId = login.getBody().as(new TypeRef<List<LoginResponse>>() {}).get(0).getUserid();
    }

    @Test(priority = 0, description = "validate create repo functionality")
    public void validateCreateRepoFunctionality() {
    /*	{"name":"Hello-World","description":"This is your first
    		repo!","homepage":"https://github.com","private":false,"} */
        name = EnvironmentDetails.getProperty("newRepoName");
        description = "This is your first repo!";
        homepage = "https://github.com";
        privateVal = "false";
        AddDataRequest createRepoRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage).build();
        Response response = apiHelper.addData(createRepoRequest);
        String newRepoName= response.jsonPath().getString("$.name");
        String loginName = response.jsonPath().getString("$.owner.login");
        String type = response.jsonPath().getString("$.owner.type");
        
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Add data functionality is not working as expected.");
        System.out.println("name of repo :" +EnvironmentDetails.getProperty("newRepoName"));
        String name1 = EnvironmentDetails.getProperty("newRepoName");
        //Assert.assertEquals(name1,newRepoName, "Repo Name not same as Name given during adding a repo ");
       // Assert.assertEquals(EnvironmentDetails.getProperty("owner"), loginName, "Name of owner displayed shown different for newly created repo ");
        //Assert.assertEquals("User", type, "Type of user displayed incorrect for newly created repo ");
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "StatusResponseSchema.json");
       

    }
    
    
    @Test(priority = 1, description = "validate add repo funct with existing repo name", dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateCreateRepoFunc_ExistingRepoName() {
        /*	{"name":"Hello-World","description":"This is your first
        		repo!","homepage":"https://github.com","private":false,"} */
            name = EnvironmentDetails.getProperty("newRepoName");
            description = "This is your first repo!";
            homepage = "https://github.com";
            privateVal = "false";
            AddDataRequest createRepoRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage).build();
            Response response = apiHelper.addData(createRepoRequest);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY, "Status code incorrect when adding a repo with an existing repo name");
           // Assert.assertEquals(response.as(AddDataResponse.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
            //JsonSchemaValidate.validateSchema(response.asPrettyString(), "StatusResponseSchema.json");
            String errmsg = response.jsonPath().getString("$.errors[0].message");
            Assert.assertEquals(TestDataUtils.getProperty("errMsgExistingRepo"), errmsg,"proper err msg not displayed on creating repo with existing repo name");
        }
    
    
    
    @Test(priority = 1, description = "update repo func", dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateUpdateRepoFunctionality() {
        /*	{
		"name": "name",
		"description": "my repository created using apis after update",
		"private": "False"
		
		} */
    		String oldRepoName = EnvironmentDetails.getProperty("newRepoName");
            name = EnvironmentDetails.getProperty("updateRepoName");
            description = "This is your first repo!";
            //privateVal = "false";
            UpdateDataRequest updateRepoRequest = UpdateDataRequest.builder().name(name).description(description).build();
            Response response = apiHelper.putData(updateRepoRequest);
            response.prettyPrint();
            String newRepoName= response.jsonPath().getString("$.name");
            //response.jsonPath().getString("$.name");
            System.out.println("new repo name :" + newRepoName);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "update repo not working as expected. old repo name:"+oldRepoName+" .New Repo name:"+name);
            //Assert.assertEquals(response.jsonPath().getString("$.name"),"myTestRepo27", "updated Repo Name incorrect ");
            
    }
    
    @Test(priority = 1, description = "delete repo func",dependsOnMethods = "validateCreateRepoFunctionality")
    public void validateDeleteRepo() {
        //DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().userId(userId).id(dataId).build();
        Response data = apiHelper.deleteRepo();
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Delete Repo functionality is not working as expected.");
       // Assert.assertEquals(data.as(StatusResponse.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
        String actualResponse = data.jsonPath().prettyPrint();
        //System.out.println(actualResponse);
       // data.jsonPath().prettyPrint();
        JsonPath content = data.jsonPath();
        String msg = content.get("message");
        Assert.assertEquals(msg, "Not Found","Msg displayed on trying to delete a non existent repo is not as expected");
        //JsonSchemaValidate.validateSchema(actualResponse, "StatusResponseSchema.json");
    }
    
    @Test
    public void validateDeleteRepo_NonExistentRepo() {
        //DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().userId(userId).id(dataId).build();
        Response data = apiHelper.deleteRepo_NonExistentRepo();
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Error on deleting a non existent repo");
       // Assert.assertEquals(data.as(StatusResponse.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
        JsonPath content = data.jsonPath();
        String msg = content.get("message");
        Assert.assertEquals(msg, "Not Found","Msg displayed on trying to delete a non existent repo is not as expected");
        //System.out.println("shanthi:"+msg);

     }
    
  

}
