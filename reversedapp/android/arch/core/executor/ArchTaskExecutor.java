package android.arch.core.executor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.concurrent.Executor;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ArchTaskExecutor extends TaskExecutor {
  @NonNull
  private static final Executor sIOThreadExecutor;
  
  private static volatile ArchTaskExecutor sInstance;
  
  @NonNull
  private static final Executor sMainThreadExecutor = new Executor() {
      public void execute(Runnable param1Runnable) {
        ArchTaskExecutor.getInstance().postToMainThread(param1Runnable);
      }
    };
  
  @NonNull
  private TaskExecutor mDefaultTaskExecutor = new DefaultTaskExecutor();
  
  @NonNull
  private TaskExecutor mDelegate = this.mDefaultTaskExecutor;
  
  static {
    sIOThreadExecutor = new Executor() {
        public void execute(Runnable param1Runnable) {
          ArchTaskExecutor.getInstance().executeOnDiskIO(param1Runnable);
        }
      };
  }
  
  @NonNull
  public static Executor getIOThreadExecutor() {
    return sIOThreadExecutor;
  }
  
  @NonNull
  public static ArchTaskExecutor getInstance() {
    // Byte code:
    //   0: getstatic android/arch/core/executor/ArchTaskExecutor.sInstance : Landroid/arch/core/executor/ArchTaskExecutor;
    //   3: ifnull -> 10
    //   6: getstatic android/arch/core/executor/ArchTaskExecutor.sInstance : Landroid/arch/core/executor/ArchTaskExecutor;
    //   9: areturn
    //   10: ldc android/arch/core/executor/ArchTaskExecutor
    //   12: monitorenter
    //   13: getstatic android/arch/core/executor/ArchTaskExecutor.sInstance : Landroid/arch/core/executor/ArchTaskExecutor;
    //   16: ifnonnull -> 31
    //   19: new android/arch/core/executor/ArchTaskExecutor
    //   22: astore_0
    //   23: aload_0
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: putstatic android/arch/core/executor/ArchTaskExecutor.sInstance : Landroid/arch/core/executor/ArchTaskExecutor;
    //   31: ldc android/arch/core/executor/ArchTaskExecutor
    //   33: monitorexit
    //   34: getstatic android/arch/core/executor/ArchTaskExecutor.sInstance : Landroid/arch/core/executor/ArchTaskExecutor;
    //   37: areturn
    //   38: astore_0
    //   39: ldc android/arch/core/executor/ArchTaskExecutor
    //   41: monitorexit
    //   42: aload_0
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   13	31	38	finally
    //   31	34	38	finally
    //   39	42	38	finally
  }
  
  @NonNull
  public static Executor getMainThreadExecutor() {
    return sMainThreadExecutor;
  }
  
  public void executeOnDiskIO(Runnable paramRunnable) {
    this.mDelegate.executeOnDiskIO(paramRunnable);
  }
  
  public boolean isMainThread() {
    return this.mDelegate.isMainThread();
  }
  
  public void postToMainThread(Runnable paramRunnable) {
    this.mDelegate.postToMainThread(paramRunnable);
  }
  
  public void setDelegate(@Nullable TaskExecutor paramTaskExecutor) {
    TaskExecutor taskExecutor = paramTaskExecutor;
    if (paramTaskExecutor == null)
      taskExecutor = this.mDefaultTaskExecutor; 
    this.mDelegate = taskExecutor;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\arch\core\executor\ArchTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */