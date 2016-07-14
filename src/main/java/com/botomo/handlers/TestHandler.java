package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.*;

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
		vertx.eventBus().send(ADD_ONE, b.toJson().encodePrettily(), result -> {
			if((Boolean)(result.result().body()) == true){
				ctx.response()
				.setStatusCode(201)
				.end();
			}else{
				ctx.response()
				.setStatusCode(500)
				.end();
			}
		});
//		vertx.eventBus().send(ADD_ONE, "", result -> {
//			if((Boolean)(result.result().body()) == true){
//				ctx.response()
//				.setStatusCode(201)
//				.end();
//			}else{
//				ctx.response()
//				.setStatusCode(500)
//				.end();
//			}
//		});

	}
}
