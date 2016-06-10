package pdslmonitor;

import java.util.concurrent.locks.LockSupport;

import util.Symbols;

public class RunnableTask implements Task {

  volatile private boolean isDone;
  volatile Thread submitter;

  private Runnable runnable;

  public RunnableTask() {
    submitter = null;
    isDone = false;
  }

  public void get() {
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
  }

  public void execute() {
    try {
      runnable.run();
    } catch(Exception e) {
      e.printStackTrace();
    }
    isDone = true;
    if (submitter != null) {
      LockSupport.unpark(submitter);
    }
  }
}
