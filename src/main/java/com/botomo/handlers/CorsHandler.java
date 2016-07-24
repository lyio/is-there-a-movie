package com.botomo.handlers;

import io.vertx.core.http.HttpMethod;

public class CorsHandler {

    private final io.vertx.ext.web.handler.CorsHandler cors;

    public CorsHandler() {
        String host = System.getenv("CORS_HOST");
        cors = io.vertx.ext.web.handler.CorsHandler.create(host == null ? "http://localhost:8088" : host)
                .allowedHeader("Accept")
                .allowedHeader("Content-Type")
                .allowCredentials(true)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS);
    }

    public io.vertx.ext.web.handler.CorsHandler getCors() {
        return cors;
    }
}
