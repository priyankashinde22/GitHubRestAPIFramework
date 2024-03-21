package com.github.base;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpStatus;

import com.github.requestPOJO.AddDataRequest;
import com.github.requestPOJO.DeleteDataRequest;
//import com.github.requestPOJO.AddDataRequest;
//import com.github.requestPOJO.DeleteDataRequest;
import com.github.requestPOJO.LoginRequest;
import com.github.requestPOJO.UpdateDataRequest;
//import com.github.requestPOJO.UpdateDataRequest;
import com.github.responsePOJO.LoginResponse;
import com.github.utils.EnvironmentDetails;
import org.testng.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIHelper {
    RequestSpecification reqSpec;
    String id ;
    String token="";

    public APIHelper() {
    	System.out.println(EnvironmentDetails.getProperty("baseURL"));
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given();
       
    }

    public Response login(String bearer_Token) {
      
       LoginRequest loginRequest = LoginRequest.builder().bearer_Token(bearer_Token).build();
       
        reqSpec.headers(getHeaders(true));
        Response response = null;
        try {
            reqSpec.body(loginRequest); //Serializing loginrequest class to byte stream
             response = reqSpec.get();
             System.out.println(response.asPrettyString());
            if (response.getStatusCode() == HttpStatus.SC_OK) {
            	Assert.assertTrue(true,"Login functionality");
            	
            	//List<LoginResponse> loginResponse = response.getBody().as(new TypeRef<List<LoginResponse>>() {});
                //this.id = loginResponse.get(1).getId();
               // System.out.println("id====="+id);
                token =bearer_Token;
                System.out.println(token);
            }
        } catch (Exception e) {
            Assert.fail("Login functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response getData(String repoInfo) {
    	//reqSpec = RestAssured.given().auth().oauth2(token);
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
  
            response = reqSpec.get(repoInfo);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }
   
    
    public Response getData() {
    	//reqSpec = RestAssured.given().auth().oauth2(token);
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
           
            response = reqSpec.get("/user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }

    
    public Response addData(AddDataRequest addDataRequest) {
        //reqSpec = RestAssured.given().auth().oauth2(token);
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(addDataRequest));
            reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(addDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.post("/user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Add data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response patchData(UpdateDataRequest updateDataRequest,String repo) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(updateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.patch(repo);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response deleteData(DeleteDataRequest deleteDataRequest,String repo) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.delete(repo);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public HashMap<String, String> getHeaders(boolean forLogin) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        
        if (!forLogin) {
            //headers.put("bearer_Token", token);
        	headers.put("Authorization", "Bearer " + EnvironmentDetails.getProperty("bearer_Token")); 
        }
        return headers;
    }

}
