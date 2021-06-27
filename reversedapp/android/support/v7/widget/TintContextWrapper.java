package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TintContextWrapper extends ContextWrapper {
  private static final Object CACHE_LOCK = new Object();
  
  private static ArrayList<WeakReference<TintContextWrapper>> sCache;
  
  private final Resources mResources;
  
  private final Resources.Theme mTheme;
  
  private TintContextWrapper(@NonNull Context paramContext) {
    super(paramContext);
    if (VectorEnabledTintResources.shouldBeUsed()) {
      this.mResources = new VectorEnabledTintResources((Context)this, paramContext.getResources());
      this.mTheme = this.mResources.newTheme();
      this.mTheme.setTo(paramContext.getTheme());
    } else {
      this.mResources = new TintResources((Context)this, paramContext.getResources());
      this.mTheme = null;
    } 
  }
  
  private static boolean shouldWrap(@NonNull Context paramContext) {
    boolean bool = paramContext instanceof TintContextWrapper;
    boolean bool1 = false;
    if (bool || paramContext.getResources() instanceof TintResources || paramContext.getResources() instanceof VectorEnabledTintResources)
      return false; 
    if (Build.VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed())
      bool1 = true; 
    return bool1;
  }
  
  public static Context wrap(@NonNull Context paramContext) {
    if (shouldWrap(paramContext))
      synchronized (CACHE_LOCK) {
        if (sCache == null) {
          ArrayList<WeakReference<TintContextWrapper>> arrayList1 = new ArrayList();
          this();
          sCache = arrayList1;
        } else {
          int i;
          for (i = sCache.size() - 1; i >= 0; i--) {
            WeakReference weakReference1 = sCache.get(i);
            if (weakReference1 == null || weakReference1.get() == null)
              sCache.remove(i); 
          } 
          for (i = sCache.size() - 1; i >= 0; i--) {
            WeakReference<TintContextWrapper> weakReference1 = sCache.get(i);
            if (weakReference1 != null) {
              TintContextWrapper tintContextWrapper1 = weakReference1.get();
            } else {
              weakReference1 = null;
            } 
            if (weakReference1 != null && weakReference1.getBaseContext() == paramContext)
              return (Context)weakReference1; 
          } 
        } 
        TintContextWrapper tintContextWrapper = new TintContextWrapper();
        this(paramContext);
        ArrayList<WeakReference<TintContextWrapper>> arrayList = sCache;
        WeakReference<TintContextWrapper> weakReference = new WeakReference();
        this((T)tintContextWrapper);
        arrayList.add(weakReference);
        return (Context)tintContextWrapper;
      }  
    return paramContext;
  }
  
  public AssetManager getAssets() {
    return this.mResources.getAssets();
  }
  
  public Resources getResources() {
    return this.mResources;
  }
  
  public Resources.Theme getTheme() {
    Resources.Theme theme;
    if (this.mTheme == null) {
      theme = super.getTheme();
    } else {
      theme = this.mTheme;
    } 
    return theme;
  }
  
  public void setTheme(int paramInt) {
    if (this.mTheme == null) {
      super.setTheme(paramInt);
    } else {
      this.mTheme.applyStyle(paramInt, true);
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\TintContextWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */