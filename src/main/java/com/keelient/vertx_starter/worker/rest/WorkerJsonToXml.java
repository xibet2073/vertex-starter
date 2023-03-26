package com.keelient.vertx_starter.worker.rest;

import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkerJsonToXml extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(WorkerJsonToXml.class);

  public WorkerJsonToXml(Vertx vertx) {
    this.vertx = vertx;
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    LOG.debug("Deployed WorkerJsonToXml: "  + Thread.currentThread().getName() + " - " + dateFormat.format(new Date()));
    startPromise.complete();
  }

  public void execute(Promise<Void> promise, JsonObject body) {
    vertx.executeBlocking(blockingPromise -> jsonToXml(blockingPromise, body),
      result -> {
        if (result.succeeded()) {
          promise.complete();
        } else {
          promise.fail(result.cause());
        }
      }
    );
  }

  private void jsonToXml(Promise<Object> promise, JsonObject body) {
    try {
      Thread.sleep(5000);
      /*if(true){ //Error simulation
        throw new InterruptedException();
      }*/
      LOG.debug(body);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      LOG.debug("WorkerJsonToXml finished task: "  + Thread.currentThread().getName() + " - " + dateFormat.format(new Date()));
      promise.complete();
    } catch (InterruptedException e) {
      LOG.debug("Exception: " + e);
      promise.fail(e);
    }
  }
}
