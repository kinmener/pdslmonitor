package pdslmonitor;

import java.util.concurrent.Callable;

import util.Assertion;

public class PDSLMonitor {

  public void lock() {
  }

  public void unlock() {
  }

  public void waitUntil(Assertion assertion) {
  }

  public PDSLCondition newCondition(Assertion assertion) {
    return null;
  }


  public PDSLCondition newCondition(Assertion assertion, String key) {
    return null;
  }
  
  public PDSLCondition newCondition(Assertion assertion, String key, PredicateTag[] tags) {
    return null;
  }
  
  public RunnableTask asynchronousExecute(Assertion assertion, Runnable task) {
    return null;
  }
  
  public <T> CallableTask<T> asynchronousExecute(Assertion assertion, Callable<T> task) {
    return null;
  }
}
