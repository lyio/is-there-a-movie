package com.botomo.routes;

import com.botomo.handlers.BookHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;


public class Routing {

    public static final String API = "/api/v1/";

    private final Router _router;
    

    private final BookHandler bookHandler;

    public Routing(Router router, BookHandler bookHandler) {
        this._router = router;
        this.bookHandler = bookHandler;
        
        // enable CORS
        this._router.route()
        	.handler(CorsHandler.create("*")
            	.allowedHeader("Accept")
        		.allowedHeader("Content-Type")
        		.allowedMethod(HttpMethod.GET)
       			.allowedMethod(HttpMethod.POST)
       			.allowedMethod(HttpMethod.OPTIONS));
    }

    /**
     * Single place to register all the routes in the application.
     * Instead of the inline Handler definition, there will be handlers
     * defined in separate classes to make this more maintainable.
     *
     * @return the configured Router object.
     */
    public Router register() {
        // Handles static resources
        _router.route("/static/*").handler(StaticHandler.create("webroot/static"));
        _router.route("/").handler(ctx -> ctx.response().sendFile("webroot/index.html"));

        // list of book2movie suggestions
        _router.get(API + "books").handler(bookHandler::getAll);

        return _router;
    }
}
