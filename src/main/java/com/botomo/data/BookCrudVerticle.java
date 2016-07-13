package com.botomo.data;

import static com.botomo.routes.EventBusAddresses.*;

import java.util.List;
import java.util.stream.Collectors;

import com.botomo.models.Book;

import io.netty.util.internal.SystemPropertyUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * This verticle realizes CRUD functionality for the book entity. To access
 * these functionalities use the message defined in
 * {@link com.botomo.data.CrudAddresses}
 *
 */
public class BookCrudVerticle extends AbstractVerticle {

	private static final String COLLECTION = "books";
	private MongoClient mongo = null;

	@Override
	public void start() throws Exception {

		// Initialize mongo client
		mongo = MongoClient.createShared(vertx, config());

		/**
		 * Consumer to get all books from the db
		 */
		vertx.eventBus().consumer(GET_ALL, message -> {
			mongo.find(COLLECTION, new JsonObject(), result -> {
				if (result.succeeded()) {
					// Fetch all books from db
					List<JsonObject> foundJsonObjects = result.result();
					// Map JsonObjects to Book Objects
					List<Book> books = foundJsonObjects.stream().map(Book::new).collect(Collectors.toList());
					System.out.println("books: " + books);
					// Return the fetched books as json
					message.reply(Json.encodePrettily(books));
				} else {
					message.fail(0, result.cause().getMessage());
				}
			});
		});

		vertx.eventBus().consumer(SEARCH, message -> {
			final String searchTerm = (String) message.body();

			mongo.find(COLLECTION, buildSearchQuery(searchTerm), result -> {
				if (result.succeeded()) {
					// Fetch all books from db
					List<JsonObject> foundJsonObjects = result.result();
					// Map JsonObjects to Book Objects
					List<Book> books = foundJsonObjects.stream().map(Book::new).collect(Collectors.toList());
					System.out.println("books: " + books);
					// Return the fetched books as json
					message.reply(Json.encodePrettily(books));
				} else {
					message.fail(0, result.cause().getMessage());
				}
			});
		});
	}
	
	private JsonObject buildSearchQuery(final String searchTerm){
		
		// Build the json query
		final String pattern = ".*" + searchTerm + ".*";
		
		JsonObject q = new JsonObject();
		
		JsonArray a = new JsonArray();
		a.add(new JsonObject().put("title", new JsonObject().put("$regex", pattern)));
		a.add(new JsonObject().put("author", new JsonObject().put("$regex", pattern)));
		a.add(new JsonObject().put("subtitle", new JsonObject().put("$regex", pattern)));
		a.add(new JsonObject().put("year", new JsonObject().put("$regex", pattern)));

		q.put("$or", a);
		
		return q;
	}

}
