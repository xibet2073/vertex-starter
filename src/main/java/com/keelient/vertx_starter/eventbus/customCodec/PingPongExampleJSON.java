package com.keelient.vertx_starter.eventbus.customCodec;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.impl.codecs.JsonObjectMessageCodec;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class PingPongExampleJSON extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(PingPongExampleJSON.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(),
      logOnError());
    vertx.deployVerticle(new PongVerticle(),
      logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        LOG.error("Error: ".concat(ar.cause().toString()));
      }
    };
  }

  /*
    Può accedere direttamente ai membri statici della classe esterna.
    Non ha accesso ai membri non statici della classe esterna.
    Una sola istanza della classe static inner class è condivisa da tutte
    le istanze della classe esterna.
    Può essere istanziata senza istanziare la classe esterna:
    Inner inner = new Outer.Inner();
    String name = inner.getName(); // "Outer"
    */
   static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);
    static final String MY_REQUEST_ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      EventBus eventBus = vertx.eventBus();
      final var message = new JsonObject();
      message
        .put("id", 1)
        .put("name", "Mark");
      LOG.debug("Sending: ".concat(message.toString()));
      eventBus.<JsonObject>request(
        MY_REQUEST_ADDRESS,
        message,
        reply -> {
          if (reply.failed()) {
            LOG.error("Failed: ".concat(reply.cause().toString()));
            return;
          }
          String responseMessage = reply.result().body().toString();
          LOG.debug("Response: " + responseMessage);
      });
      startPromise.complete();
    }
  }

   static class PongVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      EventBus eventBus = vertx.eventBus();
      eventBus.<JsonObject>consumer(
        PingVerticle.MY_REQUEST_ADDRESS,
        message -> {
          LOG.debug("Received Message: ".concat(message.body().toString()));
          message.reply(new JsonObject().put("message", "Hello Mark!"));
        }
      ).exceptionHandler(error ->
        LOG.error("Error: ".concat(error.toString())));
      startPromise.complete();
    }
  }
}
