package com.keelient.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Deployed as worker verticle");
    startPromise.complete();
    Thread.sleep(5000);
  }
}
