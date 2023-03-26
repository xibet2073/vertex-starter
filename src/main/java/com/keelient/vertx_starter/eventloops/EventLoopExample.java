package com.keelient.vertx_starter.eventloops;

import com.keelient.vertx_starter.verticles.VerticleN;
import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class EventLoopExample extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500) // Five hundred millisecond
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1) // One second
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
        .setEventLoopPoolSize(1)
    );
    vertx.deployVerticle(EventLoopExample.class.getName(),
                          new DeploymentOptions().setInstances(10));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start " + getClass().getName());
    //Do not do this inside a Verticle (it's blocking code)
    //Thread.sleep(5000);
    long startTime = System.currentTimeMillis();

    Random rand = new Random();
    int randNum = rand.nextInt(100) + 1;
    int sleepTime = (randNum % 2) == 0 ? 500 : 5000;
    Thread.sleep(sleepTime);
    System.out.println("#################### VERTICLE TASK ####################");
    long endTime = System.currentTimeMillis();

    System.out.println("Time taken to execute method: " + (endTime - startTime) + "ms");
  }
}
