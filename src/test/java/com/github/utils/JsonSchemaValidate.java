package com.github.utils;

import java.io.File;
import java.util.Objects;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonSchemaValidate {
    public static void validateSchema(String actualResponse , String schemaFile) {
        //Converting the expected Json to json schema
    	JSONObject jsonSchemaexpected = new JSONObject(new JSONTokener(Objects.requireNonNull(JsonSchemaValidate.class.getResourceAsStream("/ExpectedJsonSchema" + File.separator + schemaFile))));
       //Actual Json response is converted to json Object
    	JSONObject jsonSubjectActual = new JSONObject(new JSONTokener(actualResponse));  //Deserialization of json response.
        Schema schema = SchemaLoader.load(jsonSchemaexpected);
        try {
            schema.validate(jsonSubjectActual);
        } catch (Exception e) {
            log.error("Schema validation failed due to :: " + e.getMessage());
            Assert.fail("Schema validation failed due to :: " + e.getMessage());
        }
    }
    
    
    public static void validateSchemaInClassPath(Response actualResponse , String schemaFilePath) {
        actualResponse.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFilePath));
    }
}
