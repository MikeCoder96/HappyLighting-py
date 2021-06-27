package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.coordinatorlayout.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v4.widget.ViewGroupUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2 {
  static final Class<?>[] CONSTRUCTOR_PARAMS;
  
  static final int EVENT_NESTED_SCROLL = 1;
  
  static final int EVENT_PRE_DRAW = 0;
  
  static final int EVENT_VIEW_REMOVED = 2;
  
  static final String TAG = "CoordinatorLayout";
  
  static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
  
  private static final int TYPE_ON_INTERCEPT = 0;
  
  private static final int TYPE_ON_TOUCH = 1;
  
  static final String WIDGET_PACKAGE_NAME;
  
  static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
  
  private static final Pools.Pool<Rect> sRectPool;
  
  private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
  
  private View mBehaviorTouchView;
  
  private final DirectedAcyclicGraph<View> mChildDag;
  
  private final List<View> mDependencySortedChildren;
  
  private boolean mDisallowInterceptReset;
  
  private boolean mDrawStatusBarBackground;
  
  private boolean mIsAttachedToWindow;
  
  private int[] mKeylines;
  
  private WindowInsetsCompat mLastInsets;
  
  private boolean mNeedsPreDrawListener;
  
  private final NestedScrollingParentHelper mNestedScrollingParentHelper;
  
  private View mNestedScrollingTarget;
  
  ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
  
  private OnPreDrawListener mOnPreDrawListener;
  
  private Paint mScrimPaint;
  
  private Drawable mStatusBarBackground;
  
  private final List<View> mTempDependenciesList;
  
  private final int[] mTempIntPair;
  
  private final List<View> mTempList1;
  
  static {
    Package package_ = CoordinatorLayout.class.getPackage();
    if (package_ != null) {
      String str = package_.getName();
    } else {
      package_ = null;
    } 
    WIDGET_PACKAGE_NAME = (String)package_;
    if (Build.VERSION.SDK_INT >= 21) {
      TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
    } else {
      TOP_SORTED_CHILDREN_COMPARATOR = null;
    } 
    CONSTRUCTOR_PARAMS = new Class[] { Context.class, AttributeSet.class };
    sConstructors = new ThreadLocal<Map<String, Constructor<Behavior>>>();
    sRectPool = (Pools.Pool<Rect>)new Pools.SynchronizedPool(12);
  }
  
  public CoordinatorLayout(@NonNull Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CoordinatorLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.coordinatorLayoutStyle);
  }
  
  public CoordinatorLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray;
    this.mDependencySortedChildren = new ArrayList<View>();
    this.mChildDag = new DirectedAcyclicGraph();
    this.mTempList1 = new ArrayList<View>();
    this.mTempDependenciesList = new ArrayList<View>();
    this.mTempIntPair = new int[2];
    this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    boolean bool = false;
    if (paramInt == 0) {
      typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CoordinatorLayout, 0, R.style.Widget_Support_CoordinatorLayout);
    } else {
      typedArray = paramContext.obtainStyledAttributes((AttributeSet)typedArray, R.styleable.CoordinatorLayout, paramInt, 0);
    } 
    paramInt = typedArray.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
    if (paramInt != 0) {
      Resources resources = paramContext.getResources();
      this.mKeylines = resources.getIntArray(paramInt);
      float f = (resources.getDisplayMetrics()).density;
      int i = this.mKeylines.length;
      for (paramInt = bool; paramInt < i; paramInt++)
        this.mKeylines[paramInt] = (int)(this.mKeylines[paramInt] * f); 
    } 
    this.mStatusBarBackground = typedArray.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
    typedArray.recycle();
    setupForInsets();
    super.setOnHierarchyChangeListener(new HierarchyChangeListener());
  }
  
  @NonNull
  private static Rect acquireTempRect() {
    Rect rect1 = (Rect)sRectPool.acquire();
    Rect rect2 = rect1;
    if (rect1 == null)
      rect2 = new Rect(); 
    return rect2;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt1 < paramInt2) ? paramInt2 : ((paramInt1 > paramInt3) ? paramInt3 : paramInt1);
  }
  
  private void constrainChildRect(LayoutParams paramLayoutParams, Rect paramRect, int paramInt1, int paramInt2) {
    int i = getWidth();
    int j = getHeight();
    i = Math.max(getPaddingLeft() + paramLayoutParams.leftMargin, Math.min(paramRect.left, i - getPaddingRight() - paramInt1 - paramLayoutParams.rightMargin));
    j = Math.max(getPaddingTop() + paramLayoutParams.topMargin, Math.min(paramRect.top, j - getPaddingBottom() - paramInt2 - paramLayoutParams.bottomMargin));
    paramRect.set(i, j, paramInt1 + i, paramInt2 + j);
  }
  
  private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat;
    if (paramWindowInsetsCompat.isConsumed())
      return paramWindowInsetsCompat; 
    byte b = 0;
    int i = getChildCount();
    while (true) {
      windowInsetsCompat = paramWindowInsetsCompat;
      if (b < i) {
        View view = getChildAt(b);
        windowInsetsCompat = paramWindowInsetsCompat;
        if (ViewCompat.getFitsSystemWindows(view)) {
          Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
          windowInsetsCompat = paramWindowInsetsCompat;
          if (behavior != null) {
            paramWindowInsetsCompat = behavior.onApplyWindowInsets(this, view, paramWindowInsetsCompat);
            windowInsetsCompat = paramWindowInsetsCompat;
            if (paramWindowInsetsCompat.isConsumed()) {
              windowInsetsCompat = paramWindowInsetsCompat;
              break;
            } 
          } 
        } 
        b++;
        paramWindowInsetsCompat = windowInsetsCompat;
        continue;
      } 
      break;
    } 
    return windowInsetsCompat;
  }
  
  private void getDesiredAnchoredChildRectWithoutConstraints(View paramView, int paramInt1, Rect paramRect1, Rect paramRect2, LayoutParams paramLayoutParams, int paramInt2, int paramInt3) {
    int i = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(paramLayoutParams.gravity), paramInt1);
    int j = GravityCompat.getAbsoluteGravity(resolveGravity(paramLayoutParams.anchorGravity), paramInt1);
    int k = i & 0x7;
    int m = i & 0x70;
    paramInt1 = j & 0x7;
    j &= 0x70;
    if (paramInt1 != 1) {
      if (paramInt1 != 5) {
        paramInt1 = paramRect1.left;
      } else {
        paramInt1 = paramRect1.right;
      } 
    } else {
      paramInt1 = paramRect1.left + paramRect1.width() / 2;
    } 
    if (j != 16) {
      if (j != 80) {
        j = paramRect1.top;
      } else {
        j = paramRect1.bottom;
      } 
    } else {
      j = paramRect1.top + paramRect1.height() / 2;
    } 
    if (k != 1) {
      i = paramInt1;
      if (k != 5)
        i = paramInt1 - paramInt2; 
    } else {
      i = paramInt1 - paramInt2 / 2;
    } 
    if (m != 16) {
      paramInt1 = j;
      if (m != 80)
        paramInt1 = j - paramInt3; 
    } else {
      paramInt1 = j - paramInt3 / 2;
    } 
    paramRect2.set(i, paramInt1, paramInt2 + i, paramInt3 + paramInt1);
  }
  
  private int getKeyline(int paramInt) {
    if (this.mKeylines == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("No keylines defined for ");
      stringBuilder.append(this);
      stringBuilder.append(" - attempted index lookup ");
      stringBuilder.append(paramInt);
      Log.e("CoordinatorLayout", stringBuilder.toString());
      return 0;
    } 
    if (paramInt < 0 || paramInt >= this.mKeylines.length) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Keyline index ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" out of range for ");
      stringBuilder.append(this);
      Log.e("CoordinatorLayout", stringBuilder.toString());
      return 0;
    } 
    return this.mKeylines[paramInt];
  }
  
  private void getTopSortedChildren(List<View> paramList) {
    paramList.clear();
    boolean bool = isChildrenDrawingOrderEnabled();
    int i = getChildCount();
    for (int j = i - 1; j >= 0; j--) {
      int k;
      if (bool) {
        k = getChildDrawingOrder(i, j);
      } else {
        k = j;
      } 
      paramList.add(getChildAt(k));
    } 
    if (TOP_SORTED_CHILDREN_COMPARATOR != null)
      Collections.sort(paramList, TOP_SORTED_CHILDREN_COMPARATOR); 
  }
  
  private boolean hasDependencies(View paramView) {
    return this.mChildDag.hasOutgoingEdges(paramView);
  }
  
  private void layoutChild(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect1 = acquireTempRect();
    rect1.set(getPaddingLeft() + layoutParams.leftMargin, getPaddingTop() + layoutParams.topMargin, getWidth() - getPaddingRight() - layoutParams.rightMargin, getHeight() - getPaddingBottom() - layoutParams.bottomMargin);
    if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) && !ViewCompat.getFitsSystemWindows(paramView)) {
      rect1.left += this.mLastInsets.getSystemWindowInsetLeft();
      rect1.top += this.mLastInsets.getSystemWindowInsetTop();
      rect1.right -= this.mLastInsets.getSystemWindowInsetRight();
      rect1.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
    } 
    Rect rect2 = acquireTempRect();
    GravityCompat.apply(resolveGravity(layoutParams.gravity), paramView.getMeasuredWidth(), paramView.getMeasuredHeight(), rect1, rect2, paramInt);
    paramView.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
    releaseTempRect(rect1);
    releaseTempRect(rect2);
  }
  
  private void layoutChildWithAnchor(View paramView1, View paramView2, int paramInt) {
    Rect rect1 = acquireTempRect();
    Rect rect2 = acquireTempRect();
    try {
      getDescendantRect(paramView2, rect1);
      getDesiredAnchoredChildRect(paramView1, paramInt, rect1, rect2);
      paramView1.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
      return;
    } finally {
      releaseTempRect(rect1);
      releaseTempRect(rect2);
    } 
  }
  
  private void layoutChildWithKeyline(View paramView, int paramInt1, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), paramInt2);
    int j = i & 0x7;
    int k = i & 0x70;
    int m = getWidth();
    int n = getHeight();
    int i1 = paramView.getMeasuredWidth();
    int i2 = paramView.getMeasuredHeight();
    i = paramInt1;
    if (paramInt2 == 1)
      i = m - paramInt1; 
    paramInt1 = getKeyline(i) - i1;
    paramInt2 = 0;
    if (j != 1) {
      if (j == 5)
        paramInt1 += i1; 
    } else {
      paramInt1 += i1 / 2;
    } 
    if (k != 16) {
      if (k == 80)
        paramInt2 = i2 + 0; 
    } else {
      paramInt2 = 0 + i2 / 2;
    } 
    paramInt1 = Math.max(getPaddingLeft() + layoutParams.leftMargin, Math.min(paramInt1, m - getPaddingRight() - i1 - layoutParams.rightMargin));
    paramInt2 = Math.max(getPaddingTop() + layoutParams.topMargin, Math.min(paramInt2, n - getPaddingBottom() - i2 - layoutParams.bottomMargin));
    paramView.layout(paramInt1, paramInt2, i1 + paramInt1, i2 + paramInt2);
  }
  
  private void offsetChildByInset(View paramView, Rect paramRect, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic isLaidOut : (Landroid/view/View;)Z
    //   4: ifne -> 8
    //   7: return
    //   8: aload_1
    //   9: invokevirtual getWidth : ()I
    //   12: ifle -> 455
    //   15: aload_1
    //   16: invokevirtual getHeight : ()I
    //   19: ifgt -> 25
    //   22: goto -> 455
    //   25: aload_1
    //   26: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   29: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   32: astore #4
    //   34: aload #4
    //   36: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   39: astore #5
    //   41: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   44: astore #6
    //   46: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   49: astore #7
    //   51: aload #7
    //   53: aload_1
    //   54: invokevirtual getLeft : ()I
    //   57: aload_1
    //   58: invokevirtual getTop : ()I
    //   61: aload_1
    //   62: invokevirtual getRight : ()I
    //   65: aload_1
    //   66: invokevirtual getBottom : ()I
    //   69: invokevirtual set : (IIII)V
    //   72: aload #5
    //   74: ifnull -> 158
    //   77: aload #5
    //   79: aload_0
    //   80: aload_1
    //   81: aload #6
    //   83: invokevirtual getInsetDodgeRect : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/graphics/Rect;)Z
    //   86: ifeq -> 158
    //   89: aload #7
    //   91: aload #6
    //   93: invokevirtual contains : (Landroid/graphics/Rect;)Z
    //   96: ifeq -> 102
    //   99: goto -> 165
    //   102: new java/lang/StringBuilder
    //   105: dup
    //   106: invokespecial <init> : ()V
    //   109: astore_1
    //   110: aload_1
    //   111: ldc_w 'Rect should be within the child's bounds. Rect:'
    //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: aload_1
    //   119: aload #6
    //   121: invokevirtual toShortString : ()Ljava/lang/String;
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload_1
    //   129: ldc_w ' | Bounds:'
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_1
    //   137: aload #7
    //   139: invokevirtual toShortString : ()Ljava/lang/String;
    //   142: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: pop
    //   146: new java/lang/IllegalArgumentException
    //   149: dup
    //   150: aload_1
    //   151: invokevirtual toString : ()Ljava/lang/String;
    //   154: invokespecial <init> : (Ljava/lang/String;)V
    //   157: athrow
    //   158: aload #6
    //   160: aload #7
    //   162: invokevirtual set : (Landroid/graphics/Rect;)V
    //   165: aload #7
    //   167: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   170: aload #6
    //   172: invokevirtual isEmpty : ()Z
    //   175: ifeq -> 184
    //   178: aload #6
    //   180: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   183: return
    //   184: aload #4
    //   186: getfield dodgeInsetEdges : I
    //   189: iload_3
    //   190: invokestatic getAbsoluteGravity : (II)I
    //   193: istore #8
    //   195: iload #8
    //   197: bipush #48
    //   199: iand
    //   200: bipush #48
    //   202: if_icmpne -> 247
    //   205: aload #6
    //   207: getfield top : I
    //   210: aload #4
    //   212: getfield topMargin : I
    //   215: isub
    //   216: aload #4
    //   218: getfield mInsetOffsetY : I
    //   221: isub
    //   222: istore_3
    //   223: iload_3
    //   224: aload_2
    //   225: getfield top : I
    //   228: if_icmpge -> 247
    //   231: aload_0
    //   232: aload_1
    //   233: aload_2
    //   234: getfield top : I
    //   237: iload_3
    //   238: isub
    //   239: invokespecial setInsetOffsetY : (Landroid/view/View;I)V
    //   242: iconst_1
    //   243: istore_3
    //   244: goto -> 249
    //   247: iconst_0
    //   248: istore_3
    //   249: iload_3
    //   250: istore #9
    //   252: iload #8
    //   254: bipush #80
    //   256: iand
    //   257: bipush #80
    //   259: if_icmpne -> 313
    //   262: aload_0
    //   263: invokevirtual getHeight : ()I
    //   266: aload #6
    //   268: getfield bottom : I
    //   271: isub
    //   272: aload #4
    //   274: getfield bottomMargin : I
    //   277: isub
    //   278: aload #4
    //   280: getfield mInsetOffsetY : I
    //   283: iadd
    //   284: istore #10
    //   286: iload_3
    //   287: istore #9
    //   289: iload #10
    //   291: aload_2
    //   292: getfield bottom : I
    //   295: if_icmpge -> 313
    //   298: aload_0
    //   299: aload_1
    //   300: iload #10
    //   302: aload_2
    //   303: getfield bottom : I
    //   306: isub
    //   307: invokespecial setInsetOffsetY : (Landroid/view/View;I)V
    //   310: iconst_1
    //   311: istore #9
    //   313: iload #9
    //   315: ifne -> 324
    //   318: aload_0
    //   319: aload_1
    //   320: iconst_0
    //   321: invokespecial setInsetOffsetY : (Landroid/view/View;I)V
    //   324: iload #8
    //   326: iconst_3
    //   327: iand
    //   328: iconst_3
    //   329: if_icmpne -> 374
    //   332: aload #6
    //   334: getfield left : I
    //   337: aload #4
    //   339: getfield leftMargin : I
    //   342: isub
    //   343: aload #4
    //   345: getfield mInsetOffsetX : I
    //   348: isub
    //   349: istore_3
    //   350: iload_3
    //   351: aload_2
    //   352: getfield left : I
    //   355: if_icmpge -> 374
    //   358: aload_0
    //   359: aload_1
    //   360: aload_2
    //   361: getfield left : I
    //   364: iload_3
    //   365: isub
    //   366: invokespecial setInsetOffsetX : (Landroid/view/View;I)V
    //   369: iconst_1
    //   370: istore_3
    //   371: goto -> 376
    //   374: iconst_0
    //   375: istore_3
    //   376: iload_3
    //   377: istore #9
    //   379: iload #8
    //   381: iconst_5
    //   382: iand
    //   383: iconst_5
    //   384: if_icmpne -> 438
    //   387: aload_0
    //   388: invokevirtual getWidth : ()I
    //   391: aload #6
    //   393: getfield right : I
    //   396: isub
    //   397: aload #4
    //   399: getfield rightMargin : I
    //   402: isub
    //   403: aload #4
    //   405: getfield mInsetOffsetX : I
    //   408: iadd
    //   409: istore #8
    //   411: iload_3
    //   412: istore #9
    //   414: iload #8
    //   416: aload_2
    //   417: getfield right : I
    //   420: if_icmpge -> 438
    //   423: aload_0
    //   424: aload_1
    //   425: iload #8
    //   427: aload_2
    //   428: getfield right : I
    //   431: isub
    //   432: invokespecial setInsetOffsetX : (Landroid/view/View;I)V
    //   435: iconst_1
    //   436: istore #9
    //   438: iload #9
    //   440: ifne -> 449
    //   443: aload_0
    //   444: aload_1
    //   445: iconst_0
    //   446: invokespecial setInsetOffsetX : (Landroid/view/View;I)V
    //   449: aload #6
    //   451: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   454: return
    //   455: return
  }
  
  static Behavior parseBehavior(Context paramContext, AttributeSet paramAttributeSet, String paramString) {
    String str;
    if (TextUtils.isEmpty(paramString))
      return null; 
    if (paramString.startsWith(".")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramContext.getPackageName());
      stringBuilder.append(paramString);
      str = stringBuilder.toString();
    } else if (paramString.indexOf('.') >= 0) {
      str = paramString;
    } else {
      str = paramString;
      if (!TextUtils.isEmpty(WIDGET_PACKAGE_NAME)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WIDGET_PACKAGE_NAME);
        stringBuilder.append('.');
        stringBuilder.append(paramString);
        str = stringBuilder.toString();
      } 
    } 
    try {
      Map<Object, Object> map2 = (Map)sConstructors.get();
      Map<Object, Object> map1 = map2;
      if (map2 == null) {
        map1 = new HashMap<Object, Object>();
        super();
        sConstructors.set(map1);
      } 
      Constructor<?> constructor2 = (Constructor)map1.get(str);
      Constructor<?> constructor1 = constructor2;
      if (constructor2 == null) {
        constructor1 = paramContext.getClassLoader().loadClass(str).getConstructor(CONSTRUCTOR_PARAMS);
        constructor1.setAccessible(true);
        map1.put(str, constructor1);
      } 
      return (Behavior)constructor1.newInstance(new Object[] { paramContext, paramAttributeSet });
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not inflate Behavior subclass ");
      stringBuilder.append(str);
      throw new RuntimeException(stringBuilder.toString(), exception);
    } 
  }
  
  private boolean performIntercept(MotionEvent paramMotionEvent, int paramInt) {
    boolean bool2;
    int i = paramMotionEvent.getActionMasked();
    List<View> list = this.mTempList1;
    getTopSortedChildren(list);
    int j = list.size();
    LayoutParams layoutParams = null;
    byte b = 0;
    boolean bool1 = false;
    boolean bool = false;
    while (true) {
      bool2 = bool1;
      if (b < j) {
        MotionEvent motionEvent;
        LayoutParams layoutParams1;
        boolean bool3;
        boolean bool4;
        View view = list.get(b);
        LayoutParams layoutParams2 = (LayoutParams)view.getLayoutParams();
        Behavior<View> behavior = layoutParams2.getBehavior();
        if ((bool1 || bool) && i != 0) {
          bool3 = bool1;
          bool4 = bool;
          layoutParams2 = layoutParams;
          if (behavior != null) {
            layoutParams2 = layoutParams;
            if (layoutParams == null) {
              long l = SystemClock.uptimeMillis();
              motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
            } 
            switch (paramInt) {
              default:
                bool3 = bool1;
                bool4 = bool;
                break;
              case 1:
                behavior.onTouchEvent(this, view, motionEvent);
                bool3 = bool1;
                bool4 = bool;
                break;
              case 0:
                behavior.onInterceptTouchEvent(this, view, motionEvent);
                bool3 = bool1;
                bool4 = bool;
                break;
            } 
          } 
        } else {
          bool2 = bool1;
          if (!bool1) {
            bool2 = bool1;
            if (behavior != null) {
              switch (paramInt) {
                case 1:
                  bool1 = behavior.onTouchEvent(this, view, paramMotionEvent);
                  break;
                case 0:
                  bool1 = behavior.onInterceptTouchEvent(this, view, paramMotionEvent);
                  break;
              } 
              bool2 = bool1;
              if (bool1) {
                this.mBehaviorTouchView = view;
                bool2 = bool1;
              } 
            } 
          } 
          bool3 = motionEvent.didBlockInteraction();
          bool1 = motionEvent.isBlockingInteractionBelow(this, view);
          if (bool1 && !bool3) {
            bool = true;
          } else {
            bool = false;
          } 
          bool3 = bool2;
          bool4 = bool;
          layoutParams1 = layoutParams;
          if (bool1) {
            bool3 = bool2;
            bool4 = bool;
            layoutParams1 = layoutParams;
            if (!bool)
              break; 
          } 
        } 
        b++;
        bool1 = bool3;
        bool = bool4;
        layoutParams = layoutParams1;
        continue;
      } 
      break;
    } 
    list.clear();
    return bool2;
  }
  
  private void prepareChildren() {
    this.mDependencySortedChildren.clear();
    this.mChildDag.clear();
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = getResolvedLayoutParams(view);
      layoutParams.findAnchorView(this, view);
      this.mChildDag.addNode(view);
      for (byte b1 = 0; b1 < i; b1++) {
        if (b1 != b) {
          View view1 = getChildAt(b1);
          if (layoutParams.dependsOn(this, view, view1)) {
            if (!this.mChildDag.contains(view1))
              this.mChildDag.addNode(view1); 
            this.mChildDag.addEdge(view1, view);
          } 
        } 
      } 
    } 
    this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
    Collections.reverse(this.mDependencySortedChildren);
  }
  
  private static void releaseTempRect(@NonNull Rect paramRect) {
    paramRect.setEmpty();
    sRectPool.release(paramRect);
  }
  
  private void resetTouchBehaviors(boolean paramBoolean) {
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
      if (behavior != null) {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        if (paramBoolean) {
          behavior.onInterceptTouchEvent(this, view, motionEvent);
        } else {
          behavior.onTouchEvent(this, view, motionEvent);
        } 
        motionEvent.recycle();
      } 
    } 
    for (b = 0; b < i; b++)
      ((LayoutParams)getChildAt(b).getLayoutParams()).resetTouchBehaviorTracking(); 
    this.mBehaviorTouchView = null;
    this.mDisallowInterceptReset = false;
  }
  
  private static int resolveAnchoredChildGravity(int paramInt) {
    int i = paramInt;
    if (paramInt == 0)
      i = 17; 
    return i;
  }
  
  private static int resolveGravity(int paramInt) {
    int i = paramInt;
    if ((paramInt & 0x7) == 0)
      i = paramInt | 0x800003; 
    paramInt = i;
    if ((i & 0x70) == 0)
      paramInt = i | 0x30; 
    return paramInt;
  }
  
  private static int resolveKeylineGravity(int paramInt) {
    int i = paramInt;
    if (paramInt == 0)
      i = 8388661; 
    return i;
  }
  
  private void setInsetOffsetX(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetX != paramInt) {
      ViewCompat.offsetLeftAndRight(paramView, paramInt - layoutParams.mInsetOffsetX);
      layoutParams.mInsetOffsetX = paramInt;
    } 
  }
  
  private void setInsetOffsetY(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetY != paramInt) {
      ViewCompat.offsetTopAndBottom(paramView, paramInt - layoutParams.mInsetOffsetY);
      layoutParams.mInsetOffsetY = paramInt;
    } 
  }
  
  private void setupForInsets() {
    if (Build.VERSION.SDK_INT < 21)
      return; 
    if (ViewCompat.getFitsSystemWindows((View)this)) {
      if (this.mApplyWindowInsetsListener == null)
        this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
              return CoordinatorLayout.this.setWindowInsets(param1WindowInsetsCompat);
            }
          }; 
      ViewCompat.setOnApplyWindowInsetsListener((View)this, this.mApplyWindowInsetsListener);
      setSystemUiVisibility(1280);
    } else {
      ViewCompat.setOnApplyWindowInsetsListener((View)this, null);
    } 
  }
  
  void addPreDrawListener() {
    if (this.mIsAttachedToWindow) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    this.mNeedsPreDrawListener = true;
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
  
  public void dispatchDependentViewsChanged(@NonNull View paramView) {
    List<View> list = this.mChildDag.getIncomingEdges(paramView);
    if (list != null && !list.isEmpty())
      for (byte b = 0; b < list.size(); b++) {
        View view = list.get(b);
        Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior != null)
          behavior.onDependentViewChanged(this, view, paramView); 
      }  
  }
  
  public boolean doViewsOverlap(@NonNull View paramView1, @NonNull View paramView2) {
    int i = paramView1.getVisibility();
    boolean bool = false;
    if (i == 0 && paramView2.getVisibility() == 0) {
      Rect rect2 = acquireTempRect();
      if (paramView1.getParent() != this) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      getChildRect(paramView1, bool1, rect2);
      Rect rect1 = acquireTempRect();
      if (paramView2.getParent() != this) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      getChildRect(paramView2, bool1, rect1);
      boolean bool1 = bool;
      try {
        if (rect2.left <= rect1.right) {
          bool1 = bool;
          if (rect2.top <= rect1.bottom) {
            bool1 = bool;
            if (rect2.right >= rect1.left) {
              i = rect2.bottom;
              int j = rect1.top;
              bool1 = bool;
              if (i >= j)
                bool1 = true; 
            } 
          } 
        } 
        return bool1;
      } finally {
        releaseTempRect(rect2);
        releaseTempRect(rect1);
      } 
    } 
    return false;
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mBehavior != null) {
      float f = layoutParams.mBehavior.getScrimOpacity(this, paramView);
      if (f > 0.0F) {
        if (this.mScrimPaint == null)
          this.mScrimPaint = new Paint(); 
        this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, paramView));
        this.mScrimPaint.setAlpha(clamp(Math.round(f * 255.0F), 0, 255));
        int i = paramCanvas.save();
        if (paramView.isOpaque())
          paramCanvas.clipRect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom(), Region.Op.DIFFERENCE); 
        paramCanvas.drawRect(getPaddingLeft(), getPaddingTop(), (getWidth() - getPaddingRight()), (getHeight() - getPaddingBottom()), this.mScrimPaint);
        paramCanvas.restoreToCount(i);
      } 
    } 
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    Drawable drawable = this.mStatusBarBackground;
    byte b = 0;
    int i = b;
    if (drawable != null) {
      i = b;
      if (drawable.isStateful())
        i = false | drawable.setState(arrayOfInt); 
    } 
    if (i != 0)
      invalidate(); 
  }
  
  void ensurePreDrawListener() {
    boolean bool2;
    int i = getChildCount();
    boolean bool1 = false;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        if (hasDependencies(getChildAt(b))) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool2 != this.mNeedsPreDrawListener)
      if (bool2) {
        addPreDrawListener();
      } else {
        removePreDrawListener();
      }  
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams));
  }
  
  void getChildRect(View paramView, boolean paramBoolean, Rect paramRect) {
    if (paramView.isLayoutRequested() || paramView.getVisibility() == 8) {
      paramRect.setEmpty();
      return;
    } 
    if (paramBoolean) {
      getDescendantRect(paramView, paramRect);
    } else {
      paramRect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    } 
  }
  
  @NonNull
  public List<View> getDependencies(@NonNull View paramView) {
    List<? extends View> list = this.mChildDag.getOutgoingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  @VisibleForTesting
  final List<View> getDependencySortedChildren() {
    prepareChildren();
    return Collections.unmodifiableList(this.mDependencySortedChildren);
  }
  
  @NonNull
  public List<View> getDependents(@NonNull View paramView) {
    List<? extends View> list = this.mChildDag.getIncomingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  void getDescendantRect(View paramView, Rect paramRect) {
    ViewGroupUtils.getDescendantRect(this, paramView, paramRect);
  }
  
  void getDesiredAnchoredChildRect(View paramView, int paramInt, Rect paramRect1, Rect paramRect2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    getDesiredAnchoredChildRectWithoutConstraints(paramView, paramInt, paramRect1, paramRect2, layoutParams, i, j);
    constrainChildRect(layoutParams, paramRect2, i, j);
  }
  
  void getLastChildRect(View paramView, Rect paramRect) {
    paramRect.set(((LayoutParams)paramView.getLayoutParams()).getLastChildRect());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public final WindowInsetsCompat getLastWindowInsets() {
    return this.mLastInsets;
  }
  
  public int getNestedScrollAxes() {
    return this.mNestedScrollingParentHelper.getNestedScrollAxes();
  }
  
  LayoutParams getResolvedLayoutParams(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mBehaviorResolved) {
      Behavior behavior;
      if (paramView instanceof AttachedBehavior) {
        behavior = ((AttachedBehavior)paramView).getBehavior();
        if (behavior == null)
          Log.e("CoordinatorLayout", "Attached behavior class is null"); 
        layoutParams.setBehavior(behavior);
        layoutParams.mBehaviorResolved = true;
      } else {
        DefaultBehavior defaultBehavior;
        Class<?> clazz = behavior.getClass();
        behavior = null;
        while (clazz != null) {
          DefaultBehavior defaultBehavior1 = clazz.<DefaultBehavior>getAnnotation(DefaultBehavior.class);
          defaultBehavior = defaultBehavior1;
          if (defaultBehavior1 == null) {
            clazz = clazz.getSuperclass();
            defaultBehavior = defaultBehavior1;
          } 
        } 
        if (defaultBehavior != null)
          try {
            layoutParams.setBehavior(defaultBehavior.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
          } catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Default behavior class ");
            stringBuilder.append(defaultBehavior.value().getName());
            stringBuilder.append(" could not be instantiated. Did you forget");
            stringBuilder.append(" a default constructor?");
            Log.e("CoordinatorLayout", stringBuilder.toString(), exception);
          }  
        layoutParams.mBehaviorResolved = true;
      } 
    } 
    return layoutParams;
  }
  
  @Nullable
  public Drawable getStatusBarBackground() {
    return this.mStatusBarBackground;
  }
  
  protected int getSuggestedMinimumHeight() {
    return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
  }
  
  protected int getSuggestedMinimumWidth() {
    return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
  }
  
  public boolean isPointInChildBounds(@NonNull View paramView, int paramInt1, int paramInt2) {
    Rect rect = acquireTempRect();
    getDescendantRect(paramView, rect);
    try {
      return rect.contains(paramInt1, paramInt2);
    } finally {
      releaseTempRect(rect);
    } 
  }
  
  void offsetChildToAnchor(View paramView, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   7: astore_3
    //   8: aload_3
    //   9: getfield mAnchorView : Landroid/view/View;
    //   12: ifnull -> 210
    //   15: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   18: astore #4
    //   20: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   23: astore #5
    //   25: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   28: astore #6
    //   30: aload_0
    //   31: aload_3
    //   32: getfield mAnchorView : Landroid/view/View;
    //   35: aload #4
    //   37: invokevirtual getDescendantRect : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   40: iconst_0
    //   41: istore #7
    //   43: aload_0
    //   44: aload_1
    //   45: iconst_0
    //   46: aload #5
    //   48: invokevirtual getChildRect : (Landroid/view/View;ZLandroid/graphics/Rect;)V
    //   51: aload_1
    //   52: invokevirtual getMeasuredWidth : ()I
    //   55: istore #8
    //   57: aload_1
    //   58: invokevirtual getMeasuredHeight : ()I
    //   61: istore #9
    //   63: aload_0
    //   64: aload_1
    //   65: iload_2
    //   66: aload #4
    //   68: aload #6
    //   70: aload_3
    //   71: iload #8
    //   73: iload #9
    //   75: invokespecial getDesiredAnchoredChildRectWithoutConstraints : (Landroid/view/View;ILandroid/graphics/Rect;Landroid/graphics/Rect;Landroid/support/design/widget/CoordinatorLayout$LayoutParams;II)V
    //   78: aload #6
    //   80: getfield left : I
    //   83: aload #5
    //   85: getfield left : I
    //   88: if_icmpne -> 107
    //   91: iload #7
    //   93: istore_2
    //   94: aload #6
    //   96: getfield top : I
    //   99: aload #5
    //   101: getfield top : I
    //   104: if_icmpeq -> 109
    //   107: iconst_1
    //   108: istore_2
    //   109: aload_0
    //   110: aload_3
    //   111: aload #6
    //   113: iload #8
    //   115: iload #9
    //   117: invokespecial constrainChildRect : (Landroid/support/design/widget/CoordinatorLayout$LayoutParams;Landroid/graphics/Rect;II)V
    //   120: aload #6
    //   122: getfield left : I
    //   125: aload #5
    //   127: getfield left : I
    //   130: isub
    //   131: istore #9
    //   133: aload #6
    //   135: getfield top : I
    //   138: aload #5
    //   140: getfield top : I
    //   143: isub
    //   144: istore #7
    //   146: iload #9
    //   148: ifeq -> 157
    //   151: aload_1
    //   152: iload #9
    //   154: invokestatic offsetLeftAndRight : (Landroid/view/View;I)V
    //   157: iload #7
    //   159: ifeq -> 168
    //   162: aload_1
    //   163: iload #7
    //   165: invokestatic offsetTopAndBottom : (Landroid/view/View;I)V
    //   168: iload_2
    //   169: ifeq -> 195
    //   172: aload_3
    //   173: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   176: astore #10
    //   178: aload #10
    //   180: ifnull -> 195
    //   183: aload #10
    //   185: aload_0
    //   186: aload_1
    //   187: aload_3
    //   188: getfield mAnchorView : Landroid/view/View;
    //   191: invokevirtual onDependentViewChanged : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/View;)Z
    //   194: pop
    //   195: aload #4
    //   197: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   200: aload #5
    //   202: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   205: aload #6
    //   207: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   210: return
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    resetTouchBehaviors(false);
    if (this.mNeedsPreDrawListener) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows((View)this))
      ViewCompat.requestApplyInsets((View)this); 
    this.mIsAttachedToWindow = true;
  }
  
  final void onChildViewsChanged(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    int j = this.mDependencySortedChildren.size();
    Rect rect1 = acquireTempRect();
    Rect rect2 = acquireTempRect();
    Rect rect3 = acquireTempRect();
    for (byte b = 0; b < j; b++) {
      View view = this.mDependencySortedChildren.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (paramInt == 0 && view.getVisibility() == 8)
        continue; 
      int k;
      for (k = 0; k < b; k++) {
        View view1 = this.mDependencySortedChildren.get(k);
        if (layoutParams.mAnchorDirectChild == view1)
          offsetChildToAnchor(view, i); 
      } 
      getChildRect(view, true, rect2);
      if (layoutParams.insetEdge != 0 && !rect2.isEmpty()) {
        int m = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, i);
        k = m & 0x70;
        if (k != 48) {
          if (k == 80)
            rect1.bottom = Math.max(rect1.bottom, getHeight() - rect2.top); 
        } else {
          rect1.top = Math.max(rect1.top, rect2.bottom);
        } 
        k = m & 0x7;
        if (k != 3) {
          if (k == 5)
            rect1.right = Math.max(rect1.right, getWidth() - rect2.left); 
        } else {
          rect1.left = Math.max(rect1.left, rect2.right);
        } 
      } 
      if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0)
        offsetChildByInset(view, rect1, i); 
      if (paramInt != 2) {
        getLastChildRect(view, rect3);
        if (rect3.equals(rect2))
          continue; 
        recordLastChildRect(view, rect2);
      } 
      for (k = b + 1; k < j; k++) {
        View view1 = this.mDependencySortedChildren.get(k);
        LayoutParams layoutParams1 = (LayoutParams)view1.getLayoutParams();
        Behavior<View> behavior = layoutParams1.getBehavior();
        if (behavior != null && behavior.layoutDependsOn(this, view1, view))
          if (paramInt == 0 && layoutParams1.getChangedAfterNestedScroll()) {
            layoutParams1.resetChangedAfterNestedScroll();
          } else {
            boolean bool;
            if (paramInt != 2) {
              bool = behavior.onDependentViewChanged(this, view1, view);
            } else {
              behavior.onDependentViewRemoved(this, view1, view);
              bool = true;
            } 
            if (paramInt == 1)
              layoutParams1.setChangedAfterNestedScroll(bool); 
          }  
      } 
      continue;
    } 
    releaseTempRect(rect1);
    releaseTempRect(rect2);
    releaseTempRect(rect3);
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    resetTouchBehaviors(false);
    if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    if (this.mNestedScrollingTarget != null)
      onStopNestedScroll(this.mNestedScrollingTarget); 
    this.mIsAttachedToWindow = false;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      boolean bool;
      if (this.mLastInsets != null) {
        bool = this.mLastInsets.getSystemWindowInsetTop();
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
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      resetTouchBehaviors(true); 
    boolean bool = performIntercept(paramMotionEvent, 0);
    if (i == 1 || i == 3)
      resetTouchBehaviors(true); 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = ViewCompat.getLayoutDirection((View)this);
    paramInt3 = this.mDependencySortedChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = this.mDependencySortedChildren.get(paramInt1);
      if (view.getVisibility() != 8) {
        Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior == null || !behavior.onLayoutChild(this, view, paramInt2))
          onLayoutChild(view, paramInt2); 
      } 
    } 
  }
  
  public void onLayoutChild(@NonNull View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.checkAnchorChanged()) {
      if (layoutParams.mAnchorView != null) {
        layoutChildWithAnchor(paramView, layoutParams.mAnchorView, paramInt);
      } else if (layoutParams.keyline >= 0) {
        layoutChildWithKeyline(paramView, layoutParams.keyline, paramInt);
      } else {
        layoutChild(paramView, paramInt);
      } 
      return;
    } 
    throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial prepareChildren : ()V
    //   4: aload_0
    //   5: invokevirtual ensurePreDrawListener : ()V
    //   8: aload_0
    //   9: invokevirtual getPaddingLeft : ()I
    //   12: istore_3
    //   13: aload_0
    //   14: invokevirtual getPaddingTop : ()I
    //   17: istore #4
    //   19: aload_0
    //   20: invokevirtual getPaddingRight : ()I
    //   23: istore #5
    //   25: aload_0
    //   26: invokevirtual getPaddingBottom : ()I
    //   29: istore #6
    //   31: aload_0
    //   32: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   35: istore #7
    //   37: iload #7
    //   39: iconst_1
    //   40: if_icmpne -> 49
    //   43: iconst_1
    //   44: istore #8
    //   46: goto -> 52
    //   49: iconst_0
    //   50: istore #8
    //   52: iload_1
    //   53: invokestatic getMode : (I)I
    //   56: istore #9
    //   58: iload_1
    //   59: invokestatic getSize : (I)I
    //   62: istore #10
    //   64: iload_2
    //   65: invokestatic getMode : (I)I
    //   68: istore #11
    //   70: iload_2
    //   71: invokestatic getSize : (I)I
    //   74: istore #12
    //   76: aload_0
    //   77: invokevirtual getSuggestedMinimumWidth : ()I
    //   80: istore #13
    //   82: aload_0
    //   83: invokevirtual getSuggestedMinimumHeight : ()I
    //   86: istore #14
    //   88: aload_0
    //   89: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   92: ifnull -> 108
    //   95: aload_0
    //   96: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   99: ifeq -> 108
    //   102: iconst_1
    //   103: istore #15
    //   105: goto -> 111
    //   108: iconst_0
    //   109: istore #15
    //   111: aload_0
    //   112: getfield mDependencySortedChildren : Ljava/util/List;
    //   115: invokeinterface size : ()I
    //   120: istore #16
    //   122: iconst_0
    //   123: istore #17
    //   125: iconst_0
    //   126: istore #18
    //   128: iload #18
    //   130: iload #16
    //   132: if_icmpge -> 508
    //   135: aload_0
    //   136: getfield mDependencySortedChildren : Ljava/util/List;
    //   139: iload #18
    //   141: invokeinterface get : (I)Ljava/lang/Object;
    //   146: checkcast android/view/View
    //   149: astore #19
    //   151: aload #19
    //   153: invokevirtual getVisibility : ()I
    //   156: bipush #8
    //   158: if_icmpne -> 164
    //   161: goto -> 502
    //   164: aload #19
    //   166: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   169: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   172: astore #20
    //   174: aload #20
    //   176: getfield keyline : I
    //   179: iflt -> 290
    //   182: iload #9
    //   184: ifeq -> 290
    //   187: aload_0
    //   188: aload #20
    //   190: getfield keyline : I
    //   193: invokespecial getKeyline : (I)I
    //   196: istore #21
    //   198: aload #20
    //   200: getfield gravity : I
    //   203: invokestatic resolveKeylineGravity : (I)I
    //   206: iload #7
    //   208: invokestatic getAbsoluteGravity : (II)I
    //   211: bipush #7
    //   213: iand
    //   214: istore #22
    //   216: iload #22
    //   218: iconst_3
    //   219: if_icmpne -> 227
    //   222: iload #8
    //   224: ifeq -> 238
    //   227: iload #22
    //   229: iconst_5
    //   230: if_icmpne -> 255
    //   233: iload #8
    //   235: ifeq -> 255
    //   238: iconst_0
    //   239: iload #10
    //   241: iload #5
    //   243: isub
    //   244: iload #21
    //   246: isub
    //   247: invokestatic max : (II)I
    //   250: istore #22
    //   252: goto -> 293
    //   255: iload #22
    //   257: iconst_5
    //   258: if_icmpne -> 266
    //   261: iload #8
    //   263: ifeq -> 277
    //   266: iload #22
    //   268: iconst_3
    //   269: if_icmpne -> 290
    //   272: iload #8
    //   274: ifeq -> 290
    //   277: iconst_0
    //   278: iload #21
    //   280: iload_3
    //   281: isub
    //   282: invokestatic max : (II)I
    //   285: istore #22
    //   287: goto -> 293
    //   290: iconst_0
    //   291: istore #22
    //   293: iload #14
    //   295: istore #23
    //   297: iload #15
    //   299: ifeq -> 379
    //   302: aload #19
    //   304: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   307: ifne -> 379
    //   310: aload_0
    //   311: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   314: invokevirtual getSystemWindowInsetLeft : ()I
    //   317: istore #24
    //   319: aload_0
    //   320: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   323: invokevirtual getSystemWindowInsetRight : ()I
    //   326: istore #21
    //   328: aload_0
    //   329: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   332: invokevirtual getSystemWindowInsetTop : ()I
    //   335: istore #14
    //   337: aload_0
    //   338: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   341: invokevirtual getSystemWindowInsetBottom : ()I
    //   344: istore #25
    //   346: iload #10
    //   348: iload #24
    //   350: iload #21
    //   352: iadd
    //   353: isub
    //   354: iload #9
    //   356: invokestatic makeMeasureSpec : (II)I
    //   359: istore #21
    //   361: iload #12
    //   363: iload #14
    //   365: iload #25
    //   367: iadd
    //   368: isub
    //   369: iload #11
    //   371: invokestatic makeMeasureSpec : (II)I
    //   374: istore #14
    //   376: goto -> 385
    //   379: iload_1
    //   380: istore #21
    //   382: iload_2
    //   383: istore #14
    //   385: aload #20
    //   387: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   390: astore #26
    //   392: aload #26
    //   394: ifnull -> 418
    //   397: aload #26
    //   399: aload_0
    //   400: aload #19
    //   402: iload #21
    //   404: iload #22
    //   406: iload #14
    //   408: iconst_0
    //   409: invokevirtual onMeasureChild : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;IIII)Z
    //   412: ifne -> 431
    //   415: goto -> 418
    //   418: aload_0
    //   419: aload #19
    //   421: iload #21
    //   423: iload #22
    //   425: iload #14
    //   427: iconst_0
    //   428: invokevirtual onMeasureChild : (Landroid/view/View;IIII)V
    //   431: iload #13
    //   433: iload_3
    //   434: iload #5
    //   436: iadd
    //   437: aload #19
    //   439: invokevirtual getMeasuredWidth : ()I
    //   442: iadd
    //   443: aload #20
    //   445: getfield leftMargin : I
    //   448: iadd
    //   449: aload #20
    //   451: getfield rightMargin : I
    //   454: iadd
    //   455: invokestatic max : (II)I
    //   458: istore #13
    //   460: iload #23
    //   462: iload #4
    //   464: iload #6
    //   466: iadd
    //   467: aload #19
    //   469: invokevirtual getMeasuredHeight : ()I
    //   472: iadd
    //   473: aload #20
    //   475: getfield topMargin : I
    //   478: iadd
    //   479: aload #20
    //   481: getfield bottomMargin : I
    //   484: iadd
    //   485: invokestatic max : (II)I
    //   488: istore #14
    //   490: iload #17
    //   492: aload #19
    //   494: invokevirtual getMeasuredState : ()I
    //   497: invokestatic combineMeasuredStates : (II)I
    //   500: istore #17
    //   502: iinc #18, 1
    //   505: goto -> 128
    //   508: aload_0
    //   509: iload #13
    //   511: iload_1
    //   512: ldc_w -16777216
    //   515: iload #17
    //   517: iand
    //   518: invokestatic resolveSizeAndState : (III)I
    //   521: iload #14
    //   523: iload_2
    //   524: iload #17
    //   526: bipush #16
    //   528: ishl
    //   529: invokestatic resolveSizeAndState : (III)I
    //   532: invokevirtual setMeasuredDimension : (II)V
    //   535: return
  }
  
  public void onMeasureChild(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    int i = getChildCount();
    byte b = 0;
    boolean bool;
    for (bool = false; b < i; bool = bool1) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isNestedScrollAccepted(0)) {
          bool1 = bool;
        } else {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = bool | behavior.onNestedFling(this, view, paramView, paramFloat1, paramFloat2, paramBoolean); 
        } 
      } 
      b++;
    } 
    if (bool)
      onChildViewsChanged(1); 
    return bool;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    int i = getChildCount();
    byte b = 0;
    boolean bool;
    for (bool = false; b < i; bool = bool1) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isNestedScrollAccepted(0)) {
          bool1 = bool;
        } else {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = bool | behavior.onNestedPreFling(this, view, paramView, paramFloat1, paramFloat2); 
        } 
      } 
      b++;
    } 
    return bool;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint, 0);
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    int i = getChildCount();
    boolean bool = false;
    byte b = 0;
    int j = 0;
    int k;
    for (k = 0; b < i; k = n) {
      int m;
      int n;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        m = j;
        n = k;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isNestedScrollAccepted(paramInt3)) {
          m = j;
          n = k;
        } else {
          Behavior<View> behavior = layoutParams.getBehavior();
          m = j;
          n = k;
          if (behavior != null) {
            int[] arrayOfInt = this.mTempIntPair;
            this.mTempIntPair[1] = 0;
            arrayOfInt[0] = 0;
            behavior.onNestedPreScroll(this, view, paramView, paramInt1, paramInt2, this.mTempIntPair, paramInt3);
            if (paramInt1 > 0) {
              n = Math.max(j, this.mTempIntPair[0]);
            } else {
              n = Math.min(j, this.mTempIntPair[0]);
            } 
            if (paramInt2 > 0) {
              k = Math.max(k, this.mTempIntPair[1]);
            } else {
              k = Math.min(k, this.mTempIntPair[1]);
            } 
            m = n;
            n = k;
            bool = true;
          } 
        } 
      } 
      b++;
      j = m;
    } 
    paramArrayOfint[0] = j;
    paramArrayOfint[1] = k;
    if (bool)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int i = getChildCount();
    boolean bool = false;
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isNestedScrollAccepted(paramInt5)) {
          Behavior<View> behavior = layoutParams.getBehavior();
          if (behavior != null) {
            behavior.onNestedScroll(this, view, paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
            bool = true;
          } 
        } 
      } 
    } 
    if (bool)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {
    onNestedScrollAccepted(paramView1, paramView2, paramInt, 0);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    this.mNestedScrollingParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
    this.mNestedScrollingTarget = paramView2;
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted(paramInt2)) {
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onNestedScrollAccepted(this, view, paramView1, paramView2, paramInt1, paramInt2); 
      } 
    } 
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    SparseArray<Parcelable> sparseArray = savedState.behaviorStates;
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      int j = view.getId();
      Behavior<View> behavior = getResolvedLayoutParams(view).getBehavior();
      if (j != -1 && behavior != null) {
        Parcelable parcelable = (Parcelable)sparseArray.get(j);
        if (parcelable != null)
          behavior.onRestoreInstanceState(this, view, parcelable); 
      } 
      b++;
    } 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    SparseArray<Parcelable> sparseArray = new SparseArray();
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      int j = view.getId();
      Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
      if (j != -1 && behavior != null) {
        Parcelable parcelable = behavior.onSaveInstanceState(this, view);
        if (parcelable != null)
          sparseArray.append(j, parcelable); 
      } 
    } 
    savedState.behaviorStates = sparseArray;
    return (Parcelable)savedState;
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) {
    return onStartNestedScroll(paramView1, paramView2, paramInt, 0);
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    int i = getChildCount();
    byte b = 0;
    boolean bool = false;
    while (b < i) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null) {
          boolean bool1 = behavior.onStartNestedScroll(this, view, paramView1, paramView2, paramInt1, paramInt2);
          layoutParams.setNestedScrollAccepted(paramInt2, bool1);
          bool |= bool1;
        } else {
          layoutParams.setNestedScrollAccepted(paramInt2, false);
        } 
      } 
      b++;
    } 
    return bool;
  }
  
  public void onStopNestedScroll(View paramView) {
    onStopNestedScroll(paramView, 0);
  }
  
  public void onStopNestedScroll(View paramView, int paramInt) {
    this.mNestedScrollingParentHelper.onStopNestedScroll(paramView, paramInt);
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted(paramInt)) {
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onStopNestedScroll(this, view, paramView, paramInt); 
        layoutParams.resetNestedScroll(paramInt);
        layoutParams.resetChangedAfterNestedScroll();
      } 
    } 
    this.mNestedScrollingTarget = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getActionMasked : ()I
    //   4: istore_2
    //   5: aload_0
    //   6: getfield mBehaviorTouchView : Landroid/view/View;
    //   9: ifnonnull -> 42
    //   12: aload_0
    //   13: aload_1
    //   14: iconst_1
    //   15: invokespecial performIntercept : (Landroid/view/MotionEvent;I)Z
    //   18: istore_3
    //   19: iload_3
    //   20: istore #4
    //   22: iload_3
    //   23: ifeq -> 29
    //   26: goto -> 44
    //   29: iconst_0
    //   30: istore #5
    //   32: iload #4
    //   34: istore_3
    //   35: iload #5
    //   37: istore #4
    //   39: goto -> 80
    //   42: iconst_0
    //   43: istore_3
    //   44: aload_0
    //   45: getfield mBehaviorTouchView : Landroid/view/View;
    //   48: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   51: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   54: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   57: astore #6
    //   59: iload_3
    //   60: istore #4
    //   62: aload #6
    //   64: ifnull -> 29
    //   67: aload #6
    //   69: aload_0
    //   70: aload_0
    //   71: getfield mBehaviorTouchView : Landroid/view/View;
    //   74: aload_1
    //   75: invokevirtual onTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   78: istore #4
    //   80: aload_0
    //   81: getfield mBehaviorTouchView : Landroid/view/View;
    //   84: astore #7
    //   86: aconst_null
    //   87: astore #6
    //   89: aload #7
    //   91: ifnonnull -> 110
    //   94: iload #4
    //   96: aload_0
    //   97: aload_1
    //   98: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   101: ior
    //   102: istore #5
    //   104: aload #6
    //   106: astore_1
    //   107: goto -> 148
    //   110: iload #4
    //   112: istore #5
    //   114: aload #6
    //   116: astore_1
    //   117: iload_3
    //   118: ifeq -> 148
    //   121: invokestatic uptimeMillis : ()J
    //   124: lstore #8
    //   126: lload #8
    //   128: lload #8
    //   130: iconst_3
    //   131: fconst_0
    //   132: fconst_0
    //   133: iconst_0
    //   134: invokestatic obtain : (JJIFFI)Landroid/view/MotionEvent;
    //   137: astore_1
    //   138: aload_0
    //   139: aload_1
    //   140: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   143: pop
    //   144: iload #4
    //   146: istore #5
    //   148: aload_1
    //   149: ifnull -> 156
    //   152: aload_1
    //   153: invokevirtual recycle : ()V
    //   156: iload_2
    //   157: iconst_1
    //   158: if_icmpeq -> 166
    //   161: iload_2
    //   162: iconst_3
    //   163: if_icmpne -> 171
    //   166: aload_0
    //   167: iconst_0
    //   168: invokespecial resetTouchBehaviors : (Z)V
    //   171: iload #5
    //   173: ireturn
  }
  
  void recordLastChildRect(View paramView, Rect paramRect) {
    ((LayoutParams)paramView.getLayoutParams()).setLastChildRect(paramRect);
  }
  
  void removePreDrawListener() {
    if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    this.mNeedsPreDrawListener = false;
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    Behavior<View> behavior = ((LayoutParams)paramView.getLayoutParams()).getBehavior();
    return (behavior != null && behavior.onRequestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean)) ? true : super.requestChildRectangleOnScreen(paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    if (paramBoolean && !this.mDisallowInterceptReset) {
      resetTouchBehaviors(false);
      this.mDisallowInterceptReset = true;
    } 
  }
  
  public void setFitsSystemWindows(boolean paramBoolean) {
    super.setFitsSystemWindows(paramBoolean);
    setupForInsets();
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener) {
    this.mOnHierarchyChangeListener = paramOnHierarchyChangeListener;
  }
  
  public void setStatusBarBackground(@Nullable Drawable paramDrawable) {
    if (this.mStatusBarBackground != paramDrawable) {
      Drawable drawable1 = this.mStatusBarBackground;
      Drawable drawable2 = null;
      if (drawable1 != null)
        this.mStatusBarBackground.setCallback(null); 
      if (paramDrawable != null)
        drawable2 = paramDrawable.mutate(); 
      this.mStatusBarBackground = drawable2;
      if (this.mStatusBarBackground != null) {
        boolean bool;
        if (this.mStatusBarBackground.isStateful())
          this.mStatusBarBackground.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection((View)this));
        paramDrawable = this.mStatusBarBackground;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.mStatusBarBackground.setCallback((Drawable.Callback)this);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) {
    setStatusBarBackground((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setStatusBarBackgroundResource(@DrawableRes int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setStatusBarBackground(drawable);
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mStatusBarBackground != null && this.mStatusBarBackground.isVisible() != bool)
      this.mStatusBarBackground.setVisible(bool, false); 
  }
  
  final WindowInsetsCompat setWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = paramWindowInsetsCompat;
    if (!ObjectsCompat.equals(this.mLastInsets, paramWindowInsetsCompat)) {
      boolean bool2;
      this.mLastInsets = paramWindowInsetsCompat;
      boolean bool1 = true;
      if (paramWindowInsetsCompat != null && paramWindowInsetsCompat.getSystemWindowInsetTop() > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.mDrawStatusBarBackground = bool2;
      if (!this.mDrawStatusBarBackground && getBackground() == null) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      setWillNotDraw(bool2);
      windowInsetsCompat = dispatchApplyWindowInsetsToBehaviors(paramWindowInsetsCompat);
      requestLayout();
    } 
    return windowInsetsCompat;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mStatusBarBackground);
  }
  
  public static interface AttachedBehavior {
    @NonNull
    CoordinatorLayout.Behavior getBehavior();
  }
  
  public static abstract class Behavior<V extends View> {
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {}
    
    @Nullable
    public static Object getTag(@NonNull View param1View) {
      return ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag;
    }
    
    public static void setTag(@NonNull View param1View, @Nullable Object param1Object) {
      ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag = param1Object;
    }
    
    public boolean blocksInteractionBelow(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) {
      boolean bool;
      if (getScrimOpacity(param1CoordinatorLayout, param1V) > 0.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Rect param1Rect) {
      return false;
    }
    
    @ColorInt
    public int getScrimColor(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) {
      return -16777216;
    }
    
    @FloatRange(from = 0.0D, to = 1.0D)
    public float getScrimOpacity(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) {
      return 0.0F;
    }
    
    public boolean layoutDependsOn(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {
      return false;
    }
    
    @NonNull
    public WindowInsetsCompat onApplyWindowInsets(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull WindowInsetsCompat param1WindowInsetsCompat) {
      return param1WindowInsetsCompat;
    }
    
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams param1LayoutParams) {}
    
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {
      return false;
    }
    
    public void onDependentViewRemoved(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {}
    
    public void onDetachedFromLayoutParams() {}
    
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull MotionEvent param1MotionEvent) {
      return false;
    }
    
    public boolean onLayoutChild(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, int param1Int) {
      return false;
    }
    
    public boolean onMeasureChild(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return false;
    }
    
    public boolean onNestedFling(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, float param1Float1, float param1Float2, boolean param1Boolean) {
      return false;
    }
    
    public boolean onNestedPreFling(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, float param1Float1, float param1Float2) {
      return false;
    }
    
    @Deprecated
    public void onNestedPreScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, @NonNull int[] param1ArrayOfint) {}
    
    public void onNestedPreScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, @NonNull int[] param1ArrayOfint, int param1Int3) {
      if (param1Int3 == 0)
        onNestedPreScroll(param1CoordinatorLayout, param1V, param1View, param1Int1, param1Int2, param1ArrayOfint); 
    }
    
    @Deprecated
    public void onNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      if (param1Int5 == 0)
        onNestedScroll(param1CoordinatorLayout, param1V, param1View, param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    @Deprecated
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int) {}
    
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int1, int param1Int2) {
      if (param1Int2 == 0)
        onNestedScrollAccepted(param1CoordinatorLayout, param1V, param1View1, param1View2, param1Int1); 
    }
    
    public boolean onRequestChildRectangleOnScreen(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Rect param1Rect, boolean param1Boolean) {
      return false;
    }
    
    public void onRestoreInstanceState(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Parcelable param1Parcelable) {}
    
    @Nullable
    public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) {
      return (Parcelable)View.BaseSavedState.EMPTY_STATE;
    }
    
    @Deprecated
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int) {
      return false;
    }
    
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int1, int param1Int2) {
      return (param1Int2 == 0) ? onStartNestedScroll(param1CoordinatorLayout, param1V, param1View1, param1View2, param1Int1) : false;
    }
    
    @Deprecated
    public void onStopNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {}
    
    public void onStopNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int) {
      if (param1Int == 0)
        onStopNestedScroll(param1CoordinatorLayout, param1V, param1View); 
    }
    
    public boolean onTouchEvent(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull MotionEvent param1MotionEvent) {
      return false;
    }
  }
  
  @Deprecated
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface DefaultBehavior {
    Class<? extends CoordinatorLayout.Behavior> value();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DispatchChangeEvent {}
  
  private class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
    public void onChildViewAdded(View param1View1, View param1View2) {
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(param1View1, param1View2); 
    }
    
    public void onChildViewRemoved(View param1View1, View param1View2) {
      CoordinatorLayout.this.onChildViewsChanged(2);
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(param1View1, param1View2); 
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int anchorGravity = 0;
    
    public int dodgeInsetEdges = 0;
    
    public int gravity = 0;
    
    public int insetEdge = 0;
    
    public int keyline = -1;
    
    View mAnchorDirectChild;
    
    int mAnchorId = -1;
    
    View mAnchorView;
    
    CoordinatorLayout.Behavior mBehavior;
    
    boolean mBehaviorResolved = false;
    
    Object mBehaviorTag;
    
    private boolean mDidAcceptNestedScrollNonTouch;
    
    private boolean mDidAcceptNestedScrollTouch;
    
    private boolean mDidBlockInteraction;
    
    private boolean mDidChangeAfterNestedScroll;
    
    int mInsetOffsetX;
    
    int mInsetOffsetY;
    
    final Rect mLastChildRect = new Rect();
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    LayoutParams(@NonNull Context param1Context, @Nullable AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.CoordinatorLayout_Layout);
      this.gravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
      this.mAnchorId = typedArray.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
      this.anchorGravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
      this.keyline = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
      this.insetEdge = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
      this.dodgeInsetEdges = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
      this.mBehaviorResolved = typedArray.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
      if (this.mBehaviorResolved)
        this.mBehavior = CoordinatorLayout.parseBehavior(param1Context, param1AttributeSet, typedArray.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior)); 
      typedArray.recycle();
      if (this.mBehavior != null)
        this.mBehavior.onAttachedToLayoutParams(this); 
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    private void resolveAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      this.mAnchorView = param1CoordinatorLayout.findViewById(this.mAnchorId);
      if (this.mAnchorView != null) {
        if (this.mAnchorView == param1CoordinatorLayout) {
          if (param1CoordinatorLayout.isInEditMode()) {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
            return;
          } 
          throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
        } 
        View view = this.mAnchorView;
        for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != param1CoordinatorLayout && viewParent != null; viewParent = viewParent.getParent()) {
          if (viewParent == param1View) {
            if (param1CoordinatorLayout.isInEditMode()) {
              this.mAnchorDirectChild = null;
              this.mAnchorView = null;
              return;
            } 
            throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
          } 
          if (viewParent instanceof View)
            view = (View)viewParent; 
        } 
        this.mAnchorDirectChild = view;
        return;
      } 
      if (param1CoordinatorLayout.isInEditMode()) {
        this.mAnchorDirectChild = null;
        this.mAnchorView = null;
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not find CoordinatorLayout descendant view with id ");
      stringBuilder.append(param1CoordinatorLayout.getResources().getResourceName(this.mAnchorId));
      stringBuilder.append(" to anchor view ");
      stringBuilder.append(param1View);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    private boolean shouldDodge(View param1View, int param1Int) {
      boolean bool;
      int i = GravityCompat.getAbsoluteGravity(((LayoutParams)param1View.getLayoutParams()).insetEdge, param1Int);
      if (i != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, param1Int) & i) == i) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private boolean verifyAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      if (this.mAnchorView.getId() != this.mAnchorId)
        return false; 
      View view = this.mAnchorView;
      for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != param1CoordinatorLayout; viewParent = viewParent.getParent()) {
        if (viewParent == null || viewParent == param1View) {
          this.mAnchorDirectChild = null;
          this.mAnchorView = null;
          return false;
        } 
        if (viewParent instanceof View)
          view = (View)viewParent; 
      } 
      this.mAnchorDirectChild = view;
      return true;
    }
    
    boolean checkAnchorChanged() {
      boolean bool;
      if (this.mAnchorView == null && this.mAnchorId != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean dependsOn(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      return (param1View2 == this.mAnchorDirectChild || shouldDodge(param1View2, ViewCompat.getLayoutDirection((View)param1CoordinatorLayout)) || (this.mBehavior != null && this.mBehavior.layoutDependsOn(param1CoordinatorLayout, param1View1, param1View2)));
    }
    
    boolean didBlockInteraction() {
      if (this.mBehavior == null)
        this.mDidBlockInteraction = false; 
      return this.mDidBlockInteraction;
    }
    
    View findAnchorView(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      if (this.mAnchorId == -1) {
        this.mAnchorDirectChild = null;
        this.mAnchorView = null;
        return null;
      } 
      if (this.mAnchorView == null || !verifyAnchorView(param1View, param1CoordinatorLayout))
        resolveAnchorView(param1View, param1CoordinatorLayout); 
      return this.mAnchorView;
    }
    
    @IdRes
    public int getAnchorId() {
      return this.mAnchorId;
    }
    
    @Nullable
    public CoordinatorLayout.Behavior getBehavior() {
      return this.mBehavior;
    }
    
    boolean getChangedAfterNestedScroll() {
      return this.mDidChangeAfterNestedScroll;
    }
    
    Rect getLastChildRect() {
      return this.mLastChildRect;
    }
    
    void invalidateAnchor() {
      this.mAnchorDirectChild = null;
      this.mAnchorView = null;
    }
    
    boolean isBlockingInteractionBelow(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      boolean bool2;
      if (this.mDidBlockInteraction)
        return true; 
      boolean bool1 = this.mDidBlockInteraction;
      if (this.mBehavior != null) {
        bool2 = this.mBehavior.blocksInteractionBelow(param1CoordinatorLayout, param1View);
      } else {
        bool2 = false;
      } 
      bool2 |= bool1;
      this.mDidBlockInteraction = bool2;
      return bool2;
    }
    
    boolean isNestedScrollAccepted(int param1Int) {
      switch (param1Int) {
        default:
          return false;
        case 1:
          return this.mDidAcceptNestedScrollNonTouch;
        case 0:
          break;
      } 
      return this.mDidAcceptNestedScrollTouch;
    }
    
    void resetChangedAfterNestedScroll() {
      this.mDidChangeAfterNestedScroll = false;
    }
    
    void resetNestedScroll(int param1Int) {
      setNestedScrollAccepted(param1Int, false);
    }
    
    void resetTouchBehaviorTracking() {
      this.mDidBlockInteraction = false;
    }
    
    public void setAnchorId(@IdRes int param1Int) {
      invalidateAnchor();
      this.mAnchorId = param1Int;
    }
    
    public void setBehavior(@Nullable CoordinatorLayout.Behavior param1Behavior) {
      if (this.mBehavior != param1Behavior) {
        if (this.mBehavior != null)
          this.mBehavior.onDetachedFromLayoutParams(); 
        this.mBehavior = param1Behavior;
        this.mBehaviorTag = null;
        this.mBehaviorResolved = true;
        if (param1Behavior != null)
          param1Behavior.onAttachedToLayoutParams(this); 
      } 
    }
    
    void setChangedAfterNestedScroll(boolean param1Boolean) {
      this.mDidChangeAfterNestedScroll = param1Boolean;
    }
    
    void setLastChildRect(Rect param1Rect) {
      this.mLastChildRect.set(param1Rect);
    }
    
    void setNestedScrollAccepted(int param1Int, boolean param1Boolean) {
      switch (param1Int) {
        default:
          return;
        case 1:
          this.mDidAcceptNestedScrollNonTouch = param1Boolean;
        case 0:
          break;
      } 
      this.mDidAcceptNestedScrollTouch = param1Boolean;
    }
  }
  
  class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
    public boolean onPreDraw() {
      CoordinatorLayout.this.onChildViewsChanged(0);
      return true;
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public CoordinatorLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new CoordinatorLayout.SavedState(param2Parcel, null);
        }
        
        public CoordinatorLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new CoordinatorLayout.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public CoordinatorLayout.SavedState[] newArray(int param2Int) {
          return new CoordinatorLayout.SavedState[param2Int];
        }
      };
    
    SparseArray<Parcelable> behaviorStates;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      int i = param1Parcel.readInt();
      int[] arrayOfInt = new int[i];
      param1Parcel.readIntArray(arrayOfInt);
      Parcelable[] arrayOfParcelable = param1Parcel.readParcelableArray(param1ClassLoader);
      this.behaviorStates = new SparseArray(i);
      for (byte b = 0; b < i; b++)
        this.behaviorStates.append(arrayOfInt[b], arrayOfParcelable[b]); 
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      byte b2;
      super.writeToParcel(param1Parcel, param1Int);
      SparseArray<Parcelable> sparseArray = this.behaviorStates;
      byte b1 = 0;
      if (sparseArray != null) {
        b2 = this.behaviorStates.size();
      } else {
        b2 = 0;
      } 
      param1Parcel.writeInt(b2);
      int[] arrayOfInt = new int[b2];
      Parcelable[] arrayOfParcelable = new Parcelable[b2];
      while (b1 < b2) {
        arrayOfInt[b1] = this.behaviorStates.keyAt(b1);
        arrayOfParcelable[b1] = (Parcelable)this.behaviorStates.valueAt(b1);
        b1++;
      } 
      param1Parcel.writeIntArray(arrayOfInt);
      param1Parcel.writeParcelableArray(arrayOfParcelable, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public CoordinatorLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new CoordinatorLayout.SavedState(param1Parcel, null);
    }
    
    public CoordinatorLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new CoordinatorLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public CoordinatorLayout.SavedState[] newArray(int param1Int) {
      return new CoordinatorLayout.SavedState[param1Int];
    }
  }
  
  static class ViewElevationComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      float f1 = ViewCompat.getZ(param1View1);
      float f2 = ViewCompat.getZ(param1View2);
      return (f1 > f2) ? -1 : ((f1 < f2) ? 1 : 0);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\design\widget\CoordinatorLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */