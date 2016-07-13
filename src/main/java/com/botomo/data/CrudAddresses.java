package com.botomo.data;

public enum CrudAddresses {

	GET_ALL("botomo.db.getAll");
	
	private String message;
	
	private CrudAddresses(final String message) {
		this.message = message;
	}
	
	public String message(){
		return this.message;
	}
	
}
