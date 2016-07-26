package com.botomo.routes;

import com.botomo.handlers.BookHandler;

import com.botomo.handlers.CorsHandler;
import io.vertx.core.http.HttpMethod;
import com.botomo.handlers.GoodreadsHandler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;

import static com.botomo.routes.EventBusAddresses.GOODREADS;

public class Routing {

    public static final String API = "/api/v1/";

    private final Router _router;
    
    private final GoodreadsHandler goodreadsHandler;

    private final BookHandler bookHandler;

    public Routing(Router router, BookHandler bookHandler, GoodreadsHandler goodreadsHandler) {
        this._router = router;
        this.bookHandler = bookHandler;
        this.goodreadsHandler = goodreadsHandler;
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

        // Cookie handler
        _router.route().handler(CookieHandler.create());

        // list of book2movie suggestions
        _router.get(API + "books").handler(bookHandler::getAll);

        // creating new books
        _router.route(API + "books*").handler(BodyHandler.create());
        _router.post(API + "books").handler(bookHandler::create);

        // upvote single books
        _router.post(API + "books/:id/upvote").handler(bookHandler::upvote);

        // downvote single books
        _router.post(API + "books/:id/downvote").handler(bookHandler::downvote);

        _router.get(API + "goodreads").handler(goodreadsHandler::lookup);

        return _router;
    }
}
