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
import com.github.responsePOJO.CreateDataResponse;
import com.github.responsePOJO.DeleteDataResponse;
import com.github.responsePOJO.ErrorResponsePOJO;
import com.github.responsePOJO.GetDataResponse;
import com.github.responsePOJO.NonExistRepoResponse;
import com.github.responsePOJO.UpdateDataResponse;
import com.github.utils.EnvironmentDetails;
import com.github.utils.ExtentReportsUtility;
import com.github.utils.JsonSchemaValidate;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

public class CheckCRUDFunctionality extends BaseTest {
	APIHelper apiHelper;
	ExtentReportsUtility report = ExtentReportsUtility.getInstance();
	String full_name, name, description, homepage;
	boolean private1;
	private Faker faker;
	String updatedRepoName = "";
	String newRepoName = "PSHello-World-14137";

	@BeforeClass
	public void beforeClass() {
		faker = new Faker();
		apiHelper = new APIHelper();

		Response login = apiHelper.login(EnvironmentDetails.getProperty("bearer_Token"));

	}

	@Test(priority = 0, description = "validate a single repository")
	public void validateASingleRepositoryGetData() {
		Response data = apiHelper.getData(EnvironmentDetails.getProperty("repoName"));
		GetDataResponse response = data.getBody().as(new TypeRef<GetDataResponse>() {
		});
		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
		 report.logTestInfo("successfull login with statuscode 200");

		Assert.assertEquals(response.getFull_name(), "priyankashinde22/JavaPractice");
		String actcontentType = data.header("Content-Type");
		Assert.assertEquals(actcontentType, "application/json; charset=utf-8");
		String actualResponse = data.jsonPath().prettyPrint();
		JsonSchemaValidate.validateSchema(actualResponse, "GetDataResponseSchema.json");
		 report.logTestInfo("Validated default branch is main/master");

	}

