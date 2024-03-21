package com.github.CRUD;

import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.base.APIHelper;
import com.github.base.BaseTest;
import com.github.javafaker.Faker;
import com.github.requestPOJO.AddDataRequest;
import com.github.requestPOJO.DeleteDataRequest;
import com.github.requestPOJO.UpdateDataRequest;
import com.github.responsePOJO.GetDataResponse;
import com.github.utils.EnvironmentDetails;
import com.github.utils.ExtentReportsUtility;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

public class ValidateCRUDFunctionality extends BaseTest {
	APIHelper apiHelper;
	ExtentReportsUtility report = ExtentReportsUtility.getInstance();
	String full_name, name, description, homepage;
	boolean private1;
	private Faker faker;
	
	@BeforeClass
	public void beforeClass() {
		faker = new Faker();
		apiHelper = new APIHelper();

		Response login = apiHelper.login(EnvironmentDetails.getProperty("bearer_Token"));

	}

	@Test(priority = 3, description = "validate create or add repo data")
	public void validateCreateOrAddRepoDataFunctionality() {
		name = "PSHello-World-" + faker.number().numberBetween(10000, 20000);

		description = "This is your first repo!";
		homepage = "https://github.com";
		private1 = false;
		AddDataRequest addDataRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage)
				.private1(private1).build();
		Response response = apiHelper.addData(addDataRequest);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED,"Add data functionality is not working as expected.");
		// report.logTestInfo("Successfully created 201");
		String act_RepoName =response.body().jsonPath().get("findAll{it}.name").toString();
		Assert.assertEquals(act_RepoName, name);
		String actlogin =response.body().jsonPath().get("findAll{it}.owner.login").toString();
		Assert.assertEquals(actlogin, "priyankashinde22");
		String actType =response.body().jsonPath().get("findAll{it}.owner.type").toString();
		Assert.assertEquals(actType, "User");
		

	}

	@Test(priority = 4, description = "validate create or add the repo with existing name")
	public void validateCreateOrAddRepoWithExistingNameDataFunctionality() {
		name = "PSHello-World-13681";
		description = "This is your first repo!";
		homepage = "https://github.com";
		private1 = false;
		AddDataRequest addDataRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage)
				.private1(private1).build();
		Response response = apiHelper.addData(addDataRequest);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY,"name already exists on this account");
		// report.logTestInfo("Repository creation failed 422");
		String actMessage =response.body().jsonPath().get("findAll{it}.errors.message").toString();
		
		Assert.assertEquals(actMessage, "[name already exists on this account]");
			
			 
	}

	@Test(priority = 5, description = "validate Update the repository name data functionality")
	public void validateUpdateRepoDataFunctionality() {
		name = "PSHello-World-" + faker.number().numberBetween(10000, 20000);
		description = "my repository created using apis after update";
		private1 = false;
		UpdateDataRequest updateDataRequest = UpdateDataRequest.builder().name(name).description(description).private1(private1).build();
		Response response = apiHelper.patchData(updateDataRequest,EnvironmentDetails.getProperty("update_Repo"));
		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,"Update data functionality is not working as expected.");
		// report.logTestInfo("Repository updated 200");
		String actRepoName=response.body().jsonPath().get("findAll{it}.name").toString();
		System.out.println(actRepoName);
		Assert.assertEquals(actRepoName,name);
		//String actFullName=response.body().jsonPath().get("findAll{it}.full_name").toString();
		
		


	}

	@Test(priority = 0, description = "validate a single repository")
	public void validateASingleRepositoryGetData() {
		Response data = apiHelper.getData(EnvironmentDetails.getProperty("repoName"));
		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
		//report.logTestInfo("successfull login with statuscode 200");
		String actfull_name =data.body().jsonPath().get("findAll{it}.full_name").toString();
		Assert.assertEquals(actfull_name, "priyankashinde22/JavaPractice");
		String actcontentType = data.header("Content-Type"); 
		Assert.assertEquals(actcontentType, "application/json; charset=utf-8");
		

	}

	@Test(priority = 1, description = "Validate get a single repository with non existing repo name")
	public void validateANonExistingSingleRepositoryGetData() {

		Response data = apiHelper.getData(EnvironmentDetails.getProperty("nonExist_Repo"));

		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND,
				"Response code is not matching for get data.");
		// report.logTestInfo("Not Found 404");
		String actMsg=data.body().jsonPath().get("findAll{it}.message").toString();
		Assert.assertEquals(actMsg, "Not Found");

	}

	@Test(priority = 2, description = "Validate get all repositories")
	public void validateGetAllRepositories() {

		Response data = apiHelper.getData(EnvironmentDetails.getProperty("all_Repos"));

		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
		// report.logTestInfo("successful login with statuscode 200");
		String actcontentType = data.header("Content-Type"); 
		Assert.assertEquals(actcontentType, "application/json; charset=utf-8");
		List<Integer> RepoNameCount = data.body().jsonPath().getList("findAll{it->it.visibility=='public'}.full_name"); 
		System.out.println(RepoNameCount.size());
		List<String> RepoNames = data.body().jsonPath().getList("findAll{it->it.visibility=='public'}.full_name"); 
		for(String name:RepoNames)
		{
			System.out.println(name);
		}
		

	}



    @Test(priority = 6, description = "delete repo data functionality")
    public void validateDeleteRepoData() {
        DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().build();
        Response data = apiHelper.deleteData(deleteDataRequest,EnvironmentDetails.getProperty("delete_Repo"));
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Delete data functionality is not working as expected.");
     // report.logTestInfo("successful delete repo 204");
        String actMsg= data.body().asPrettyString();
        System.out.println(actMsg);
        Assert.assertEquals(actMsg, "");
        
        
       
    }

    @Test(priority = 7, description = "delete the repo with non existing name functionality")
    public void validateDeleteRepoNonExistingData() {
        DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().build();
        Response data = apiHelper.deleteData(deleteDataRequest,EnvironmentDetails.getProperty("delete_NonExist_Repo"));
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Delete data functionality is not working as expected.");
     // report.logTestInfo("Not Found 404");
       
    String actMsg= data.body().jsonPath().get("findAll{it}.message").toString();
    Assert.assertEquals(actMsg, "Not Found");
    
    
    }
	public GetDataResponse returnTheMatchingGetDataResponse(String full_name,List<GetDataResponse> getDataResponseList) {
		for (GetDataResponse dataResponse : getDataResponseList) {

			if (dataResponse.getFull_name().equals(full_name))
				return dataResponse;
		}
		return null;

	}

}
