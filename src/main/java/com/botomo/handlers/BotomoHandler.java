package com.botomo.handlers;

import com.botomo.data.AsyncReply;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


public class BotomoHandler {
	protected static final String APP_JSON = "application/json; charset=utf-8";
	protected static final String QPARAM_SEARCH = "search";

	protected final Vertx vertx;

	public BotomoHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	protected AsyncReply extractReply(AsyncResult<Message<Object>> result) {
		String reply = (String) result.result().body();
		return new AsyncReply(reply);
	}

	protected void handleReply(RoutingContext context, int status, String contentType, String body) {

		// Handle successful database request
		context.response().setStatusCode(status).putHeader("content-type", contentType).end(body);
	}

	protected void handleGetReply(String jsonResult, RoutingContext context) {

		this.handleReply(context, 200, APP_JSON, jsonResult);
	}

	protected void handleBeanViolationReply(RoutingContext context,
											JsonObject violations) {
        this.handleReply(context,
                400,
                BookHandler.APP_JSON,
                violations.encodePrettily());
    }

	protected void handleDbError(RoutingContext context, String body) {

		/*
		 * Get the status code from the api error object which is provided as
		 * json string
		 */
		JsonObject error = new JsonObject(body);
		this.handleReply(context, error.getInteger("statusCode"), APP_JSON, body);
	}
}
