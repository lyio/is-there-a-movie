package com.botomo;

import com.botomo.data.BookCrudVerticle;
import com.botomo.handlers.BookHandler;
import com.botomo.routes.Routing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {

    private enum Config { port, dbname, dburl }

    @Override
	public void start(Future<Void> startFuture) throws Exception {
    	
    	this.deployBookCrudVerticle(startFuture);
    	
		// create router
		Router router = Router.router(vertx);
		// create Book handler 
		BookHandler bookHandler = new BookHandler(vertx);
		bookHandler.createData();
		// create Routing
		Routing routing = new Routing(router, bookHandler, vertx);
		// create server
		vertx
			.createHttpServer()
			.requestHandler(routing.register()::accept)
			.listen(Integer.getInteger(Config.port.name(), 8080), result -> {
				if (result.succeeded()) {
					startFuture.complete();
				} else {
					startFuture.fail(result.cause());
				}
			});
	}
    
    private void deployBookCrudVerticle(Future<Void> fut){
    	JsonObject dbConf = new JsonObject()
    			.put("db_name", System.getProperty(Config.dbname.name()))
    			.put("connection_string", System.getProperty(Config.dburl.name()));
    	DeploymentOptions dbOpt = new DeploymentOptions().setConfig(dbConf);
    	
    	// deploy book crud verticle
    	vertx.deployVerticle(BookCrudVerticle.class.getName(), dbOpt, ar -> {
    		if (ar.failed()){
    			System.out.println("Failed to deploy Book CRUD Verticle");
    			fut.fail(ar.cause());
    		} else {
    			// Replace this with some loggin framework
    			System.out.println("Book CRUD Verticle successfully deployed");
    		}
    	});
    }
}
