package com.app.spellcheck;

import com.app.spellcheck.service.SpellingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.ResponseStatus;
import dto.SpellCheckDTO;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.*;

public class AppVertx {

    private Set<String> words = new HashSet<>();



    public static void main(String[] args) {

        SpellingService spellingService = new SpellingService();

        SpellCheckDTO spellCheckDTO = new SpellCheckDTO();

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
                .handler(BodyHandler.create())
                .handler(routingContext -> {
//                    System.out.println("show routing context::: " + routingContext.getBodyAsJson().getString("sentence"));

                    HttpServerResponse response = routingContext.response();

                    spellCheckDTO.setSentence(routingContext.getBodyAsJson().getString("sentence"));

                    String dictFound = SpellingService.checkSpellingInDictionary(routingContext.getBodyAsJson().getString("sentence")) ? "FOUND" : "NOT_FOUND";
                    List<String> suggestion = SpellingService.suggestSimilarWords(routingContext.getBodyAsJson().getString("sentence"));

                    spellCheckDTO.setSuggestion(suggestion);
                    if(dictFound.equalsIgnoreCase("FOUND")){
                        spellCheckDTO.setMessage("Looks nice :)");
                        spellCheckDTO.setCode(ResponseStatus.CORRECT.getCode());
                        spellCheckDTO.setStatus(ResponseStatus.CORRECT.getMessage());
                    }else{
                        spellCheckDTO.setMessage("Words not found in dictionary!");
                        spellCheckDTO.setCode(ResponseStatus.NOT_CORRECT.getCode());
                        spellCheckDTO.setStatus(ResponseStatus.NOT_CORRECT.getMessage());
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    String myObjectListJsonStr = "";
                    try {
                         myObjectListJsonStr = objectMapper.writeValueAsString(spellCheckDTO);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    response.setChunked(true);
                    response.write("Spell check....");
                    response.end(myObjectListJsonStr);
                });


        httpServer
                .requestHandler(router::accept)
                .listen(8091);

    }



}
