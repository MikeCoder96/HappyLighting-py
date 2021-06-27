package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
  @Nullable
  public static IBinder getBinder(@NonNull Bundle paramBundle, @Nullable String paramString) {
    return (Build.VERSION.SDK_INT >= 18) ? paramBundle.getBinder(paramString) : BundleCompatBaseImpl.getBinder(paramBundle, paramString);
  }
  
  public static void putBinder(@NonNull Bundle paramBundle, @Nullable String paramString, @Nullable IBinder paramIBinder) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramBundle.putBinder(paramString, paramIBinder);
    } else {
      BundleCompatBaseImpl.putBinder(paramBundle, paramString, paramIBinder);
    } 
  }
  
  static class BundleCompatBaseImpl {
    private static final String TAG = "BundleCompatBaseImpl";
    
    private static Method sGetIBinderMethod;
    
    private static boolean sGetIBinderMethodFetched;
    
    private static Method sPutIBinderMethod;
    
    private static boolean sPutIBinderMethodFetched;
    
    public static IBinder getBinder(Bundle param1Bundle, String param1String) {
      if (!sGetIBinderMethodFetched) {
        try {
          sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[] { String.class });
          sGetIBinderMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", noSuchMethodException);
        } 
        sGetIBinderMethodFetched = true;
      } 
      if (sGetIBinderMethod != null)
        try {
          return (IBinder)sGetIBinderMethod.invoke(param1Bundle, new Object[] { param1String });
        } catch (InvocationTargetException|IllegalAccessException|IllegalArgumentException invocationTargetException) {
          Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", invocationTargetException);
          sGetIBinderMethod = null;
        }  
      return null;
    }
    
    public static void putBinder(Bundle param1Bundle, String param1String, IBinder param1IBinder) {
      if (!sPutIBinderMethodFetched) {
        try {
          sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[] { String.class, IBinder.class });
          sPutIBinderMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", noSuchMethodException);
        } 
        sPutIBinderMethodFetched = true;
      } 
      if (sPutIBinderMethod != null)
        try {
          sPutIBinderMethod.invoke(param1Bundle, new Object[] { param1String, param1IBinder });
        } catch (InvocationTargetException|IllegalAccessException|IllegalArgumentException invocationTargetException) {
          Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", invocationTargetException);
          sPutIBinderMethod = null;
        }  
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\BundleCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */