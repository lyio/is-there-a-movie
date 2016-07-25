package com.botomo;

import io.vertx.core.json.JsonObject;

/**
 * Class to provide a collection of api errors. The errors comprise
 * a application related error code, a appropriate status code and 
 * a human readable message. The error object cann be used to communicate
 * application error to the front end through its toJsonString method.
 *
 */
public enum ApiErrors {

	// Database errors
	DB000(500, "Critical database error occured"),
	DB001(404, "No book found for the provided ID"),
	DB002(400, "Id must not be empty or null"),
	DB003(400, "Request must contain a valid book formatted as json string"),
	DB004(400, "Book with the provided title and author already exists"),
	
	// Validation error
	V000(400, "Invalid json format");
	
	private final int statusCode;
	private final String msg;
	
	private ApiErrors(final int code, final String msg) {
		this.statusCode = code;
		this.msg = msg;
	}
	
	/**
	 * Provides a json representation of this error
	 * @return
	 */
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
