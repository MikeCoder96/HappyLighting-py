package com.qh.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {
  public static final int TYPE_FLYME = 1;
  
  public static final int TYPE_M = 3;
  
  public static final int TYPE_MIUI = 0;
  
  public static int getStatusBarHeight(Context paramContext) {
    int i = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (i > 0) {
      i = paramContext.getResources().getDimensionPixelSize(i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  public static boolean setCommonUI(Activity paramActivity, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 23) {
      View view = paramActivity.getWindow().getDecorView();
      if (view != null) {
        int i = view.getSystemUiVisibility();
        if (paramBoolean) {
          i |= 0x2000;
        } else {
          i &= 0xFFFFDFFF;
        } 
        if (view.getSystemUiVisibility() != i)
          view.setSystemUiVisibility(i); 
        return true;
      } 
    } 
    return false;
  }
  
  public static boolean setFlymeUI(Activity paramActivity, boolean paramBoolean) {
    try {
      Window window = paramActivity.getWindow();
      WindowManager.LayoutParams layoutParams = window.getAttributes();
      Field field1 = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
      Field field2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
      field1.setAccessible(true);
      field2.setAccessible(true);
      int i = field1.getInt((Object)null);
      int j = field2.getInt(layoutParams);
      if (paramBoolean) {
        i = j | i;
      } else {
        i = (i ^ 0xFFFFFFFF) & j;
      } 
      field2.setInt(layoutParams, i);
      window.setAttributes(layoutParams);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
      return false;
    } 
  }
  
  public static boolean setMiuiUI(Activity paramActivity, boolean paramBoolean) {
    try {
      Window window = paramActivity.getWindow();
      Class<?> clazz2 = paramActivity.getWindow().getClass();
      Class<?> clazz1 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
      int i = clazz1.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(clazz1);
      Method method = clazz2.getDeclaredMethod("setExtraFlags", new Class[] { int.class, int.class });
      method.setAccessible(true);
      if (paramBoolean) {
        method.invoke(window, new Object[] { Integer.valueOf(i), Integer.valueOf(i) });
      } else {
        method.invoke(window, new Object[] { Integer.valueOf(0), Integer.valueOf(i) });
      } 
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
      return false;
    } 
  }
  
  public static void setRootViewFitsSystemWindows(Activity paramActivity, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      ViewGroup viewGroup = (ViewGroup)paramActivity.findViewById(16908290);
      if (viewGroup.getChildCount() > 0) {
        viewGroup = (ViewGroup)viewGroup.getChildAt(0);
        if (viewGroup != null)
          viewGroup.setFitsSystemWindows(paramBoolean); 
      } 
    } 
  }
  
  public static void setStatusBarColor(Activity paramActivity, int paramInt) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramActivity.getWindow().setStatusBarColor(paramInt);
    } else if (Build.VERSION.SDK_INT >= 19) {
      setTranslucentStatus(paramActivity);
      SystemBarTintManager systemBarTintManager = new SystemBarTintManager(paramActivity);
      systemBarTintManager.setStatusBarTintEnabled(true);
      systemBarTintManager.setStatusBarTintColor(paramInt);
    } 
  }
  
  public static boolean setStatusBarDarkTheme(Activity paramActivity, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      if (Build.VERSION.SDK_INT >= 23) {
        setStatusBarFontIconDark(paramActivity, 3, paramBoolean);
      } else if (OSUtils.isMiui()) {
        setStatusBarFontIconDark(paramActivity, 0, paramBoolean);
      } else {
        if (OSUtils.isFlyme()) {
          setStatusBarFontIconDark(paramActivity, 1, paramBoolean);
          return true;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  public static boolean setStatusBarFontIconDark(Activity paramActivity, int paramInt, boolean paramBoolean) {
    switch (paramInt) {
      default:
        return setCommonUI(paramActivity, paramBoolean);
      case 1:
        return setFlymeUI(paramActivity, paramBoolean);
      case 0:
        break;
    } 
    return setMiuiUI(paramActivity, paramBoolean);
  }
  
  @TargetApi(19)
  public static void setTranslucentStatus(Activity paramActivity) {
    Window window;
    if (Build.VERSION.SDK_INT >= 21) {
      window = paramActivity.getWindow();
      window.getDecorView().setSystemUiVisibility(1280);
      window.addFlags(-2147483648);
      window.setStatusBarColor(0);
    } else if (Build.VERSION.SDK_INT >= 19) {
      Window window1 = window.getWindow();
      WindowManager.LayoutParams layoutParams = window1.getAttributes();
      layoutParams.flags = 0x4000000 | layoutParams.flags;
      window1.setAttributes(layoutParams);
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface ViewType {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\StatusBarUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */