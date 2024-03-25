package com.github.responsePOJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorPOJO {

	@JsonProperty(value = "resource")
	public String resource;
	@JsonProperty(value = "code")
	public String code;
	@JsonProperty(value = "field")
	public String field;
	@JsonProperty(value = "message")
	public String message;

}
