package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup {
  private static final boolean ALLOW_EDGE_LOCK = false;
  
  static final boolean CAN_HIDE_DESCENDANTS;
  
  private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
  
  private static final int DEFAULT_SCRIM_COLOR = -1728053248;
  
  private static final int DRAWER_ELEVATION = 10;
  
  static final int[] LAYOUT_ATTRS;
  
  public static final int LOCK_MODE_LOCKED_CLOSED = 1;
  
  public static final int LOCK_MODE_LOCKED_OPEN = 2;
  
  public static final int LOCK_MODE_UNDEFINED = 3;
  
  public static final int LOCK_MODE_UNLOCKED = 0;
  
  private static final int MIN_DRAWER_MARGIN = 64;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final int PEEK_DELAY = 160;
  
  private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "DrawerLayout";
  
  private static final int[] THEME_ATTRS = new int[] { 16843828 };
  
  private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
  
  private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
  
  private Rect mChildHitRect;
  
  private Matrix mChildInvertedMatrix;
  
  private boolean mChildrenCanceledTouch;
  
  private boolean mDisallowInterceptRequested;
  
  private boolean mDrawStatusBarBackground;
  
  private float mDrawerElevation;
  
  private int mDrawerState;
  
  private boolean mFirstLayout = true;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private Object mLastInsets;
  
  private final ViewDragCallback mLeftCallback;
  
  private final ViewDragHelper mLeftDragger;
  
  @Nullable
  private DrawerListener mListener;
  
  private List<DrawerListener> mListeners;
  
  private int mLockModeEnd = 3;
  
  private int mLockModeLeft = 3;
  
  private int mLockModeRight = 3;
  
  private int mLockModeStart = 3;
  
  private int mMinDrawerMargin;
  
  private final ArrayList<View> mNonDrawerViews;
  
  private final ViewDragCallback mRightCallback;
  
  private final ViewDragHelper mRightDragger;
  
  private int mScrimColor = -1728053248;
  
  private float mScrimOpacity;
  
  private Paint mScrimPaint = new Paint();
  
  private Drawable mShadowEnd = null;
  
  private Drawable mShadowLeft = null;
  
  private Drawable mShadowLeftResolved;
  
  private Drawable mShadowRight = null;
  
  private Drawable mShadowRightResolved;
  
  private Drawable mShadowStart = null;
  
  private Drawable mStatusBarBackground;
  
  private CharSequence mTitleLeft;
  
  private CharSequence mTitleRight;
  
  static {
    LAYOUT_ATTRS = new int[] { 16842931 };
    if (Build.VERSION.SDK_INT >= 19) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    CAN_HIDE_DESCENDANTS = bool2;
    if (Build.VERSION.SDK_INT >= 21) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    SET_DRAWER_SHADOW_FROM_ELEVATION = bool2;
  }
  
  public DrawerLayout(@NonNull Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public DrawerLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DrawerLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setDescendantFocusability(262144);
    float f1 = (getResources().getDisplayMetrics()).density;
    this.mMinDrawerMargin = (int)(64.0F * f1 + 0.5F);
    float f2 = 400.0F * f1;
    this.mLeftCallback = new ViewDragCallback(3);
    this.mRightCallback = new ViewDragCallback(5);
    this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
    this.mLeftDragger.setEdgeTrackingEnabled(1);
    this.mLeftDragger.setMinVelocity(f2);
    this.mLeftCallback.setDragger(this.mLeftDragger);
    this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
    this.mRightDragger.setEdgeTrackingEnabled(2);
    this.mRightDragger.setMinVelocity(f2);
    this.mRightCallback.setDragger(this.mRightDragger);
    setFocusableInTouchMode(true);
    ViewCompat.setImportantForAccessibility((View)this, 1);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    setMotionEventSplittingEnabled(false);
    if (ViewCompat.getFitsSystemWindows((View)this))
      if (Build.VERSION.SDK_INT >= 21) {
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
              public WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
                boolean bool;
                DrawerLayout drawerLayout = (DrawerLayout)param1View;
                if (param1WindowInsets.getSystemWindowInsetTop() > 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                drawerLayout.setChildInsets(param1WindowInsets, bool);
                return param1WindowInsets.consumeSystemWindowInsets();
              }
            });
        setSystemUiVisibility(1280);
        TypedArray typedArray = paramContext.obtainStyledAttributes(THEME_ATTRS);
        try {
          this.mStatusBarBackground = typedArray.getDrawable(0);
        } finally {
          typedArray.recycle();
        } 
      } else {
        this.mStatusBarBackground = null;
      }  
    this.mDrawerElevation = f1 * 10.0F;
    this.mNonDrawerViews = new ArrayList<View>();
  }
  
  private boolean dispatchTransformedGenericPointerEvent(MotionEvent paramMotionEvent, View paramView) {
    boolean bool;
    if (!paramView.getMatrix().isIdentity()) {
      paramMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.recycle();
    } else {
      float f1 = (getScrollX() - paramView.getLeft());
      float f2 = (getScrollY() - paramView.getTop());
      paramMotionEvent.offsetLocation(f1, f2);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-f1, -f2);
    } 
    return bool;
  }
  
  private MotionEvent getTransformedMotionEvent(MotionEvent paramMotionEvent, View paramView) {
    float f1 = (getScrollX() - paramView.getLeft());
    float f2 = (getScrollY() - paramView.getTop());
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.offsetLocation(f1, f2);
    Matrix matrix = paramView.getMatrix();
    if (!matrix.isIdentity()) {
      if (this.mChildInvertedMatrix == null)
        this.mChildInvertedMatrix = new Matrix(); 
      matrix.invert(this.mChildInvertedMatrix);
      paramMotionEvent.transform(this.mChildInvertedMatrix);
    } 
    return paramMotionEvent;
  }
  
  static String gravityToString(int paramInt) {
    return ((paramInt & 0x3) == 3) ? "LEFT" : (((paramInt & 0x5) == 5) ? "RIGHT" : Integer.toHexString(paramInt));
  }
  
  private static boolean hasOpaqueBackground(View paramView) {
    Drawable drawable = paramView.getBackground();
    boolean bool = false;
    if (drawable != null) {
      if (drawable.getOpacity() == -1)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private boolean hasPeekingDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      if (((LayoutParams)getChildAt(b).getLayoutParams()).isPeeking)
        return true; 
    } 
    return false;
  }
  
  private boolean hasVisibleDrawer() {
    boolean bool;
    if (findVisibleDrawer() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean includeChildForAccessibility(View paramView) {
    boolean bool;
    if (ViewCompat.getImportantForAccessibility(paramView) != 4 && ViewCompat.getImportantForAccessibility(paramView) != 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isInBoundsOfChild(float paramFloat1, float paramFloat2, View paramView) {
    if (this.mChildHitRect == null)
      this.mChildHitRect = new Rect(); 
    paramView.getHitRect(this.mChildHitRect);
    return this.mChildHitRect.contains((int)paramFloat1, (int)paramFloat2);
  }
  
  private boolean mirror(Drawable paramDrawable, int paramInt) {
    if (paramDrawable == null || !DrawableCompat.isAutoMirrored(paramDrawable))
      return false; 
    DrawableCompat.setLayoutDirection(paramDrawable, paramInt);
    return true;
  }
  
  private Drawable resolveLeftShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      if (this.mShadowStart != null) {
        mirror(this.mShadowStart, i);
        return this.mShadowStart;
      } 
    } else if (this.mShadowEnd != null) {
      mirror(this.mShadowEnd, i);
      return this.mShadowEnd;
    } 
    return this.mShadowLeft;
  }
  
  private Drawable resolveRightShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      if (this.mShadowEnd != null) {
        mirror(this.mShadowEnd, i);
        return this.mShadowEnd;
      } 
    } else if (this.mShadowStart != null) {
      mirror(this.mShadowStart, i);
      return this.mShadowStart;
    } 
    return this.mShadowRight;
  }
  
  private void resolveShadowDrawables() {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    this.mShadowLeftResolved = resolveLeftShadow();
    this.mShadowRightResolved = resolveRightShadow();
  }
  
  private void updateChildrenImportantForAccessibility(View paramView, boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((!paramBoolean && !isDrawerView(view)) || (paramBoolean && view == paramView)) {
        ViewCompat.setImportantForAccessibility(view, 1);
      } else {
        ViewCompat.setImportantForAccessibility(view, 4);
      } 
    } 
  }
  
  public void addDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    if (this.mListeners == null)
      this.mListeners = new ArrayList<DrawerListener>(); 
    this.mListeners.add(paramDrawerListener);
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    if (getDescendantFocusability() == 393216)
      return; 
    int i = getChildCount();
    boolean bool = false;
    byte b = 0;
    int j = 0;
    while (b < i) {
      View view = getChildAt(b);
      if (isDrawerView(view)) {
        if (isDrawerOpen(view)) {
          view.addFocusables(paramArrayList, paramInt1, paramInt2);
          j = 1;
        } 
      } else {
        this.mNonDrawerViews.add(view);
      } 
      b++;
    } 
    if (!j) {
      j = this.mNonDrawerViews.size();
      for (b = bool; b < j; b++) {
        View view = this.mNonDrawerViews.get(b);
        if (view.getVisibility() == 0)
          view.addFocusables(paramArrayList, paramInt1, paramInt2); 
      } 
    } 
    this.mNonDrawerViews.clear();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (findOpenDrawer() != null || isDrawerView(paramView)) {
      ViewCompat.setImportantForAccessibility(paramView, 4);
    } else {
      ViewCompat.setImportantForAccessibility(paramView, 1);
    } 
    if (!CAN_HIDE_DESCENDANTS)
      ViewCompat.setAccessibilityDelegate(paramView, this.mChildAccessibilityDelegate); 
  }
  
  void cancelChildViewTouch() {
    if (!this.mChildrenCanceledTouch) {
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
      int i = getChildCount();
      for (byte b = 0; b < i; b++)
        getChildAt(b).dispatchTouchEvent(motionEvent); 
      motionEvent.recycle();
      this.mChildrenCanceledTouch = true;
    } 
  }
  
  boolean checkDrawerViewAbsoluteGravity(View paramView, int paramInt) {
    boolean bool;
    if ((getDrawerViewAbsoluteGravity(paramView) & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void closeDrawer(int paramInt) {
    closeDrawer(paramInt, true);
  }
  
  public void closeDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      closeDrawer(view, paramBoolean);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No drawer view found with gravity ");
    stringBuilder.append(gravityToString(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void closeDrawer(@NonNull View paramView) {
    closeDrawer(paramView, true);
  }
  
  public void closeDrawer(@NonNull View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 0.0F;
        layoutParams.openState = 0;
      } else if (paramBoolean) {
        layoutParams.openState |= 0x4;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, -paramView.getWidth(), paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 0.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(4);
      } 
      invalidate();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a sliding drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void closeDrawers() {
    closeDrawers(false);
  }
  
  void closeDrawers(boolean paramBoolean) {
    boolean bool;
    int i = getChildCount();
    byte b1 = 0;
    byte b2 = 0;
    while (b1 < i) {
      boolean bool1;
      View view = getChildAt(b1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int j = b2;
      if (isDrawerView(view))
        if (paramBoolean && !layoutParams.isPeeking) {
          j = b2;
        } else {
          boolean bool2;
          j = view.getWidth();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            bool2 = b2 | this.mLeftDragger.smoothSlideViewTo(view, -j, view.getTop());
          } else {
            bool2 |= this.mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
          } 
          layoutParams.isPeeking = false;
          bool1 = bool2;
        }  
      b1++;
      bool = bool1;
    } 
    this.mLeftCallback.removeCallbacks();
    this.mRightCallback.removeCallbacks();
    if (bool)
      invalidate(); 
  }
  
  public void computeScroll() {
    int i = getChildCount();
    float f = 0.0F;
    for (byte b = 0; b < i; b++)
      f = Math.max(f, ((LayoutParams)getChildAt(b).getLayoutParams()).onScreen); 
    this.mScrimOpacity = f;
    boolean bool1 = this.mLeftDragger.continueSettling(true);
    boolean bool2 = this.mRightDragger.continueSettling(true);
    if (bool1 || bool2)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) == 0 || paramMotionEvent.getAction() == 10 || this.mScrimOpacity <= 0.0F)
      return super.dispatchGenericMotionEvent(paramMotionEvent); 
    int i = getChildCount();
    if (i != 0) {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      while (--i >= 0) {
        View view = getChildAt(i);
        if (isInBoundsOfChild(f1, f2, view) && !isContentView(view) && dispatchTransformedGenericPointerEvent(paramMotionEvent, view))
          return true; 
        i--;
      } 
    } 
    return false;
  }
  
  void dispatchOnDrawerClosed(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 1) {
      layoutParams.openState = 0;
      if (this.mListeners != null)
        for (int i = this.mListeners.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerClosed(paramView);  
      updateChildrenImportantForAccessibility(paramView, false);
      if (hasWindowFocus()) {
        paramView = getRootView();
        if (paramView != null)
          paramView.sendAccessibilityEvent(32); 
      } 
    } 
  }
  
  void dispatchOnDrawerOpened(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 0) {
      layoutParams.openState = 1;
      if (this.mListeners != null)
        for (int i = this.mListeners.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerOpened(paramView);  
      updateChildrenImportantForAccessibility(paramView, true);
      if (hasWindowFocus())
        sendAccessibilityEvent(32); 
    } 
  }
  
  void dispatchOnDrawerSlide(View paramView, float paramFloat) {
    if (this.mListeners != null)
      for (int i = this.mListeners.size() - 1; i >= 0; i--)
        ((DrawerListener)this.mListeners.get(i)).onDrawerSlide(paramView, paramFloat);  
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    int i = getHeight();
    boolean bool1 = isContentView(paramView);
    int j = getWidth();
    int k = paramCanvas.save();
    int m = 0;
    if (bool1) {
      int n = getChildCount();
      byte b = 0;
      m = 0;
      while (b < n) {
        View view = getChildAt(b);
        int i1 = m;
        int i2 = j;
        if (view != paramView) {
          i1 = m;
          i2 = j;
          if (view.getVisibility() == 0) {
            i1 = m;
            i2 = j;
            if (hasOpaqueBackground(view)) {
              i1 = m;
              i2 = j;
              if (isDrawerView(view))
                if (view.getHeight() < i) {
                  i1 = m;
                  i2 = j;
                } else if (checkDrawerViewAbsoluteGravity(view, 3)) {
                  int i3 = view.getRight();
                  i1 = m;
                  i2 = j;
                  if (i3 > m) {
                    i1 = i3;
                    i2 = j;
                  } 
                } else {
                  int i3 = view.getLeft();
                  i1 = m;
                  i2 = j;
                  if (i3 < j) {
                    i2 = i3;
                    i1 = m;
                  } 
                }  
            } 
          } 
        } 
        b++;
        m = i1;
        j = i2;
      } 
      paramCanvas.clipRect(m, 0, j, getHeight());
    } 
    boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(k);
    if (this.mScrimOpacity > 0.0F && bool1) {
      int n = (int)(((this.mScrimColor & 0xFF000000) >>> 24) * this.mScrimOpacity);
      int i1 = this.mScrimColor;
      this.mScrimPaint.setColor(n << 24 | i1 & 0xFFFFFF);
      paramCanvas.drawRect(m, 0.0F, j, getHeight(), this.mScrimPaint);
    } else if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(paramView, 3)) {
      m = this.mShadowLeftResolved.getIntrinsicWidth();
      int n = paramView.getRight();
      j = this.mLeftDragger.getEdgeSize();
      float f = Math.max(0.0F, Math.min(n / j, 1.0F));
      this.mShadowLeftResolved.setBounds(n, paramView.getTop(), m + n, paramView.getBottom());
      this.mShadowLeftResolved.setAlpha((int)(f * 255.0F));
      this.mShadowLeftResolved.draw(paramCanvas);
    } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(paramView, 5)) {
      int i1 = this.mShadowRightResolved.getIntrinsicWidth();
      int n = paramView.getLeft();
      j = getWidth();
      m = this.mRightDragger.getEdgeSize();
      float f = Math.max(0.0F, Math.min((j - n) / m, 1.0F));
      this.mShadowRightResolved.setBounds(n - i1, paramView.getTop(), n, paramView.getBottom());
      this.mShadowRightResolved.setAlpha((int)(f * 255.0F));
      this.mShadowRightResolved.draw(paramCanvas);
    } 
    return bool2;
  }
  
  View findDrawerWithGravity(int paramInt) {
    int i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    int j = getChildCount();
    for (paramInt = 0; paramInt < j; paramInt++) {
      View view = getChildAt(paramInt);
      if ((getDrawerViewAbsoluteGravity(view) & 0x7) == (i & 0x7))
        return view; 
    } 
    return null;
  }
  
  View findOpenDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((((LayoutParams)view.getLayoutParams()).openState & 0x1) == 1)
        return view; 
    } 
    return null;
  }
  
  View findVisibleDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (isDrawerView(view) && isDrawerVisible(view))
        return view; 
    } 
    return null;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams(-1, -1);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutParams layoutParams;
    if (paramLayoutParams instanceof LayoutParams) {
      layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
    } else if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      layoutParams = new LayoutParams(layoutParams);
    } else {
      layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    return (ViewGroup.LayoutParams)layoutParams;
  }
  
  public float getDrawerElevation() {
    return SET_DRAWER_SHADOW_FROM_ELEVATION ? this.mDrawerElevation : 0.0F;
  }
  
  public int getDrawerLockMode(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (paramInt != 3) {
      if (paramInt != 5) {
        if (paramInt != 8388611) {
          if (paramInt == 8388613) {
            if (this.mLockModeEnd != 3)
              return this.mLockModeEnd; 
            if (i == 0) {
              paramInt = this.mLockModeRight;
            } else {
              paramInt = this.mLockModeLeft;
            } 
            if (paramInt != 3)
              return paramInt; 
          } 
        } else {
          if (this.mLockModeStart != 3)
            return this.mLockModeStart; 
          if (i == 0) {
            paramInt = this.mLockModeLeft;
          } else {
            paramInt = this.mLockModeRight;
          } 
          if (paramInt != 3)
            return paramInt; 
        } 
      } else {
        if (this.mLockModeRight != 3)
          return this.mLockModeRight; 
        if (i == 0) {
          paramInt = this.mLockModeEnd;
        } else {
          paramInt = this.mLockModeStart;
        } 
        if (paramInt != 3)
          return paramInt; 
      } 
    } else {
      if (this.mLockModeLeft != 3)
        return this.mLockModeLeft; 
      if (i == 0) {
        paramInt = this.mLockModeStart;
      } else {
        paramInt = this.mLockModeEnd;
      } 
      if (paramInt != 3)
        return paramInt; 
    } 
    return 0;
  }
  
  public int getDrawerLockMode(@NonNull View paramView) {
    if (isDrawerView(paramView))
      return getDrawerLockMode(((LayoutParams)paramView.getLayoutParams()).gravity); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @Nullable
  public CharSequence getDrawerTitle(int paramInt) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    return (paramInt == 3) ? this.mTitleLeft : ((paramInt == 5) ? this.mTitleRight : null);
  }
  
  int getDrawerViewAbsoluteGravity(View paramView) {
    return GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
  }
  
  float getDrawerViewOffset(View paramView) {
    return ((LayoutParams)paramView.getLayoutParams()).onScreen;
  }
  
  @Nullable
  public Drawable getStatusBarBackgroundDrawable() {
    return this.mStatusBarBackground;
  }
  
  boolean isContentView(View paramView) {
    boolean bool;
    if (((LayoutParams)paramView.getLayoutParams()).gravity == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDrawerOpen(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerOpen(view) : false;
  }
  
  public boolean isDrawerOpen(@NonNull View paramView) {
    if (isDrawerView(paramView)) {
      int i = ((LayoutParams)paramView.getLayoutParams()).openState;
      boolean bool = true;
      if ((i & 0x1) != 1)
        bool = false; 
      return bool;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  boolean isDrawerView(View paramView) {
    int i = GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(paramView));
    return ((i & 0x3) != 0) ? true : (((i & 0x5) != 0));
  }
  
  public boolean isDrawerVisible(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerVisible(view) : false;
  }
  
  public boolean isDrawerVisible(@NonNull View paramView) {
    if (isDrawerView(paramView)) {
      boolean bool;
      if (((LayoutParams)paramView.getLayoutParams()).onScreen > 0.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  void moveDrawerToOffset(View paramView, float paramFloat) {
    float f1 = getDrawerViewOffset(paramView);
    float f2 = paramView.getWidth();
    int i = (int)(f1 * f2);
    i = (int)(f2 * paramFloat) - i;
    if (!checkDrawerViewAbsoluteGravity(paramView, 3))
      i = -i; 
    paramView.offsetLeftAndRight(i);
    setDrawerViewOffset(paramView, paramFloat);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      boolean bool;
      if (Build.VERSION.SDK_INT >= 21 && this.mLastInsets != null) {
        bool = ((WindowInsets)this.mLastInsets).getSystemWindowInsetTop();
      } else {
        bool = false;
      } 
      if (bool) {
        this.mStatusBarBackground.setBounds(0, 0, getWidth(), bool);
        this.mStatusBarBackground.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getActionMasked : ()I
    //   4: istore_2
    //   5: aload_0
    //   6: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   9: aload_1
    //   10: invokevirtual shouldInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   13: istore_3
    //   14: aload_0
    //   15: getfield mRightDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   18: aload_1
    //   19: invokevirtual shouldInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   22: istore #4
    //   24: iconst_1
    //   25: istore #5
    //   27: iload_2
    //   28: tableswitch default -> 60, 0 -> 109, 1 -> 91, 2 -> 63, 3 -> 91
    //   60: goto -> 188
    //   63: aload_0
    //   64: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   67: iconst_3
    //   68: invokevirtual checkTouchSlop : (I)Z
    //   71: ifeq -> 188
    //   74: aload_0
    //   75: getfield mLeftCallback : Landroid/support/v4/widget/DrawerLayout$ViewDragCallback;
    //   78: invokevirtual removeCallbacks : ()V
    //   81: aload_0
    //   82: getfield mRightCallback : Landroid/support/v4/widget/DrawerLayout$ViewDragCallback;
    //   85: invokevirtual removeCallbacks : ()V
    //   88: goto -> 188
    //   91: aload_0
    //   92: iconst_1
    //   93: invokevirtual closeDrawers : (Z)V
    //   96: aload_0
    //   97: iconst_0
    //   98: putfield mDisallowInterceptRequested : Z
    //   101: aload_0
    //   102: iconst_0
    //   103: putfield mChildrenCanceledTouch : Z
    //   106: goto -> 188
    //   109: aload_1
    //   110: invokevirtual getX : ()F
    //   113: fstore #6
    //   115: aload_1
    //   116: invokevirtual getY : ()F
    //   119: fstore #7
    //   121: aload_0
    //   122: fload #6
    //   124: putfield mInitialMotionX : F
    //   127: aload_0
    //   128: fload #7
    //   130: putfield mInitialMotionY : F
    //   133: aload_0
    //   134: getfield mScrimOpacity : F
    //   137: fconst_0
    //   138: fcmpl
    //   139: ifle -> 173
    //   142: aload_0
    //   143: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   146: fload #6
    //   148: f2i
    //   149: fload #7
    //   151: f2i
    //   152: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   155: astore_1
    //   156: aload_1
    //   157: ifnull -> 173
    //   160: aload_0
    //   161: aload_1
    //   162: invokevirtual isContentView : (Landroid/view/View;)Z
    //   165: ifeq -> 173
    //   168: iconst_1
    //   169: istore_2
    //   170: goto -> 175
    //   173: iconst_0
    //   174: istore_2
    //   175: aload_0
    //   176: iconst_0
    //   177: putfield mDisallowInterceptRequested : Z
    //   180: aload_0
    //   181: iconst_0
    //   182: putfield mChildrenCanceledTouch : Z
    //   185: goto -> 190
    //   188: iconst_0
    //   189: istore_2
    //   190: iload #5
    //   192: istore #8
    //   194: iload_3
    //   195: iload #4
    //   197: ior
    //   198: ifne -> 237
    //   201: iload #5
    //   203: istore #8
    //   205: iload_2
    //   206: ifne -> 237
    //   209: iload #5
    //   211: istore #8
    //   213: aload_0
    //   214: invokespecial hasPeekingDrawer : ()Z
    //   217: ifne -> 237
    //   220: aload_0
    //   221: getfield mChildrenCanceledTouch : Z
    //   224: ifeq -> 234
    //   227: iload #5
    //   229: istore #8
    //   231: goto -> 237
    //   234: iconst_0
    //   235: istore #8
    //   237: iload #8
    //   239: ireturn
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && hasVisibleDrawer()) {
      paramKeyEvent.startTracking();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    View view;
    if (paramInt == 4) {
      boolean bool;
      view = findVisibleDrawer();
      if (view != null && getDrawerLockMode(view) == 0)
        closeDrawers(); 
      if (view != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    return super.onKeyUp(paramInt, (KeyEvent)view);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mInLayout = true;
    int i = paramInt3 - paramInt1;
    int j = getChildCount();
    for (paramInt3 = 0; paramInt3 < j; paramInt3++) {
      View view = getChildAt(paramInt3);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (isContentView(view)) {
          view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
        } else {
          float f;
          int n;
          boolean bool;
          int k = view.getMeasuredWidth();
          int m = view.getMeasuredHeight();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            paramInt1 = -k;
            f = k;
            n = paramInt1 + (int)(layoutParams.onScreen * f);
            f = (k + n) / f;
          } else {
            f = k;
            n = i - (int)(layoutParams.onScreen * f);
            f = (i - n) / f;
          } 
          if (f != layoutParams.onScreen) {
            bool = true;
          } else {
            bool = false;
          } 
          paramInt1 = layoutParams.gravity & 0x70;
          if (paramInt1 != 16) {
            if (paramInt1 != 80) {
              view.layout(n, layoutParams.topMargin, k + n, layoutParams.topMargin + m);
            } else {
              paramInt1 = paramInt4 - paramInt2;
              view.layout(n, paramInt1 - layoutParams.bottomMargin - view.getMeasuredHeight(), k + n, paramInt1 - layoutParams.bottomMargin);
            } 
          } else {
            int i1 = paramInt4 - paramInt2;
            int i2 = (i1 - m) / 2;
            if (i2 < layoutParams.topMargin) {
              paramInt1 = layoutParams.topMargin;
            } else {
              paramInt1 = i2;
              if (i2 + m > i1 - layoutParams.bottomMargin)
                paramInt1 = i1 - layoutParams.bottomMargin - m; 
            } 
            view.layout(n, paramInt1, k + n, m + paramInt1);
          } 
          if (bool)
            setDrawerViewOffset(view, f); 
          if (layoutParams.onScreen > 0.0F) {
            paramInt1 = 0;
          } else {
            paramInt1 = 4;
          } 
          if (view.getVisibility() != paramInt1)
            view.setVisibility(paramInt1); 
        } 
      } 
    } 
    this.mInLayout = false;
    this.mFirstLayout = false;
  }
  
  @SuppressLint({"WrongConstant"})
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_2
    //   6: invokestatic getMode : (I)I
    //   9: istore #4
    //   11: iload_1
    //   12: invokestatic getSize : (I)I
    //   15: istore #5
    //   17: iload_2
    //   18: invokestatic getSize : (I)I
    //   21: istore #6
    //   23: iload_3
    //   24: ldc_w 1073741824
    //   27: if_icmpne -> 46
    //   30: iload #5
    //   32: istore #7
    //   34: iload #6
    //   36: istore #8
    //   38: iload #4
    //   40: ldc_w 1073741824
    //   43: if_icmpeq -> 113
    //   46: aload_0
    //   47: invokevirtual isInEditMode : ()Z
    //   50: ifeq -> 804
    //   53: iload_3
    //   54: ldc_w -2147483648
    //   57: if_icmpne -> 63
    //   60: goto -> 72
    //   63: iload_3
    //   64: ifne -> 72
    //   67: sipush #300
    //   70: istore #5
    //   72: iload #4
    //   74: ldc_w -2147483648
    //   77: if_icmpne -> 91
    //   80: iload #5
    //   82: istore #7
    //   84: iload #6
    //   86: istore #8
    //   88: goto -> 113
    //   91: iload #5
    //   93: istore #7
    //   95: iload #6
    //   97: istore #8
    //   99: iload #4
    //   101: ifne -> 113
    //   104: sipush #300
    //   107: istore #8
    //   109: iload #5
    //   111: istore #7
    //   113: aload_0
    //   114: iload #7
    //   116: iload #8
    //   118: invokevirtual setMeasuredDimension : (II)V
    //   121: aload_0
    //   122: getfield mLastInsets : Ljava/lang/Object;
    //   125: ifnull -> 141
    //   128: aload_0
    //   129: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   132: ifeq -> 141
    //   135: iconst_1
    //   136: istore #4
    //   138: goto -> 144
    //   141: iconst_0
    //   142: istore #4
    //   144: aload_0
    //   145: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   148: istore #9
    //   150: aload_0
    //   151: invokevirtual getChildCount : ()I
    //   154: istore #10
    //   156: iconst_0
    //   157: istore_3
    //   158: iconst_0
    //   159: istore #6
    //   161: iconst_0
    //   162: istore #5
    //   164: iload_3
    //   165: iload #10
    //   167: if_icmpge -> 803
    //   170: aload_0
    //   171: iload_3
    //   172: invokevirtual getChildAt : (I)Landroid/view/View;
    //   175: astore #11
    //   177: aload #11
    //   179: invokevirtual getVisibility : ()I
    //   182: bipush #8
    //   184: if_icmpne -> 190
    //   187: goto -> 494
    //   190: aload #11
    //   192: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   195: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   198: astore #12
    //   200: iload #4
    //   202: ifeq -> 440
    //   205: aload #12
    //   207: getfield gravity : I
    //   210: iload #9
    //   212: invokestatic getAbsoluteGravity : (II)I
    //   215: istore #13
    //   217: aload #11
    //   219: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   222: ifeq -> 318
    //   225: getstatic android/os/Build$VERSION.SDK_INT : I
    //   228: bipush #21
    //   230: if_icmplt -> 440
    //   233: aload_0
    //   234: getfield mLastInsets : Ljava/lang/Object;
    //   237: checkcast android/view/WindowInsets
    //   240: astore #14
    //   242: iload #13
    //   244: iconst_3
    //   245: if_icmpne -> 274
    //   248: aload #14
    //   250: aload #14
    //   252: invokevirtual getSystemWindowInsetLeft : ()I
    //   255: aload #14
    //   257: invokevirtual getSystemWindowInsetTop : ()I
    //   260: iconst_0
    //   261: aload #14
    //   263: invokevirtual getSystemWindowInsetBottom : ()I
    //   266: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   269: astore #15
    //   271: goto -> 307
    //   274: aload #14
    //   276: astore #15
    //   278: iload #13
    //   280: iconst_5
    //   281: if_icmpne -> 307
    //   284: aload #14
    //   286: iconst_0
    //   287: aload #14
    //   289: invokevirtual getSystemWindowInsetTop : ()I
    //   292: aload #14
    //   294: invokevirtual getSystemWindowInsetRight : ()I
    //   297: aload #14
    //   299: invokevirtual getSystemWindowInsetBottom : ()I
    //   302: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   305: astore #15
    //   307: aload #11
    //   309: aload #15
    //   311: invokevirtual dispatchApplyWindowInsets : (Landroid/view/WindowInsets;)Landroid/view/WindowInsets;
    //   314: pop
    //   315: goto -> 440
    //   318: getstatic android/os/Build$VERSION.SDK_INT : I
    //   321: bipush #21
    //   323: if_icmplt -> 440
    //   326: aload_0
    //   327: getfield mLastInsets : Ljava/lang/Object;
    //   330: checkcast android/view/WindowInsets
    //   333: astore #14
    //   335: iload #13
    //   337: iconst_3
    //   338: if_icmpne -> 367
    //   341: aload #14
    //   343: aload #14
    //   345: invokevirtual getSystemWindowInsetLeft : ()I
    //   348: aload #14
    //   350: invokevirtual getSystemWindowInsetTop : ()I
    //   353: iconst_0
    //   354: aload #14
    //   356: invokevirtual getSystemWindowInsetBottom : ()I
    //   359: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   362: astore #15
    //   364: goto -> 400
    //   367: aload #14
    //   369: astore #15
    //   371: iload #13
    //   373: iconst_5
    //   374: if_icmpne -> 400
    //   377: aload #14
    //   379: iconst_0
    //   380: aload #14
    //   382: invokevirtual getSystemWindowInsetTop : ()I
    //   385: aload #14
    //   387: invokevirtual getSystemWindowInsetRight : ()I
    //   390: aload #14
    //   392: invokevirtual getSystemWindowInsetBottom : ()I
    //   395: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   398: astore #15
    //   400: aload #12
    //   402: aload #15
    //   404: invokevirtual getSystemWindowInsetLeft : ()I
    //   407: putfield leftMargin : I
    //   410: aload #12
    //   412: aload #15
    //   414: invokevirtual getSystemWindowInsetTop : ()I
    //   417: putfield topMargin : I
    //   420: aload #12
    //   422: aload #15
    //   424: invokevirtual getSystemWindowInsetRight : ()I
    //   427: putfield rightMargin : I
    //   430: aload #12
    //   432: aload #15
    //   434: invokevirtual getSystemWindowInsetBottom : ()I
    //   437: putfield bottomMargin : I
    //   440: aload_0
    //   441: aload #11
    //   443: invokevirtual isContentView : (Landroid/view/View;)Z
    //   446: ifeq -> 497
    //   449: aload #11
    //   451: iload #7
    //   453: aload #12
    //   455: getfield leftMargin : I
    //   458: isub
    //   459: aload #12
    //   461: getfield rightMargin : I
    //   464: isub
    //   465: ldc_w 1073741824
    //   468: invokestatic makeMeasureSpec : (II)I
    //   471: iload #8
    //   473: aload #12
    //   475: getfield topMargin : I
    //   478: isub
    //   479: aload #12
    //   481: getfield bottomMargin : I
    //   484: isub
    //   485: ldc_w 1073741824
    //   488: invokestatic makeMeasureSpec : (II)I
    //   491: invokevirtual measure : (II)V
    //   494: goto -> 724
    //   497: aload_0
    //   498: aload #11
    //   500: invokevirtual isDrawerView : (Landroid/view/View;)Z
    //   503: ifeq -> 730
    //   506: getstatic android/support/v4/widget/DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION : Z
    //   509: ifeq -> 534
    //   512: aload #11
    //   514: invokestatic getElevation : (Landroid/view/View;)F
    //   517: aload_0
    //   518: getfield mDrawerElevation : F
    //   521: fcmpl
    //   522: ifeq -> 534
    //   525: aload #11
    //   527: aload_0
    //   528: getfield mDrawerElevation : F
    //   531: invokestatic setElevation : (Landroid/view/View;F)V
    //   534: aload_0
    //   535: aload #11
    //   537: invokevirtual getDrawerViewAbsoluteGravity : (Landroid/view/View;)I
    //   540: bipush #7
    //   542: iand
    //   543: istore #16
    //   545: iload #16
    //   547: iconst_3
    //   548: if_icmpne -> 557
    //   551: iconst_1
    //   552: istore #13
    //   554: goto -> 560
    //   557: iconst_0
    //   558: istore #13
    //   560: iload #13
    //   562: ifeq -> 570
    //   565: iload #6
    //   567: ifne -> 583
    //   570: iload #13
    //   572: ifne -> 660
    //   575: iload #5
    //   577: ifne -> 583
    //   580: goto -> 660
    //   583: new java/lang/StringBuilder
    //   586: dup
    //   587: invokespecial <init> : ()V
    //   590: astore #15
    //   592: aload #15
    //   594: ldc_w 'Child drawer has absolute gravity '
    //   597: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   600: pop
    //   601: aload #15
    //   603: iload #16
    //   605: invokestatic gravityToString : (I)Ljava/lang/String;
    //   608: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   611: pop
    //   612: aload #15
    //   614: ldc_w ' but this '
    //   617: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   620: pop
    //   621: aload #15
    //   623: ldc 'DrawerLayout'
    //   625: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: pop
    //   629: aload #15
    //   631: ldc_w ' already has a '
    //   634: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   637: pop
    //   638: aload #15
    //   640: ldc_w 'drawer view along that edge'
    //   643: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   646: pop
    //   647: new java/lang/IllegalStateException
    //   650: dup
    //   651: aload #15
    //   653: invokevirtual toString : ()Ljava/lang/String;
    //   656: invokespecial <init> : (Ljava/lang/String;)V
    //   659: athrow
    //   660: iload #13
    //   662: ifeq -> 671
    //   665: iconst_1
    //   666: istore #6
    //   668: goto -> 674
    //   671: iconst_1
    //   672: istore #5
    //   674: aload #11
    //   676: iload_1
    //   677: aload_0
    //   678: getfield mMinDrawerMargin : I
    //   681: aload #12
    //   683: getfield leftMargin : I
    //   686: iadd
    //   687: aload #12
    //   689: getfield rightMargin : I
    //   692: iadd
    //   693: aload #12
    //   695: getfield width : I
    //   698: invokestatic getChildMeasureSpec : (III)I
    //   701: iload_2
    //   702: aload #12
    //   704: getfield topMargin : I
    //   707: aload #12
    //   709: getfield bottomMargin : I
    //   712: iadd
    //   713: aload #12
    //   715: getfield height : I
    //   718: invokestatic getChildMeasureSpec : (III)I
    //   721: invokevirtual measure : (II)V
    //   724: iinc #3, 1
    //   727: goto -> 164
    //   730: new java/lang/StringBuilder
    //   733: dup
    //   734: invokespecial <init> : ()V
    //   737: astore #15
    //   739: aload #15
    //   741: ldc_w 'Child '
    //   744: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   747: pop
    //   748: aload #15
    //   750: aload #11
    //   752: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   755: pop
    //   756: aload #15
    //   758: ldc_w ' at index '
    //   761: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   764: pop
    //   765: aload #15
    //   767: iload_3
    //   768: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   771: pop
    //   772: aload #15
    //   774: ldc_w ' does not have a valid layout_gravity - must be Gravity.LEFT, '
    //   777: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: pop
    //   781: aload #15
    //   783: ldc_w 'Gravity.RIGHT or Gravity.NO_GRAVITY'
    //   786: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   789: pop
    //   790: new java/lang/IllegalStateException
    //   793: dup
    //   794: aload #15
    //   796: invokevirtual toString : ()Ljava/lang/String;
    //   799: invokespecial <init> : (Ljava/lang/String;)V
    //   802: athrow
    //   803: return
    //   804: new java/lang/IllegalArgumentException
    //   807: dup
    //   808: ldc_w 'DrawerLayout must be measured with MeasureSpec.EXACTLY.'
    //   811: invokespecial <init> : (Ljava/lang/String;)V
    //   814: athrow
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.openDrawerGravity != 0) {
      View view = findDrawerWithGravity(savedState.openDrawerGravity);
      if (view != null)
        openDrawer(view); 
    } 
    if (savedState.lockModeLeft != 3)
      setDrawerLockMode(savedState.lockModeLeft, 3); 
    if (savedState.lockModeRight != 3)
      setDrawerLockMode(savedState.lockModeRight, 5); 
    if (savedState.lockModeStart != 3)
      setDrawerLockMode(savedState.lockModeStart, 8388611); 
    if (savedState.lockModeEnd != 3)
      setDrawerLockMode(savedState.lockModeEnd, 8388613); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    resolveShadowDrawables();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.openState;
      boolean bool = true;
      if (j == 1) {
        j = 1;
      } else {
        j = 0;
      } 
      if (layoutParams.openState != 2)
        bool = false; 
      if (j != 0 || bool) {
        savedState.openDrawerGravity = layoutParams.gravity;
        break;
      } 
    } 
    savedState.lockModeLeft = this.mLockModeLeft;
    savedState.lockModeRight = this.mLockModeRight;
    savedState.lockModeStart = this.mLockModeStart;
    savedState.lockModeEnd = this.mLockModeEnd;
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   4: aload_1
    //   5: invokevirtual processTouchEvent : (Landroid/view/MotionEvent;)V
    //   8: aload_0
    //   9: getfield mRightDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   12: aload_1
    //   13: invokevirtual processTouchEvent : (Landroid/view/MotionEvent;)V
    //   16: aload_1
    //   17: invokevirtual getAction : ()I
    //   20: sipush #255
    //   23: iand
    //   24: istore_2
    //   25: iload_2
    //   26: iconst_3
    //   27: if_icmpeq -> 211
    //   30: iload_2
    //   31: tableswitch default -> 52, 0 -> 176, 1 -> 55
    //   52: goto -> 226
    //   55: aload_1
    //   56: invokevirtual getX : ()F
    //   59: fstore_3
    //   60: aload_1
    //   61: invokevirtual getY : ()F
    //   64: fstore #4
    //   66: aload_0
    //   67: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   70: fload_3
    //   71: f2i
    //   72: fload #4
    //   74: f2i
    //   75: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   78: astore_1
    //   79: aload_1
    //   80: ifnull -> 159
    //   83: aload_0
    //   84: aload_1
    //   85: invokevirtual isContentView : (Landroid/view/View;)Z
    //   88: ifeq -> 159
    //   91: fload_3
    //   92: aload_0
    //   93: getfield mInitialMotionX : F
    //   96: fsub
    //   97: fstore_3
    //   98: fload #4
    //   100: aload_0
    //   101: getfield mInitialMotionY : F
    //   104: fsub
    //   105: fstore #4
    //   107: aload_0
    //   108: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   111: invokevirtual getTouchSlop : ()I
    //   114: istore_2
    //   115: fload_3
    //   116: fload_3
    //   117: fmul
    //   118: fload #4
    //   120: fload #4
    //   122: fmul
    //   123: fadd
    //   124: iload_2
    //   125: iload_2
    //   126: imul
    //   127: i2f
    //   128: fcmpg
    //   129: ifge -> 159
    //   132: aload_0
    //   133: invokevirtual findOpenDrawer : ()Landroid/view/View;
    //   136: astore_1
    //   137: aload_1
    //   138: ifnull -> 159
    //   141: aload_0
    //   142: aload_1
    //   143: invokevirtual getDrawerLockMode : (Landroid/view/View;)I
    //   146: iconst_2
    //   147: if_icmpne -> 153
    //   150: goto -> 159
    //   153: iconst_0
    //   154: istore #5
    //   156: goto -> 162
    //   159: iconst_1
    //   160: istore #5
    //   162: aload_0
    //   163: iload #5
    //   165: invokevirtual closeDrawers : (Z)V
    //   168: aload_0
    //   169: iconst_0
    //   170: putfield mDisallowInterceptRequested : Z
    //   173: goto -> 226
    //   176: aload_1
    //   177: invokevirtual getX : ()F
    //   180: fstore_3
    //   181: aload_1
    //   182: invokevirtual getY : ()F
    //   185: fstore #4
    //   187: aload_0
    //   188: fload_3
    //   189: putfield mInitialMotionX : F
    //   192: aload_0
    //   193: fload #4
    //   195: putfield mInitialMotionY : F
    //   198: aload_0
    //   199: iconst_0
    //   200: putfield mDisallowInterceptRequested : Z
    //   203: aload_0
    //   204: iconst_0
    //   205: putfield mChildrenCanceledTouch : Z
    //   208: goto -> 226
    //   211: aload_0
    //   212: iconst_1
    //   213: invokevirtual closeDrawers : (Z)V
    //   216: aload_0
    //   217: iconst_0
    //   218: putfield mDisallowInterceptRequested : Z
    //   221: aload_0
    //   222: iconst_0
    //   223: putfield mChildrenCanceledTouch : Z
    //   226: iconst_1
    //   227: ireturn
  }
  
  public void openDrawer(int paramInt) {
    openDrawer(paramInt, true);
  }
  
  public void openDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      openDrawer(view, paramBoolean);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No drawer view found with gravity ");
    stringBuilder.append(gravityToString(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void openDrawer(@NonNull View paramView) {
    openDrawer(paramView, true);
  }
  
  public void openDrawer(@NonNull View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 1.0F;
        layoutParams.openState = 1;
        updateChildrenImportantForAccessibility(paramView, true);
      } else if (paramBoolean) {
        layoutParams.openState |= 0x2;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, 0, paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth() - paramView.getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 1.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(0);
      } 
      invalidate();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a sliding drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void removeDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    if (this.mListeners == null)
      return; 
    this.mListeners.remove(paramDrawerListener);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    this.mDisallowInterceptRequested = paramBoolean;
    if (paramBoolean)
      closeDrawers(true); 
  }
  
  public void requestLayout() {
    if (!this.mInLayout)
      super.requestLayout(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setChildInsets(Object paramObject, boolean paramBoolean) {
    this.mLastInsets = paramObject;
    this.mDrawStatusBarBackground = paramBoolean;
    if (!paramBoolean && getBackground() == null) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    setWillNotDraw(paramBoolean);
    requestLayout();
  }
  
  public void setDrawerElevation(float paramFloat) {
    this.mDrawerElevation = paramFloat;
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (isDrawerView(view))
        ViewCompat.setElevation(view, this.mDrawerElevation); 
    } 
  }
  
  @Deprecated
  public void setDrawerListener(DrawerListener paramDrawerListener) {
    if (this.mListener != null)
      removeDrawerListener(this.mListener); 
    if (paramDrawerListener != null)
      addDrawerListener(paramDrawerListener); 
    this.mListener = paramDrawerListener;
  }
  
  public void setDrawerLockMode(int paramInt) {
    setDrawerLockMode(paramInt, 3);
    setDrawerLockMode(paramInt, 5);
  }
  
  public void setDrawerLockMode(int paramInt1, int paramInt2) {
    int i = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this));
    if (paramInt2 != 3) {
      if (paramInt2 != 5) {
        if (paramInt2 != 8388611) {
          if (paramInt2 == 8388613)
            this.mLockModeEnd = paramInt1; 
        } else {
          this.mLockModeStart = paramInt1;
        } 
      } else {
        this.mLockModeRight = paramInt1;
      } 
    } else {
      this.mLockModeLeft = paramInt1;
    } 
    if (paramInt1 != 0) {
      ViewDragHelper viewDragHelper;
      if (i == 3) {
        viewDragHelper = this.mLeftDragger;
      } else {
        viewDragHelper = this.mRightDragger;
      } 
      viewDragHelper.cancel();
    } 
    switch (paramInt1) {
      default:
        return;
      case 2:
        view = findDrawerWithGravity(i);
        if (view != null)
          openDrawer(view); 
      case 1:
        break;
    } 
    View view = findDrawerWithGravity(i);
    if (view != null)
      closeDrawer(view); 
  }
  
  public void setDrawerLockMode(int paramInt, @NonNull View paramView) {
    if (isDrawerView(paramView)) {
      setDrawerLockMode(paramInt, ((LayoutParams)paramView.getLayoutParams()).gravity);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a ");
    stringBuilder.append("drawer with appropriate layout_gravity");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDrawerShadow(@DrawableRes int paramInt1, int paramInt2) {
    setDrawerShadow(ContextCompat.getDrawable(getContext(), paramInt1), paramInt2);
  }
  
  public void setDrawerShadow(Drawable paramDrawable, int paramInt) {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    if ((paramInt & 0x800003) == 8388611) {
      this.mShadowStart = paramDrawable;
    } else if ((paramInt & 0x800005) == 8388613) {
      this.mShadowEnd = paramDrawable;
    } else if ((paramInt & 0x3) == 3) {
      this.mShadowLeft = paramDrawable;
    } else if ((paramInt & 0x5) == 5) {
      this.mShadowRight = paramDrawable;
    } else {
      return;
    } 
    resolveShadowDrawables();
    invalidate();
  }
  
  public void setDrawerTitle(int paramInt, @Nullable CharSequence paramCharSequence) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    if (paramInt == 3) {
      this.mTitleLeft = paramCharSequence;
    } else if (paramInt == 5) {
      this.mTitleRight = paramCharSequence;
    } 
  }
  
  void setDrawerViewOffset(View paramView, float paramFloat) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat == layoutParams.onScreen)
      return; 
    layoutParams.onScreen = paramFloat;
    dispatchOnDrawerSlide(paramView, paramFloat);
  }
  
  public void setScrimColor(@ColorInt int paramInt) {
    this.mScrimColor = paramInt;
    invalidate();
  }
  
  public void setStatusBarBackground(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    this.mStatusBarBackground = drawable;
    invalidate();
  }
  
  public void setStatusBarBackground(@Nullable Drawable paramDrawable) {
    this.mStatusBarBackground = paramDrawable;
    invalidate();
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) {
    this.mStatusBarBackground = (Drawable)new ColorDrawable(paramInt);
    invalidate();
  }
  
  void updateDrawerState(int paramInt1, int paramInt2, View paramView) {
    int i = this.mLeftDragger.getViewDragState();
    int j = this.mRightDragger.getViewDragState();
    byte b = 2;
    if (i == 1 || j == 1) {
      paramInt1 = 1;
    } else {
      paramInt1 = b;
      if (i != 2)
        if (j == 2) {
          paramInt1 = b;
        } else {
          paramInt1 = 0;
        }  
    } 
    if (paramView != null && paramInt2 == 0) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (layoutParams.onScreen == 0.0F) {
        dispatchOnDrawerClosed(paramView);
      } else if (layoutParams.onScreen == 1.0F) {
        dispatchOnDrawerOpened(paramView);
      } 
    } 
    if (paramInt1 != this.mDrawerState) {
      this.mDrawerState = paramInt1;
      if (this.mListeners != null)
        for (paramInt2 = this.mListeners.size() - 1; paramInt2 >= 0; paramInt2--)
          ((DrawerListener)this.mListeners.get(paramInt2)).onDrawerStateChanged(paramInt1);  
    } 
  }
  
  static {
    boolean bool2;
    boolean bool1 = true;
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    private void addChildrenForAccessibility(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat, ViewGroup param1ViewGroup) {
      int i = param1ViewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = param1ViewGroup.getChildAt(b);
        if (DrawerLayout.includeChildForAccessibility(view))
          param1AccessibilityNodeInfoCompat.addChild(view); 
      } 
    }
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      List<CharSequence> list;
      CharSequence charSequence;
      if (param1AccessibilityEvent.getEventType() == 32) {
        list = param1AccessibilityEvent.getText();
        View view = DrawerLayout.this.findVisibleDrawer();
        if (view != null) {
          int i = DrawerLayout.this.getDrawerViewAbsoluteGravity(view);
          charSequence = DrawerLayout.this.getDrawerTitle(i);
          if (charSequence != null)
            list.add(charSequence); 
        } 
        return true;
      } 
      return super.dispatchPopulateAccessibilityEvent((View)list, (AccessibilityEvent)charSequence);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(DrawerLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      } else {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
        super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
        param1AccessibilityNodeInfoCompat.setSource(param1View);
        ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
        if (viewParent instanceof View)
          param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
        copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.recycle();
        addChildrenForAccessibility(param1AccessibilityNodeInfoCompat, (ViewGroup)param1View);
      } 
      param1AccessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setFocusable(false);
      param1AccessibilityNodeInfoCompat.setFocused(false);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(param1View)) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      if (!DrawerLayout.includeChildForAccessibility(param1View))
        param1AccessibilityNodeInfoCompat.setParent(null); 
    }
  }
  
  public static interface DrawerListener {
    void onDrawerClosed(@NonNull View param1View);
    
    void onDrawerOpened(@NonNull View param1View);
    
    void onDrawerSlide(@NonNull View param1View, float param1Float);
    
    void onDrawerStateChanged(int param1Int);
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int FLAG_IS_CLOSING = 4;
    
    private static final int FLAG_IS_OPENED = 1;
    
    private static final int FLAG_IS_OPENING = 2;
    
    public int gravity = 0;
    
    boolean isPeeking;
    
    float onScreen;
    
    int openState;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(@NonNull Context param1Context, @Nullable AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, DrawerLayout.LAYOUT_ATTRS);
      this.gravity = typedArray.getInt(0, 0);
      typedArray.recycle();
    }
    
    public LayoutParams(@NonNull LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(@NonNull ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(@NonNull ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new DrawerLayout.SavedState(param2Parcel, null);
        }
        
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new DrawerLayout.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public DrawerLayout.SavedState[] newArray(int param2Int) {
          return new DrawerLayout.SavedState[param2Int];
        }
      };
    
    int lockModeEnd;
    
    int lockModeLeft;
    
    int lockModeRight;
    
    int lockModeStart;
    
    int openDrawerGravity = 0;
    
    public SavedState(@NonNull Parcel param1Parcel, @Nullable ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.openDrawerGravity = param1Parcel.readInt();
      this.lockModeLeft = param1Parcel.readInt();
      this.lockModeRight = param1Parcel.readInt();
      this.lockModeStart = param1Parcel.readInt();
      this.lockModeEnd = param1Parcel.readInt();
    }
    
    public SavedState(@NonNull Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.openDrawerGravity);
      param1Parcel.writeInt(this.lockModeLeft);
      param1Parcel.writeInt(this.lockModeRight);
      param1Parcel.writeInt(this.lockModeStart);
      param1Parcel.writeInt(this.lockModeEnd);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new DrawerLayout.SavedState(param1Parcel, null);
    }
    
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new DrawerLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public DrawerLayout.SavedState[] newArray(int param1Int) {
      return new DrawerLayout.SavedState[param1Int];
    }
  }
  
  public static abstract class SimpleDrawerListener implements DrawerListener {
    public void onDrawerClosed(View param1View) {}
    
    public void onDrawerOpened(View param1View) {}
    
    public void onDrawerSlide(View param1View, float param1Float) {}
    
    public void onDrawerStateChanged(int param1Int) {}
  }
  
  private class ViewDragCallback extends ViewDragHelper.Callback {
    private final int mAbsGravity;
    
    private ViewDragHelper mDragger;
    
    private final Runnable mPeekRunnable = new Runnable() {
        public void run() {
          DrawerLayout.ViewDragCallback.this.peekDrawer();
        }
      };
    
    ViewDragCallback(int param1Int) {
      this.mAbsGravity = param1Int;
    }
    
    private void closeOtherDrawer() {
      int i = this.mAbsGravity;
      byte b = 3;
      if (i == 3)
        b = 5; 
      View view = DrawerLayout.this.findDrawerWithGravity(b);
      if (view != null)
        DrawerLayout.this.closeDrawer(view); 
    }
    
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3))
        return Math.max(-param1View.getWidth(), Math.min(param1Int1, 0)); 
      param1Int2 = DrawerLayout.this.getWidth();
      return Math.max(param1Int2 - param1View.getWidth(), Math.min(param1Int1, param1Int2));
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      boolean bool;
      if (DrawerLayout.this.isDrawerView(param1View)) {
        bool = param1View.getWidth();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      View view;
      if ((param1Int1 & 0x1) == 1) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
      } 
      if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0)
        this.mDragger.captureChildView(view, param1Int2); 
    }
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {
      DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      ((DrawerLayout.LayoutParams)param1View.getLayoutParams()).isPeeking = false;
      closeOtherDrawer();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      DrawerLayout.this.updateDrawerState(this.mAbsGravity, param1Int, this.mDragger.getCapturedView());
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      float f;
      param1Int2 = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        f = (param1Int1 + param1Int2) / param1Int2;
      } else {
        f = (DrawerLayout.this.getWidth() - param1Int1) / param1Int2;
      } 
      DrawerLayout.this.setDrawerViewOffset(param1View, f);
      if (f == 0.0F) {
        param1Int1 = 4;
      } else {
        param1Int1 = 0;
      } 
      param1View.setVisibility(param1Int1);
      DrawerLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Landroid/support/v4/widget/DrawerLayout;
      //   4: aload_1
      //   5: invokevirtual getDrawerViewOffset : (Landroid/view/View;)F
      //   8: fstore_3
      //   9: aload_1
      //   10: invokevirtual getWidth : ()I
      //   13: istore #4
      //   15: aload_0
      //   16: getfield this$0 : Landroid/support/v4/widget/DrawerLayout;
      //   19: aload_1
      //   20: iconst_3
      //   21: invokevirtual checkDrawerViewAbsoluteGravity : (Landroid/view/View;I)Z
      //   24: ifeq -> 63
      //   27: fload_2
      //   28: fconst_0
      //   29: fcmpl
      //   30: ifgt -> 57
      //   33: fload_2
      //   34: fconst_0
      //   35: fcmpl
      //   36: ifne -> 49
      //   39: fload_3
      //   40: ldc 0.5
      //   42: fcmpl
      //   43: ifle -> 49
      //   46: goto -> 57
      //   49: iload #4
      //   51: ineg
      //   52: istore #5
      //   54: goto -> 106
      //   57: iconst_0
      //   58: istore #5
      //   60: goto -> 106
      //   63: aload_0
      //   64: getfield this$0 : Landroid/support/v4/widget/DrawerLayout;
      //   67: invokevirtual getWidth : ()I
      //   70: istore #6
      //   72: fload_2
      //   73: fconst_0
      //   74: fcmpg
      //   75: iflt -> 99
      //   78: iload #6
      //   80: istore #5
      //   82: fload_2
      //   83: fconst_0
      //   84: fcmpl
      //   85: ifne -> 106
      //   88: iload #6
      //   90: istore #5
      //   92: fload_3
      //   93: ldc 0.5
      //   95: fcmpl
      //   96: ifle -> 106
      //   99: iload #6
      //   101: iload #4
      //   103: isub
      //   104: istore #5
      //   106: aload_0
      //   107: getfield mDragger : Landroid/support/v4/widget/ViewDragHelper;
      //   110: iload #5
      //   112: aload_1
      //   113: invokevirtual getTop : ()I
      //   116: invokevirtual settleCapturedViewAt : (II)Z
      //   119: pop
      //   120: aload_0
      //   121: getfield this$0 : Landroid/support/v4/widget/DrawerLayout;
      //   124: invokevirtual invalidate : ()V
      //   127: return
    }
    
    void peekDrawer() {
      View view;
      int i = this.mDragger.getEdgeSize();
      int j = this.mAbsGravity;
      int k = 0;
      if (j == 3) {
        j = 1;
      } else {
        j = 0;
      } 
      if (j != 0) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
        if (view != null)
          k = -view.getWidth(); 
        k += i;
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
        k = DrawerLayout.this.getWidth() - i;
      } 
      if (view != null && ((j != 0 && view.getLeft() < k) || (j == 0 && view.getLeft() > k)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams)view.getLayoutParams();
        this.mDragger.smoothSlideViewTo(view, k, view.getTop());
        layoutParams.isPeeking = true;
        DrawerLayout.this.invalidate();
        closeOtherDrawer();
        DrawerLayout.this.cancelChildViewTouch();
      } 
    }
    
    public void removeCallbacks() {
      DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
    }
    
    public void setDragger(ViewDragHelper param1ViewDragHelper) {
      this.mDragger = param1ViewDragHelper;
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      boolean bool;
      if (DrawerLayout.this.isDrawerView(param1View) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(param1View) == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.peekDrawer();
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v4\widget\DrawerLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */