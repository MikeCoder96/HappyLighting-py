package android.support.v4.media;

import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class VolumeProviderCompatApi21 {
  public static Object createVolumeProvider(int paramInt1, int paramInt2, int paramInt3, final Delegate delegate) {
    return new VolumeProvider(paramInt1, paramInt2, paramInt3) {
        public void onAdjustVolume(int param1Int) {
          delegate.onAdjustVolume(param1Int);
        }
        
        public void onSetVolumeTo(int param1Int) {
          delegate.onSetVolumeTo(param1Int);
        }
      };
  }
  
  public static void setCurrentVolume(Object paramObject, int paramInt) {
    ((VolumeProvider)paramObject).setCurrentVolume(paramInt);
  }
  
  public static interface Delegate {
    void onAdjustVolume(int param1Int);
    
    void onSetVolumeTo(int param1Int);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\VolumeProviderCompatApi21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */