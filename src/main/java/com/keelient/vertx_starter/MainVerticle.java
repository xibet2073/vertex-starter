package com.keelient.vertx_starter;

import com.keelient.vertx_starter.verticles.VerticleN;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.Random;

public class MainVerticle extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.debug("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
    vertx.setPeriodic(500, id -> {
      LOG.debug(new Random().nextDouble());
    });
  }
}
