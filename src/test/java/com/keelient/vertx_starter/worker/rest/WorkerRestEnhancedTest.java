package com.keelient.vertx_starter.worker.rest;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class WorkerRestEnhancedTest {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    //vertx.deployVerticle(new WorkerRestEnhanced(), testContext.succeeding(id -> testContext.completeNow()));
    for (int i = 0; i < 5; i++) {
      vertx.deployVerticle(new WorkerRestEnhanced());
    }
  }
  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
