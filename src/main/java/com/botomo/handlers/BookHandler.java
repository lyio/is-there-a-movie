package com.botomo.handlers;

import com.botomo.StringUtils;
import com.botomo.models.Book;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class BookHandler {

    public static final List<Book> list = new ArrayList<>(10);

    public void createData() {
        for (int i = 1; i < 11; i++) {
            Book b = new Book();
            b.setId(i);
            b.setAuthor("Hodor" + i);
            b.setSubtitle("Never gonna let you go");
            b.setTitle("Never gonna give you up");
            b.setYear(new Date());
            b.setUps((int) (Math.random() * 1000 * i));
            b.setDowns((int) (Math.random() * 1000 * i));
            list.add(b);
        }
    }

    public void getAll(RoutingContext context) {
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
