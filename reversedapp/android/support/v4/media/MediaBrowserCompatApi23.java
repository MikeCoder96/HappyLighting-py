package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class MediaBrowserCompatApi23 {
  public static Object createItemCallback(ItemCallback paramItemCallback) {
    return new ItemCallbackProxy<ItemCallback>(paramItemCallback);
  }
  
  public static void getItem(Object paramObject1, String paramString, Object paramObject2) {
    ((MediaBrowser)paramObject1).getItem(paramString, (MediaBrowser.ItemCallback)paramObject2);
  }
  
  static interface ItemCallback {
    void onError(@NonNull String param1String);
    
    void onItemLoaded(Parcel param1Parcel);
  }
  
  static class ItemCallbackProxy<T extends ItemCallback> extends MediaBrowser.ItemCallback {
    protected final T mItemCallback;
    
    public ItemCallbackProxy(T param1T) {
      this.mItemCallback = param1T;
    }
    
    public void onError(@NonNull String param1String) {
      this.mItemCallback.onError(param1String);
    }
    
    public void onItemLoaded(MediaBrowser.MediaItem param1MediaItem) {
      if (param1MediaItem == null) {
        this.mItemCallback.onItemLoaded(null);
      } else {
        Parcel parcel = Parcel.obtain();
        param1MediaItem.writeToParcel(parcel, 0);
        this.mItemCallback.onItemLoaded(parcel);
      } 
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\media\MediaBrowserCompatApi23.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */