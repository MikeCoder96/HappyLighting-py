package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> {
  private AtomicBoolean mComputing = new AtomicBoolean(false);
  
  private final Executor mExecutor;
  
  private AtomicBoolean mInvalid = new AtomicBoolean(true);
  
  @VisibleForTesting
  final Runnable mInvalidationRunnable = new Runnable() {
      @MainThread
      public void run() {
        boolean bool = ComputableLiveData.this.mLiveData.hasActiveObservers();
        if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && bool)
          ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable); 
      }
    };
  
  private final LiveData<T> mLiveData;
  
  @VisibleForTesting
  final Runnable mRefreshRunnable = new Runnable() {
      @WorkerThread
      public void run() {
        boolean bool;
        do {
          if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
            null = null;
            bool = false;
            try {
              while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                null = (Exception)ComputableLiveData.this.compute();
                bool = true;
              } 
              if (bool)
                ComputableLiveData.this.mLiveData.postValue(null); 
            } finally {
              ComputableLiveData.this.mComputing.set(false);
            } 
          } else {
            bool = false;
          } 
        } while (bool && ComputableLiveData.this.mInvalid.get());
      }
    };
  
  public ComputableLiveData() {
    this(ArchTaskExecutor.getIOThreadExecutor());
  }
  
  public ComputableLiveData(@NonNull Executor paramExecutor) {
    this.mExecutor = paramExecutor;
    this.mLiveData = new LiveData<T>() {
        protected void onActive() {
          ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
        }
      };
  }
  
  @WorkerThread
  protected abstract T compute();
  
  @NonNull
  public LiveData<T> getLiveData() {
    return this.mLiveData;
  }
  
  public void invalidate() {
    ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\arch\lifecycle\ComputableLiveData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */