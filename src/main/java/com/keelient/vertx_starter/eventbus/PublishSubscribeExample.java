package com.keelient.vertx_starter.eventbus;

import com.keelient.vertx_starter.worker.WorkerRest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.time.Duration;

public class PublishSubscribeExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(
      Subscriber2.class.getName(),
      new DeploymentOptions().setInstances(2)
    );
  }

  public static class Publish extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id -> {
        vertx.eventBus().publish(Publish.class.getName(), "A message for everyone!");
      });
    }
  }

  public static class Subscriber1 extends AbstractVerticle {
    public static final Logger LOG = LoggerFactory.getLogger(Subscriber1.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
        LOG.debug("Received ".concat(message.body()));
      });
    }
  }

  public static class Subscriber2 extends AbstractVerticle {
    public static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
        LOG.debug("Received ".concat(message.body()));
      });
    }
  }

}
