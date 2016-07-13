package com.botomo.data;

import static com.botomo.routes.EventBusAddresses.GET_ALL;

import java.util.List;
import java.util.stream.Collectors;

import com.botomo.models.Book;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
/**
 * This verticle realizes CRUD functionality for the book entity. To
 * access these functionalities use the message 
 * defined in {@link com.botomo.data.CrudAddresses}
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

	}

}
