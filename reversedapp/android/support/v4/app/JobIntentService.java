package android.support.v4.app;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class JobIntentService extends Service {
  static final boolean DEBUG = false;
  
  static final String TAG = "JobIntentService";
  
  static final HashMap<ComponentName, WorkEnqueuer> sClassWorkEnqueuer;
  
  static final Object sLock = new Object();
  
  final ArrayList<CompatWorkItem> mCompatQueue;
  
  WorkEnqueuer mCompatWorkEnqueuer;
  
  CommandProcessor mCurProcessor;
  
  boolean mDestroyed = false;
  
  boolean mInterruptIfStopped = false;
  
  CompatJobEngine mJobImpl;
  
  boolean mStopped = false;
  
  static {
    sClassWorkEnqueuer = new HashMap<ComponentName, WorkEnqueuer>();
  }
  
  public JobIntentService() {
    if (Build.VERSION.SDK_INT >= 26) {
      this.mCompatQueue = null;
    } else {
      this.mCompatQueue = new ArrayList<CompatWorkItem>();
    } 
  }
  
  public static void enqueueWork(@NonNull Context paramContext, @NonNull ComponentName paramComponentName, int paramInt, @NonNull Intent paramIntent) {
    if (paramIntent != null)
      synchronized (sLock) {
        WorkEnqueuer workEnqueuer = getWorkEnqueuer(paramContext, paramComponentName, true, paramInt);
        workEnqueuer.ensureJobId(paramInt);
        workEnqueuer.enqueueWork(paramIntent);
        return;
      }  
    throw new IllegalArgumentException("work must not be null");
  }
  
  public static void enqueueWork(@NonNull Context paramContext, @NonNull Class paramClass, int paramInt, @NonNull Intent paramIntent) {
    enqueueWork(paramContext, new ComponentName(paramContext, paramClass), paramInt, paramIntent);
  }
  
  static WorkEnqueuer getWorkEnqueuer(Context paramContext, ComponentName paramComponentName, boolean paramBoolean, int paramInt) {
    WorkEnqueuer workEnqueuer1 = sClassWorkEnqueuer.get(paramComponentName);
    WorkEnqueuer workEnqueuer2 = workEnqueuer1;
    if (workEnqueuer1 == null) {
      JobWorkEnqueuer jobWorkEnqueuer;
      CompatWorkEnqueuer compatWorkEnqueuer;
      if (Build.VERSION.SDK_INT >= 26) {
        if (paramBoolean) {
          jobWorkEnqueuer = new JobWorkEnqueuer(paramContext, paramComponentName, paramInt);
        } else {
          throw new IllegalArgumentException("Can't be here without a job id");
        } 
      } else {
        compatWorkEnqueuer = new CompatWorkEnqueuer((Context)jobWorkEnqueuer, paramComponentName);
      } 
      sClassWorkEnqueuer.put(paramComponentName, compatWorkEnqueuer);
      workEnqueuer2 = compatWorkEnqueuer;
    } 
    return workEnqueuer2;
  }
  
  GenericWorkItem dequeueWork() {
    if (this.mJobImpl != null)
      return this.mJobImpl.dequeueWork(); 
    synchronized (this.mCompatQueue) {
      if (this.mCompatQueue.size() > 0)
        return this.mCompatQueue.remove(0); 
      return null;
    } 
  }
  
  boolean doStopCurrentWork() {
    if (this.mCurProcessor != null)
      this.mCurProcessor.cancel(this.mInterruptIfStopped); 
    this.mStopped = true;
    return onStopCurrentWork();
  }
  
  void ensureProcessorRunningLocked(boolean paramBoolean) {
    if (this.mCurProcessor == null) {
      this.mCurProcessor = new CommandProcessor();
      if (this.mCompatWorkEnqueuer != null && paramBoolean)
        this.mCompatWorkEnqueuer.serviceProcessingStarted(); 
      this.mCurProcessor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    } 
  }
  
  public boolean isStopped() {
    return this.mStopped;
  }
  
  public IBinder onBind(@NonNull Intent paramIntent) {
    return (this.mJobImpl != null) ? this.mJobImpl.compatGetBinder() : null;
  }
  
  public void onCreate() {
    super.onCreate();
    if (Build.VERSION.SDK_INT >= 26) {
      this.mJobImpl = new JobServiceEngineImpl(this);
      this.mCompatWorkEnqueuer = null;
    } else {
      this.mJobImpl = null;
      this.mCompatWorkEnqueuer = getWorkEnqueuer((Context)this, new ComponentName((Context)this, getClass()), false, 0);
    } 
  }
  
  public void onDestroy() {
    super.onDestroy();
    if (this.mCompatQueue != null)
      synchronized (this.mCompatQueue) {
        this.mDestroyed = true;
        this.mCompatWorkEnqueuer.serviceProcessingFinished();
      }  
  }
  
  protected abstract void onHandleWork(@NonNull Intent paramIntent);
  
  public int onStartCommand(@Nullable Intent paramIntent, int paramInt1, int paramInt2) {
    if (this.mCompatQueue != null) {
      this.mCompatWorkEnqueuer.serviceStartReceived();
      synchronized (this.mCompatQueue) {
        ArrayList<CompatWorkItem> arrayList = this.mCompatQueue;
        CompatWorkItem compatWorkItem = new CompatWorkItem();
        if (paramIntent == null)
          paramIntent = new Intent(); 
        this(this, paramIntent, paramInt2);
        arrayList.add(compatWorkItem);
        ensureProcessorRunningLocked(true);
        return 3;
      } 
    } 
    return 2;
  }
  
  public boolean onStopCurrentWork() {
    return true;
  }
  
  void processorFinished() {
    if (this.mCompatQueue != null)
      synchronized (this.mCompatQueue) {
        this.mCurProcessor = null;
        if (this.mCompatQueue != null && this.mCompatQueue.size() > 0) {
          ensureProcessorRunningLocked(false);
        } else if (!this.mDestroyed) {
          this.mCompatWorkEnqueuer.serviceProcessingFinished();
        } 
      }  
  }
  
  public void setInterruptIfStopped(boolean paramBoolean) {
    this.mInterruptIfStopped = paramBoolean;
  }
  
  final class CommandProcessor extends AsyncTask<Void, Void, Void> {
    protected Void doInBackground(Void... param1VarArgs) {
      while (true) {
        JobIntentService.GenericWorkItem genericWorkItem = JobIntentService.this.dequeueWork();
        if (genericWorkItem != null) {
          JobIntentService.this.onHandleWork(genericWorkItem.getIntent());
          genericWorkItem.complete();
          continue;
        } 
        return null;
      } 
    }
    
    protected void onCancelled(Void param1Void) {
      JobIntentService.this.processorFinished();
    }
    
    protected void onPostExecute(Void param1Void) {
      JobIntentService.this.processorFinished();
    }
  }
  
  static interface CompatJobEngine {
    IBinder compatGetBinder();
    
    JobIntentService.GenericWorkItem dequeueWork();
  }
  
  static final class CompatWorkEnqueuer extends WorkEnqueuer {
    private final Context mContext;
    
    private final PowerManager.WakeLock mLaunchWakeLock;
    
    boolean mLaunchingService;
    
    private final PowerManager.WakeLock mRunWakeLock;
    
    boolean mServiceProcessing;
    
    CompatWorkEnqueuer(Context param1Context, ComponentName param1ComponentName) {
      super(param1Context, param1ComponentName);
      this.mContext = param1Context.getApplicationContext();
      PowerManager powerManager = (PowerManager)param1Context.getSystemService("power");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1ComponentName.getClassName());
      stringBuilder.append(":launch");
      this.mLaunchWakeLock = powerManager.newWakeLock(1, stringBuilder.toString());
      this.mLaunchWakeLock.setReferenceCounted(false);
      stringBuilder = new StringBuilder();
      stringBuilder.append(param1ComponentName.getClassName());
      stringBuilder.append(":run");
      this.mRunWakeLock = powerManager.newWakeLock(1, stringBuilder.toString());
      this.mRunWakeLock.setReferenceCounted(false);
    }
    
    void enqueueWork(Intent param1Intent) {
      // Byte code:
      //   0: new android/content/Intent
      //   3: dup
      //   4: aload_1
      //   5: invokespecial <init> : (Landroid/content/Intent;)V
      //   8: astore_1
      //   9: aload_1
      //   10: aload_0
      //   11: getfield mComponentName : Landroid/content/ComponentName;
      //   14: invokevirtual setComponent : (Landroid/content/ComponentName;)Landroid/content/Intent;
      //   17: pop
      //   18: aload_0
      //   19: getfield mContext : Landroid/content/Context;
      //   22: aload_1
      //   23: invokevirtual startService : (Landroid/content/Intent;)Landroid/content/ComponentName;
      //   26: ifnull -> 70
      //   29: aload_0
      //   30: monitorenter
      //   31: aload_0
      //   32: getfield mLaunchingService : Z
      //   35: ifne -> 60
      //   38: aload_0
      //   39: iconst_1
      //   40: putfield mLaunchingService : Z
      //   43: aload_0
      //   44: getfield mServiceProcessing : Z
      //   47: ifne -> 60
      //   50: aload_0
      //   51: getfield mLaunchWakeLock : Landroid/os/PowerManager$WakeLock;
      //   54: ldc2_w 60000
      //   57: invokevirtual acquire : (J)V
      //   60: aload_0
      //   61: monitorexit
      //   62: goto -> 70
      //   65: astore_1
      //   66: aload_0
      //   67: monitorexit
      //   68: aload_1
      //   69: athrow
      //   70: return
      // Exception table:
      //   from	to	target	type
      //   31	60	65	finally
      //   60	62	65	finally
      //   66	68	65	finally
    }
    
    public void serviceProcessingFinished() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mServiceProcessing : Z
      //   6: ifeq -> 38
      //   9: aload_0
      //   10: getfield mLaunchingService : Z
      //   13: ifeq -> 26
      //   16: aload_0
      //   17: getfield mLaunchWakeLock : Landroid/os/PowerManager$WakeLock;
      //   20: ldc2_w 60000
      //   23: invokevirtual acquire : (J)V
      //   26: aload_0
      //   27: iconst_0
      //   28: putfield mServiceProcessing : Z
      //   31: aload_0
      //   32: getfield mRunWakeLock : Landroid/os/PowerManager$WakeLock;
      //   35: invokevirtual release : ()V
      //   38: aload_0
      //   39: monitorexit
      //   40: return
      //   41: astore_1
      //   42: aload_0
      //   43: monitorexit
      //   44: aload_1
      //   45: athrow
      // Exception table:
      //   from	to	target	type
      //   2	26	41	finally
      //   26	38	41	finally
      //   38	40	41	finally
      //   42	44	41	finally
    }
    
    public void serviceProcessingStarted() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mServiceProcessing : Z
      //   6: ifne -> 31
      //   9: aload_0
      //   10: iconst_1
      //   11: putfield mServiceProcessing : Z
      //   14: aload_0
      //   15: getfield mRunWakeLock : Landroid/os/PowerManager$WakeLock;
      //   18: ldc2_w 600000
      //   21: invokevirtual acquire : (J)V
      //   24: aload_0
      //   25: getfield mLaunchWakeLock : Landroid/os/PowerManager$WakeLock;
      //   28: invokevirtual release : ()V
      //   31: aload_0
      //   32: monitorexit
      //   33: return
      //   34: astore_1
      //   35: aload_0
      //   36: monitorexit
      //   37: aload_1
      //   38: athrow
      // Exception table:
      //   from	to	target	type
      //   2	31	34	finally
      //   31	33	34	finally
      //   35	37	34	finally
    }
    
    public void serviceStartReceived() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_0
      //   4: putfield mLaunchingService : Z
      //   7: aload_0
      //   8: monitorexit
      //   9: return
      //   10: astore_1
      //   11: aload_0
      //   12: monitorexit
      //   13: aload_1
      //   14: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	10	finally
      //   11	13	10	finally
    }
  }
  
  final class CompatWorkItem implements GenericWorkItem {
    final Intent mIntent;
    
    final int mStartId;
    
    CompatWorkItem(Intent param1Intent, int param1Int) {
      this.mIntent = param1Intent;
      this.mStartId = param1Int;
    }
    
    public void complete() {
      JobIntentService.this.stopSelf(this.mStartId);
    }
    
    public Intent getIntent() {
      return this.mIntent;
    }
  }
  
  static interface GenericWorkItem {
    void complete();
    
    Intent getIntent();
  }
  
  @RequiresApi(26)
  static final class JobServiceEngineImpl extends JobServiceEngine implements CompatJobEngine {
    static final boolean DEBUG = false;
    
    static final String TAG = "JobServiceEngineImpl";
    
    final Object mLock = new Object();
    
    JobParameters mParams;
    
    final JobIntentService mService;
    
    JobServiceEngineImpl(JobIntentService param1JobIntentService) {
      super(param1JobIntentService);
      this.mService = param1JobIntentService;
    }
    
    public IBinder compatGetBinder() {
      return getBinder();
    }
    
    public JobIntentService.GenericWorkItem dequeueWork() {
      synchronized (this.mLock) {
        if (this.mParams == null)
          return null; 
        JobWorkItem jobWorkItem = this.mParams.dequeueWork();
        if (jobWorkItem != null) {
          jobWorkItem.getIntent().setExtrasClassLoader(this.mService.getClassLoader());
          return new WrapperWorkItem(jobWorkItem);
        } 
        return null;
      } 
    }
    
    public boolean onStartJob(JobParameters param1JobParameters) {
      this.mParams = param1JobParameters;
      this.mService.ensureProcessorRunningLocked(false);
      return true;
    }
    
    public boolean onStopJob(JobParameters param1JobParameters) {
      boolean bool = this.mService.doStopCurrentWork();
      synchronized (this.mLock) {
        this.mParams = null;
        return bool;
      } 
    }
    
    final class WrapperWorkItem implements JobIntentService.GenericWorkItem {
      final JobWorkItem mJobWork;
      
      WrapperWorkItem(JobWorkItem param2JobWorkItem) {
        this.mJobWork = param2JobWorkItem;
      }
      
      public void complete() {
        synchronized (JobIntentService.JobServiceEngineImpl.this.mLock) {
          if (JobIntentService.JobServiceEngineImpl.this.mParams != null)
            JobIntentService.JobServiceEngineImpl.this.mParams.completeWork(this.mJobWork); 
          return;
        } 
      }
      
      public Intent getIntent() {
        return this.mJobWork.getIntent();
      }
    }
  }
  
  final class WrapperWorkItem implements GenericWorkItem {
    final JobWorkItem mJobWork;
    
    WrapperWorkItem(JobWorkItem param1JobWorkItem) {
      this.mJobWork = param1JobWorkItem;
    }
    
    public void complete() {
      synchronized (this.this$0.mLock) {
        if (this.this$0.mParams != null)
          this.this$0.mParams.completeWork(this.mJobWork); 
        return;
      } 
    }
    
    public Intent getIntent() {
      return this.mJobWork.getIntent();
    }
  }
  
  @RequiresApi(26)
  static final class JobWorkEnqueuer extends WorkEnqueuer {
    private final JobInfo mJobInfo;
    
    private final JobScheduler mJobScheduler;
    
    JobWorkEnqueuer(Context param1Context, ComponentName param1ComponentName, int param1Int) {
      super(param1Context, param1ComponentName);
      ensureJobId(param1Int);
      this.mJobInfo = (new JobInfo.Builder(param1Int, this.mComponentName)).setOverrideDeadline(0L).build();
      this.mJobScheduler = (JobScheduler)param1Context.getApplicationContext().getSystemService("jobscheduler");
    }
    
    void enqueueWork(Intent param1Intent) {
      this.mJobScheduler.enqueue(this.mJobInfo, new JobWorkItem(param1Intent));
    }
  }
  
  static abstract class WorkEnqueuer {
    final ComponentName mComponentName;
    
    boolean mHasJobId;
    
    int mJobId;
    
    WorkEnqueuer(Context param1Context, ComponentName param1ComponentName) {
      this.mComponentName = param1ComponentName;
    }
    
    abstract void enqueueWork(Intent param1Intent);
    
    void ensureJobId(int param1Int) {
      if (!this.mHasJobId) {
        this.mHasJobId = true;
        this.mJobId = param1Int;
      } else if (this.mJobId != param1Int) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Given job ID ");
        stringBuilder.append(param1Int);
        stringBuilder.append(" is different than previous ");
        stringBuilder.append(this.mJobId);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    }
    
    public void serviceProcessingFinished() {}
    
    public void serviceProcessingStarted() {}
    
    public void serviceStartReceived() {}
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\JobIntentService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */