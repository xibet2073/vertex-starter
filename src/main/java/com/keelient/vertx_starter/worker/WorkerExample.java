package com.keelient.vertx_starter.worker;

import com.keelient.vertx_starter.eventloops.EventLoopExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle")
    );
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
        LOG.debug("Executing blocking code");
        try {
          Thread.sleep(5000);
          event.complete();
        } catch (InterruptedException e) {
          LOG.error("Failed: " + e);
          event.fail(e);
        }
      }, result -> {
        if (result.succeeded()) {
          LOG.debug("Blocking call done");
        } else {
          LOG.debug("Blocking failed due to: " + result.cause());
        }
      }
    );
  }
}
