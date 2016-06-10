
package examples.BoundedBuffer;


import pdslmonitor.PDSLMonitor;
import pdslmonitor.RunnableTask;
import pdslmonitor.CallableTask;
import util.Assertion;


public class BoundedBuffer {

  private PDSLMonitor monitor;
  private int putPtr, takePtr, count;
  Object[] items;

  public BoundedBuffer(int capacity) {
    monitor = new PDSLMonitor();
    putPtr = takePtr = count = 0;
    items = new Object[capacity];
  }
  

  public void put (Object x) {
    monitor.lock();
    monitor.waitUntil(()->(count < items.length));
    threadUnsafePut(x);
    monitor.unlock();
  }

  public Object take () {
    monitor.lock();
    monitor.waitUntil(()->(count > 0));
    Object ret = threadUnsafeTake();
    monitor.unlock();
    return ret;
  }

  public void asynchronousPut(Object x) {
    monitor.asynchronousExecute(()->(count < items.length), ()->threadUnsafePut(x));
  }

  public Object asynchronousTake() {
    CallableTask<Object> ret = 
      monitor.asynchronousExecute(()->(count > 0), ()->threadUnsafeTake());
    return ret.get();
  }

  public int size() {
    return count;
  }

  private void threadUnsafePut(Object x) {
    items[putPtr] = x;
    if (++putPtr == items.length) putPtr = 0;
    ++count;
  }

  private Object threadUnsafeTake() {
    Object x = items[takePtr];
    if (++takePtr == items.length) takePtr = 0;
    --count;
    return x;
  }
}
