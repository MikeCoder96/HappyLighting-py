package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.InputContentInfo;

public final class InputContentInfoCompat {
  private final InputContentInfoCompatImpl mImpl;
  
  public InputContentInfoCompat(@NonNull Uri paramUri1, @NonNull ClipDescription paramClipDescription, @Nullable Uri paramUri2) {
    if (Build.VERSION.SDK_INT >= 25) {
      this.mImpl = new InputContentInfoCompatApi25Impl(paramUri1, paramClipDescription, paramUri2);
    } else {
      this.mImpl = new InputContentInfoCompatBaseImpl(paramUri1, paramClipDescription, paramUri2);
    } 
  }
  
  private InputContentInfoCompat(@NonNull InputContentInfoCompatImpl paramInputContentInfoCompatImpl) {
    this.mImpl = paramInputContentInfoCompatImpl;
  }
  
  @Nullable
  public static InputContentInfoCompat wrap(@Nullable Object paramObject) {
    return (paramObject == null) ? null : ((Build.VERSION.SDK_INT < 25) ? null : new InputContentInfoCompat(new InputContentInfoCompatApi25Impl(paramObject)));
  }
  
  @NonNull
  public Uri getContentUri() {
    return this.mImpl.getContentUri();
  }
  
  @NonNull
  public ClipDescription getDescription() {
    return this.mImpl.getDescription();
  }
  
  @Nullable
  public Uri getLinkUri() {
    return this.mImpl.getLinkUri();
  }
  
  public void releasePermission() {
    this.mImpl.releasePermission();
  }
  
  public void requestPermission() {
    this.mImpl.requestPermission();
  }
  
  @Nullable
  public Object unwrap() {
    return this.mImpl.getInputContentInfo();
  }
  
  @RequiresApi(25)
  private static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompatImpl {
    @NonNull
    final InputContentInfo mObject;
    
    InputContentInfoCompatApi25Impl(@NonNull Uri param1Uri1, @NonNull ClipDescription param1ClipDescription, @Nullable Uri param1Uri2) {
      this.mObject = new InputContentInfo(param1Uri1, param1ClipDescription, param1Uri2);
    }
    
    InputContentInfoCompatApi25Impl(@NonNull Object param1Object) {
      this.mObject = (InputContentInfo)param1Object;
    }
    
    @NonNull
    public Uri getContentUri() {
      return this.mObject.getContentUri();
    }
    
    @NonNull
    public ClipDescription getDescription() {
      return this.mObject.getDescription();
    }
    
    @Nullable
    public Object getInputContentInfo() {
      return this.mObject;
    }
    
    @Nullable
    public Uri getLinkUri() {
      return this.mObject.getLinkUri();
    }
    
    public void releasePermission() {
      this.mObject.releasePermission();
    }
    
    public void requestPermission() {
      this.mObject.requestPermission();
    }
  }
  
  private static final class InputContentInfoCompatBaseImpl implements InputContentInfoCompatImpl {
    @NonNull
    private final Uri mContentUri;
    
    @NonNull
    private final ClipDescription mDescription;
    
    @Nullable
    private final Uri mLinkUri;
    
    InputContentInfoCompatBaseImpl(@NonNull Uri param1Uri1, @NonNull ClipDescription param1ClipDescription, @Nullable Uri param1Uri2) {
      this.mContentUri = param1Uri1;
      this.mDescription = param1ClipDescription;
      this.mLinkUri = param1Uri2;
    }
    
    @NonNull
    public Uri getContentUri() {
      return this.mContentUri;
    }
    
    @NonNull
    public ClipDescription getDescription() {
      return this.mDescription;
    }
    
    @Nullable
    public Object getInputContentInfo() {
      return null;
    }
    
    @Nullable
    public Uri getLinkUri() {
      return this.mLinkUri;
    }
    
    public void releasePermission() {}
    
    public void requestPermission() {}
  }
  
  private static interface InputContentInfoCompatImpl {
    @NonNull
    Uri getContentUri();
    
    @NonNull
    ClipDescription getDescription();
    
    @Nullable
    Object getInputContentInfo();
    
    @Nullable
    Uri getLinkUri();
    
    void releasePermission();
    
    void requestPermission();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v13\view\inputmethod\InputContentInfoCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */