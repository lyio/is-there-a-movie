package com.botomo.data;

import static com.botomo.ApiErrors.*;
import static com.botomo.routes.EventBusAddresses.*;

import java.util.List;
import java.util.stream.Collectors;

import com.botomo.StringUtils;
import com.botomo.models.Book;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * This verticle realizes CRUD functionality for the book entity. To access
 * these functionalities use the message defined in
 * {@link com.botomo.routes.EventBusAddresses}
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
		 * Consumer to get all books from the db. Returns a list of books as
		 * json.
		 */
		this.registerFindAll(vertx);

		/**
		 * Consumer to get only searched books from the db. The following fields
		 * of the book entity are considered for the search: title, author,
		 * subtitle, year. This method doesn't provide a full text search.
		 * Instead it realizes a like-wise search.
		 * 
		 */
		this.registerSearch(vertx);

		/**
		 * Consumer to add one book to the database. The book which should be
		 * stored must be provided through the event bus message as json
		 * formatted string. If no json string is provided or an error occurs
		 * during the processing the operation will reply with false. Else if
		 * the operation will reply with true.
		 */
		this.registerAddOne(vertx);

		/**
		 * Consumer to up vote the provided book entry. The operation requires
		 * the id of the book which should be up voted and returns the book
		 * object as json with the updated ups field.
		 */
		this.registerUpVote(vertx);

		/**
		 * Consumer to down vote the provided book entry. The operation requires
		 * the id of the book which should be down voted and returns the book
		 * object as json with the updated downs field.
		 */
		this.registerDownVote(vertx);

	}

	private void registerFindAll(Vertx vertx) {
		vertx.eventBus().consumer(GET_ALL, message -> {
		    mongo.find(COLLECTION, new JsonObject(), result -> {
			     if (result.succeeded()) {
				     // Fetch all books from db
				     List<JsonObject> foundJsonObjects = result.result();
				     // Map JsonObjects to Book Objects
				     List<Book> books = foundJsonObjects
			    		 .stream()
			             .map(Book::new)
			             .collect(Collectors.toList());
				     System.out.println("books: " + books);
				     // Return the fetched books as
				     // json
				     message.reply(
	                   new AsyncReply(true,Json.encodePrettily(books)).toJsonString());
			     } else {
				     message.reply(
	                   new AsyncReply(false,DB000.toJsonString()).toJsonString());
			     }
		     });
	    });
	}

	private void registerSearch(Vertx vertx) {
		vertx.eventBus().consumer(SEARCH, message -> {
		    final String searchTerm = (String) message.body();
		    mongo.find(COLLECTION, buildSearchQuery(searchTerm), result -> {
			     if (result.succeeded()) {
				     // Fetch all books from db
				     List<JsonObject> foundJsonObjects = result.result();
				     // Map JsonObjects to Book Objects
				     List<Book> books = foundJsonObjects
			    		.stream()
                        .map(Book::new)
                        .collect(Collectors.toList());
				     System.out.println("books: " + books);
				     // Return the fetched books as
				     // json
				     message.reply(new AsyncReply(
                          true,
                          Json.encodePrettily(books)).toJsonString());
			     } else {
				     message.reply(new AsyncReply(
                          false,
                          DB000.toJsonString()).toJsonString());
			     }
		     });
	    });
	}

	private void registerUpVote(Vertx vertx) {
		vertx.eventBus().consumer(UP_VOTE, msg -> {
			     this.vote(msg, "ups");
		     });
	}

	private void registerDownVote(Vertx vertx) {
		vertx.eventBus().consumer(DOWN_VOTE, msg -> {
			     this.vote(msg, "downs");
		     });
	}

	private void registerAddOne(Vertx vertx) {
		vertx.eventBus().consumer(ADD_ONE, message -> {
		    String bookJson = (String) message.body();
		    if (!StringUtils.isNullOrEmpty(bookJson)) {
			     Book book = Json.decodeValue(bookJson, Book.class);
			     book.set_id(null);
			     // Check if book exists
			     this.checkIfBookExists(book, message, nothing -> {
			    	 // This is called if no book with the provided title and author exists
				     this.addBook(book, message);
			     });
		     } else {
			     message.reply(new AsyncReply(
	                  false,
	                  DB003.toJsonString()).toJsonString());
		     }
	    });
	}

	private void checkIfBookExists(Book book,
	                               Message<Object> message,
	                               Handler<Void> next) {
		JsonObject query = new JsonObject()
			.put("title", book.getTitle())
	        .put("author", book.getAuthor());
		mongo.find(COLLECTION, query, ar -> {
			if (ar.succeeded()) {
				if (ar.result().size() > 0) {
					// Book exists
					message.reply(new AsyncReply(
	                     false,
	                     DB004.toJsonString()).toJsonString());
				} else {
					// Book does not exist
					next.handle(null);
				}
			} else {
				// Error
				message.reply(new AsyncReply(
	                 false,
	                 DB000.toJsonString()).toJsonString());
			}
		});
	}

	private void addBook(Book book, Message<Object> message) {
		mongo.insert(COLLECTION, book.toJson(), result -> {
			if (result.succeeded()) {
				final String id = result.result();
				book.set_id(id);
				message.reply(new AsyncReply(
	                 true,
	                 book.toJson().encodePrettily()).toJsonString());
			} else {
				message.reply(new AsyncReply(
                     false,
                     DB000.toJsonString()).toJsonString());
			}
		});
	}

	private void vote(Message<Object> msg, String field) {
		String id = (String) msg.body();
		if (StringUtils.isNullOrEmpty(id)) {
			msg.reply(new AsyncReply(
                 false,
                 DB002.toJsonString()));
		} else {
			mongo.find(COLLECTION, new JsonObject().put("_id", id), result -> {
				List<JsonObject> jsonObjects = result.result();
				if (jsonObjects.size() > 0) {
					// Get only the first book of the result set and
					// extract the id
					JsonObject book = jsonObjects.get(0);
					int ups = book.getInteger(field);
					book.put(field, ++ups);
					mongo.update(
			             COLLECTION,
			             new JsonObject().put("_id", id),
			             new JsonObject()
			             	.put("$set", new JsonObject()
			             	.put(field, ups)),
			             ar -> {
				             if (ar.succeeded()) {
					             msg.reply(new AsyncReply(
                                      true,
                                      book.encodePrettily()).toJsonString());
				             } else {
					             msg.reply(new AsyncReply(
                                      false,
                                      DB000.toJsonString()).toJsonString());
				             }
			             });
				} else {
					msg.reply(new AsyncReply(
                         false,
                         DB001.toJsonString()).toJsonString());
				}
			});
		}
	}

	private JsonObject buildSearchQuery(final String searchTerm) {

		// Build the json query
		final String pattern = ".*" + searchTerm + ".*";

		JsonObject q = new JsonObject();

		JsonObject patternWithOption = new JsonObject();
		patternWithOption.put("$regex", pattern);
		patternWithOption.put("$options", "i");

		JsonArray searchFields = new JsonArray();
		searchFields.add(new JsonObject().put("title", patternWithOption));
		searchFields.add(new JsonObject().put("author", patternWithOption));
		searchFields.add(new JsonObject().put("subtitle", patternWithOption));
		searchFields.add(new JsonObject().put("year", patternWithOption));

		q.put("$or", searchFields);

		return q;
	}

}
