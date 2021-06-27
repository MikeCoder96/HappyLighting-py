package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.util.Iterator;
import java.util.List;

@RequiresApi(22)
class PlaybackStateCompatApi22 {
  public static Bundle getExtras(Object paramObject) {
    return ((PlaybackState)paramObject).getExtras();
  }
  
  public static Object newInstance(int paramInt, long paramLong1, long paramLong2, float paramFloat, long paramLong3, CharSequence paramCharSequence, long paramLong4, List<Object> paramList, long paramLong5, Bundle paramBundle) {
    PlaybackState.Builder builder = new PlaybackState.Builder();
    builder.setState(paramInt, paramLong1, paramFloat, paramLong4);
    builder.setBufferedPosition(paramLong2);
    builder.setActions(paramLong3);
    builder.setErrorMessage(paramCharSequence);
    Iterator<PlaybackState.CustomAction> iterator = paramList.iterator();
    while (iterator.hasNext())
      builder.addCustomAction(iterator.next()); 
    builder.setActiveQueueItemId(paramLong5);
    builder.setExtras(paramBundle);
    return builder.build();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\session\PlaybackStateCompatApi22.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */