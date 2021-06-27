package android.support.v4.media;

import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

@RequiresApi(18)
interface TransportMediatorCallback {
  long getPlaybackPosition();
  
  void handleAudioFocusChange(int paramInt);
  
  void handleKey(KeyEvent paramKeyEvent);
  
  void playbackPositionUpdate(long paramLong);
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\TransportMediatorCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */