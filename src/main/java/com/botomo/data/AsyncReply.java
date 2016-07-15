package com.botomo.data;

import com.botomo.models.Book;

import io.vertx.core.json.JsonObject;

/**
 * Transfer object to reply messages from a verticle. Contains a state
 * which represents if the requested operation was successful or not. The
 * payload contains the content of the reply as String.
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
	
	/**
	 * Converts this object to a json String representation to enable
	 * the transportation over the event bus.
	 * @return A json formatted String representing this object.
	 */
	public String toJsonString(){
		JsonObject json = new JsonObject();
		json.put("state", state);
		json.put("payload", payload);
		
		return json.encodePrettily();
	}
}
