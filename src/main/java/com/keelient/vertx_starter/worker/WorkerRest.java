package com.keelient.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;

public class WorkerRest extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(WorkerRest.class);
  private WebClient webClient;
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerRest());
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
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

          // Esegue il task in un thread worker
          manageResponse(startPromise, body);
        } else {
          startPromise.fail("Failure: " + ar.cause());
        }
      });
  }

  private void manageResponse(Promise<Void> startPromise, JsonObject body) {
    vertx.executeBlocking(promise -> jsonToXml(promise, body),
      result -> {
      if (result.succeeded()) {
        LOG.debug("jsonToXml succefully completed");
        startPromise.complete();
      } else {
        LOG.debug("Failure in jsonToXml: " + result.cause());
        startPromise.fail("Failure: " + result.cause());
      }
    });
  }

  private void jsonToXml(Promise<Object> promise, JsonObject body) {
    try {
      Thread.sleep(2000);
      /*if(true){ //Error simulation
        throw new InterruptedException();
      }*/
      LOG.debug(body);
      promise.complete();
    } catch (InterruptedException e) {
      LOG.debug("Exception: " + e);
      promise.fail(e);
    }
  }
}
