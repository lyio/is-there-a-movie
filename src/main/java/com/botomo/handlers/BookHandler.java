package com.botomo.handlers;

import com.botomo.StringUtils;
import com.botomo.data.CrudAddresses;
import com.botomo.models.Book;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookHandler {

    private final Vertx vertx;
	
	public static final List<Book> list = new ArrayList<>(10);
    
    
    public BookHandler(final Vertx vertx){
    	this.vertx = vertx;
    }

    public void createData() {
        for (int i = 1; i < 11; i++) {
            Book b = new Book();
            b.setId(i);
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
    public void getAll(RoutingContext context){
    	vertx.eventBus().send(
    			CrudAddresses.GET_ALL.message(),
    			null,
    			message -> {
    				if (message.succeeded()) {
    					context
							.response()
							.putHeader("content-type", "application/json; charset=utf-8")
							.end((String)message.result().body());
    				}else{
    					JsonObject error = new JsonObject().put("errorMessage", message.cause());
    					context
    						.response()
    						.setStatusCode(500)
    						.putHeader("content-type", "application/json; charset=utf-8")
    						.end(error.encodePrettily());
    				}
    			});
    }
    
	public void getAllBySearchTerm(RoutingContext context) {
        String searchTerm = context.request().getParam("search");
        Stream<Book> responseList =
                StringUtils.isNullOrEmpty(searchTerm)
                        ? list.stream()
                        : list.stream().filter(book -> book.search(searchTerm));
        context.response()
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encode(responseList.toArray()));
    }
}
