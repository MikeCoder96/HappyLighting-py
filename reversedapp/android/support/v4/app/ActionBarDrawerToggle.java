package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

@Deprecated
public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
  private static final int ID_HOME = 16908332;
  
  private static final String TAG = "ActionBarDrawerToggle";
  
  private static final int[] THEME_ATTRS = new int[] { 16843531 };
  
  private static final float TOGGLE_DRAWABLE_OFFSET = 0.33333334F;
  
  final Activity mActivity;
  
  private final Delegate mActivityImpl;
  
  private final int mCloseDrawerContentDescRes;
  
  private Drawable mDrawerImage;
  
  private final int mDrawerImageResource;
  
  private boolean mDrawerIndicatorEnabled;
  
  private final DrawerLayout mDrawerLayout;
  
  private boolean mHasCustomUpIndicator;
  
  private Drawable mHomeAsUpIndicator;
  
  private final int mOpenDrawerContentDescRes;
  
  private SetIndicatorInfo mSetIndicatorInfo;
  
  private SlideDrawable mSlider;
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, @DrawableRes int paramInt1, @StringRes int paramInt2, @StringRes int paramInt3) {
    this(paramActivity, paramDrawerLayout, assumeMaterial((Context)paramActivity) ^ true, paramInt1, paramInt2, paramInt3);
  }
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, boolean paramBoolean, @DrawableRes int paramInt1, @StringRes int paramInt2, @StringRes int paramInt3) {
    float f;
    this.mDrawerIndicatorEnabled = true;
    this.mActivity = paramActivity;
    if (paramActivity instanceof DelegateProvider) {
      this.mActivityImpl = ((DelegateProvider)paramActivity).getDrawerToggleDelegate();
    } else {
      this.mActivityImpl = null;
    } 
    this.mDrawerLayout = paramDrawerLayout;
    this.mDrawerImageResource = paramInt1;
    this.mOpenDrawerContentDescRes = paramInt2;
    this.mCloseDrawerContentDescRes = paramInt3;
    this.mHomeAsUpIndicator = getThemeUpIndicator();
    this.mDrawerImage = ContextCompat.getDrawable((Context)paramActivity, paramInt1);
    this.mSlider = new SlideDrawable(this.mDrawerImage);
    SlideDrawable slideDrawable = this.mSlider;
    if (paramBoolean) {
      f = 0.33333334F;
    } else {
      f = 0.0F;
    } 
    slideDrawable.setOffset(f);
  }
  
  private static boolean assumeMaterial(Context paramContext) {
    boolean bool;
    if ((paramContext.getApplicationInfo()).targetSdkVersion >= 21 && Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Drawable getThemeUpIndicator() {
    if (this.mActivityImpl != null)
      return this.mActivityImpl.getThemeUpIndicator(); 
    if (Build.VERSION.SDK_INT >= 18) {
      Activity activity;
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null) {
        Context context = actionBar.getThemedContext();
      } else {
        activity = this.mActivity;
      } 
      TypedArray typedArray1 = activity.obtainStyledAttributes(null, THEME_ATTRS, 16843470, 0);
      Drawable drawable1 = typedArray1.getDrawable(0);
      typedArray1.recycle();
      return drawable1;
    } 
    TypedArray typedArray = this.mActivity.obtainStyledAttributes(THEME_ATTRS);
    Drawable drawable = typedArray.getDrawable(0);
    typedArray.recycle();
    return drawable;
  }
  
  private void setActionBarDescription(int paramInt) {
    if (this.mActivityImpl != null) {
      this.mActivityImpl.setActionBarDescription(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 18) {
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null)
        actionBar.setHomeActionContentDescription(paramInt); 
    } else {
      if (this.mSetIndicatorInfo == null)
        this.mSetIndicatorInfo = new SetIndicatorInfo(this.mActivity); 
      if (this.mSetIndicatorInfo.mSetHomeAsUpIndicator != null)
        try {
          ActionBar actionBar = this.mActivity.getActionBar();
          this.mSetIndicatorInfo.mSetHomeActionContentDescription.invoke(actionBar, new Object[] { Integer.valueOf(paramInt) });
          actionBar.setSubtitle(actionBar.getSubtitle());
        } catch (Exception exception) {
          Log.w("ActionBarDrawerToggle", "Couldn't set content description via JB-MR2 API", exception);
        }  
    } 
  }
  
  private void setActionBarUpIndicator(Drawable paramDrawable, int paramInt) {
    if (this.mActivityImpl != null) {
      this.mActivityImpl.setActionBarUpIndicator(paramDrawable, paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 18) {
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(paramDrawable);
        actionBar.setHomeActionContentDescription(paramInt);
      } 
    } else {
      if (this.mSetIndicatorInfo == null)
        this.mSetIndicatorInfo = new SetIndicatorInfo(this.mActivity); 
      if (this.mSetIndicatorInfo.mSetHomeAsUpIndicator != null) {
        try {
          ActionBar actionBar = this.mActivity.getActionBar();
          this.mSetIndicatorInfo.mSetHomeAsUpIndicator.invoke(actionBar, new Object[] { paramDrawable });
          this.mSetIndicatorInfo.mSetHomeActionContentDescription.invoke(actionBar, new Object[] { Integer.valueOf(paramInt) });
        } catch (Exception exception) {
          Log.w("ActionBarDrawerToggle", "Couldn't set home-as-up indicator via JB-MR2 API", exception);
        } 
      } else if (this.mSetIndicatorInfo.mUpIndicatorView != null) {
        this.mSetIndicatorInfo.mUpIndicatorView.setImageDrawable((Drawable)exception);
      } else {
        Log.w("ActionBarDrawerToggle", "Couldn't set home-as-up indicator");
      } 
    } 
  }
  
  public boolean isDrawerIndicatorEnabled() {
    return this.mDrawerIndicatorEnabled;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mHasCustomUpIndicator)
      this.mHomeAsUpIndicator = getThemeUpIndicator(); 
    this.mDrawerImage = ContextCompat.getDrawable((Context)this.mActivity, this.mDrawerImageResource);
    syncState();
  }
  
  public void onDrawerClosed(View paramView) {
    this.mSlider.setPosition(0.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mOpenDrawerContentDescRes); 
  }
  
  public void onDrawerOpened(View paramView) {
    this.mSlider.setPosition(1.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mCloseDrawerContentDescRes); 
  }
  
  public void onDrawerSlide(View paramView, float paramFloat) {
    float f = this.mSlider.getPosition();
    if (paramFloat > 0.5F) {
      paramFloat = Math.max(f, Math.max(0.0F, paramFloat - 0.5F) * 2.0F);
    } else {
      paramFloat = Math.min(f, paramFloat * 2.0F);
    } 
    this.mSlider.setPosition(paramFloat);
  }
  
  public void onDrawerStateChanged(int paramInt) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem != null && paramMenuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
      if (this.mDrawerLayout.isDrawerVisible(8388611)) {
        this.mDrawerLayout.closeDrawer(8388611);
      } else {
        this.mDrawerLayout.openDrawer(8388611);
      } 
      return true;
    } 
    return false;
  }
  
  public void setDrawerIndicatorEnabled(boolean paramBoolean) {
    if (paramBoolean != this.mDrawerIndicatorEnabled) {
      if (paramBoolean) {
        int i;
        SlideDrawable slideDrawable = this.mSlider;
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
          i = this.mCloseDrawerContentDescRes;
        } else {
          i = this.mOpenDrawerContentDescRes;
        } 
        setActionBarUpIndicator((Drawable)slideDrawable, i);
      } else {
        setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
      } 
      this.mDrawerIndicatorEnabled = paramBoolean;
    } 
  }
  
  public void setHomeAsUpIndicator(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable((Context)this.mActivity, paramInt);
    } else {
      drawable = null;
    } 
    setHomeAsUpIndicator(drawable);
  }
  
  public void setHomeAsUpIndicator(Drawable paramDrawable) {
    if (paramDrawable == null) {
      this.mHomeAsUpIndicator = getThemeUpIndicator();
      this.mHasCustomUpIndicator = false;
    } else {
      this.mHomeAsUpIndicator = paramDrawable;
      this.mHasCustomUpIndicator = true;
    } 
    if (!this.mDrawerIndicatorEnabled)
      setActionBarUpIndicator(this.mHomeAsUpIndicator, 0); 
  }
  
  public void syncState() {
    if (this.mDrawerLayout.isDrawerOpen(8388611)) {
      this.mSlider.setPosition(1.0F);
    } else {
      this.mSlider.setPosition(0.0F);
    } 
    if (this.mDrawerIndicatorEnabled) {
      int i;
      SlideDrawable slideDrawable = this.mSlider;
      if (this.mDrawerLayout.isDrawerOpen(8388611)) {
        i = this.mCloseDrawerContentDescRes;
      } else {
        i = this.mOpenDrawerContentDescRes;
      } 
      setActionBarUpIndicator((Drawable)slideDrawable, i);
    } 
  }
  
  @Deprecated
  public static interface Delegate {
    @Nullable
    Drawable getThemeUpIndicator();
    
    void setActionBarDescription(@StringRes int param1Int);
    
    void setActionBarUpIndicator(Drawable param1Drawable, @StringRes int param1Int);
  }
  
  @Deprecated
  public static interface DelegateProvider {
    @Nullable
    ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
  }
  
  private static class SetIndicatorInfo {
    Method mSetHomeActionContentDescription;
    
    Method mSetHomeAsUpIndicator;
    
    ImageView mUpIndicatorView;
    
    SetIndicatorInfo(Activity param1Activity) {
      try {
        this.mSetHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", new Class[] { Drawable.class });
        this.mSetHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", new Class[] { int.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        View view1 = param1Activity.findViewById(16908332);
        if (view1 == null)
          return; 
        ViewGroup viewGroup = (ViewGroup)view1.getParent();
        if (viewGroup.getChildCount() != 2)
          return; 
        view1 = viewGroup.getChildAt(0);
        View view2 = viewGroup.getChildAt(1);
        if (view1.getId() == 16908332)
          view1 = view2; 
        if (view1 instanceof ImageView)
          this.mUpIndicatorView = (ImageView)view1; 
        return;
      } 
    }
  }
  
  private class SlideDrawable extends InsetDrawable implements Drawable.Callback {
    private final boolean mHasMirroring;
    
    private float mOffset;
    
    private float mPosition;
    
    private final Rect mTmpRect;
    
    SlideDrawable(Drawable param1Drawable) {
      super(param1Drawable, 0);
      if (Build.VERSION.SDK_INT > 18)
        bool = true; 
      this.mHasMirroring = bool;
      this.mTmpRect = new Rect();
    }
    
    public void draw(@NonNull Canvas param1Canvas) {
      copyBounds(this.mTmpRect);
      param1Canvas.save();
      int i = ViewCompat.getLayoutDirection(ActionBarDrawerToggle.this.mActivity.getWindow().getDecorView());
      byte b = 1;
      if (i == 1) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i != 0)
        b = -1; 
      int j = this.mTmpRect.width();
      float f1 = -this.mOffset;
      float f2 = j;
      param1Canvas.translate(f1 * f2 * this.mPosition * b, 0.0F);
      if (i != 0 && !this.mHasMirroring) {
        param1Canvas.translate(f2, 0.0F);
        param1Canvas.scale(-1.0F, 1.0F);
      } 
      super.draw(param1Canvas);
      param1Canvas.restore();
    }
    
    public float getPosition() {
      return this.mPosition;
    }
    
    public void setOffset(float param1Float) {
      this.mOffset = param1Float;
      invalidateSelf();
    }
    
    public void setPosition(float param1Float) {
      this.mPosition = param1Float;
      invalidateSelf();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\app\ActionBarDrawerToggle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */