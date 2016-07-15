package com.botomo;

import com.botomo.handlers.BookHandler;
import com.botomo.routes.Routing;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {
    
    @Override
	public void start(Future<Void> startFuture) throws Exception {
		// create router
		Router router = Router.router(vertx);
		// create Book handler 
		BookHandler bookHandler = new BookHandler();
		bookHandler.createData();
		// create Routing
		Routing routing = new Routing(router, bookHandler);
		// create server
		vertx
			.createHttpServer()
			.requestHandler(routing.register()::accept)
			.listen(config().getInteger("http.port", 8080), result -> {
				if (result.succeeded()) {
					System.out.println("Main Verticle started");
					startFuture.complete();
				} else {
					startFuture.fail(result.cause());
				}
			});
	}
    
    @Override
    public void stop() throws Exception {
    	System.out.println("Main Verticle stoped");
    }
}
