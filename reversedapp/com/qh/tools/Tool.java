package com.qh.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.util.Log;

public class Tool {
  public static boolean isdebug = false;
  
  private Context mcontext;
  
  public Tool(Context paramContext) {
    this.mcontext = paramContext;
    isdebug = isApkInDebug(paramContext);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("isdebug=");
    stringBuilder.append(isdebug);
    Log.e("debug", stringBuilder.toString());
  }
  
  public static void Log(String paramString) {
    if (isdebug)
      Log.e("debug", paramString); 
  }
  
  public static boolean checkDeviceHasNavigationBar(Context paramContext) {
    try {
      Resources resources = paramContext.getResources();
      int i = resources.getIdentifier("config_showNavigationBar", "bool", "android");
      if (i > 0) {
        boolean bool = resources.getBoolean(i);
      } else {
        boolean bool = false;
      } 
      try {
        Class<?> clazz = Class.forName("android.os.SystemProperties");
        String str = (String)clazz.getMethod("get", new Class[] { String.class }).invoke(clazz, new Object[] { "qemu.hw.mainkeys" });
        if ("1".equals(str)) {
          boolean bool = false;
        } else {
          boolean bool = "0".equals(str);
          if (bool)
            boolean bool1 = true; 
        } 
      } catch (Exception exception) {}
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static int dp2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static String getIPAddress(Context paramContext) {
    return null;
  }
  
  public static int getVersionCode(Context paramContext) {
    boolean bool;
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      bool = (packageManager.getPackageInfo(paramContext.getPackageName(), 0)).versionCode;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      bool = true;
    } 
    return bool;
  }
  
  public static String getVersionName(Context paramContext) {
    String str;
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      str = (packageManager.getPackageInfo(paramContext.getPackageName(), 0)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str = "";
    } 
    return str;
  }
  
  public static byte[] hexStringToByte(String paramString) {
    int i = paramString.length() / 2;
    byte[] arrayOfByte = new byte[i];
    char[] arrayOfChar = paramString.toCharArray();
    for (byte b = 0; b < i; b++) {
      int j = b * 2;
      int k = toByte(arrayOfChar[j]);
      arrayOfByte[b] = (byte)(byte)(toByte(arrayOfChar[j + 1]) | k << 4);
    } 
    return arrayOfByte;
  }
  
  public static String intIP2StringIP(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 8 & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 16 & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 24 & 0xFF);
    return stringBuilder.toString();
  }
  
  public static boolean isApkInDebug(Context paramContext) {
    boolean bool = false;
    try {
      int i = (paramContext.getApplicationInfo()).flags;
      if ((i & 0x2) != 0)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static final boolean isOPen(Context paramContext) {
    LocationManager locationManager = (LocationManager)paramContext.getSystemService("location");
    boolean bool1 = locationManager.isProviderEnabled("gps");
    boolean bool2 = locationManager.isProviderEnabled("network");
    return (bool1 || bool2);
  }
  
  public static final void openGPS(Activity paramActivity) {
    paramActivity.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 0);
  }
  
  public static int px2dp(Context paramContext, float paramFloat) {
    return (int)(paramFloat / (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  private static int toByte(char paramChar) {
    return (byte)"0123456789abcdef".indexOf(paramChar);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\Tool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */