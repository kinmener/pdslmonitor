package pdslmonitor;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.LockSupport;

import util.Symbols;

public class CallableTask<T> implements Task {

  volatile private boolean isDone;
  volatile Thread submitter;
  private T result;
  private Callable<T> callable;

  public CallableTask(Callable<T> callable) {
    submitter = null;
    isDone = false;
    this.callable = callable;
  }

  public T get() {
    submitter = Thread.currentThread();
    for (;;) {
      int spin = Symbols.MAX_SPIN;
      while (!isDone && spin > 0) {
        --spin;
      }
      if (isDone) {
        break;
      }

      LockSupport.parkNanos(this, Symbols.PARK_TIMEOUT_NANO);
    }
    submitter = null;
    return result;
  }

  public void execute() {
    T ret = null;
    try {
      ret = callable.call();
    } catch(Exception e) {
      e.printStackTrace();
    }
    result = ret;
    isDone = true;
    if (submitter != null) {
      LockSupport.unpark(submitter);
    }
  }
}
