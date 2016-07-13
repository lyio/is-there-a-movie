package com.botomo.handlers;

import java.util.List;

import com.botomo.data.CrudAddresses;
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
	public void handle(RoutingContext event) {
		vertx.eventBus().send(CrudAddresses.GET_ALL.message(),
				null, reply -> {
					String json = (String)(reply.result().body());
					System.out.println("******************");
					System.out.println(json);
					System.out.println("******************");
				});

	}
}
