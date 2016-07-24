package com.botomo.models;

import io.vertx.core.json.Json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Vote {
    private List<String> upvotes = new ArrayList<>();

    private List<String> downvotes = new ArrayList<>();

    public List<String> getDownvotes() {
        return downvotes;
    }

    public List<String> getUpvotes() {
        return upvotes;
    }

    public String encode() {
        try {
            return URLEncoder.encode(Json.encode(this), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "foobar";
        }
    }

    public static Vote decode(String value) {
        try {
            return Json.decodeValue(URLDecoder.decode(value, "UTF-8"), Vote.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new Vote();
        }
    }

}
