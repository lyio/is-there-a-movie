package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.botomo.StringUtils;
import com.botomo.data.AsyncReply;
import com.botomo.models.Book;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.RoutingContext;

/**
 * A Handler to manage requests concerning the book entity.
 *
 */
public class BookHandler {

	private static final String APP_JSON = "application/json; charset=utf-8";
	private static final String QPARAM_SEARCH = "search";

	private final Vertx vertx;

	public static final List<Book> list = new ArrayList<>(10);

	public BookHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	public void createData() {
		for (int i = 1; i < 11; i++) {
			Book b = new Book();
			b.setId(String.valueOf(i));
			b.setAuthor("Hodor" + i);
			b.setSubtitle("Never gonna let you go");
			b.setTitle("Never gonna give you up");
			b.setYear(String.valueOf(new Date().getTime()));
			b.setUps((int) (Math.random() * 1000 * i));
			b.setDowns((int) (Math.random() * 1000 * i));
			list.add(b);
		}
	}

	/**
	 * Gets all books from the database concerning the passed search term. If
	 * the search term isn't provided all existing books will be returned to the
	 * client. The response body will be json formatted.
	 * 
	 * @param context
	 */
	public void getAll(RoutingContext context) {

		String searchTerm = context.request().getParam(QPARAM_SEARCH);

		if (StringUtils.isNullOrEmpty(searchTerm)) {
			// If search parameter is empty call getAllBooks to fetch all books
			// from the db
			this.getAllBooks(context);
		} else {
			// If search parameter is not empty perform the search on the fake
			// data
			this.getSearchedBooks(context, searchTerm);
		}
	}

	private void getAllBooks(RoutingContext context) {
		vertx.eventBus().send(GET_ALL, null, result -> {
			AsyncReply ar = extractReply(result);
			if (ar.state()) {
				this.handleGetReply(ar.payload(), context);
			} else {
				this.handleDbError(context, ar.payload());
			}
		});
	}

	private void getSearchedBooks(RoutingContext context, final String searchTerm) {
		vertx.eventBus().send(SEARCH, searchTerm, result -> {
			AsyncReply ar = extractReply(result);
			if (ar.state()) {
				this.handleGetReply(ar.payload(), context);
			} else {
				this.handleDbError(context, ar.payload());
			}
		});
	}

	private AsyncReply extractReply(AsyncResult<Message<Object>> result) {
		String reply = (String) result.result().body();
		AsyncReply ar = new AsyncReply(reply);
		return ar;
	}

	private void handleReply(RoutingContext context, int status, String contentType, String body) {

		// Handle successful database request
		context.response().setStatusCode(status).putHeader("content-type", contentType).end(body);
	}

	private void handleGetReply(String jsonResult, RoutingContext context) {

		this.handleReply(context, 200, APP_JSON, jsonResult);
	}

	private void handleDbError(RoutingContext context, String body) {

		/*
		 * Get the status code from the api error object which is provided as
		 * json string
		 */
		JsonObject error = new JsonObject(body);
		this.handleReply(context, error.getInteger("statusCode"), APP_JSON, body);
	}

    public void create(RoutingContext context) {
        Book book = Json.decodeValue(context.getBodyAsString(), Book.class);

        // Here db logic would need to take place
        book.setUps(1);
        book.setId("" + (Math.random() * 1000));
        BookHandler.list.add(book);

        context.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encode(book));
    }
}
