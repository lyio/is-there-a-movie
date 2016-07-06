package com.socobo;

import com.socobo.routes.Routing;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Routing router = new Routing(Router.router(vertx));

        server.requestHandler(router.register()::accept).listen(8080);
    }
}
