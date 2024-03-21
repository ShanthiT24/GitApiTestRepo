package com.github.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.github.requestPOJO.AddDataRequest;
//import org.github.requestPOJO.DeleteDataRequest;
//import org.github.requestPOJO.LoginRequest;
import org.github.requestPOJO.UpdateDataRequest;
import org.github.utils.EnvironmentDetails;
import org.techArk.responsePOJO.LoginResponse;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class APIHelper {
    public RequestSpecification reqSpec;
    String token = "";
    String bearer = EnvironmentDetails.getProperty("bearer");
    String owner = EnvironmentDetails.getProperty("owner");
    String repoName = EnvironmentDetails.getProperty("reponame");
    String invalidRepoName = EnvironmentDetails.getProperty("invalidreponame");
    String updateRepoName = EnvironmentDetails.getProperty("updateRepoName");
    String oldRepoName = EnvironmentDetails.getProperty("newRepoName");
    String newRepoName = EnvironmentDetails.getProperty("newRepoName");
    String nonExistentRepoName = EnvironmentDetails.getProperty("nonExistentRepoName");
    

    public APIHelper() {
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given()
        		.header("Authorization", "Bearer " + bearer);
        		
       
    }
    
 
    public Response getSingleRepo() {
        // reqSpec = RestAssured.given();
        //reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",repoName)
            		.get("/repos/{owner}/{repo}");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    public Response getNonExistantRepo() {
        // reqSpec = RestAssured.given();
        //reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",invalidRepoName)
            		.get("/repos/{owner}/{repo}");
            		
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    public Response getAllRepositories() {
        // reqSpec = RestAssured.given();
        //reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            response = reqSpec
                   		.get("user/repos");
            		
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    

    public Response addData(AddDataRequest addDataRequest) {
        //reqSpec = RestAssured.given();
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(addDataRequest));
            //reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(addDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.post("user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Add data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response putData(UpdateDataRequest updateDataRequest) {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(updateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",oldRepoName)
            		.patch("/repos/{owner}/{repo}");
            		
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response deleteRepo() {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
        	System.out.println("delete repo owner:"+owner);
        	System.out.println("delete repo newRepoName: "+newRepoName);
            //reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",newRepoName)
            		.delete("/repos/{owner}/{repo}");
            System.out.println("Response in delete repo:"+ response.prettyPrint());
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    
    public Response deleteRepo_NonExistentRepo() {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            //reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",nonExistentRepoName)
            		.delete("/repos/{owner}/{repo}");
            
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    
}
