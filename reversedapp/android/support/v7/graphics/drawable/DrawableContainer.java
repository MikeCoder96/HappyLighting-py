package android.support.v7.graphics.drawable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.SparseArray;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class DrawableContainer extends Drawable implements Drawable.Callback {
  private static final boolean DEBUG = false;
  
  private static final boolean DEFAULT_DITHER = true;
  
  private static final String TAG = "DrawableContainer";
  
  private int mAlpha = 255;
  
  private Runnable mAnimationRunnable;
  
  private BlockInvalidateCallback mBlockInvalidateCallback;
  
  private int mCurIndex = -1;
  
  private Drawable mCurrDrawable;
  
  private DrawableContainerState mDrawableContainerState;
  
  private long mEnterAnimationEnd;
  
  private long mExitAnimationEnd;
  
  private boolean mHasAlpha;
  
  private Rect mHotspotBounds;
  
  private Drawable mLastDrawable;
  
  private int mLastIndex = -1;
  
  private boolean mMutated;
  
  private void initializeDrawableForDisplay(Drawable paramDrawable) {
    if (this.mBlockInvalidateCallback == null)
      this.mBlockInvalidateCallback = new BlockInvalidateCallback(); 
    paramDrawable.setCallback(this.mBlockInvalidateCallback.wrap(paramDrawable.getCallback()));
    try {
      if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha)
        paramDrawable.setAlpha(this.mAlpha); 
      if (this.mDrawableContainerState.mHasColorFilter) {
        paramDrawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
      } else {
        if (this.mDrawableContainerState.mHasTintList)
          DrawableCompat.setTintList(paramDrawable, this.mDrawableContainerState.mTintList); 
        if (this.mDrawableContainerState.mHasTintMode)
          DrawableCompat.setTintMode(paramDrawable, this.mDrawableContainerState.mTintMode); 
      } 
      paramDrawable.setVisible(isVisible(), true);
      paramDrawable.setDither(this.mDrawableContainerState.mDither);
      paramDrawable.setState(getState());
      paramDrawable.setLevel(getLevel());
      paramDrawable.setBounds(getBounds());
      if (Build.VERSION.SDK_INT >= 23)
        paramDrawable.setLayoutDirection(getLayoutDirection()); 
      if (Build.VERSION.SDK_INT >= 19)
        paramDrawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored); 
      Rect rect = this.mHotspotBounds;
      if (Build.VERSION.SDK_INT >= 21 && rect != null)
        paramDrawable.setHotspotBounds(rect.left, rect.top, rect.right, rect.bottom); 
      return;
    } finally {
      paramDrawable.setCallback(this.mBlockInvalidateCallback.unwrap());
    } 
  }
  
  @SuppressLint({"WrongConstant"})
  @TargetApi(23)
  private boolean needsMirroring() {
    boolean bool = isAutoMirrored();
    boolean bool1 = true;
    if (!bool || getLayoutDirection() != 1)
      bool1 = false; 
    return bool1;
  }
  
  static int resolveDensity(@Nullable Resources paramResources, int paramInt) {
    if (paramResources != null)
      paramInt = (paramResources.getDisplayMetrics()).densityDpi; 
    int i = paramInt;
    if (paramInt == 0)
      i = 160; 
    return i;
  }
  
  void animate(boolean paramBoolean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield mHasAlpha : Z
    //   7: invokestatic uptimeMillis : ()J
    //   10: lstore_3
    //   11: aload_0
    //   12: getfield mCurrDrawable : Landroid/graphics/drawable/Drawable;
    //   15: ifnull -> 104
    //   18: aload_0
    //   19: getfield mEnterAnimationEnd : J
    //   22: lconst_0
    //   23: lcmp
    //   24: ifeq -> 109
    //   27: aload_0
    //   28: getfield mEnterAnimationEnd : J
    //   31: lload_3
    //   32: lcmp
    //   33: ifgt -> 55
    //   36: aload_0
    //   37: getfield mCurrDrawable : Landroid/graphics/drawable/Drawable;
    //   40: aload_0
    //   41: getfield mAlpha : I
    //   44: invokevirtual setAlpha : (I)V
    //   47: aload_0
    //   48: lconst_0
    //   49: putfield mEnterAnimationEnd : J
    //   52: goto -> 109
    //   55: aload_0
    //   56: getfield mEnterAnimationEnd : J
    //   59: lload_3
    //   60: lsub
    //   61: ldc2_w 255
    //   64: lmul
    //   65: l2i
    //   66: aload_0
    //   67: getfield mDrawableContainerState : Landroid/support/v7/graphics/drawable/DrawableContainer$DrawableContainerState;
    //   70: getfield mEnterFadeDuration : I
    //   73: idiv
    //   74: istore #5
    //   76: aload_0
    //   77: getfield mCurrDrawable : Landroid/graphics/drawable/Drawable;
    //   80: sipush #255
    //   83: iload #5
    //   85: isub
    //   86: aload_0
    //   87: getfield mAlpha : I
    //   90: imul
    //   91: sipush #255
    //   94: idiv
    //   95: invokevirtual setAlpha : (I)V
    //   98: iconst_1
    //   99: istore #5
    //   101: goto -> 112
    //   104: aload_0
    //   105: lconst_0
    //   106: putfield mEnterAnimationEnd : J
    //   109: iconst_0
    //   110: istore #5
    //   112: aload_0
    //   113: getfield mLastDrawable : Landroid/graphics/drawable/Drawable;
    //   116: ifnull -> 210
    //   119: aload_0
    //   120: getfield mExitAnimationEnd : J
    //   123: lconst_0
    //   124: lcmp
    //   125: ifeq -> 215
    //   128: aload_0
    //   129: getfield mExitAnimationEnd : J
    //   132: lload_3
    //   133: lcmp
    //   134: ifgt -> 165
    //   137: aload_0
    //   138: getfield mLastDrawable : Landroid/graphics/drawable/Drawable;
    //   141: iconst_0
    //   142: iconst_0
    //   143: invokevirtual setVisible : (ZZ)Z
    //   146: pop
    //   147: aload_0
    //   148: aconst_null
    //   149: putfield mLastDrawable : Landroid/graphics/drawable/Drawable;
    //   152: aload_0
    //   153: iconst_m1
    //   154: putfield mLastIndex : I
    //   157: aload_0
    //   158: lconst_0
    //   159: putfield mExitAnimationEnd : J
    //   162: goto -> 215
    //   165: aload_0
    //   166: getfield mExitAnimationEnd : J
    //   169: lload_3
    //   170: lsub
    //   171: ldc2_w 255
    //   174: lmul
    //   175: l2i
    //   176: aload_0
    //   177: getfield mDrawableContainerState : Landroid/support/v7/graphics/drawable/DrawableContainer$DrawableContainerState;
    //   180: getfield mExitFadeDuration : I
    //   183: idiv
    //   184: istore #5
    //   186: aload_0
    //   187: getfield mLastDrawable : Landroid/graphics/drawable/Drawable;
    //   190: iload #5
    //   192: aload_0
    //   193: getfield mAlpha : I
    //   196: imul
    //   197: sipush #255
    //   200: idiv
    //   201: invokevirtual setAlpha : (I)V
    //   204: iload_2
    //   205: istore #5
    //   207: goto -> 215
    //   210: aload_0
    //   211: lconst_0
    //   212: putfield mExitAnimationEnd : J
    //   215: iload_1
    //   216: ifeq -> 237
    //   219: iload #5
    //   221: ifeq -> 237
    //   224: aload_0
    //   225: aload_0
    //   226: getfield mAnimationRunnable : Ljava/lang/Runnable;
    //   229: lload_3
    //   230: ldc2_w 16
    //   233: ladd
    //   234: invokevirtual scheduleSelf : (Ljava/lang/Runnable;J)V
    //   237: return
  }
  
  @RequiresApi(21)
  public void applyTheme(@NonNull Resources.Theme paramTheme) {
    this.mDrawableContainerState.applyTheme(paramTheme);
  }
  
  @RequiresApi(21)
  public boolean canApplyTheme() {
    return this.mDrawableContainerState.canApplyTheme();
  }
  
  void clearMutated() {
    this.mDrawableContainerState.clearMutated();
    this.mMutated = false;
  }
  
  DrawableContainerState cloneConstantState() {
    return this.mDrawableContainerState;
  }
  
  public void draw(@NonNull Canvas paramCanvas) {
    if (this.mCurrDrawable != null)
      this.mCurrDrawable.draw(paramCanvas); 
    if (this.mLastDrawable != null)
      this.mLastDrawable.draw(paramCanvas); 
  }
  
  public int getAlpha() {
    return this.mAlpha;
  }
  
  public int getChangingConfigurations() {
    return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
  }
  
  public final Drawable.ConstantState getConstantState() {
    if (this.mDrawableContainerState.canConstantState()) {
      this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
      return this.mDrawableContainerState;
    } 
    return null;
  }
  
  @NonNull
  public Drawable getCurrent() {
    return this.mCurrDrawable;
  }
  
  int getCurrentIndex() {
    return this.mCurIndex;
  }
  
  public void getHotspotBounds(@NonNull Rect paramRect) {
    if (this.mHotspotBounds != null) {
      paramRect.set(this.mHotspotBounds);
    } else {
      super.getHotspotBounds(paramRect);
    } 
  }
  
  public int getIntrinsicHeight() {
    byte b;
    if (this.mDrawableContainerState.isConstantSize())
      return this.mDrawableContainerState.getConstantHeight(); 
    if (this.mCurrDrawable != null) {
      b = this.mCurrDrawable.getIntrinsicHeight();
    } else {
      b = -1;
    } 
    return b;
  }
  
  public int getIntrinsicWidth() {
    byte b;
    if (this.mDrawableContainerState.isConstantSize())
      return this.mDrawableContainerState.getConstantWidth(); 
    if (this.mCurrDrawable != null) {
      b = this.mCurrDrawable.getIntrinsicWidth();
    } else {
      b = -1;
    } 
    return b;
  }
  
  public int getMinimumHeight() {
    boolean bool;
    if (this.mDrawableContainerState.isConstantSize())
      return this.mDrawableContainerState.getConstantMinimumHeight(); 
    if (this.mCurrDrawable != null) {
      bool = this.mCurrDrawable.getMinimumHeight();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getMinimumWidth() {
    boolean bool;
    if (this.mDrawableContainerState.isConstantSize())
      return this.mDrawableContainerState.getConstantMinimumWidth(); 
    if (this.mCurrDrawable != null) {
      bool = this.mCurrDrawable.getMinimumWidth();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getOpacity() {
    return (this.mCurrDrawable == null || !this.mCurrDrawable.isVisible()) ? -2 : this.mDrawableContainerState.getOpacity();
  }
  
  @RequiresApi(21)
  public void getOutline(@NonNull Outline paramOutline) {
    if (this.mCurrDrawable != null)
      this.mCurrDrawable.getOutline(paramOutline); 
  }
  
  public boolean getPadding(@NonNull Rect paramRect) {
    boolean bool;
    Rect rect = this.mDrawableContainerState.getConstantPadding();
    if (rect != null) {
      paramRect.set(rect);
      int i = rect.left;
      int j = rect.top;
      int k = rect.bottom;
      if ((rect.right | i | j | k) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
    } else if (this.mCurrDrawable != null) {
      bool = this.mCurrDrawable.getPadding(paramRect);
    } else {
      bool = super.getPadding(paramRect);
    } 
    if (needsMirroring()) {
      int i = paramRect.left;
      paramRect.left = paramRect.right;
      paramRect.right = i;
    } 
    return bool;
  }
  
  public void invalidateDrawable(@NonNull Drawable paramDrawable) {
    if (this.mDrawableContainerState != null)
      this.mDrawableContainerState.invalidateCache(); 
    if (paramDrawable == this.mCurrDrawable && getCallback() != null)
      getCallback().invalidateDrawable(this); 
  }
  
  public boolean isAutoMirrored() {
    return this.mDrawableContainerState.mAutoMirrored;
  }
  
  public boolean isStateful() {
    return this.mDrawableContainerState.isStateful();
  }
  
  public void jumpToCurrentState() {
    boolean bool;
    if (this.mLastDrawable != null) {
      this.mLastDrawable.jumpToCurrentState();
      this.mLastDrawable = null;
      this.mLastIndex = -1;
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mCurrDrawable != null) {
      this.mCurrDrawable.jumpToCurrentState();
      if (this.mHasAlpha)
        this.mCurrDrawable.setAlpha(this.mAlpha); 
    } 
    if (this.mExitAnimationEnd != 0L) {
      this.mExitAnimationEnd = 0L;
      bool = true;
    } 
    if (this.mEnterAnimationEnd != 0L) {
      this.mEnterAnimationEnd = 0L;
      bool = true;
    } 
    if (bool)
      invalidateSelf(); 
  }
  
  @NonNull
  public Drawable mutate() {
    if (!this.mMutated && super.mutate() == this) {
      DrawableContainerState drawableContainerState = cloneConstantState();
      drawableContainerState.mutate();
      setConstantState(drawableContainerState);
      this.mMutated = true;
    } 
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.mLastDrawable != null)
      this.mLastDrawable.setBounds(paramRect); 
    if (this.mCurrDrawable != null)
      this.mCurrDrawable.setBounds(paramRect); 
  }
  
  public boolean onLayoutDirectionChanged(int paramInt) {
    return this.mDrawableContainerState.setLayoutDirection(paramInt, getCurrentIndex());
  }
  
  protected boolean onLevelChange(int paramInt) {
    return (this.mLastDrawable != null) ? this.mLastDrawable.setLevel(paramInt) : ((this.mCurrDrawable != null) ? this.mCurrDrawable.setLevel(paramInt) : false);
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    return (this.mLastDrawable != null) ? this.mLastDrawable.setState(paramArrayOfint) : ((this.mCurrDrawable != null) ? this.mCurrDrawable.setState(paramArrayOfint) : false);
  }
  
  public void scheduleDrawable(@NonNull Drawable paramDrawable, @NonNull Runnable paramRunnable, long paramLong) {
    if (paramDrawable == this.mCurrDrawable && getCallback() != null)
      getCallback().scheduleDrawable(this, paramRunnable, paramLong); 
  }
  
  boolean selectDrawable(int paramInt) {
    if (paramInt == this.mCurIndex)
      return false; 
    long l = SystemClock.uptimeMillis();
    if (this.mDrawableContainerState.mExitFadeDuration > 0) {
      if (this.mLastDrawable != null)
        this.mLastDrawable.setVisible(false, false); 
      if (this.mCurrDrawable != null) {
        this.mLastDrawable = this.mCurrDrawable;
        this.mLastIndex = this.mCurIndex;
        this.mExitAnimationEnd = this.mDrawableContainerState.mExitFadeDuration + l;
      } else {
        this.mLastDrawable = null;
        this.mLastIndex = -1;
        this.mExitAnimationEnd = 0L;
      } 
    } else if (this.mCurrDrawable != null) {
      this.mCurrDrawable.setVisible(false, false);
    } 
    if (paramInt >= 0 && paramInt < this.mDrawableContainerState.mNumChildren) {
      Drawable drawable = this.mDrawableContainerState.getChild(paramInt);
      this.mCurrDrawable = drawable;
      this.mCurIndex = paramInt;
      if (drawable != null) {
        if (this.mDrawableContainerState.mEnterFadeDuration > 0)
          this.mEnterAnimationEnd = l + this.mDrawableContainerState.mEnterFadeDuration; 
        initializeDrawableForDisplay(drawable);
      } 
    } else {
      this.mCurrDrawable = null;
      this.mCurIndex = -1;
    } 
    if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
      if (this.mAnimationRunnable == null) {
        this.mAnimationRunnable = new Runnable() {
            public void run() {
              DrawableContainer.this.animate(true);
              DrawableContainer.this.invalidateSelf();
            }
          };
      } else {
        unscheduleSelf(this.mAnimationRunnable);
      } 
      animate(true);
    } 
    invalidateSelf();
    return true;
  }
  
  public void setAlpha(int paramInt) {
    if (!this.mHasAlpha || this.mAlpha != paramInt) {
      this.mHasAlpha = true;
      this.mAlpha = paramInt;
      if (this.mCurrDrawable != null)
        if (this.mEnterAnimationEnd == 0L) {
          this.mCurrDrawable.setAlpha(paramInt);
        } else {
          animate(false);
        }  
    } 
  }
  
  public void setAutoMirrored(boolean paramBoolean) {
    if (this.mDrawableContainerState.mAutoMirrored != paramBoolean) {
      this.mDrawableContainerState.mAutoMirrored = paramBoolean;
      if (this.mCurrDrawable != null)
        DrawableCompat.setAutoMirrored(this.mCurrDrawable, this.mDrawableContainerState.mAutoMirrored); 
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mDrawableContainerState.mHasColorFilter = true;
    if (this.mDrawableContainerState.mColorFilter != paramColorFilter) {
      this.mDrawableContainerState.mColorFilter = paramColorFilter;
      if (this.mCurrDrawable != null)
        this.mCurrDrawable.setColorFilter(paramColorFilter); 
    } 
  }
  
  protected void setConstantState(DrawableContainerState paramDrawableContainerState) {
    this.mDrawableContainerState = paramDrawableContainerState;
    if (this.mCurIndex >= 0) {
      this.mCurrDrawable = paramDrawableContainerState.getChild(this.mCurIndex);
      if (this.mCurrDrawable != null)
        initializeDrawableForDisplay(this.mCurrDrawable); 
    } 
    this.mLastIndex = -1;
    this.mLastDrawable = null;
  }
  
  void setCurrentIndex(int paramInt) {
    selectDrawable(paramInt);
  }
  
  public void setDither(boolean paramBoolean) {
    if (this.mDrawableContainerState.mDither != paramBoolean) {
      this.mDrawableContainerState.mDither = paramBoolean;
      if (this.mCurrDrawable != null)
        this.mCurrDrawable.setDither(this.mDrawableContainerState.mDither); 
    } 
  }
  
  public void setEnterFadeDuration(int paramInt) {
    this.mDrawableContainerState.mEnterFadeDuration = paramInt;
  }
  
  public void setExitFadeDuration(int paramInt) {
    this.mDrawableContainerState.mExitFadeDuration = paramInt;
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2) {
    if (this.mCurrDrawable != null)
      DrawableCompat.setHotspot(this.mCurrDrawable, paramFloat1, paramFloat2); 
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mHotspotBounds == null) {
      this.mHotspotBounds = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      this.mHotspotBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
    if (this.mCurrDrawable != null)
      DrawableCompat.setHotspotBounds(this.mCurrDrawable, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    this.mDrawableContainerState.mHasTintList = true;
    if (this.mDrawableContainerState.mTintList != paramColorStateList) {
      this.mDrawableContainerState.mTintList = paramColorStateList;
      DrawableCompat.setTintList(this.mCurrDrawable, paramColorStateList);
    } 
  }
  
  public void setTintMode(@NonNull PorterDuff.Mode paramMode) {
    this.mDrawableContainerState.mHasTintMode = true;
    if (this.mDrawableContainerState.mTintMode != paramMode) {
      this.mDrawableContainerState.mTintMode = paramMode;
      DrawableCompat.setTintMode(this.mCurrDrawable, paramMode);
    } 
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if (this.mLastDrawable != null)
      this.mLastDrawable.setVisible(paramBoolean1, paramBoolean2); 
    if (this.mCurrDrawable != null)
      this.mCurrDrawable.setVisible(paramBoolean1, paramBoolean2); 
    return bool;
  }
  
  public void unscheduleDrawable(@NonNull Drawable paramDrawable, @NonNull Runnable paramRunnable) {
    if (paramDrawable == this.mCurrDrawable && getCallback() != null)
      getCallback().unscheduleDrawable(this, paramRunnable); 
  }
  
  final void updateDensity(Resources paramResources) {
    this.mDrawableContainerState.updateDensity(paramResources);
  }
  
  static class BlockInvalidateCallback implements Drawable.Callback {
    private Drawable.Callback mCallback;
    
    public void invalidateDrawable(@NonNull Drawable param1Drawable) {}
    
    public void scheduleDrawable(@NonNull Drawable param1Drawable, @NonNull Runnable param1Runnable, long param1Long) {
      if (this.mCallback != null)
        this.mCallback.scheduleDrawable(param1Drawable, param1Runnable, param1Long); 
    }
    
    public void unscheduleDrawable(@NonNull Drawable param1Drawable, @NonNull Runnable param1Runnable) {
      if (this.mCallback != null)
        this.mCallback.unscheduleDrawable(param1Drawable, param1Runnable); 
    }
    
    public Drawable.Callback unwrap() {
      Drawable.Callback callback = this.mCallback;
      this.mCallback = null;
      return callback;
    }
    
    public BlockInvalidateCallback wrap(Drawable.Callback param1Callback) {
      this.mCallback = param1Callback;
      return this;
    }
  }
  
  static abstract class DrawableContainerState extends Drawable.ConstantState {
    boolean mAutoMirrored;
    
    boolean mCanConstantState;
    
    int mChangingConfigurations;
    
    boolean mCheckedConstantSize;
    
    boolean mCheckedConstantState;
    
    boolean mCheckedOpacity;
    
    boolean mCheckedPadding;
    
    boolean mCheckedStateful;
    
    int mChildrenChangingConfigurations;
    
    ColorFilter mColorFilter;
    
    int mConstantHeight;
    
    int mConstantMinimumHeight;
    
    int mConstantMinimumWidth;
    
    Rect mConstantPadding;
    
    boolean mConstantSize;
    
    int mConstantWidth;
    
    int mDensity;
    
    boolean mDither;
    
    SparseArray<Drawable.ConstantState> mDrawableFutures;
    
    Drawable[] mDrawables;
    
    int mEnterFadeDuration;
    
    int mExitFadeDuration;
    
    boolean mHasColorFilter;
    
    boolean mHasTintList;
    
    boolean mHasTintMode;
    
    int mLayoutDirection;
    
    boolean mMutated;
    
    int mNumChildren;
    
    int mOpacity;
    
    final DrawableContainer mOwner;
    
    Resources mSourceRes;
    
    boolean mStateful;
    
    ColorStateList mTintList;
    
    PorterDuff.Mode mTintMode;
    
    boolean mVariablePadding;
    
    DrawableContainerState(DrawableContainerState param1DrawableContainerState, DrawableContainer param1DrawableContainer, Resources param1Resources) {
      byte b2;
      this.mDensity = 160;
      byte b1 = 0;
      this.mVariablePadding = false;
      this.mConstantSize = false;
      this.mDither = true;
      this.mEnterFadeDuration = 0;
      this.mExitFadeDuration = 0;
      this.mOwner = param1DrawableContainer;
      if (param1Resources != null) {
        Resources resources = param1Resources;
      } else if (param1DrawableContainerState != null) {
        Resources resources = param1DrawableContainerState.mSourceRes;
      } else {
        param1DrawableContainer = null;
      } 
      this.mSourceRes = (Resources)param1DrawableContainer;
      if (param1DrawableContainerState != null) {
        b2 = param1DrawableContainerState.mDensity;
      } else {
        b2 = 0;
      } 
      this.mDensity = DrawableContainer.resolveDensity(param1Resources, b2);
      if (param1DrawableContainerState != null) {
        this.mChangingConfigurations = param1DrawableContainerState.mChangingConfigurations;
        this.mChildrenChangingConfigurations = param1DrawableContainerState.mChildrenChangingConfigurations;
        this.mCheckedConstantState = true;
        this.mCanConstantState = true;
        this.mVariablePadding = param1DrawableContainerState.mVariablePadding;
        this.mConstantSize = param1DrawableContainerState.mConstantSize;
        this.mDither = param1DrawableContainerState.mDither;
        this.mMutated = param1DrawableContainerState.mMutated;
        this.mLayoutDirection = param1DrawableContainerState.mLayoutDirection;
        this.mEnterFadeDuration = param1DrawableContainerState.mEnterFadeDuration;
        this.mExitFadeDuration = param1DrawableContainerState.mExitFadeDuration;
        this.mAutoMirrored = param1DrawableContainerState.mAutoMirrored;
        this.mColorFilter = param1DrawableContainerState.mColorFilter;
        this.mHasColorFilter = param1DrawableContainerState.mHasColorFilter;
        this.mTintList = param1DrawableContainerState.mTintList;
        this.mTintMode = param1DrawableContainerState.mTintMode;
        this.mHasTintList = param1DrawableContainerState.mHasTintList;
        this.mHasTintMode = param1DrawableContainerState.mHasTintMode;
        if (param1DrawableContainerState.mDensity == this.mDensity) {
          if (param1DrawableContainerState.mCheckedPadding) {
            this.mConstantPadding = new Rect(param1DrawableContainerState.mConstantPadding);
            this.mCheckedPadding = true;
          } 
          if (param1DrawableContainerState.mCheckedConstantSize) {
            this.mConstantWidth = param1DrawableContainerState.mConstantWidth;
            this.mConstantHeight = param1DrawableContainerState.mConstantHeight;
            this.mConstantMinimumWidth = param1DrawableContainerState.mConstantMinimumWidth;
            this.mConstantMinimumHeight = param1DrawableContainerState.mConstantMinimumHeight;
            this.mCheckedConstantSize = true;
          } 
        } 
        if (param1DrawableContainerState.mCheckedOpacity) {
          this.mOpacity = param1DrawableContainerState.mOpacity;
          this.mCheckedOpacity = true;
        } 
        if (param1DrawableContainerState.mCheckedStateful) {
          this.mStateful = param1DrawableContainerState.mStateful;
          this.mCheckedStateful = true;
        } 
        Drawable[] arrayOfDrawable = param1DrawableContainerState.mDrawables;
        this.mDrawables = new Drawable[arrayOfDrawable.length];
        this.mNumChildren = param1DrawableContainerState.mNumChildren;
        SparseArray<Drawable.ConstantState> sparseArray = param1DrawableContainerState.mDrawableFutures;
        if (sparseArray != null) {
          this.mDrawableFutures = sparseArray.clone();
        } else {
          this.mDrawableFutures = new SparseArray(this.mNumChildren);
        } 
        int i = this.mNumChildren;
        for (b2 = b1; b2 < i; b2++) {
          if (arrayOfDrawable[b2] != null) {
            Drawable.ConstantState constantState = arrayOfDrawable[b2].getConstantState();
            if (constantState != null) {
              this.mDrawableFutures.put(b2, constantState);
            } else {
              this.mDrawables[b2] = arrayOfDrawable[b2];
            } 
          } 
        } 
      } else {
        this.mDrawables = new Drawable[10];
        this.mNumChildren = 0;
      } 
    }
    
    private void createAllFutures() {
      if (this.mDrawableFutures != null) {
        int i = this.mDrawableFutures.size();
        for (byte b = 0; b < i; b++) {
          int j = this.mDrawableFutures.keyAt(b);
          Drawable.ConstantState constantState = (Drawable.ConstantState)this.mDrawableFutures.valueAt(b);
          this.mDrawables[j] = prepareDrawable(constantState.newDrawable(this.mSourceRes));
        } 
        this.mDrawableFutures = null;
      } 
    }
    
    private Drawable prepareDrawable(Drawable param1Drawable) {
      if (Build.VERSION.SDK_INT >= 23)
        param1Drawable.setLayoutDirection(this.mLayoutDirection); 
      param1Drawable = param1Drawable.mutate();
      param1Drawable.setCallback(this.mOwner);
      return param1Drawable;
    }
    
    public final int addChild(Drawable param1Drawable) {
      int i = this.mNumChildren;
      if (i >= this.mDrawables.length)
        growArray(i, i + 10); 
      param1Drawable.mutate();
      param1Drawable.setVisible(false, true);
      param1Drawable.setCallback(this.mOwner);
      this.mDrawables[i] = param1Drawable;
      this.mNumChildren++;
      int j = this.mChildrenChangingConfigurations;
      this.mChildrenChangingConfigurations = param1Drawable.getChangingConfigurations() | j;
      invalidateCache();
      this.mConstantPadding = null;
      this.mCheckedPadding = false;
      this.mCheckedConstantSize = false;
      this.mCheckedConstantState = false;
      return i;
    }
    
    @RequiresApi(21)
    final void applyTheme(Resources.Theme param1Theme) {
      if (param1Theme != null) {
        createAllFutures();
        int i = this.mNumChildren;
        Drawable[] arrayOfDrawable = this.mDrawables;
        for (byte b = 0; b < i; b++) {
          if (arrayOfDrawable[b] != null && arrayOfDrawable[b].canApplyTheme()) {
            arrayOfDrawable[b].applyTheme(param1Theme);
            this.mChildrenChangingConfigurations |= arrayOfDrawable[b].getChangingConfigurations();
          } 
        } 
        updateDensity(param1Theme.getResources());
      } 
    }
    
    @RequiresApi(21)
    public boolean canApplyTheme() {
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      for (byte b = 0; b < i; b++) {
        Drawable drawable = arrayOfDrawable[b];
        if (drawable != null) {
          if (drawable.canApplyTheme())
            return true; 
        } else {
          Drawable.ConstantState constantState = (Drawable.ConstantState)this.mDrawableFutures.get(b);
          if (constantState != null && constantState.canApplyTheme())
            return true; 
        } 
      } 
      return false;
    }
    
    public boolean canConstantState() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mCheckedConstantState : Z
      //   6: ifeq -> 18
      //   9: aload_0
      //   10: getfield mCanConstantState : Z
      //   13: istore_1
      //   14: aload_0
      //   15: monitorexit
      //   16: iload_1
      //   17: ireturn
      //   18: aload_0
      //   19: invokespecial createAllFutures : ()V
      //   22: aload_0
      //   23: iconst_1
      //   24: putfield mCheckedConstantState : Z
      //   27: aload_0
      //   28: getfield mNumChildren : I
      //   31: istore_2
      //   32: aload_0
      //   33: getfield mDrawables : [Landroid/graphics/drawable/Drawable;
      //   36: astore_3
      //   37: iconst_0
      //   38: istore #4
      //   40: iload #4
      //   42: iload_2
      //   43: if_icmpge -> 71
      //   46: aload_3
      //   47: iload #4
      //   49: aaload
      //   50: invokevirtual getConstantState : ()Landroid/graphics/drawable/Drawable$ConstantState;
      //   53: ifnonnull -> 65
      //   56: aload_0
      //   57: iconst_0
      //   58: putfield mCanConstantState : Z
      //   61: aload_0
      //   62: monitorexit
      //   63: iconst_0
      //   64: ireturn
      //   65: iinc #4, 1
      //   68: goto -> 40
      //   71: aload_0
      //   72: iconst_1
      //   73: putfield mCanConstantState : Z
      //   76: aload_0
      //   77: monitorexit
      //   78: iconst_1
      //   79: ireturn
      //   80: astore_3
      //   81: aload_0
      //   82: monitorexit
      //   83: aload_3
      //   84: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	80	finally
      //   18	37	80	finally
      //   46	61	80	finally
      //   71	76	80	finally
    }
    
    final void clearMutated() {
      this.mMutated = false;
    }
    
    protected void computeConstantSize() {
      this.mCheckedConstantSize = true;
      createAllFutures();
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      this.mConstantHeight = -1;
      this.mConstantWidth = -1;
      byte b = 0;
      this.mConstantMinimumHeight = 0;
      this.mConstantMinimumWidth = 0;
      while (b < i) {
        Drawable drawable = arrayOfDrawable[b];
        int j = drawable.getIntrinsicWidth();
        if (j > this.mConstantWidth)
          this.mConstantWidth = j; 
        j = drawable.getIntrinsicHeight();
        if (j > this.mConstantHeight)
          this.mConstantHeight = j; 
        j = drawable.getMinimumWidth();
        if (j > this.mConstantMinimumWidth)
          this.mConstantMinimumWidth = j; 
        j = drawable.getMinimumHeight();
        if (j > this.mConstantMinimumHeight)
          this.mConstantMinimumHeight = j; 
        b++;
      } 
    }
    
    final int getCapacity() {
      return this.mDrawables.length;
    }
    
    public int getChangingConfigurations() {
      return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
    }
    
    public final Drawable getChild(int param1Int) {
      Drawable drawable = this.mDrawables[param1Int];
      if (drawable != null)
        return drawable; 
      if (this.mDrawableFutures != null) {
        int i = this.mDrawableFutures.indexOfKey(param1Int);
        if (i >= 0) {
          drawable = prepareDrawable(((Drawable.ConstantState)this.mDrawableFutures.valueAt(i)).newDrawable(this.mSourceRes));
          this.mDrawables[param1Int] = drawable;
          this.mDrawableFutures.removeAt(i);
          if (this.mDrawableFutures.size() == 0)
            this.mDrawableFutures = null; 
          return drawable;
        } 
      } 
      return null;
    }
    
    public final int getChildCount() {
      return this.mNumChildren;
    }
    
    public final int getConstantHeight() {
      if (!this.mCheckedConstantSize)
        computeConstantSize(); 
      return this.mConstantHeight;
    }
    
    public final int getConstantMinimumHeight() {
      if (!this.mCheckedConstantSize)
        computeConstantSize(); 
      return this.mConstantMinimumHeight;
    }
    
    public final int getConstantMinimumWidth() {
      if (!this.mCheckedConstantSize)
        computeConstantSize(); 
      return this.mConstantMinimumWidth;
    }
    
    public final Rect getConstantPadding() {
      if (this.mVariablePadding)
        return null; 
      if (this.mConstantPadding != null || this.mCheckedPadding)
        return this.mConstantPadding; 
      createAllFutures();
      Rect rect1 = new Rect();
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      Rect rect2 = null;
      byte b = 0;
      while (b < i) {
        Rect rect = rect2;
        if (arrayOfDrawable[b].getPadding(rect1)) {
          Rect rect3 = rect2;
          if (rect2 == null)
            rect3 = new Rect(0, 0, 0, 0); 
          if (rect1.left > rect3.left)
            rect3.left = rect1.left; 
          if (rect1.top > rect3.top)
            rect3.top = rect1.top; 
          if (rect1.right > rect3.right)
            rect3.right = rect1.right; 
          rect = rect3;
          if (rect1.bottom > rect3.bottom) {
            rect3.bottom = rect1.bottom;
            rect = rect3;
          } 
        } 
        b++;
        rect2 = rect;
      } 
      this.mCheckedPadding = true;
      this.mConstantPadding = rect2;
      return rect2;
    }
    
    public final int getConstantWidth() {
      if (!this.mCheckedConstantSize)
        computeConstantSize(); 
      return this.mConstantWidth;
    }
    
    public final int getEnterFadeDuration() {
      return this.mEnterFadeDuration;
    }
    
    public final int getExitFadeDuration() {
      return this.mExitFadeDuration;
    }
    
    public final int getOpacity() {
      if (this.mCheckedOpacity)
        return this.mOpacity; 
      createAllFutures();
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      if (i > 0) {
        b = arrayOfDrawable[0].getOpacity();
      } else {
        b = -2;
      } 
      boolean bool = true;
      int j = b;
      for (byte b = bool; b < i; b++)
        j = Drawable.resolveOpacity(j, arrayOfDrawable[b].getOpacity()); 
      this.mOpacity = j;
      this.mCheckedOpacity = true;
      return j;
    }
    
    public void growArray(int param1Int1, int param1Int2) {
      Drawable[] arrayOfDrawable = new Drawable[param1Int2];
      System.arraycopy(this.mDrawables, 0, arrayOfDrawable, 0, param1Int1);
      this.mDrawables = arrayOfDrawable;
    }
    
    void invalidateCache() {
      this.mCheckedOpacity = false;
      this.mCheckedStateful = false;
    }
    
    public final boolean isConstantSize() {
      return this.mConstantSize;
    }
    
    public final boolean isStateful() {
      boolean bool2;
      if (this.mCheckedStateful)
        return this.mStateful; 
      createAllFutures();
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      boolean bool1 = false;
      byte b = 0;
      while (true) {
        bool2 = bool1;
        if (b < i) {
          if (arrayOfDrawable[b].isStateful()) {
            bool2 = true;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      this.mStateful = bool2;
      this.mCheckedStateful = true;
      return bool2;
    }
    
    void mutate() {
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      for (byte b = 0; b < i; b++) {
        if (arrayOfDrawable[b] != null)
          arrayOfDrawable[b].mutate(); 
      } 
      this.mMutated = true;
    }
    
    public final void setConstantSize(boolean param1Boolean) {
      this.mConstantSize = param1Boolean;
    }
    
    public final void setEnterFadeDuration(int param1Int) {
      this.mEnterFadeDuration = param1Int;
    }
    
    public final void setExitFadeDuration(int param1Int) {
      this.mExitFadeDuration = param1Int;
    }
    
    final boolean setLayoutDirection(int param1Int1, int param1Int2) {
      int i = this.mNumChildren;
      Drawable[] arrayOfDrawable = this.mDrawables;
      byte b = 0;
      boolean bool;
      for (bool = false; b < i; bool = bool1) {
        boolean bool1 = bool;
        if (arrayOfDrawable[b] != null) {
          boolean bool2;
          if (Build.VERSION.SDK_INT >= 23) {
            bool2 = arrayOfDrawable[b].setLayoutDirection(param1Int1);
          } else {
            bool2 = false;
          } 
          bool1 = bool;
          if (b == param1Int2)
            bool1 = bool2; 
        } 
        b++;
      } 
      this.mLayoutDirection = param1Int1;
      return bool;
    }
    
    public final void setVariablePadding(boolean param1Boolean) {
      this.mVariablePadding = param1Boolean;
    }
    
    final void updateDensity(Resources param1Resources) {
      if (param1Resources != null) {
        this.mSourceRes = param1Resources;
        int i = DrawableContainer.resolveDensity(param1Resources, this.mDensity);
        int j = this.mDensity;
        this.mDensity = i;
        if (j != i) {
          this.mCheckedConstantSize = false;
          this.mCheckedPadding = false;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\graphics\drawable\DrawableContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */