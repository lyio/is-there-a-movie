package com.botomo.data;

import com.botomo.models.Book;

import io.vertx.core.json.JsonObject;

/**
 * Transfer object to reply messages from a verticle. Contains a state
 * which represents if the requested operation was successful or not. The
 * payload contains the content of the reply as String.
 * @author pode
 *
 */
public class AsyncReply{

	private final boolean state;
	
	private final String payload;
	
	public AsyncReply(final boolean state, final String payload) {
		this.state = state;
		this.payload = payload;
	}
	
	public AsyncReply(final String json) {
		JsonObject reply = new JsonObject(json);
		this.state = reply.getBoolean("state");
		this.payload = reply.getString("payload");
	}

	public boolean state(){
		return state;
	}
	
	public String payload(){
		return payload;
	}
	
	public String encode(){
		JsonObject json = new JsonObject();
		json.put("state", state);
		json.put("payload", payload);
		
		return json.encodePrettily();
	}

	public static void main(String[] args) {
		Book b = new Book();
		b.setTitle("TEST title");
		
		AsyncReply ar = new AsyncReply(true, b.toJson().encodePrettily());
		String arJson = ar.encode();
		System.out.println(arJson);
		AsyncReply ar2 = new AsyncReply(arJson);
		System.out.println(ar2);
	}
}
