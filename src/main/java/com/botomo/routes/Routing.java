package com.botomo.routes;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;


public class Routing {

    private final Router _router;

    public Routing(Router router) {
        this._router = router;
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
        _router.get("/api/suggestions").handler(rq -> {
           String rsp = "list of suggestions";
           rq.response().putHeader("Content-Length", Integer.toString(rsp.length()));
           rq.response().setStatusCode(200);
           rq.response().write(rsp);
           rq.response().end();
        });

        return _router;
    }
}
