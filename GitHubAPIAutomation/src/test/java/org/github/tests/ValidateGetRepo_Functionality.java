package org.github.tests;


import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.github.responsePOJO.ResponsePOJO;
import org.github.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.base.APIHelper;
import com.github.base.BaseTest;

@Slf4j
public class ValidateGetRepo_Functionality extends BaseTest {
	
    APIHelper apiHelper;
    ExtentReportsUtility report =ExtentReportsUtility.getInstance(); ;
       
    @BeforeClass
    public void beforeClass() {
        apiHelper = new APIHelper();
      
       
    }
    
    
    @Test
    public void validateGetSingleRepo()
    {
    	
    	Response getRepoInfo= apiHelper.getSingleRepo();
    	Assert.assertEquals(getRepoInfo.getStatusCode(), HttpStatus.SC_OK, "Not able to get the repository info");
    	log.info("Status Code returned for GetSingleRepo():" +getRepoInfo.getStatusCode());
    	report.logTestInfo("Status Code returned for GetSingleRepo():" +getRepoInfo.getStatusCode());
    	log.info(getRepoInfo.prettyPrint());
    	
    	ResponsePOJO pojoSingleRepo = getRepoInfo.getBody().as(new TypeRef<ResponsePOJO>() {});
    	String fullName = pojoSingleRepo.getFullName();
    	log.info("full name on repo :" +fullName);
    	report.logTestInfo("full name on repo :" +fullName);
    	
    	String defaultBranch = pojoSingleRepo.getDefaultBranch();
    	log.info("default branch :" +defaultBranch);
    	report.logTestInfo("default branch :" +defaultBranch);
    	
    	String expectedFullName = EnvironmentDetails.getProperty("owner")+"/"+EnvironmentDetails.getProperty("reponame");
    	
    	Assert.assertEquals(getRepoInfo.getStatusCode(),HttpStatus.SC_OK,"Status code returned for GetSingleRepo info is incorrect");
    	Assert.assertEquals(fullName,expectedFullName,"Full Name shown in the repo info looks different");
       	Assert.assertEquals(defaultBranch,"main","default branch name for GetSingleRepo info is incorrect: "+defaultBranch);
    	Assert.assertEquals(getRepoInfo.getContentType(),"application/json; charset=utf-8","Content Type displayed incorrectly");
    	
    }
    
    @Test
    public void validateGetRepo_InvalidRepo()
    {
    	Response getInvalidRepoInfo= apiHelper.getNonExistantRepo();
    	Assert.assertEquals(getInvalidRepoInfo.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Response invalid when trying to get info non existent Repository");
    	log.info(getInvalidRepoInfo.prettyPrint());
    	ResponsePOJO pojoSingleRepo = getInvalidRepoInfo.getBody().as(new TypeRef<ResponsePOJO>() {});
    	String msg = pojoSingleRepo.message.toString();
    	log.info("msg seen when repo info is requested with invalid repo name: "+msg);
    	report.logTestInfo("msg seen when repo info is requested with invalid repo name: "+msg);
    	//Assert.assertEquals(getInvalidRepoInfo.getStatusCode(),HttpStatus.SC_NOT_FOUND,"wrong status code displayed");
    	Assert.assertEquals(msg,"Not Found","incorrect msg seen when non exixtent repo info is requested");
    }
    
    @Test
    public void validateGetAllRepositories()
    {
    	Response getAllRepoInfo= apiHelper.getAllRepositories();
    	Assert.assertEquals(getAllRepoInfo.getStatusCode(), HttpStatus.SC_OK, "Error when trying to get info of all repositories");
    	log.info(getAllRepoInfo.prettyPrint());
    	JsonSchemaValidate.validateSchema(getAllRepoInfo.asPrettyString(), "/GetAllUserReposSchema.json");
     	List<ResponsePOJO> allRepoInfo = getAllRepoInfo.as(new TypeRef<List<ResponsePOJO>>() {});
     	int totRepo = allRepoInfo.size();
     	log.info("total repositories:"+totRepo);
    	report.logTestInfo("total repositories:"+totRepo);
     	int cnt =0;
     	int totCnt=0;
     	ArrayList<String> repoArrName = new ArrayList<String>();
     	log.info("List of public repositories");
     	report.logTestInfo("List of public repositories");
     	for (int i=0;i<totRepo;i++)
     	{
     		if(allRepoInfo.get(i).getVisibility().equals("public"))
     		{
     			cnt = cnt +1;
     			String publicRepoName =   allRepoInfo.get(i).getName();
     			log.info("repo name:" +publicRepoName);
     			report.logTestInfo("repo name:" +publicRepoName);
       			repoArrName.add(publicRepoName);
       			
       		}
     		totCnt = cnt;
     	}
      	log.info("total public repositories:"+totCnt);
      	report.logTestInfo("total public repositories:"+totCnt);
     	log.info("Name of Public Repositories:" + repoArrName );
     	report.logTestInfo("Name of Public Repositories:" + repoArrName );
       	Assert.assertEquals(getAllRepoInfo.getContentType(),"application/json; charset=utf-8","Content Type displayed incorrectly for get all repo");
     }
    
    

}
