package com.keelient.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleA extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(VerticleA.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start " + getClass().getName());
    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      LOG.debug("Deployed: " + VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result()); // Verticle id
    });
    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      LOG.debug("Deployed: " + VerticleAB.class.getName());
    });
    startPromise.complete();
  }
}
