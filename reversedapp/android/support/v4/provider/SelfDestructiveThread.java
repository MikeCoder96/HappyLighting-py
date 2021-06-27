package android.support.v4.provider;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SelfDestructiveThread {
  private static final int MSG_DESTRUCTION = 0;
  
  private static final int MSG_INVOKE_RUNNABLE = 1;
  
  private Handler.Callback mCallback = new Handler.Callback() {
      public boolean handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return true;
          case 1:
            SelfDestructiveThread.this.onInvokeRunnable((Runnable)param1Message.obj);
            return true;
          case 0:
            break;
        } 
        SelfDestructiveThread.this.onDestruction();
        return true;
      }
    };
  
  private final int mDestructAfterMillisec;
  
  @GuardedBy("mLock")
  private int mGeneration;
  
  @GuardedBy("mLock")
  private Handler mHandler;
  
  private final Object mLock = new Object();
  
  private final int mPriority;
  
  @GuardedBy("mLock")
  private HandlerThread mThread;
  
  private final String mThreadName;
  
  public SelfDestructiveThread(String paramString, int paramInt1, int paramInt2) {
    this.mThreadName = paramString;
    this.mPriority = paramInt1;
    this.mDestructAfterMillisec = paramInt2;
    this.mGeneration = 0;
  }
  
  private void post(Runnable paramRunnable) {
    synchronized (this.mLock) {
      if (this.mThread == null) {
        HandlerThread handlerThread = new HandlerThread();
        this(this.mThreadName, this.mPriority);
        this.mThread = handlerThread;
        this.mThread.start();
        Handler handler = new Handler();
        this(this.mThread.getLooper(), this.mCallback);
        this.mHandler = handler;
        this.mGeneration++;
      } 
      this.mHandler.removeMessages(0);
      this.mHandler.sendMessage(this.mHandler.obtainMessage(1, paramRunnable));
      return;
    } 
  }
  
  @VisibleForTesting
  public int getGeneration() {
    synchronized (this.mLock) {
      return this.mGeneration;
    } 
  }
  
  @VisibleForTesting
  public boolean isRunning() {
    synchronized (this.mLock) {
      boolean bool;
      if (this.mThread != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  void onDestruction() {
    synchronized (this.mLock) {
      if (this.mHandler.hasMessages(1))
        return; 
      this.mThread.quit();
      this.mThread = null;
      this.mHandler = null;
      return;
    } 
  }
  
  void onInvokeRunnable(Runnable paramRunnable) {
    paramRunnable.run();
    synchronized (this.mLock) {
      this.mHandler.removeMessages(0);
      this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), this.mDestructAfterMillisec);
      return;
    } 
  }
  
  public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> reply) {
    post(new Runnable() {
          public void run() {
            try {
              exception = (Exception)callable.call();
            } catch (Exception exception) {
              exception = null;
            } 
            callingHandler.post(new Runnable() {
                  public void run() {
                    reply.onReply(result);
                  }
                });
          }
        });
  }
  
  public <T> T postAndWait(final Callable<T> callable, int paramInt) throws InterruptedException {
    final ReentrantLock lock = new ReentrantLock();
    final Condition cond = reentrantLock.newCondition();
    final AtomicReference<Callable<T>> holder = new AtomicReference();
    final AtomicBoolean running = new AtomicBoolean(true);
    post(new Runnable() {
          public void run() {
            try {
              holder.set(callable.call());
            } catch (Exception exception) {}
            lock.lock();
            try {
              running.set(false);
              cond.signal();
              return;
            } finally {
              lock.unlock();
            } 
          }
        });
    reentrantLock.lock();
    try {
      if (!atomicBoolean.get()) {
        callable = atomicReference.get();
        return (T)callable;
      } 
      long l = TimeUnit.MILLISECONDS.toNanos(paramInt);
      while (true) {
        try {
          long l1 = condition.awaitNanos(l);
          l = l1;
        } catch (InterruptedException interruptedException1) {}
        if (!atomicBoolean.get()) {
          callable = atomicReference.get();
          return (T)callable;
        } 
        if (l > 0L)
          continue; 
        InterruptedException interruptedException = new InterruptedException();
        this("timeout");
        throw interruptedException;
      } 
    } finally {
      reentrantLock.unlock();
    } 
  }
  
  public static interface ReplyCallback<T> {
    void onReply(T param1T);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\provider\SelfDestructiveThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */