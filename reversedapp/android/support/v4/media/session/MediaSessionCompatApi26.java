package android.support.v4.media.session;

import android.media.session.MediaSession;

class MediaSessionCompatApi26 {
  public static Object createCallback(Callback paramCallback) {
    return new CallbackProxy<Callback>(paramCallback);
  }
  
  public static void setRepeatMode(Object paramObject, int paramInt) {
    ((MediaSession)paramObject).setRepeatMode(paramInt);
  }
  
  public static void setShuffleModeEnabled(Object paramObject, boolean paramBoolean) {
    ((MediaSession)paramObject).setShuffleModeEnabled(paramBoolean);
  }
  
  public static interface Callback extends MediaSessionCompatApi24.Callback {
    void onSetRepeatMode(int param1Int);
    
    void onSetShuffleModeEnabled(boolean param1Boolean);
  }
  
  static class CallbackProxy<T extends Callback> extends MediaSessionCompatApi24.CallbackProxy<T> {
    CallbackProxy(T param1T) {
      super(param1T);
    }
    
    public void onSetRepeatMode(int param1Int) {
      ((MediaSessionCompatApi26.Callback)this.mCallback).onSetRepeatMode(param1Int);
    }
    
    public void onSetShuffleModeEnabled(boolean param1Boolean) {
      ((MediaSessionCompatApi26.Callback)this.mCallback).onSetShuffleModeEnabled(param1Boolean);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\session\MediaSessionCompatApi26.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */