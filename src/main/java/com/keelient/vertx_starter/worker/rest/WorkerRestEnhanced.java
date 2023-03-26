package com.keelient.vertx_starter.worker.rest;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkerRestEnhanced extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(WorkerRestEnhanced.class);
  private WebClient webClient;
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(
      new VertxOptions()
        .setEventLoopPoolSize(2)
    );
    vertx.deployVerticle(new WorkerRestEnhanced());
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    LOG.debug("Starting WorkerRestEnhanced: " + Thread.currentThread().getName() + " - " + dateFormat.format(new Date()));
    webClient = WebClient.create(vertx);

    // Esegue un GET request a un servizio REST
    webClient.get("www.alphavantage.co", "/query")
      .addQueryParam("function", "EARNINGS")
      .addQueryParam("symbol", "IBM")
      .addQueryParam("apikey", "ROMSNL4PGI859X8N")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          JsonObject body = response.bodyAsJsonObject();

          WorkerJsonToXml worker = new WorkerJsonToXml(vertx);
          vertx.deployVerticle(worker,
            new DeploymentOptions()
              .setWorker(true)
              .setWorkerPoolSize(5)
              .setWorkerPoolName("my-worker-verticle")
          );
          worker.execute(startPromise, body);
          LOG.debug("Stopping WorkerRestEnhanced: " + Thread.currentThread().getName() + " - " + dateFormat.format(new Date()));

        } else {
          startPromise.fail("Failure: " + ar.cause());
        }
      });
  }
}

