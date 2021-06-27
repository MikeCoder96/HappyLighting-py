package com.qh.tools;

import android.os.Build;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSUtils {
  private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
  
  private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
  
  private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
  
  private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
  
  private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
  
  public static final String ROM_EMUI = "EMUI";
  
  public static final String ROM_FLYME = "FLYME";
  
  public static final String ROM_MIUI = "MIUI";
  
  public static final String ROM_OPPO = "OPPO";
  
  public static final String ROM_QIKU = "QIKU";
  
  public static final String ROM_SMARTISAN = "SMARTISAN";
  
  public static final String ROM_VIVO = "VIVO";
  
  private static String sName;
  
  private static String sVersion;
  
  public static boolean check(String paramString) {
    if (sName != null)
      return sName.equals(paramString); 
    String str = getProp("ro.miui.ui.version.name");
    sVersion = str;
    if (!TextUtils.isEmpty(str)) {
      sName = "MIUI";
    } else {
      str = getProp("ro.build.version.emui");
      sVersion = str;
      if (!TextUtils.isEmpty(str)) {
        sName = "EMUI";
      } else {
        str = getProp("ro.build.version.opporom");
        sVersion = str;
        if (!TextUtils.isEmpty(str)) {
          sName = "OPPO";
        } else {
          str = getProp("ro.vivo.os.version");
          sVersion = str;
          if (!TextUtils.isEmpty(str)) {
            sName = "VIVO";
          } else {
            str = getProp("ro.smartisan.version");
            sVersion = str;
            if (!TextUtils.isEmpty(str)) {
              sName = "SMARTISAN";
            } else {
              sVersion = Build.DISPLAY;
              if (sVersion.toUpperCase().contains("FLYME")) {
                sName = "FLYME";
              } else {
                sVersion = "unknown";
                sName = Build.MANUFACTURER.toUpperCase();
              } 
            } 
          } 
        } 
      } 
    } 
    return sName.equals(paramString);
  }
  
  public static String getName() {
    if (sName == null)
      check(""); 
    return sName;
  }
  
  public static String getProp(String paramString) {
    String str = null;
    try {
      Runtime runtime = Runtime.getRuntime();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("getprop ");
      stringBuilder.append(paramString);
      Process process = runtime.exec(stringBuilder.toString());
      BufferedReader bufferedReader = new BufferedReader();
      InputStreamReader inputStreamReader = new InputStreamReader();
      this(process.getInputStream());
      this(inputStreamReader, 1024);
      try {
        return str1;
      } catch (IOException iOException1) {
      
      } finally {
        process = null;
      } 
      throw process;
    } catch (IOException null) {
    
    } finally {
      Exception exception = null;
      paramString = str;
    } 
    if (iOException != null)
      try {
        iOException.close();
      } catch (IOException iOException1) {
        iOException1.printStackTrace();
      }  
    return null;
  }
  
  public static String getVersion() {
    if (sVersion == null)
      check(""); 
    return sVersion;
  }
  
  public static boolean is360() {
    return (check("QIKU") || check("360"));
  }
  
  public static boolean isEmui() {
    return check("EMUI");
  }
  
  public static boolean isFlyme() {
    return check("FLYME");
  }
  
  public static boolean isMiui() {
    return check("MIUI");
  }
  
  public static boolean isOppo() {
    return check("OPPO");
  }
  
  public static boolean isSmartisan() {
    return check("SMARTISAN");
  }
  
  public static boolean isVivo() {
    return check("VIVO");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\OSUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */