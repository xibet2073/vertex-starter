package com.keelient.vertx_starter.eventbus;

import com.keelient.vertx_starter.verticles.VerticleA;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class RequestResponseExample extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
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
   static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);
    static final String MY_REQUEST_ADDRESS = "my.request.address";

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      final String message = "Hello World!";
      LOG.debug("Sending: ".concat(message));
      eventBus.<String>request(
        MY_REQUEST_ADDRESS,
        message,
        reply -> {
        LOG.debug("Response: ".concat(reply.result().body()));
      });
    }
  }

   static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(
        RequestVerticle.MY_REQUEST_ADDRESS,
        message -> {
          LOG.debug("Received Message: ".concat(message.body()));
          message.reply("Received your message. Thanks!");
        }
      );
    }
  }
}
