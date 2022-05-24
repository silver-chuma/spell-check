package com.app.spellcheck;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import java.util.*;

public class AppVertx {

    private Set<String> words = new HashSet<>();


    public static void main(String[] args) {

        System.out.println("+++++ RUNNING VERTX +++++");


        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        Route handler1 = router
                .get("/welcome/:name")
                .handler(routingContext -> {
                    String name = routingContext.request().getParam("name");
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.write("Welcome " + name + "!" + "\n");
                    response.end();
                });
        Route handler2 = router
                .post("/hello")
                .consumes("*/json")
                .handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.write("Hello Spell check...");
                    response.end();
                });


        httpServer
                .requestHandler(router::accept)
                .listen(8091);

    }
}
