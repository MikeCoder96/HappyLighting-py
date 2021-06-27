package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Constructor;
import java.util.List;

@RequiresApi(21)
class ParceledListSliceAdapterApi21 {
  private static Constructor sConstructor;
  
  static {
    try {
      sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(new Class[] { List.class });
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } 
  }
  
  static Object newInstance(List<MediaBrowser.MediaItem> paramList) {
    try {
      paramList = sConstructor.newInstance(new Object[] { paramList });
    } catch (InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException instantiationException) {
      instantiationException.printStackTrace();
      instantiationException = null;
    } 
    return instantiationException;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\ParceledListSliceAdapterApi21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */