package android.arch.lifecycle;

import android.arch.core.util.Function;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Transformations {
  @MainThread
  public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> paramLiveData, @NonNull final Function<X, Y> func) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          public void onChanged(@Nullable X param1X) {
            result.setValue(func.apply(param1X));
          }
        });
    return mediatorLiveData;
  }
  
  @MainThread
  public static <X, Y> LiveData<Y> switchMap(@NonNull LiveData<X> paramLiveData, @NonNull final Function<X, LiveData<Y>> func) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          LiveData<Y> mSource;
          
          public void onChanged(@Nullable X param1X) {
            LiveData<Y> liveData = (LiveData)func.apply(param1X);
            if (this.mSource == liveData)
              return; 
            if (this.mSource != null)
              result.removeSource(this.mSource); 
            this.mSource = liveData;
            if (this.mSource != null)
              result.addSource(this.mSource, new Observer() {
                    public void onChanged(@Nullable Y param2Y) {
                      result.setValue(param2Y);
                    }
                  }); 
          }
        });
    return mediatorLiveData;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\arch\lifecycle\Transformations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */