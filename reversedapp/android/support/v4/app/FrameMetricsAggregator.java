package android.support.v4.app;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;
import android.view.FrameMetrics;
import android.view.Window;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FrameMetricsAggregator {
  public static final int ANIMATION_DURATION = 256;
  
  public static final int ANIMATION_INDEX = 8;
  
  public static final int COMMAND_DURATION = 32;
  
  public static final int COMMAND_INDEX = 5;
  
  private static final boolean DBG = false;
  
  public static final int DELAY_DURATION = 128;
  
  public static final int DELAY_INDEX = 7;
  
  public static final int DRAW_DURATION = 8;
  
  public static final int DRAW_INDEX = 3;
  
  public static final int EVERY_DURATION = 511;
  
  public static final int INPUT_DURATION = 2;
  
  public static final int INPUT_INDEX = 1;
  
  private static final int LAST_INDEX = 8;
  
  public static final int LAYOUT_MEASURE_DURATION = 4;
  
  public static final int LAYOUT_MEASURE_INDEX = 2;
  
  public static final int SWAP_DURATION = 64;
  
  public static final int SWAP_INDEX = 6;
  
  public static final int SYNC_DURATION = 16;
  
  public static final int SYNC_INDEX = 4;
  
  private static final String TAG = "FrameMetrics";
  
  public static final int TOTAL_DURATION = 1;
  
  public static final int TOTAL_INDEX = 0;
  
  private FrameMetricsBaseImpl mInstance;
  
  public FrameMetricsAggregator() {
    this(1);
  }
  
  public FrameMetricsAggregator(int paramInt) {
    if (Build.VERSION.SDK_INT >= 24) {
      this.mInstance = new FrameMetricsApi24Impl(paramInt);
    } else {
      this.mInstance = new FrameMetricsBaseImpl();
    } 
  }
  
  public void add(@NonNull Activity paramActivity) {
    this.mInstance.add(paramActivity);
  }
  
  @Nullable
  public SparseIntArray[] getMetrics() {
    return this.mInstance.getMetrics();
  }
  
  @Nullable
  public SparseIntArray[] remove(@NonNull Activity paramActivity) {
    return this.mInstance.remove(paramActivity);
  }
  
  @Nullable
  public SparseIntArray[] reset() {
    return this.mInstance.reset();
  }
  
  @Nullable
  public SparseIntArray[] stop() {
    return this.mInstance.stop();
  }
  
  @RequiresApi(24)
  private static class FrameMetricsApi24Impl extends FrameMetricsBaseImpl {
    private static final int NANOS_PER_MS = 1000000;
    
    private static final int NANOS_ROUNDING_VALUE = 500000;
    
    private static Handler sHandler;
    
    private static HandlerThread sHandlerThread;
    
    private ArrayList<WeakReference<Activity>> mActivities = new ArrayList<WeakReference<Activity>>();
    
    Window.OnFrameMetricsAvailableListener mListener = new Window.OnFrameMetricsAvailableListener() {
        public void onFrameMetricsAvailable(Window param2Window, FrameMetrics param2FrameMetrics, int param2Int) {
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x1) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[0], param2FrameMetrics.getMetric(8)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x2) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[1], param2FrameMetrics.getMetric(1)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x4) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[2], param2FrameMetrics.getMetric(3)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x8) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[3], param2FrameMetrics.getMetric(4)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x10) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[4], param2FrameMetrics.getMetric(5)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x40) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[6], param2FrameMetrics.getMetric(7)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x20) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[5], param2FrameMetrics.getMetric(6)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x80) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[7], param2FrameMetrics.getMetric(0)); 
          if ((FrameMetricsAggregator.FrameMetricsApi24Impl.this.mTrackingFlags & 0x100) != 0)
            FrameMetricsAggregator.FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsAggregator.FrameMetricsApi24Impl.this.mMetrics[8], param2FrameMetrics.getMetric(2)); 
        }
      };
    
    SparseIntArray[] mMetrics = new SparseIntArray[9];
    
    int mTrackingFlags;
    
    FrameMetricsApi24Impl(int param1Int) {
      this.mTrackingFlags = param1Int;
    }
    
    public void add(Activity param1Activity) {
      if (sHandlerThread == null) {
        sHandlerThread = new HandlerThread("FrameMetricsAggregator");
        sHandlerThread.start();
        sHandler = new Handler(sHandlerThread.getLooper());
      } 
      for (byte b = 0; b <= 8; b++) {
        if (this.mMetrics[b] == null && (this.mTrackingFlags & 1 << b) != 0)
          this.mMetrics[b] = new SparseIntArray(); 
      } 
      param1Activity.getWindow().addOnFrameMetricsAvailableListener(this.mListener, sHandler);
      this.mActivities.add(new WeakReference<Activity>(param1Activity));
    }
    
    void addDurationItem(SparseIntArray param1SparseIntArray, long param1Long) {
      if (param1SparseIntArray != null) {
        int i = (int)((500000L + param1Long) / 1000000L);
        if (param1Long >= 0L)
          param1SparseIntArray.put(i, param1SparseIntArray.get(i) + 1); 
      } 
    }
    
    public SparseIntArray[] getMetrics() {
      return this.mMetrics;
    }
    
    public SparseIntArray[] remove(Activity param1Activity) {
      for (WeakReference<Activity> weakReference : this.mActivities) {
        if (weakReference.get() == param1Activity) {
          this.mActivities.remove(weakReference);
          break;
        } 
      } 
      param1Activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
      return this.mMetrics;
    }
    
    public SparseIntArray[] reset() {
      SparseIntArray[] arrayOfSparseIntArray = this.mMetrics;
      this.mMetrics = new SparseIntArray[9];
      return arrayOfSparseIntArray;
    }
    
    public SparseIntArray[] stop() {
      for (int i = this.mActivities.size() - 1; i >= 0; i--) {
        WeakReference<Activity> weakReference = this.mActivities.get(i);
        Activity activity = weakReference.get();
        if (weakReference.get() != null) {
          activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
          this.mActivities.remove(i);
        } 
      } 
      return this.mMetrics;
    }
  }
  
  class null implements Window.OnFrameMetricsAvailableListener {
    public void onFrameMetricsAvailable(Window param1Window, FrameMetrics param1FrameMetrics, int param1Int) {
      if ((this.this$0.mTrackingFlags & 0x1) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[0], param1FrameMetrics.getMetric(8)); 
      if ((this.this$0.mTrackingFlags & 0x2) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[1], param1FrameMetrics.getMetric(1)); 
      if ((this.this$0.mTrackingFlags & 0x4) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[2], param1FrameMetrics.getMetric(3)); 
      if ((this.this$0.mTrackingFlags & 0x8) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[3], param1FrameMetrics.getMetric(4)); 
      if ((this.this$0.mTrackingFlags & 0x10) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[4], param1FrameMetrics.getMetric(5)); 
      if ((this.this$0.mTrackingFlags & 0x40) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[6], param1FrameMetrics.getMetric(7)); 
      if ((this.this$0.mTrackingFlags & 0x20) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[5], param1FrameMetrics.getMetric(6)); 
      if ((this.this$0.mTrackingFlags & 0x80) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[7], param1FrameMetrics.getMetric(0)); 
      if ((this.this$0.mTrackingFlags & 0x100) != 0)
        this.this$0.addDurationItem(this.this$0.mMetrics[8], param1FrameMetrics.getMetric(2)); 
    }
  }
  
  private static class FrameMetricsBaseImpl {
    public void add(Activity param1Activity) {}
    
    public SparseIntArray[] getMetrics() {
      return null;
    }
    
    public SparseIntArray[] remove(Activity param1Activity) {
      return null;
    }
    
    public SparseIntArray[] reset() {
      return null;
    }
    
    public SparseIntArray[] stop() {
      return null;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface MetricType {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\FrameMetricsAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */