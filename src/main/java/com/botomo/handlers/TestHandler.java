package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.GET_ALL;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public class TestHandler implements Handler<RoutingContext> {
	
	private Vertx vertx;
	
	public TestHandler(Vertx vertx) {
		this.vertx = vertx;
	}
	
	@Override
	public void handle(RoutingContext event) {
		vertx.eventBus().send(GET_ALL,
				null, reply -> {
					String json = (String)(reply.result().body());
					System.out.println("******************");
					System.out.println(json);
					System.out.println("******************");
				});

	}
}
