package com.botomo;

import com.botomo.data.BookCrudVerticle;
import com.botomo.handlers.BookHandler;
import com.botomo.routes.Routing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.FutureFactoryImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    private enum Config { port, dbname, dburl }

    @Override
	public void start(Future<Void> startFuture) throws Exception {
    	
		// create router
		Router router = Router.router(vertx);
		// create Book handler 
		BookHandler bookHandler = new BookHandler(vertx);
		// create Routing
		Routing routing = new Routing(router, bookHandler);
		// create server
		
		// Define Future for http sever
		Future<HttpServer> httpServerFut = Future.future();
		vertx
			.createHttpServer()
			.requestHandler(routing.register()::accept)
			.listen(Integer.getInteger(Config.port.name(), 8080), result -> {
				if (result.succeeded()) {
					System.out.println("HttpServer started");
					httpServerFut.completer();
				} else {
					System.out.println("Failed to start HttpServer");
					httpServerFut.fail(result.cause());
				}
			});
		
		// Define Future for BookCrudVerticle deployment
		
		Future<BookCrudVerticle> bookCrudVerticalFut = Future.future();
		this.deployBookCrudVerticle(bookCrudVerticalFut);
		
		CompositeFuture.all(httpServerFut, bookCrudVerticalFut).setHandler(ar -> {
			if(ar.succeeded()){
				startFuture.complete();
			}else{
				startFuture.fail(ar.cause());
			}
		});
	}
    
    @Override
    public void stop() throws Exception {
    	System.out.println("MainVerticle stopped");
    }
    	
    private void deployBookCrudVerticle(Future<BookCrudVerticle> fut){
    	JsonObject dbConf = new JsonObject()
    			.put("db_name", "chats")
    			.put("connection_string", "mongodb://localhost:27017");
    	DeploymentOptions dbOpt = new DeploymentOptions().setConfig(dbConf);
    	
    	// deploy book crud verticle
    	vertx.deployVerticle(BookCrudVerticle.class.getName(), dbOpt, ar -> {
    		if (ar.failed()){
    			System.out.println("Failed to deploy BookCrudVerticle");
    			ar.cause().printStackTrace();
    			fut.fail(ar.cause());
    		} else {
    			// Replace this with some loggin framework
    			System.out.println("BookCrudVerticle successfully deployed");
    			fut.completer();
    		}
    	});
    }
}
