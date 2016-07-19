package com.botomo.routes;

import com.botomo.handlers.BookHandler;

import com.botomo.handlers.CorsHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Routing {

    public static final String API = "/api/v1/";

    private final Router _router;
    

    private final BookHandler bookHandler;

    public Routing(Router router, BookHandler bookHandler) {
        this._router = router;
        this.bookHandler = bookHandler;
    }

    /**
     * Single place to register all the routes in the application.
     * Instead of the inline Handler definition, there will be handlers
     * defined in separate classes to make this more maintainable.
     *
     * @return the configured Router object.
     */
    public Router register() {
        // enable CORS
        _router.route().handler(new CorsHandler().getCors());

        // list of book2movie suggestions
        _router.get(API + "books").handler(bookHandler::getAll);

        // creating new books
        _router.route(API + "books*").handler(BodyHandler.create());
        _router.post(API + "books").handler(bookHandler::create);

        return _router;
    }
}
