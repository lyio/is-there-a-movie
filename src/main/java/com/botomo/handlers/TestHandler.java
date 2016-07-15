package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.*;

import com.botomo.data.AsyncReply;
import com.botomo.models.Book;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public class TestHandler implements Handler<RoutingContext> {
	
	private Vertx vertx;
	
	public TestHandler(Vertx vertx) {
		this.vertx = vertx;
	}
	
	@Override
	public void handle(RoutingContext ctx) {
		Book b = new Book();
		b.setTitle("ADD TEST");
		b.setSubtitle("ADD TEST SUB");
		b.setAuthor("Mr. ADD TEST");
		b.setYear("1234");
		b.setUps(12);
		b.setDowns(98);
		System.out.println("TRY TO INSERT BOOK");
		vertx.eventBus().send(DOWN_VOTE, "57869b216dc71db38b5780e1", result -> {
			AsyncReply ar = new AsyncReply((String)result.result().body());
			System.out.println("PAYLOAD: " + ar.payload());
			if(ar.state()){
				ctx.response()
				.setStatusCode(200)
				.end(ar.payload());
			}else{
				ctx.response()
				.setStatusCode(500)
				.end(ar.payload());
			}
		});
//		
		// Positiv result of add one
//		vertx.eventBus().send(ADD_ONE, b.toJson().encodePrettily(), result -> {
//			AsyncReply ar = new AsyncReply((String)result.result().body());
//			System.out.println("PAYLOAD: " + ar.payload());
//			
//			if(ar.state()){
//				ctx.response()
//				.setStatusCode(201)
//				.end();
//			}else{
//				ctx.response()
//				.setStatusCode(500)
//				.end();
//			}
//		});
		//Negative result of add one
//		vertx.eventBus().send(ADD_ONE, "", result -> {
//			AsyncReply ar = new AsyncReply((String)result.result().body());
//			System.out.println("PAYLOAD: " + ar.payload());
//			if(ar.state()){
//				ctx.response()
//				.setStatusCode(200)
//				.end(ar.payload());
//			}else{
//				ctx.response()
//				.setStatusCode(500)
//				.end(ar.payload());
//			}
//		});
//
	}
}
