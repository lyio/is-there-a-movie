package com.botomo.handlers;

import io.vertx.core.http.HttpMethod;

public class CorsHandler {

    private final io.vertx.ext.web.handler.CorsHandler cors;

    public CorsHandler() {
        cors = io.vertx.ext.web.handler.CorsHandler.create("*")
                .allowedHeader("Accept")
                .allowedHeader("Content-Type")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS);
    }

    public io.vertx.ext.web.handler.CorsHandler getCors() {
        return cors;
    }
}
