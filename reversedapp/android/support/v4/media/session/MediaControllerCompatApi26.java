package android.support.v4.media.session;

import android.media.session.MediaController;

class MediaControllerCompatApi26 {
  public static Object createCallback(Callback paramCallback) {
    return new CallbackProxy<Callback>(paramCallback);
  }
  
  public static int getRepeatMode(Object paramObject) {
    return ((MediaController)paramObject).getRepeatMode();
  }
  
  public static boolean isShuffleModeEnabled(Object paramObject) {
    return ((MediaController)paramObject).isShuffleModeEnabled();
  }
  
  public static interface Callback extends MediaControllerCompatApi21.Callback {
    void onRepeatModeChanged(int param1Int);
    
    void onShuffleModeChanged(boolean param1Boolean);
  }
  
  static class CallbackProxy<T extends Callback> extends MediaControllerCompatApi21.CallbackProxy<T> {
    CallbackProxy(T param1T) {
      super(param1T);
    }
    
    public void onRepeatModeChanged(int param1Int) {
      ((MediaControllerCompatApi26.Callback)this.mCallback).onRepeatModeChanged(param1Int);
    }
    
    public void onShuffleModeChanged(boolean param1Boolean) {
      ((MediaControllerCompatApi26.Callback)this.mCallback).onShuffleModeChanged(param1Boolean);
    }
  }
  
  public static class TransportControls extends MediaControllerCompatApi23.TransportControls {
    public static void setRepeatMode(Object param1Object, int param1Int) {
      ((MediaController.TransportControls)param1Object).setRepeatMode(param1Int);
    }
    
    public static void setShuffleModeEnabled(Object param1Object, boolean param1Boolean) {
      ((MediaController.TransportControls)param1Object).setShuffleModeEnabled(param1Boolean);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\session\MediaControllerCompatApi26.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */