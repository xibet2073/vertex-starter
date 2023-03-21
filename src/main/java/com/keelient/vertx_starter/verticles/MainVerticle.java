package com.keelient.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Start " + getClass().getName() + " " + Runtime.getRuntime().availableProcessors());
    vertx.deployVerticle(new VerticleA());
    vertx.deployVerticle(new VerticleB());
    vertx.deployVerticle( // 4 Verticles are deployed
      VerticleN.class.getName(),
      new DeploymentOptions().setInstances(4)
      );
    startPromise.complete();
  }
}