	@Test(priority = 1, description = "Validate get a single repository with non existing repo name")
	public void validateANonExistingSingleRepositoryGetData() {

		Response data = apiHelper.getData(EnvironmentDetails.getProperty("nonExist_Repo"));
		NonExistRepoResponse response = data.getBody().as(new TypeRef<NonExistRepoResponse>() {
		});

		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND,
				"Response code is not matching for get data.");
		 report.logTestInfo("Not Found 404");
		Assert.assertEquals(response.getMessage(), "Not Found");

	}

	@Test(priority = 2, description = "Validate get all repositories")
	public void validateGetAllRepositories() {

		Response data = apiHelper.getData();
		List<GetDataResponse> responseList = data.getBody().as(new TypeRef<List<GetDataResponse>>() {
		});
		int allrepoCount = responseList.size();
		System.out.println(allrepoCount);
		for (GetDataResponse response : responseList) {
			if (response.getVisibility().equals("public"))
				System.out.println(response.getFull_name());

		}
		Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
		 report.logTestInfo("successful login with statuscode 200");
		String actcontentType = data.header("Content-Type");
		Assert.assertEquals(actcontentType, "application/json; charset=utf-8");

	}

	@Test(priority = 3, description = "validate create or add repo data")
	public void validateCreateOrAddRepoDataFunctionality() {
		name = "PSHello-World-" + faker.number().numberBetween(10000, 20000);
		//newRepoName = name;
		description = "This is your first repo!";
		homepage = "https://github.com";
		private1 = false;
		AddDataRequest addDataRequest = AddDataRequest.builder().name(name).description(description).homepage(homepage)
				.private1(private1).build();
		Response response = apiHelper.addData(addDataRequest);

		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED,
				"Add data functionality is not working as expected.");
		 report.logTestInfo("Successfully created 201");
		CreateDataResponse createresponse = response.getBody().as(new TypeRef<CreateDataResponse>() {
		});
		Assert.assertEquals(createresponse.getName(), newRepoName);
		Assert.assertEquals(createresponse.getOwner().getLogin(), "priyankashinde22");
		Assert.assertEquals(createresponse.getOwner().getType(),"User");
		
		
	}
	
	@Test(priority = 4, description = "validate create or add the repo with existing name")
	public void validateCreateOrAddRepoWithExistingNameDataFunctionality() {
		//name = "PSHello-World-14137";
		System.out.println(newRepoName);
		description = "This is your first repo!";
		homepage = "https://github.com";
		private1 = false;
		AddDataRequest addDataRequest = AddDataRequest.builder().name(newRepoName).description(description).homepage(homepage)
				.private1(private1).build();
		Response response = apiHelper.addData(addDataRequest);
		
		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY,"name already exists on this account");
		 report.logTestInfo("Repository creation failed 422");
		ErrorResponsePOJO errporResponse =response.getBody().as(new TypeRef<ErrorResponsePOJO>() {});
		
		Assert.assertEquals(errporResponse.getErrors().get(0).message,"name already exists on this account");
		
			
			 
	}
	
	@Test(priority = 5, description = "validate Update the repository name data functionality")
	public void validateUpdateRepoDataFunctionality() {
		name = "PSHello-World-" + faker.number().numberBetween(10000, 20000);
		updatedRepoName=name;
		System.out.println(updatedRepoName);
		description = "my repository created using apis after update";
		private1 = false;
		UpdateDataRequest updateDataRequest = UpdateDataRequest.builder().name(name).description(description).private1(private1).build();
		Response response = apiHelper.patchData(updateDataRequest,EnvironmentDetails.getProperty("update_Repo"));
		Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,"Update data functionality is not working as expected.");
		report.logTestInfo("Repository updated 200");
		UpdateDataResponse updateResponse = response.getBody().as(new TypeRef<UpdateDataResponse>() {
		});
		Assert.assertEquals(updateResponse.getName(),name);


	}

	   @Test(priority = 6, description = "delete repo data functionality")
	    public void validateDeleteRepoData() {
	        DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().build();
	        Response data = apiHelper.deleteData(deleteDataRequest,EnvironmentDetails.getProperty("delete_Repo"));
	        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Delete data functionality is not working as expected.");
	        report.logTestInfo("successful delete repo 204");
	        String actMsg= data.body().asPrettyString();
	        System.out.println(actMsg);
	        Assert.assertEquals(actMsg, "");
	           
	       
	    }

	   @Test(priority = 7, description = "delete the repo with non existing name functionality")
	    public void validateDeleteRepoNonExistingData() {
	        DeleteDataRequest deleteDataRequest = DeleteDataRequest.builder().build();
	        Response data = apiHelper.deleteData(deleteDataRequest,EnvironmentDetails.getProperty("delete_NonExist_Repo"));
	        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Delete data functionality is not working as expected.");
	     report.logTestInfo("Not Found 404");
	        DeleteDataResponse deleteResponse = data.getBody().as(new TypeRef<DeleteDataResponse>() {
			});
	   
	    Assert.assertEquals(deleteResponse.getMessage(), "Not Found");
	    
	    
	    }
	   
	public GetDataResponse returnTheMatchingGetDataResponse(String full_name,
			List<GetDataResponse> getDataResponseList) {
		for (GetDataResponse dataResponse : getDataResponseList) {

			if (dataResponse.getFull_name().equals(full_name))
				return dataResponse;
		}
		return null;

	}

	public CreateDataResponse returnTheMatchingCreateDataResponse(String name, String login, String type,
			List<CreateDataResponse> createDataResponseList) {
		for (CreateDataResponse dataResponse : createDataResponseList) {

			return dataResponse;
		}
		return null;

	}
	public UpdateDataResponse returnTheMatchingUpdateDataResponse(String name,
			List<UpdateDataResponse> updateDataResponseList) {
		for (UpdateDataResponse updateResponse : updateDataResponseList) {

			return updateResponse;
		}
		return null;

	}
	
	public DeleteDataResponse returnTheMatchingDeleteDataResponse(String message,
			List<DeleteDataResponse> deleteDataResponseList) {
		for (DeleteDataResponse deleteResponse : deleteDataResponseList) {

			return deleteResponse;
		}
		return null;

	}
	
	public ErrorResponsePOJO returnTheMatchingErrorResponsePOJO(String message, List<ErrorResponsePOJO> errorMessageList) {
		for (ErrorResponsePOJO errporResponse : errorMessageList) {

			return errporResponse;
		}
		return null;

	}

}
