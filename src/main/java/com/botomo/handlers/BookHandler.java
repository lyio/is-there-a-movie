package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.botomo.StringUtils;
import com.botomo.models.Book;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BookHandler {

    private final Vertx vertx;
	
	public static final List<Book> list = new ArrayList<>(10);
    
    
    public BookHandler(final Vertx vertx){
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
     * Get all books from the database. Sends the fetch data
     * formatted as json as response.
     * @param context
     */
    private void getAllBooks(RoutingContext context){
    	vertx.eventBus().send(
    			GET_ALL,
    			null,
    			result -> {
    				this.handleReply(result, context);
    			});
    }
    
    private void getSearchedBooks(RoutingContext context, final String searchTerm){
    	vertx.eventBus().send(
    			SEARCH,
    			searchTerm,
    			result -> {
    				this.handleReply(result, context);
    			});
    }
    
    private void handleReply(AsyncResult<Message<Object>> result, RoutingContext context){
    	if (result.succeeded()) {
			context
				.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end((String)result.result().body());
		}else{
			JsonObject error = new JsonObject().put("errorMessage", result.cause());
			context
				.response()
				.setStatusCode(500)
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(error.encodePrettily());
		}
    }
    
    /**
     * Gets all books from the database concerning the passed search term.
     * If the search term isn't provided all existing books will be returned to
     * the client.
     * @param context
     */
	public void getAll(RoutingContext context) {

		String searchTerm = context.request().getParam("search");

        if(StringUtils.isNullOrEmpty(searchTerm)){
        	// If search parameter is empty call getAllBooks to fetch all books from the db        
        	this.getAllBooks(context);
        }else {
        	// If search parameter is not empty perform the search on the fake data
        	this.getSearchedBooks(context, searchTerm);
        }
    }
}
