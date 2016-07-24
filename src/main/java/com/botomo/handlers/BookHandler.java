package com.botomo.handlers;

import static com.botomo.routes.EventBusAddresses.*;

import com.botomo.ApiErrors;
import com.botomo.BeanValidator;
import com.botomo.StringUtils;
import com.botomo.data.AsyncReply;
import com.botomo.models.Book;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

/**
 * A Handler to manage requests concerning the book entity.
 */
public class BookHandler extends BotomoHandler {

    public BookHandler(final Vertx vertx) {
        super(vertx);
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

	public void create(RoutingContext context) {
		String book = context.getBodyAsString();
		Book b = null;
		try {
			b = Json.decodeValue(book, Book.class);
		} catch (Exception e) {
			handleReply(context,
			            ApiErrors.V000.getStatusCode(),
			            APP_JSON,
			            ApiErrors.V000.toJsonString());
		}
		JsonObject violations = new BeanValidator().<Book>validate(b);
		int violationsLen = violations
				.getJsonArray("violations")
		        .size();
		if (violationsLen > 0) {
			handleBeanViolationReply(context, violations);
		} else {
			vertx.eventBus().send(ADD_ONE, book, result -> {
	           AsyncReply reply = extractReply(result);
	           if (reply.state()) {
		           handleReply(context, 201, APP_JSON, reply.payload());
	           } else {
		           handleDbError(context, reply.payload());
	           }
           });
		}
	}

	/**
     * Upvote a single book. Return the updated book entity to the client.
     *
     * @param context
     */
    public void upvote(RoutingContext context)  {
        String bookId = (context.request().getParam("id"));

        final Cookie votesCookie = context.getCookie("votes");
        final Vote votes = votesCookie != null
                    ? Vote.decode(votesCookie.getValue())
                    : new Vote();
        if (votes.getUpvotes().contains(bookId)) {
            handleReply(context, 400, APP_JSON, "Bad Request");
            return;
        }
        vertx.eventBus().send(UP_VOTE, bookId, result -> {
            AsyncReply reply = extractReply(result);

            if(reply.state()) {
                votes.getUpvotes().add(bookId);
                    String encoded = votes.encode();
                    Cookie cookie = Cookie
                            .cookie("votes", encoded)
                            .setPath("api/v1/books/*")
                            .setHttpOnly(false);
                    context.addCookie(cookie);
                    handleGetReply(reply.payload(), context);
            } else {
                handleDbError(context, reply.payload());
            }
        });
    }

    /**
     * Downvote a single book. Return the updated book entity to the client.
     * @param context
     */
    public void downvote(RoutingContext context) {
        String bookId = context.request().getParam("id");

        vertx.eventBus().send(DOWN_VOTE, bookId, result -> {
            AsyncReply reply = extractReply(result);

            if(reply.state()) {
                handleReply(context, 200, APP_JSON, reply.payload());
            } else {
                handleDbError(context, reply.payload());
            }
        });
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

	private void getSearchedBooks(RoutingContext context,
	                              final String searchTerm) {
		vertx.eventBus().send(SEARCH, searchTerm, result -> {
           AsyncReply ar = extractReply(result);
           if (ar.state()) {
	           this.handleGetReply(ar.payload(), context);
           } else {
	           this.handleDbError(context, ar.payload());
           }
       });
	}
}
