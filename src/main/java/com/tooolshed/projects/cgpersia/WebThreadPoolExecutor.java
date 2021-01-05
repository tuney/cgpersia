package com.tooolshed.projects.cgpersia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebThreadPoolExecutor {
 private ExecutorService executor = Executors.newFixedThreadPool(7);

 public void add(Runnable runnable) {
  executor.execute(runnable);
 }

 public boolean isShutdown() {
  return executor.isShutdown();
 }

 public void shutdown() {
  executor.shutdown();
 }

 public boolean isTerminated() {
  return executor.isTerminated();
 }

 public void reinitialize() {
  executor = Executors.newFixedThreadPool(3);
 }
}
