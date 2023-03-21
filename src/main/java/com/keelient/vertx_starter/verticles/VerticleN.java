package com.keelient.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class VerticleN extends AbstractVerticle {
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Start " + getClass().getName() +
                      " on thread " + Thread.currentThread().getName());
    startPromise.complete();
  }
}
