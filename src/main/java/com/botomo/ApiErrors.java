package com.botomo;

import io.vertx.core.json.JsonObject;

public enum ApiErrors {

	DB000(500, "Critical database error occured"),
	DB001(404, "No book found for the provided ID"),
	DB002(400, "Id must not be empty or null"),
	DB003(400, "Request must contain a valid book formatted as json string");
	
	private final int statusCode;
	private final String msg;
	
	private ApiErrors(final int code, final String msg) {
		this.statusCode = code;
		this.msg = msg;
	}
	
	public String toJsonString(){
		
		JsonObject json = new JsonObject();
		json.put("errorCode", this.name());
		json.put("statusCode", this.statusCode);
		json.put("message", this.msg);
		
		return json.encodePrettily();
		
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMsg() {
		return msg;
	}
	
	public String getErrorCode(){
		return this.name();
	}
	
	
	
}
