package com.github.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.github.requestPOJO.AddDataRequest;
import org.github.requestPOJO.UpdateDataRequest;
import org.github.utils.EnvironmentDetails;
import org.github.utils.ExtentReportsUtility;
import org.testng.Assert;

@Slf4j
public class APIHelper {
	public Faker faker;
    public RequestSpecification reqSpec;
    String token = "";
    String bearer = EnvironmentDetails.getProperty("bearer");
    String owner = EnvironmentDetails.getProperty("owner");
    String repoName = EnvironmentDetails.getProperty("reponame");
    ExtentReportsUtility report=ExtentReportsUtility.getInstance();
  

    public APIHelper() {
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given()
        		.header("Authorization", "Bearer " + bearer);
       log.info("Testcase Base Info:");
       log.info("Base URL: " +EnvironmentDetails.getProperty("baseURL"));
       log.info("Authorization token (Bearer): " +EnvironmentDetails.getProperty("bearer"));
       
       //newRepoName = faker.animal().toString();
       
       //report.logTestInfo("Testcases Base Info:");
       //report.logTestInfo("Base URL: " +EnvironmentDetails.getProperty("baseURL"));
       //report.logTestInfo("Authorization token (Bearer): " +EnvironmentDetails.getProperty("bearer"));
       
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
            Assert.fail("Get Single Repo is failing due to :: " + e.getMessage());
            log.info("Get Single Repo is failing due to :: " + e.getMessage());
           report.logTestInfo("Get Single Repo is failing due to :: " + e.getMessage());
        }
        log.info("response for getSingleRepo()");
        log.info(response.asPrettyString());
        return response;
       
    }
    
    public Response getNonExistantRepo() {
        // reqSpec = RestAssured.given();
        //reqSpec.headers(getHeaders(false));
    	faker = new Faker();
    	String invalidRepoName = "Faker"+faker.funnyName();
    	Response response = null;
        try {
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",invalidRepoName)
            		.get("/repos/{owner}/{repo}");
            		
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get info for non existant repo is failing due to :: " + e.getMessage());
            log.info("Get info for non existant repo is failing due to :: " + e.getMessage());
            report.logTestInfo("Get info for non existant repo is failing due to :: " + e.getMessage());
        }
        log.info("response for getNonExistantRepo()");
        log.info(response.asPrettyString());
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
            Assert.fail("Get All repos data is failing due to :: " + e.getMessage());
            log.info("Get All repos data is failing due to :: " + e.getMessage());
            report.logTestInfo("Get All repos data is failing due to :: " + e.getMessage());
        }
        log.info("response for getAllRepositories()" );
        log.info(response.asPrettyString());
        return response;
    }
    
    

    public Response addData(AddDataRequest addDataRequest) {
        //reqSpec = RestAssured.given();
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(addDataRequest));
           //.logTestInfo("Adding below data :: " + new ObjectMapper().writeValueAsString(addDataRequest));
            //reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(addDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.post("user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("create new repo functionality is failing due to :: " + e.getMessage());
            log.error("create new repo functionality is failing due to :: " + e.getMessage());
           report.logTestInfo("create new repo functionality is failing due to :: " + e.getMessage());
        }
        log.info("response for add repo");
        log.info(response.asPrettyString());
        return response;
    }

    public Response putData(UpdateDataRequest updateDataRequest, String oldRepoName) {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
        	 log.info("updating below data :: " + new ObjectMapper().writeValueAsString(updateDataRequest));
             report.logTestInfo("updating below data :: " + new ObjectMapper().writeValueAsString(updateDataRequest));
            reqSpec.body(new ObjectMapper().writeValueAsString(updateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",oldRepoName)
            		.patch("/repos/{owner}/{repo}");
            		
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update data functionality is failing due to :: " + e.getMessage());
            log.error("Update data functionality is failing due to :: " + e.getMessage());
            report.logTestInfo("Update data functionality is failing due to :: " + e.getMessage());
        }
        log.info("Update Data");
        log.info(response.asPrettyString());
        return response;
    }

    public Response deleteRepo(String newRepoName1) {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
        	log.info("delete repo owner:"+owner);
        	report.logTestInfo("delete repo owner:"+owner);
        	log.info("delete repo newRepoName: "+newRepoName1);
        	report.logTestInfo("delete repo newRepoName: "+newRepoName1);
        	
            //reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",newRepoName1)
            		.delete("/repos/{owner}/{repo}");
            System.out.println("Response in delete repo:"+ response.prettyPrint());
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
            log.error("Delete data functionality is failing due to :: " + e.getMessage());
           report.logTestFailed("Delete data functionality is failing due to :: " + e.getMessage());
        }
        log.info("response for deleteRepo()");
        log.info(response.asPrettyString());
        return response;
    }
    
    
    public Response deleteRepo_NonExistentRepo() {
        //reqSpec = RestAssured.given();
       // reqSpec.headers(getHeaders(false));
    	faker = new Faker();
    	String nonExistentRepoName = "Faker"+faker.funnyName();
    	log.info("Non Existent repo name :"+nonExistentRepoName);
    	report.logTestInfo("Non Existent repo name :"+nonExistentRepoName);
        Response response = null;
        try {
            //reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec
            		.pathParam("owner",owner)
            		.pathParam("repo",nonExistentRepoName)
            		.delete("/repos/{owner}/{repo}");
            
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete repo functionality is failing due to :: " + e.getMessage());
            log.error("Delete repo functionality is failing due to :: " + e.getMessage());
            report.logTestFailed("Delete repo functionality is failing due to :: " + e.getMessage());
        }
        log.info("reponse for deleteRepo_NonExistentRepo");
        log.info(response.asPrettyString());
        return response;
    }

    
}
