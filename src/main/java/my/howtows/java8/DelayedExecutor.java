package my.howtows.java8;

import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by kkulagin on 4/6/2016.
 */
public class DelayedExecutor<O> {

  private final TimeUnit unit;
  private final long[] delays;
  private final Supplier<O> supplier;

  public DelayedExecutor(Supplier<O> supplier, TimeUnit unit, long... delays) {
    this.unit = unit;
    this.delays = delays;
    this.supplier = supplier;
  }

  public O execute() throws Exception {
    for (long delay : delays) {
      try {
        return supplier.get();
      } catch (Exception e) {
        Uninterruptibles.sleepUninterruptibly(delay, unit);
      }
    }
    return supplier.get();
  }

}
