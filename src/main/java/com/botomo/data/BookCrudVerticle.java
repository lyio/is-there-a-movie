package com.botomo.data;


import io.netty.handler.codec.http.HttpContentEncoder.Result;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import static com.botomo.data.CrudAddresses.*;

import java.util.List;
import java.util.stream.Collectors;

import com.botomo.models.Book;

public class BookCrudVerticle extends AbstractVerticle{

	private static final String COLLECTION = "books";
	private MongoClient mongo = null;
	
	@Override
	public void start() throws Exception {
		
		// Create MongoDB Client
		mongo = MongoClient.createShared(vertx, config());
		
		vertx.eventBus().consumer(GET_ALL.message(), message -> {
			mongo.find(COLLECTION, new JsonObject(), result -> {
				List<JsonObject> foundJsonObjects = result.result();
				// Map to Books
				List<Book> books = foundJsonObjects
						.stream()
						.map(Book::new)
						.collect(Collectors.toList());
				message.reply(books);
			});
		});
		
	}
	
}
