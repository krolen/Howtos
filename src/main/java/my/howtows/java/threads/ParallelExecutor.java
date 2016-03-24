package my.howtows.java.threads;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by kkulagin on 3/24/2016.
 */
public class ParallelExecutor<T> {
  private final Iterable<Callable<T>> tasks;
  private final Callback<T> callback;
  private int threadsNum = 2;
  private boolean failFast = false;
  private boolean daemon = false;
  private String name;
  private long timeout = 30L;
  private TimeUnit timeoutUnit = TimeUnit.SECONDS;


  public ParallelExecutor(Iterable<Callable<T>> tasks, Callback<T> callback) {
    this.tasks = tasks;
    this.callback = callback;
  }

  public ParallelExecutor<T> withParallelism(int threadsNum) {
    this.threadsNum = threadsNum;
    return this;
  }

  public ParallelExecutor<T> withFailFast(boolean failFast) {
    this.failFast = failFast;
    return this;
  }

  public ParallelExecutor<T> daemon(boolean daemon) {
    this.daemon = daemon;
    return this;
  }

  public ParallelExecutor<T> withName(String name) {
    this.name = name;
    return this;
  }

  public ParallelExecutor<T> withEachTaskTimeout(long timeout, TimeUnit timeoutUnit) {
    this.timeout = timeout;
    this.timeoutUnit = timeoutUnit;
    return this;
  }

  public void execute() {
    ExecutorService executorService = Executors.newFixedThreadPool(threadsNum, createFactory());
    List<Future<T>> futures = new ArrayList<>();
    for (Callable<T> task : tasks) {
      futures.add(executorService.submit(task));
    }
    executorService.shutdown();

    try {
      boolean failed = false;
      for (Future<T> future : futures) {
        if(failed) {
          future.cancel(true);
        }
        try {
          T result = future.get(timeout, timeoutUnit);
          invoke(result);
        } catch (Exception e) {
          e.printStackTrace();
          if (failFast) {
            failed = true;
          }
        }
      }
    } finally {
      boolean terminated = false;
      try {
        terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if(!terminated) {
        executorService.shutdownNow();
      }
    }
  }

  private void invoke(T result) {
    try {
      callback.invoke(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private ThreadFactory createFactory() {
    ThreadFactoryBuilder builder = new ThreadFactoryBuilder().setDaemon(daemon);
    if (name != null) {
      builder.setNameFormat(name + "-%d");
    }
    return builder.build();
  }

  public static <T> ParallelExecutor<T> create(Iterable<Callable<T>> tasks, Callback<T> callback) {
    return new ParallelExecutor<>(tasks, callback);
  }

  public interface Callback<T> {
    void invoke(T t);
  }

}
