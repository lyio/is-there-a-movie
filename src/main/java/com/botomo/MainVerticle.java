package com.botomo;

import com.botomo.data.BookCrudVerticle;
import com.botomo.handlers.BookHandler;
import com.botomo.routes.Routing;

import io.netty.util.internal.SystemPropertyUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {
    
    @Override
	public void start(Future<Void> startFuture) throws Exception {
    	
    	
    	DeploymentOptions dbOpt = new DeploymentOptions().setConfig(config());
    	
    	// deploy book crud verticle
    	vertx.deployVerticle(BookCrudVerticle.class.getName(), dbOpt, ar -> {
    		if (ar.failed()){
    			startFuture.fail(ar.cause());
    		} else {
    			// Replace this with some loggin framework
    			System.out.println("Mongo DB Verticle successfully deployed");
    		}
    	});
    	
		// create router
		Router router = Router.router(vertx);
		// create Book handler 
		BookHandler bookHandler = new BookHandler();
		bookHandler.createData();
		// create Routing
		Routing routing = new Routing(router, bookHandler, vertx);
		// create server
		vertx
			.createHttpServer()
			.requestHandler(routing.register()::accept)
			.listen(config().getInteger("http.port", 8080), result -> {
				if (result.succeeded()) {
					startFuture.complete();
				} else {
					startFuture.fail(result.cause());
				}
			});
	}
}
