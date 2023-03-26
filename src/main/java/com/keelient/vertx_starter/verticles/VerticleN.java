package com.keelient.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleN extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(VerticleN.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start " + getClass().getName() +
                      " on thread " + Thread.currentThread().getName() +
                      " with config " + config().toString());
    startPromise.complete();
  }
}
