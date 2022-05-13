package common;

import org.vertx.java.core.Vertx;

public interface EmbeddableVerticle {

    void start(Vertx vertx);

    String host();

    int port();

}
