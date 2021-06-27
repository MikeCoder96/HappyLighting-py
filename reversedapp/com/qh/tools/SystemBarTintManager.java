package com.qh.tools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.lang.reflect.Method;

public class SystemBarTintManager {
  public static final int DEFAULT_TINT_COLOR = -1728053248;
  
  private static String sNavBarOverride;
  
  private final SystemBarConfig mConfig;
  
  private boolean mNavBarAvailable;
  
  private boolean mNavBarTintEnabled;
  
  private View mNavBarTintView;
  
  private boolean mStatusBarAvailable;
  
  private boolean mStatusBarTintEnabled;
  
  private View mStatusBarTintView;
  
  static {
    if (Build.VERSION.SDK_INT >= 19)
      try {
        Method method = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[] { String.class });
        method.setAccessible(true);
        sNavBarOverride = (String)method.invoke((Object)null, new Object[] { "qemu.hw.mainkeys" });
      } catch (Throwable throwable) {
        sNavBarOverride = null;
      }  
  }
  
  @SuppressLint({"ResourceType"})
  @TargetApi(19)
  public SystemBarTintManager(Activity paramActivity) {
    Window window = paramActivity.getWindow();
    ViewGroup viewGroup = (ViewGroup)window.getDecorView();
    if (Build.VERSION.SDK_INT >= 19) {
      WindowManager.LayoutParams layoutParams;
      TypedArray typedArray = paramActivity.obtainStyledAttributes(new int[] { 16843759, 16843760 });
      try {
        this.mStatusBarAvailable = typedArray.getBoolean(0, false);
        this.mNavBarAvailable = typedArray.getBoolean(1, false);
        typedArray.recycle();
        layoutParams = window.getAttributes();
        if ((0x4000000 & layoutParams.flags) != 0)
          this.mStatusBarAvailable = true; 
      } finally {
        layoutParams.recycle();
      } 
    } 
    this.mConfig = new SystemBarConfig(paramActivity, this.mStatusBarAvailable, this.mNavBarAvailable);
    if (!this.mConfig.hasNavigtionBar())
      this.mNavBarAvailable = false; 
    if (this.mStatusBarAvailable)
      setupStatusBarView((Context)paramActivity, viewGroup); 
    if (this.mNavBarAvailable)
      setupNavBarView((Context)paramActivity, viewGroup); 
  }
  
  private void setupNavBarView(Context paramContext, ViewGroup paramViewGroup) {
    FrameLayout.LayoutParams layoutParams;
    this.mNavBarTintView = new View(paramContext);
    if (this.mConfig.isNavigationAtBottom()) {
      layoutParams = new FrameLayout.LayoutParams(-1, this.mConfig.getNavigationBarHeight());
      layoutParams.gravity = 80;
    } else {
      layoutParams = new FrameLayout.LayoutParams(this.mConfig.getNavigationBarWidth(), -1);
      layoutParams.gravity = 5;
    } 
    this.mNavBarTintView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.mNavBarTintView.setBackgroundColor(-1728053248);
    this.mNavBarTintView.setVisibility(8);
    paramViewGroup.addView(this.mNavBarTintView);
  }
  
  private void setupStatusBarView(Context paramContext, ViewGroup paramViewGroup) {
    this.mStatusBarTintView = new View(paramContext);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, this.mConfig.getStatusBarHeight());
    layoutParams.gravity = 48;
    if (this.mNavBarAvailable && !this.mConfig.isNavigationAtBottom())
      layoutParams.rightMargin = this.mConfig.getNavigationBarWidth(); 
    this.mStatusBarTintView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.mStatusBarTintView.setBackgroundColor(-1728053248);
    this.mStatusBarTintView.setVisibility(8);
    paramViewGroup.addView(this.mStatusBarTintView);
  }
  
  public SystemBarConfig getConfig() {
    return this.mConfig;
  }
  
  public boolean isNavBarTintEnabled() {
    return this.mNavBarTintEnabled;
  }
  
  public boolean isStatusBarTintEnabled() {
    return this.mStatusBarTintEnabled;
  }
  
  @TargetApi(11)
  public void setNavigationBarAlpha(float paramFloat) {
    if (this.mNavBarAvailable && Build.VERSION.SDK_INT >= 11)
      this.mNavBarTintView.setAlpha(paramFloat); 
  }
  
  public void setNavigationBarTintColor(int paramInt) {
    if (this.mNavBarAvailable)
      this.mNavBarTintView.setBackgroundColor(paramInt); 
  }
  
  public void setNavigationBarTintDrawable(Drawable paramDrawable) {
    if (this.mNavBarAvailable)
      this.mNavBarTintView.setBackgroundDrawable(paramDrawable); 
  }
  
  public void setNavigationBarTintEnabled(boolean paramBoolean) {
    this.mNavBarTintEnabled = paramBoolean;
    if (this.mNavBarAvailable) {
      byte b;
      View view = this.mNavBarTintView;
      if (paramBoolean) {
        b = 0;
      } else {
        b = 8;
      } 
      view.setVisibility(b);
    } 
  }
  
  public void setNavigationBarTintResource(int paramInt) {
    if (this.mNavBarAvailable)
      this.mNavBarTintView.setBackgroundResource(paramInt); 
  }
  
  @TargetApi(11)
  public void setStatusBarAlpha(float paramFloat) {
    if (this.mStatusBarAvailable && Build.VERSION.SDK_INT >= 11)
      this.mStatusBarTintView.setAlpha(paramFloat); 
  }
  
  public void setStatusBarTintColor(int paramInt) {
    if (this.mStatusBarAvailable)
      this.mStatusBarTintView.setBackgroundColor(paramInt); 
  }
  
  public void setStatusBarTintDrawable(Drawable paramDrawable) {
    if (this.mStatusBarAvailable)
      this.mStatusBarTintView.setBackgroundDrawable(paramDrawable); 
  }
  
  public void setStatusBarTintEnabled(boolean paramBoolean) {
    this.mStatusBarTintEnabled = paramBoolean;
    if (this.mStatusBarAvailable) {
      byte b;
      View view = this.mStatusBarTintView;
      if (paramBoolean) {
        b = 0;
      } else {
        b = 8;
      } 
      view.setVisibility(b);
    } 
  }
  
  public void setStatusBarTintResource(int paramInt) {
    if (this.mStatusBarAvailable)
      this.mStatusBarTintView.setBackgroundResource(paramInt); 
  }
  
  public void setTintAlpha(float paramFloat) {
    setStatusBarAlpha(paramFloat);
    setNavigationBarAlpha(paramFloat);
  }
  
  public void setTintColor(int paramInt) {
    setStatusBarTintColor(paramInt);
    setNavigationBarTintColor(paramInt);
  }
  
  public void setTintDrawable(Drawable paramDrawable) {
    setStatusBarTintDrawable(paramDrawable);
    setNavigationBarTintDrawable(paramDrawable);
  }
  
  public void setTintResource(int paramInt) {
    setStatusBarTintResource(paramInt);
    setNavigationBarTintResource(paramInt);
  }
  
  public static class SystemBarConfig {
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
    
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
    
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    
    private final int mActionBarHeight;
    
    private final boolean mHasNavigationBar;
    
    private final boolean mInPortrait;
    
    private final int mNavigationBarHeight;
    
    private final int mNavigationBarWidth;
    
    private final float mSmallestWidthDp;
    
    private final int mStatusBarHeight;
    
    private final boolean mTranslucentNavBar;
    
    private final boolean mTranslucentStatusBar;
    
    private SystemBarConfig(Activity param1Activity, boolean param1Boolean1, boolean param1Boolean2) {
      Resources resources = param1Activity.getResources();
      int i = (resources.getConfiguration()).orientation;
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.mInPortrait = bool2;
      this.mSmallestWidthDp = getSmallestWidthDp(param1Activity);
      this.mStatusBarHeight = getInternalDimensionSize(resources, "status_bar_height");
      this.mActionBarHeight = getActionBarHeight((Context)param1Activity);
      this.mNavigationBarHeight = getNavigationBarHeight((Context)param1Activity);
      this.mNavigationBarWidth = getNavigationBarWidth((Context)param1Activity);
      boolean bool2 = bool1;
      if (this.mNavigationBarHeight > 0)
        bool2 = true; 
      this.mHasNavigationBar = bool2;
      this.mTranslucentStatusBar = param1Boolean1;
      this.mTranslucentNavBar = param1Boolean2;
    }
    
    @TargetApi(14)
    private int getActionBarHeight(Context param1Context) {
      boolean bool;
      if (Build.VERSION.SDK_INT >= 14) {
        TypedValue typedValue = new TypedValue();
        param1Context.getTheme().resolveAttribute(16843499, typedValue, true);
        bool = TypedValue.complexToDimensionPixelSize(typedValue.data, param1Context.getResources().getDisplayMetrics());
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private int getInternalDimensionSize(Resources param1Resources, String param1String) {
      int i = param1Resources.getIdentifier(param1String, "dimen", "android");
      if (i > 0) {
        i = param1Resources.getDimensionPixelSize(i);
      } else {
        i = 0;
      } 
      return i;
    }
    
    @TargetApi(14)
    private int getNavigationBarHeight(Context param1Context) {
      Resources resources = param1Context.getResources();
      if (Build.VERSION.SDK_INT >= 14 && hasNavBar(param1Context)) {
        String str;
        if (this.mInPortrait) {
          str = "navigation_bar_height";
        } else {
          str = "navigation_bar_height_landscape";
        } 
        return getInternalDimensionSize(resources, str);
      } 
      return 0;
    }
    
    @TargetApi(14)
    private int getNavigationBarWidth(Context param1Context) {
      Resources resources = param1Context.getResources();
      return (Build.VERSION.SDK_INT >= 14 && hasNavBar(param1Context)) ? getInternalDimensionSize(resources, "navigation_bar_width") : 0;
    }
    
    @SuppressLint({"NewApi"})
    private float getSmallestWidthDp(Activity param1Activity) {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      if (Build.VERSION.SDK_INT >= 16) {
        param1Activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
      } else {
        param1Activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      } 
      return Math.min(displayMetrics.widthPixels / displayMetrics.density, displayMetrics.heightPixels / displayMetrics.density);
    }
    
    @TargetApi(14)
    private boolean hasNavBar(Context param1Context) {
      Resources resources = param1Context.getResources();
      int i = resources.getIdentifier("config_showNavigationBar", "bool", "android");
      boolean bool = true;
      if (i != 0) {
        boolean bool1 = resources.getBoolean(i);
        if ("1".equals(SystemBarTintManager.sNavBarOverride)) {
          bool1 = false;
        } else if ("0".equals(SystemBarTintManager.sNavBarOverride)) {
          bool1 = bool;
        } 
        return bool1;
      } 
      return ViewConfiguration.get(param1Context).hasPermanentMenuKey() ^ true;
    }
    
    public int getActionBarHeight() {
      return this.mActionBarHeight;
    }
    
    public int getNavigationBarHeight() {
      return this.mNavigationBarHeight;
    }
    
    public int getNavigationBarWidth() {
      return this.mNavigationBarWidth;
    }
    
    public int getPixelInsetBottom() {
      return (this.mTranslucentNavBar && isNavigationAtBottom()) ? this.mNavigationBarHeight : 0;
    }
    
    public int getPixelInsetRight() {
      return (this.mTranslucentNavBar && !isNavigationAtBottom()) ? this.mNavigationBarWidth : 0;
    }
    
    public int getPixelInsetTop(boolean param1Boolean) {
      byte b;
      boolean bool = this.mTranslucentStatusBar;
      int i = 0;
      if (bool) {
        b = this.mStatusBarHeight;
      } else {
        b = 0;
      } 
      if (param1Boolean)
        i = this.mActionBarHeight; 
      return b + i;
    }
    
    public int getStatusBarHeight() {
      return this.mStatusBarHeight;
    }
    
    public boolean hasNavigtionBar() {
      return this.mHasNavigationBar;
    }
    
    public boolean isNavigationAtBottom() {
      return (this.mSmallestWidthDp >= 600.0F || this.mInPortrait);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\tools\SystemBarTintManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */