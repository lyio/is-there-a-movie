package com.botomo.handlers;

import com.botomo.StringUtils;
import com.botomo.data.AsyncReply;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static com.botomo.routes.EventBusAddresses.GOODREADS;

public class GoodreadsHandler extends BotomoHandler {

    private static final String SEARCH = "search";

    public GoodreadsHandler(Vertx vertx) {
        super(vertx);
    }

    public void lookup(RoutingContext context) {
        String search = context.request().getParam(SEARCH);
        if (StringUtils.isNullOrEmpty(search)) {
            handleReply(context, 400, APP_JSON, Json.encode("search term missing"));
        } else {
            context.vertx().eventBus().send(GOODREADS, search, r -> {
                if(r.succeeded()) {
                    AsyncReply reply = this.extractReply(r);
                    handleGetReply(reply.payload(), context);
                } else {
                    handleReply(context, 500, APP_JSON, r.cause().getMessage());
                }
            });
        }
    }
}
