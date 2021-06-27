package com.qh.blelight;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyActivity extends AppCompatActivity {
  private SystemBarTintManager mTintManager;
  
  private boolean fixOrientation() {
    try {
      Field field = Activity.class.getDeclaredField("mActivityInfo");
      field.setAccessible(true);
      ((ActivityInfo)field.get(this)).screenOrientation = -1;
      field.setAccessible(false);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
      return false;
    } 
  }
  
  private boolean isTranslucentOrFloating() {
    boolean bool = false;
    try {
      TypedArray typedArray = obtainStyledAttributes((int[])Class.forName("com.android.internal.R$styleable").getField("Window").get((Object)null));
      Method method = ActivityInfo.class.getMethod("isTranslucentOrFloating", new Class[] { TypedArray.class });
      method.setAccessible(true);
      boolean bool1 = ((Boolean)method.invoke((Object)null, new Object[] { typedArray })).booleanValue();
      try {
        method.setAccessible(false);
      } catch (Exception null) {}
    } catch (Exception exception) {
      boolean bool1 = bool;
    } 
    exception.printStackTrace();
  }
  
  protected void onCreate(Bundle paramBundle) {
    if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating())
      fixOrientation(); 
    if (Build.VERSION.SDK_INT >= 19)
      getWindow().addFlags(67108864); 
    supportRequestWindowFeature(1);
    super.onCreate(paramBundle);
    this.mTintManager = new SystemBarTintManager((Activity)this);
    this.mTintManager.setStatusBarTintEnabled(true);
    this.mTintManager.setNavigationBarTintEnabled(true);
    this.mTintManager.setTintAlpha(255.0F);
    this.mTintManager.setTintColor(1347769685);
  }
  
  public void setRequestedOrientation(int paramInt) {
    if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating())
      return; 
    super.setRequestedOrientation(paramInt);
  }
  
  public void setTintColor(int paramInt) {
    this.mTintManager.setTintColor(paramInt);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MyActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */