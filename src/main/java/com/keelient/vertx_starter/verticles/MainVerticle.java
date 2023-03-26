package com.keelient.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start" + getClass().getName() + " " + Runtime.getRuntime().availableProcessors());
    vertx.deployVerticle(new VerticleA());
    vertx.deployVerticle(new VerticleB());
    vertx.deployVerticle( // 4 Verticles are deployed
      VerticleN.class.getName(),
      new DeploymentOptions()
        .setInstances(4)
        .setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", VerticleN.class.getName())
        )
      );
    startPromise.complete();
  }
}
