package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        if (bool) {
          j = getPaddingLeft();
        } else {
          j = getWidth() - getPaddingRight() - this.mDividerWidth;
        } 
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          j = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } else {
          j = view.getRight() + layoutParams.rightMargin;
        } 
      } 
      drawVerticalDivider(paramCanvas, j);
    } 
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        j = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        j = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, j);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -2) : ((this.mOrientation == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    if (getChildCount() > this.mBaselineAlignedChildIndex) {
      View view = getChildAt(this.mBaselineAlignedChildIndex);
      int i = view.getBaseline();
      if (i == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      int j = this.mBaselineChildTop;
      int k = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        k = j;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              k = j;
            } else {
              k = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            k = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return k + ((LayoutParams)view.getLayoutParams()).topMargin + i;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) != 0)
        bool3 = true; 
      return bool3;
    } 
    if (paramInt == getChildCount()) {
      bool3 = bool1;
      if ((this.mShowDividers & 0x4) != 0)
        bool3 = true; 
      return bool3;
    } 
    if ((this.mShowDividers & 0x2) != 0) {
      paramInt--;
      while (true) {
        bool3 = bool2;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8) {
            bool3 = true;
            break;
          } 
          paramInt--;
          continue;
        } 
        break;
      } 
      return bool3;
    } 
    return false;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool1 = ViewUtils.isLayoutRtl((View)this);
    int i = getPaddingTop();
    int j = paramInt4 - paramInt2;
    int k = getPaddingBottom();
    int m = getPaddingBottom();
    int n = getVirtualChildCount();
    paramInt4 = this.mGravity;
    paramInt2 = this.mGravity & 0x70;
    boolean bool2 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    paramInt4 = GravityCompat.getAbsoluteGravity(paramInt4 & 0x800007, ViewCompat.getLayoutDirection((View)this));
    if (paramInt4 != 1) {
      if (paramInt4 != 5) {
        paramInt1 = getPaddingLeft();
      } else {
        paramInt1 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt4 = getPaddingLeft();
      paramInt1 = (paramInt3 - paramInt1 - this.mTotalLength) / 2 + paramInt4;
    } 
    if (bool1) {
      b1 = n - 1;
      b2 = -1;
    } else {
      b1 = 0;
      b2 = 1;
    } 
    paramInt4 = 0;
    paramInt3 = i;
    while (paramInt4 < n) {
      int i1 = b1 + b2 * paramInt4;
      View view = getVirtualChildAt(i1);
      if (view == null) {
        paramInt1 += measureNullChild(i1);
      } else if (view.getVisibility() != 8) {
        int i2 = view.getMeasuredWidth();
        int i3 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool2 && layoutParams.height != -1) {
          i4 = view.getBaseline();
        } else {
          i4 = -1;
        } 
        int i5 = layoutParams.gravity;
        int i6 = i5;
        if (i5 < 0)
          i6 = paramInt2; 
        i6 &= 0x70;
        if (i6 != 16) {
          if (i6 != 48) {
            if (i6 != 80) {
              i6 = paramInt3;
            } else {
              i5 = j - k - i3 - layoutParams.bottomMargin;
              i6 = i5;
              if (i4 != -1) {
                i6 = view.getMeasuredHeight();
                i6 = i5 - arrayOfInt2[2] - i6 - i4;
              } 
            } 
          } else {
            i6 = layoutParams.topMargin + paramInt3;
            if (i4 != -1)
              i6 += arrayOfInt1[1] - i4; 
          } 
        } else {
          i6 = (j - i - m - i3) / 2 + paramInt3 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        int i4 = paramInt1;
        if (hasDividerBeforeChildAt(i1))
          i4 = paramInt1 + this.mDividerWidth; 
        paramInt1 = layoutParams.leftMargin + i4;
        setChildFrame(view, paramInt1 + getLocationOffset(view), i6, i2, i3);
        i6 = layoutParams.rightMargin;
        i4 = getNextLocationOffset(view);
        paramInt4 += getChildrenSkipCount(view, i1);
        paramInt1 += i2 + i6 + i4;
      } 
      paramInt4++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    int n = getVirtualChildCount();
    paramInt1 = this.mGravity & 0x70;
    int i1 = this.mGravity;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop();
      paramInt1 = (paramInt4 - paramInt2 - this.mTotalLength) / 2 + paramInt1;
    } 
    paramInt2 = 0;
    while (paramInt2 < n) {
      View view = getVirtualChildAt(paramInt2);
      if (view == null) {
        paramInt3 = paramInt1 + measureNullChild(paramInt2);
        paramInt4 = paramInt2;
      } else {
        paramInt3 = paramInt1;
        paramInt4 = paramInt2;
        if (view.getVisibility() != 8) {
          int i2 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt4 = layoutParams.gravity;
          paramInt3 = paramInt4;
          if (paramInt4 < 0)
            paramInt3 = i1 & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + i;
            } else {
              paramInt3 = j - k - i2 - layoutParams.rightMargin;
            } 
          } else {
            paramInt3 = (j - i - m - i2) / 2 + i + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          paramInt4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            paramInt4 = paramInt1 + this.mDividerHeight; 
          paramInt1 = paramInt4 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i2, i3);
          paramInt3 = layoutParams.bottomMargin;
          i2 = getNextLocationOffset(view);
          paramInt4 = paramInt2 + getChildrenSkipCount(view, paramInt2);
          paramInt3 = paramInt1 + i3 + paramInt3 + i2;
        } 
      } 
      paramInt2 = paramInt4 + 1;
      paramInt1 = paramInt3;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: aload_0
    //   6: invokevirtual getVirtualChildCount : ()I
    //   9: istore_3
    //   10: iload_1
    //   11: invokestatic getMode : (I)I
    //   14: istore #4
    //   16: iload_2
    //   17: invokestatic getMode : (I)I
    //   20: istore #5
    //   22: aload_0
    //   23: getfield mMaxAscent : [I
    //   26: ifnull -> 36
    //   29: aload_0
    //   30: getfield mMaxDescent : [I
    //   33: ifnonnull -> 50
    //   36: aload_0
    //   37: iconst_4
    //   38: newarray int
    //   40: putfield mMaxAscent : [I
    //   43: aload_0
    //   44: iconst_4
    //   45: newarray int
    //   47: putfield mMaxDescent : [I
    //   50: aload_0
    //   51: getfield mMaxAscent : [I
    //   54: astore #6
    //   56: aload_0
    //   57: getfield mMaxDescent : [I
    //   60: astore #7
    //   62: aload #6
    //   64: iconst_3
    //   65: iconst_m1
    //   66: iastore
    //   67: aload #6
    //   69: iconst_2
    //   70: iconst_m1
    //   71: iastore
    //   72: aload #6
    //   74: iconst_1
    //   75: iconst_m1
    //   76: iastore
    //   77: aload #6
    //   79: iconst_0
    //   80: iconst_m1
    //   81: iastore
    //   82: aload #7
    //   84: iconst_3
    //   85: iconst_m1
    //   86: iastore
    //   87: aload #7
    //   89: iconst_2
    //   90: iconst_m1
    //   91: iastore
    //   92: aload #7
    //   94: iconst_1
    //   95: iconst_m1
    //   96: iastore
    //   97: aload #7
    //   99: iconst_0
    //   100: iconst_m1
    //   101: iastore
    //   102: aload_0
    //   103: getfield mBaselineAligned : Z
    //   106: istore #8
    //   108: aload_0
    //   109: getfield mUseLargestChild : Z
    //   112: istore #9
    //   114: iload #4
    //   116: ldc 1073741824
    //   118: if_icmpne -> 127
    //   121: iconst_1
    //   122: istore #10
    //   124: goto -> 130
    //   127: iconst_0
    //   128: istore #10
    //   130: fconst_0
    //   131: fstore #11
    //   133: iconst_0
    //   134: istore #12
    //   136: iconst_0
    //   137: istore #13
    //   139: iconst_0
    //   140: istore #14
    //   142: iconst_0
    //   143: istore #15
    //   145: iconst_0
    //   146: istore #16
    //   148: iconst_0
    //   149: istore #17
    //   151: iconst_0
    //   152: istore #18
    //   154: iconst_1
    //   155: istore #19
    //   157: iconst_0
    //   158: istore #20
    //   160: iload #12
    //   162: iload_3
    //   163: if_icmpge -> 855
    //   166: aload_0
    //   167: iload #12
    //   169: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   172: astore #21
    //   174: aload #21
    //   176: ifnonnull -> 197
    //   179: aload_0
    //   180: aload_0
    //   181: getfield mTotalLength : I
    //   184: aload_0
    //   185: iload #12
    //   187: invokevirtual measureNullChild : (I)I
    //   190: iadd
    //   191: putfield mTotalLength : I
    //   194: goto -> 849
    //   197: aload #21
    //   199: invokevirtual getVisibility : ()I
    //   202: bipush #8
    //   204: if_icmpne -> 223
    //   207: iload #12
    //   209: aload_0
    //   210: aload #21
    //   212: iload #12
    //   214: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   217: iadd
    //   218: istore #12
    //   220: goto -> 194
    //   223: aload_0
    //   224: iload #12
    //   226: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   229: ifeq -> 245
    //   232: aload_0
    //   233: aload_0
    //   234: getfield mTotalLength : I
    //   237: aload_0
    //   238: getfield mDividerWidth : I
    //   241: iadd
    //   242: putfield mTotalLength : I
    //   245: aload #21
    //   247: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   250: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   253: astore #22
    //   255: fload #11
    //   257: aload #22
    //   259: getfield weight : F
    //   262: fadd
    //   263: fstore #11
    //   265: iload #4
    //   267: ldc 1073741824
    //   269: if_icmpne -> 381
    //   272: aload #22
    //   274: getfield width : I
    //   277: ifne -> 381
    //   280: aload #22
    //   282: getfield weight : F
    //   285: fconst_0
    //   286: fcmpl
    //   287: ifle -> 381
    //   290: iload #10
    //   292: ifeq -> 318
    //   295: aload_0
    //   296: aload_0
    //   297: getfield mTotalLength : I
    //   300: aload #22
    //   302: getfield leftMargin : I
    //   305: aload #22
    //   307: getfield rightMargin : I
    //   310: iadd
    //   311: iadd
    //   312: putfield mTotalLength : I
    //   315: goto -> 347
    //   318: aload_0
    //   319: getfield mTotalLength : I
    //   322: istore #23
    //   324: aload_0
    //   325: iload #23
    //   327: aload #22
    //   329: getfield leftMargin : I
    //   332: iload #23
    //   334: iadd
    //   335: aload #22
    //   337: getfield rightMargin : I
    //   340: iadd
    //   341: invokestatic max : (II)I
    //   344: putfield mTotalLength : I
    //   347: iload #8
    //   349: ifeq -> 375
    //   352: iconst_0
    //   353: iconst_0
    //   354: invokestatic makeMeasureSpec : (II)I
    //   357: istore #23
    //   359: aload #21
    //   361: iload #23
    //   363: iload #23
    //   365: invokevirtual measure : (II)V
    //   368: iload #13
    //   370: istore #23
    //   372: goto -> 566
    //   375: iconst_1
    //   376: istore #15
    //   378: goto -> 570
    //   381: aload #22
    //   383: getfield width : I
    //   386: ifne -> 412
    //   389: aload #22
    //   391: getfield weight : F
    //   394: fconst_0
    //   395: fcmpl
    //   396: ifle -> 412
    //   399: aload #22
    //   401: bipush #-2
    //   403: putfield width : I
    //   406: iconst_0
    //   407: istore #23
    //   409: goto -> 417
    //   412: ldc_w -2147483648
    //   415: istore #23
    //   417: fload #11
    //   419: fconst_0
    //   420: fcmpl
    //   421: ifne -> 433
    //   424: aload_0
    //   425: getfield mTotalLength : I
    //   428: istore #24
    //   430: goto -> 436
    //   433: iconst_0
    //   434: istore #24
    //   436: aload_0
    //   437: aload #21
    //   439: iload #12
    //   441: iload_1
    //   442: iload #24
    //   444: iload_2
    //   445: iconst_0
    //   446: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   449: iload #23
    //   451: ldc_w -2147483648
    //   454: if_icmpeq -> 464
    //   457: aload #22
    //   459: iload #23
    //   461: putfield width : I
    //   464: aload #21
    //   466: invokevirtual getMeasuredWidth : ()I
    //   469: istore #24
    //   471: iload #10
    //   473: ifeq -> 509
    //   476: aload_0
    //   477: aload_0
    //   478: getfield mTotalLength : I
    //   481: aload #22
    //   483: getfield leftMargin : I
    //   486: iload #24
    //   488: iadd
    //   489: aload #22
    //   491: getfield rightMargin : I
    //   494: iadd
    //   495: aload_0
    //   496: aload #21
    //   498: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   501: iadd
    //   502: iadd
    //   503: putfield mTotalLength : I
    //   506: goto -> 548
    //   509: aload_0
    //   510: getfield mTotalLength : I
    //   513: istore #23
    //   515: aload_0
    //   516: iload #23
    //   518: iload #23
    //   520: iload #24
    //   522: iadd
    //   523: aload #22
    //   525: getfield leftMargin : I
    //   528: iadd
    //   529: aload #22
    //   531: getfield rightMargin : I
    //   534: iadd
    //   535: aload_0
    //   536: aload #21
    //   538: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   541: iadd
    //   542: invokestatic max : (II)I
    //   545: putfield mTotalLength : I
    //   548: iload #13
    //   550: istore #23
    //   552: iload #9
    //   554: ifeq -> 566
    //   557: iload #24
    //   559: iload #13
    //   561: invokestatic max : (II)I
    //   564: istore #23
    //   566: iload #23
    //   568: istore #13
    //   570: iload #12
    //   572: istore #25
    //   574: iload #5
    //   576: ldc 1073741824
    //   578: if_icmpeq -> 599
    //   581: aload #22
    //   583: getfield height : I
    //   586: iconst_m1
    //   587: if_icmpne -> 599
    //   590: iconst_1
    //   591: istore #12
    //   593: iconst_1
    //   594: istore #20
    //   596: goto -> 602
    //   599: iconst_0
    //   600: istore #12
    //   602: aload #22
    //   604: getfield topMargin : I
    //   607: aload #22
    //   609: getfield bottomMargin : I
    //   612: iadd
    //   613: istore #23
    //   615: aload #21
    //   617: invokevirtual getMeasuredHeight : ()I
    //   620: iload #23
    //   622: iadd
    //   623: istore #24
    //   625: iload #18
    //   627: aload #21
    //   629: invokevirtual getMeasuredState : ()I
    //   632: invokestatic combineMeasuredStates : (II)I
    //   635: istore #26
    //   637: iload #8
    //   639: ifeq -> 726
    //   642: aload #21
    //   644: invokevirtual getBaseline : ()I
    //   647: istore #27
    //   649: iload #27
    //   651: iconst_m1
    //   652: if_icmpeq -> 726
    //   655: aload #22
    //   657: getfield gravity : I
    //   660: ifge -> 672
    //   663: aload_0
    //   664: getfield mGravity : I
    //   667: istore #18
    //   669: goto -> 679
    //   672: aload #22
    //   674: getfield gravity : I
    //   677: istore #18
    //   679: iload #18
    //   681: bipush #112
    //   683: iand
    //   684: iconst_4
    //   685: ishr
    //   686: bipush #-2
    //   688: iand
    //   689: iconst_1
    //   690: ishr
    //   691: istore #18
    //   693: aload #6
    //   695: iload #18
    //   697: aload #6
    //   699: iload #18
    //   701: iaload
    //   702: iload #27
    //   704: invokestatic max : (II)I
    //   707: iastore
    //   708: aload #7
    //   710: iload #18
    //   712: aload #7
    //   714: iload #18
    //   716: iaload
    //   717: iload #24
    //   719: iload #27
    //   721: isub
    //   722: invokestatic max : (II)I
    //   725: iastore
    //   726: iload #14
    //   728: iload #24
    //   730: invokestatic max : (II)I
    //   733: istore #14
    //   735: iload #19
    //   737: ifeq -> 755
    //   740: aload #22
    //   742: getfield height : I
    //   745: iconst_m1
    //   746: if_icmpne -> 755
    //   749: iconst_1
    //   750: istore #19
    //   752: goto -> 758
    //   755: iconst_0
    //   756: istore #19
    //   758: aload #22
    //   760: getfield weight : F
    //   763: fconst_0
    //   764: fcmpl
    //   765: ifle -> 795
    //   768: iload #12
    //   770: ifeq -> 776
    //   773: goto -> 783
    //   776: iload #24
    //   778: istore #23
    //   780: goto -> 773
    //   783: iload #17
    //   785: iload #23
    //   787: invokestatic max : (II)I
    //   790: istore #12
    //   792: goto -> 820
    //   795: iload #12
    //   797: ifeq -> 804
    //   800: iload #23
    //   802: istore #24
    //   804: iload #16
    //   806: iload #24
    //   808: invokestatic max : (II)I
    //   811: istore #16
    //   813: iload #17
    //   815: istore #12
    //   817: goto -> 792
    //   820: aload_0
    //   821: aload #21
    //   823: iload #25
    //   825: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   828: istore #17
    //   830: iload #26
    //   832: istore #18
    //   834: iload #17
    //   836: iload #25
    //   838: iadd
    //   839: istore #23
    //   841: iload #12
    //   843: istore #17
    //   845: iload #23
    //   847: istore #12
    //   849: iinc #12, 1
    //   852: goto -> 160
    //   855: iload #14
    //   857: istore #12
    //   859: aload_0
    //   860: getfield mTotalLength : I
    //   863: ifle -> 887
    //   866: aload_0
    //   867: iload_3
    //   868: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   871: ifeq -> 887
    //   874: aload_0
    //   875: aload_0
    //   876: getfield mTotalLength : I
    //   879: aload_0
    //   880: getfield mDividerWidth : I
    //   883: iadd
    //   884: putfield mTotalLength : I
    //   887: aload #6
    //   889: iconst_1
    //   890: iaload
    //   891: iconst_m1
    //   892: if_icmpne -> 925
    //   895: aload #6
    //   897: iconst_0
    //   898: iaload
    //   899: iconst_m1
    //   900: if_icmpne -> 925
    //   903: aload #6
    //   905: iconst_2
    //   906: iaload
    //   907: iconst_m1
    //   908: if_icmpne -> 925
    //   911: aload #6
    //   913: iconst_3
    //   914: iaload
    //   915: iconst_m1
    //   916: if_icmpeq -> 922
    //   919: goto -> 925
    //   922: goto -> 983
    //   925: iload #12
    //   927: aload #6
    //   929: iconst_3
    //   930: iaload
    //   931: aload #6
    //   933: iconst_0
    //   934: iaload
    //   935: aload #6
    //   937: iconst_1
    //   938: iaload
    //   939: aload #6
    //   941: iconst_2
    //   942: iaload
    //   943: invokestatic max : (II)I
    //   946: invokestatic max : (II)I
    //   949: invokestatic max : (II)I
    //   952: aload #7
    //   954: iconst_3
    //   955: iaload
    //   956: aload #7
    //   958: iconst_0
    //   959: iaload
    //   960: aload #7
    //   962: iconst_1
    //   963: iaload
    //   964: aload #7
    //   966: iconst_2
    //   967: iaload
    //   968: invokestatic max : (II)I
    //   971: invokestatic max : (II)I
    //   974: invokestatic max : (II)I
    //   977: iadd
    //   978: invokestatic max : (II)I
    //   981: istore #12
    //   983: iload #18
    //   985: istore #14
    //   987: iload #12
    //   989: istore #23
    //   991: iload #9
    //   993: ifeq -> 1181
    //   996: iload #4
    //   998: ldc_w -2147483648
    //   1001: if_icmpeq -> 1013
    //   1004: iload #12
    //   1006: istore #23
    //   1008: iload #4
    //   1010: ifne -> 1181
    //   1013: aload_0
    //   1014: iconst_0
    //   1015: putfield mTotalLength : I
    //   1018: iconst_0
    //   1019: istore #18
    //   1021: iload #12
    //   1023: istore #23
    //   1025: iload #18
    //   1027: iload_3
    //   1028: if_icmpge -> 1181
    //   1031: aload_0
    //   1032: iload #18
    //   1034: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1037: astore #21
    //   1039: aload #21
    //   1041: ifnonnull -> 1062
    //   1044: aload_0
    //   1045: aload_0
    //   1046: getfield mTotalLength : I
    //   1049: aload_0
    //   1050: iload #18
    //   1052: invokevirtual measureNullChild : (I)I
    //   1055: iadd
    //   1056: putfield mTotalLength : I
    //   1059: goto -> 1085
    //   1062: aload #21
    //   1064: invokevirtual getVisibility : ()I
    //   1067: bipush #8
    //   1069: if_icmpne -> 1088
    //   1072: iload #18
    //   1074: aload_0
    //   1075: aload #21
    //   1077: iload #18
    //   1079: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1082: iadd
    //   1083: istore #18
    //   1085: goto -> 1175
    //   1088: aload #21
    //   1090: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1093: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1096: astore #22
    //   1098: iload #10
    //   1100: ifeq -> 1136
    //   1103: aload_0
    //   1104: aload_0
    //   1105: getfield mTotalLength : I
    //   1108: aload #22
    //   1110: getfield leftMargin : I
    //   1113: iload #13
    //   1115: iadd
    //   1116: aload #22
    //   1118: getfield rightMargin : I
    //   1121: iadd
    //   1122: aload_0
    //   1123: aload #21
    //   1125: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1128: iadd
    //   1129: iadd
    //   1130: putfield mTotalLength : I
    //   1133: goto -> 1085
    //   1136: aload_0
    //   1137: getfield mTotalLength : I
    //   1140: istore #23
    //   1142: aload_0
    //   1143: iload #23
    //   1145: iload #23
    //   1147: iload #13
    //   1149: iadd
    //   1150: aload #22
    //   1152: getfield leftMargin : I
    //   1155: iadd
    //   1156: aload #22
    //   1158: getfield rightMargin : I
    //   1161: iadd
    //   1162: aload_0
    //   1163: aload #21
    //   1165: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1168: iadd
    //   1169: invokestatic max : (II)I
    //   1172: putfield mTotalLength : I
    //   1175: iinc #18, 1
    //   1178: goto -> 1021
    //   1181: aload_0
    //   1182: aload_0
    //   1183: getfield mTotalLength : I
    //   1186: aload_0
    //   1187: invokevirtual getPaddingLeft : ()I
    //   1190: aload_0
    //   1191: invokevirtual getPaddingRight : ()I
    //   1194: iadd
    //   1195: iadd
    //   1196: putfield mTotalLength : I
    //   1199: aload_0
    //   1200: getfield mTotalLength : I
    //   1203: aload_0
    //   1204: invokevirtual getSuggestedMinimumWidth : ()I
    //   1207: invokestatic max : (II)I
    //   1210: iload_1
    //   1211: iconst_0
    //   1212: invokestatic resolveSizeAndState : (III)I
    //   1215: istore #25
    //   1217: ldc_w 16777215
    //   1220: iload #25
    //   1222: iand
    //   1223: aload_0
    //   1224: getfield mTotalLength : I
    //   1227: isub
    //   1228: istore #24
    //   1230: iload #15
    //   1232: ifne -> 1368
    //   1235: iload #24
    //   1237: ifeq -> 1250
    //   1240: fload #11
    //   1242: fconst_0
    //   1243: fcmpl
    //   1244: ifle -> 1250
    //   1247: goto -> 1368
    //   1250: iload #16
    //   1252: iload #17
    //   1254: invokestatic max : (II)I
    //   1257: istore #12
    //   1259: iload #9
    //   1261: ifeq -> 1350
    //   1264: iload #4
    //   1266: ldc 1073741824
    //   1268: if_icmpeq -> 1350
    //   1271: iconst_0
    //   1272: istore #16
    //   1274: iload #16
    //   1276: iload_3
    //   1277: if_icmpge -> 1350
    //   1280: aload_0
    //   1281: iload #16
    //   1283: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1286: astore #7
    //   1288: aload #7
    //   1290: ifnull -> 1344
    //   1293: aload #7
    //   1295: invokevirtual getVisibility : ()I
    //   1298: bipush #8
    //   1300: if_icmpne -> 1306
    //   1303: goto -> 1344
    //   1306: aload #7
    //   1308: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1311: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1314: getfield weight : F
    //   1317: fconst_0
    //   1318: fcmpl
    //   1319: ifle -> 1344
    //   1322: aload #7
    //   1324: iload #13
    //   1326: ldc 1073741824
    //   1328: invokestatic makeMeasureSpec : (II)I
    //   1331: aload #7
    //   1333: invokevirtual getMeasuredHeight : ()I
    //   1336: ldc 1073741824
    //   1338: invokestatic makeMeasureSpec : (II)I
    //   1341: invokevirtual measure : (II)V
    //   1344: iinc #16, 1
    //   1347: goto -> 1274
    //   1350: iload_3
    //   1351: istore #13
    //   1353: iload #12
    //   1355: istore #16
    //   1357: iload #19
    //   1359: istore #12
    //   1361: iload #23
    //   1363: istore #19
    //   1365: goto -> 2104
    //   1368: aload_0
    //   1369: getfield mWeightSum : F
    //   1372: fconst_0
    //   1373: fcmpl
    //   1374: ifle -> 1383
    //   1377: aload_0
    //   1378: getfield mWeightSum : F
    //   1381: fstore #11
    //   1383: aload #6
    //   1385: iconst_3
    //   1386: iconst_m1
    //   1387: iastore
    //   1388: aload #6
    //   1390: iconst_2
    //   1391: iconst_m1
    //   1392: iastore
    //   1393: aload #6
    //   1395: iconst_1
    //   1396: iconst_m1
    //   1397: iastore
    //   1398: aload #6
    //   1400: iconst_0
    //   1401: iconst_m1
    //   1402: iastore
    //   1403: aload #7
    //   1405: iconst_3
    //   1406: iconst_m1
    //   1407: iastore
    //   1408: aload #7
    //   1410: iconst_2
    //   1411: iconst_m1
    //   1412: iastore
    //   1413: aload #7
    //   1415: iconst_1
    //   1416: iconst_m1
    //   1417: iastore
    //   1418: aload #7
    //   1420: iconst_0
    //   1421: iconst_m1
    //   1422: iastore
    //   1423: aload_0
    //   1424: iconst_0
    //   1425: putfield mTotalLength : I
    //   1428: iconst_m1
    //   1429: istore #17
    //   1431: iconst_0
    //   1432: istore #15
    //   1434: iload #19
    //   1436: istore #12
    //   1438: iload_3
    //   1439: istore #13
    //   1441: iload #16
    //   1443: istore #18
    //   1445: iload #14
    //   1447: istore #19
    //   1449: iload #24
    //   1451: istore #16
    //   1453: iload #15
    //   1455: istore #14
    //   1457: iload #14
    //   1459: iload #13
    //   1461: if_icmpge -> 1970
    //   1464: aload_0
    //   1465: iload #14
    //   1467: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1470: astore #21
    //   1472: aload #21
    //   1474: ifnull -> 1964
    //   1477: aload #21
    //   1479: invokevirtual getVisibility : ()I
    //   1482: bipush #8
    //   1484: if_icmpne -> 1490
    //   1487: goto -> 1964
    //   1490: aload #21
    //   1492: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1495: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1498: astore #22
    //   1500: aload #22
    //   1502: getfield weight : F
    //   1505: fstore #28
    //   1507: fload #28
    //   1509: fconst_0
    //   1510: fcmpl
    //   1511: ifle -> 1674
    //   1514: iload #16
    //   1516: i2f
    //   1517: fload #28
    //   1519: fmul
    //   1520: fload #11
    //   1522: fdiv
    //   1523: f2i
    //   1524: istore #23
    //   1526: iload_2
    //   1527: aload_0
    //   1528: invokevirtual getPaddingTop : ()I
    //   1531: aload_0
    //   1532: invokevirtual getPaddingBottom : ()I
    //   1535: iadd
    //   1536: aload #22
    //   1538: getfield topMargin : I
    //   1541: iadd
    //   1542: aload #22
    //   1544: getfield bottomMargin : I
    //   1547: iadd
    //   1548: aload #22
    //   1550: getfield height : I
    //   1553: invokestatic getChildMeasureSpec : (III)I
    //   1556: istore #24
    //   1558: aload #22
    //   1560: getfield width : I
    //   1563: ifne -> 1608
    //   1566: iload #4
    //   1568: ldc 1073741824
    //   1570: if_icmpeq -> 1576
    //   1573: goto -> 1608
    //   1576: iload #23
    //   1578: ifle -> 1588
    //   1581: iload #23
    //   1583: istore #15
    //   1585: goto -> 1591
    //   1588: iconst_0
    //   1589: istore #15
    //   1591: aload #21
    //   1593: iload #15
    //   1595: ldc 1073741824
    //   1597: invokestatic makeMeasureSpec : (II)I
    //   1600: iload #24
    //   1602: invokevirtual measure : (II)V
    //   1605: goto -> 1641
    //   1608: aload #21
    //   1610: invokevirtual getMeasuredWidth : ()I
    //   1613: iload #23
    //   1615: iadd
    //   1616: istore_3
    //   1617: iload_3
    //   1618: istore #15
    //   1620: iload_3
    //   1621: ifge -> 1627
    //   1624: iconst_0
    //   1625: istore #15
    //   1627: aload #21
    //   1629: iload #15
    //   1631: ldc 1073741824
    //   1633: invokestatic makeMeasureSpec : (II)I
    //   1636: iload #24
    //   1638: invokevirtual measure : (II)V
    //   1641: iload #19
    //   1643: aload #21
    //   1645: invokevirtual getMeasuredState : ()I
    //   1648: ldc_w -16777216
    //   1651: iand
    //   1652: invokestatic combineMeasuredStates : (II)I
    //   1655: istore #19
    //   1657: fload #11
    //   1659: fload #28
    //   1661: fsub
    //   1662: fstore #11
    //   1664: iload #16
    //   1666: iload #23
    //   1668: isub
    //   1669: istore #16
    //   1671: goto -> 1674
    //   1674: iload #10
    //   1676: ifeq -> 1715
    //   1679: aload_0
    //   1680: aload_0
    //   1681: getfield mTotalLength : I
    //   1684: aload #21
    //   1686: invokevirtual getMeasuredWidth : ()I
    //   1689: aload #22
    //   1691: getfield leftMargin : I
    //   1694: iadd
    //   1695: aload #22
    //   1697: getfield rightMargin : I
    //   1700: iadd
    //   1701: aload_0
    //   1702: aload #21
    //   1704: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1707: iadd
    //   1708: iadd
    //   1709: putfield mTotalLength : I
    //   1712: goto -> 1760
    //   1715: aload_0
    //   1716: getfield mTotalLength : I
    //   1719: istore #15
    //   1721: aload_0
    //   1722: iload #15
    //   1724: aload #21
    //   1726: invokevirtual getMeasuredWidth : ()I
    //   1729: iload #15
    //   1731: iadd
    //   1732: aload #22
    //   1734: getfield leftMargin : I
    //   1737: iadd
    //   1738: aload #22
    //   1740: getfield rightMargin : I
    //   1743: iadd
    //   1744: aload_0
    //   1745: aload #21
    //   1747: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1750: iadd
    //   1751: invokestatic max : (II)I
    //   1754: putfield mTotalLength : I
    //   1757: goto -> 1712
    //   1760: iload #5
    //   1762: ldc 1073741824
    //   1764: if_icmpeq -> 1782
    //   1767: aload #22
    //   1769: getfield height : I
    //   1772: iconst_m1
    //   1773: if_icmpne -> 1782
    //   1776: iconst_1
    //   1777: istore #15
    //   1779: goto -> 1785
    //   1782: iconst_0
    //   1783: istore #15
    //   1785: aload #22
    //   1787: getfield topMargin : I
    //   1790: aload #22
    //   1792: getfield bottomMargin : I
    //   1795: iadd
    //   1796: istore #24
    //   1798: aload #21
    //   1800: invokevirtual getMeasuredHeight : ()I
    //   1803: iload #24
    //   1805: iadd
    //   1806: istore_3
    //   1807: iload #17
    //   1809: iload_3
    //   1810: invokestatic max : (II)I
    //   1813: istore #23
    //   1815: iload #15
    //   1817: ifeq -> 1827
    //   1820: iload #24
    //   1822: istore #17
    //   1824: goto -> 1830
    //   1827: iload_3
    //   1828: istore #17
    //   1830: iload #18
    //   1832: iload #17
    //   1834: invokestatic max : (II)I
    //   1837: istore #17
    //   1839: iload #12
    //   1841: ifeq -> 1859
    //   1844: aload #22
    //   1846: getfield height : I
    //   1849: iconst_m1
    //   1850: if_icmpne -> 1859
    //   1853: iconst_1
    //   1854: istore #12
    //   1856: goto -> 1862
    //   1859: iconst_0
    //   1860: istore #12
    //   1862: iload #8
    //   1864: ifeq -> 1953
    //   1867: aload #21
    //   1869: invokevirtual getBaseline : ()I
    //   1872: istore #15
    //   1874: iload #15
    //   1876: iconst_m1
    //   1877: if_icmpeq -> 1953
    //   1880: aload #22
    //   1882: getfield gravity : I
    //   1885: ifge -> 1897
    //   1888: aload_0
    //   1889: getfield mGravity : I
    //   1892: istore #18
    //   1894: goto -> 1904
    //   1897: aload #22
    //   1899: getfield gravity : I
    //   1902: istore #18
    //   1904: iload #18
    //   1906: bipush #112
    //   1908: iand
    //   1909: iconst_4
    //   1910: ishr
    //   1911: bipush #-2
    //   1913: iand
    //   1914: iconst_1
    //   1915: ishr
    //   1916: istore #18
    //   1918: aload #6
    //   1920: iload #18
    //   1922: aload #6
    //   1924: iload #18
    //   1926: iaload
    //   1927: iload #15
    //   1929: invokestatic max : (II)I
    //   1932: iastore
    //   1933: aload #7
    //   1935: iload #18
    //   1937: aload #7
    //   1939: iload #18
    //   1941: iaload
    //   1942: iload_3
    //   1943: iload #15
    //   1945: isub
    //   1946: invokestatic max : (II)I
    //   1949: iastore
    //   1950: goto -> 1953
    //   1953: iload #17
    //   1955: istore #18
    //   1957: iload #23
    //   1959: istore #17
    //   1961: goto -> 1964
    //   1964: iinc #14, 1
    //   1967: goto -> 1457
    //   1970: aload_0
    //   1971: aload_0
    //   1972: getfield mTotalLength : I
    //   1975: aload_0
    //   1976: invokevirtual getPaddingLeft : ()I
    //   1979: aload_0
    //   1980: invokevirtual getPaddingRight : ()I
    //   1983: iadd
    //   1984: iadd
    //   1985: putfield mTotalLength : I
    //   1988: aload #6
    //   1990: iconst_1
    //   1991: iaload
    //   1992: iconst_m1
    //   1993: if_icmpne -> 2030
    //   1996: aload #6
    //   1998: iconst_0
    //   1999: iaload
    //   2000: iconst_m1
    //   2001: if_icmpne -> 2030
    //   2004: aload #6
    //   2006: iconst_2
    //   2007: iaload
    //   2008: iconst_m1
    //   2009: if_icmpne -> 2030
    //   2012: aload #6
    //   2014: iconst_3
    //   2015: iaload
    //   2016: iconst_m1
    //   2017: if_icmpeq -> 2023
    //   2020: goto -> 2030
    //   2023: iload #17
    //   2025: istore #16
    //   2027: goto -> 2088
    //   2030: iload #17
    //   2032: aload #6
    //   2034: iconst_3
    //   2035: iaload
    //   2036: aload #6
    //   2038: iconst_0
    //   2039: iaload
    //   2040: aload #6
    //   2042: iconst_1
    //   2043: iaload
    //   2044: aload #6
    //   2046: iconst_2
    //   2047: iaload
    //   2048: invokestatic max : (II)I
    //   2051: invokestatic max : (II)I
    //   2054: invokestatic max : (II)I
    //   2057: aload #7
    //   2059: iconst_3
    //   2060: iaload
    //   2061: aload #7
    //   2063: iconst_0
    //   2064: iaload
    //   2065: aload #7
    //   2067: iconst_1
    //   2068: iaload
    //   2069: aload #7
    //   2071: iconst_2
    //   2072: iaload
    //   2073: invokestatic max : (II)I
    //   2076: invokestatic max : (II)I
    //   2079: invokestatic max : (II)I
    //   2082: iadd
    //   2083: invokestatic max : (II)I
    //   2086: istore #16
    //   2088: iload #16
    //   2090: istore #17
    //   2092: iload #19
    //   2094: istore #14
    //   2096: iload #18
    //   2098: istore #16
    //   2100: iload #17
    //   2102: istore #19
    //   2104: iload #19
    //   2106: istore #18
    //   2108: iload #12
    //   2110: ifne -> 2128
    //   2113: iload #19
    //   2115: istore #18
    //   2117: iload #5
    //   2119: ldc 1073741824
    //   2121: if_icmpeq -> 2128
    //   2124: iload #16
    //   2126: istore #18
    //   2128: aload_0
    //   2129: iload #25
    //   2131: iload #14
    //   2133: ldc_w -16777216
    //   2136: iand
    //   2137: ior
    //   2138: iload #18
    //   2140: aload_0
    //   2141: invokevirtual getPaddingTop : ()I
    //   2144: aload_0
    //   2145: invokevirtual getPaddingBottom : ()I
    //   2148: iadd
    //   2149: iadd
    //   2150: aload_0
    //   2151: invokevirtual getSuggestedMinimumHeight : ()I
    //   2154: invokestatic max : (II)I
    //   2157: iload_2
    //   2158: iload #14
    //   2160: bipush #16
    //   2162: ishl
    //   2163: invokestatic resolveSizeAndState : (III)I
    //   2166: invokevirtual setMeasuredDimension : (II)V
    //   2169: iload #20
    //   2171: ifeq -> 2181
    //   2174: aload_0
    //   2175: iload #13
    //   2177: iload_1
    //   2178: invokespecial forceUniformHeight : (II)V
    //   2181: return
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i = getVirtualChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    float f = 0.0F;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 1;
    boolean bool1 = false;
    while (i5 < i) {
      View view = getVirtualChildAt(i5);
      if (view == null) {
        this.mTotalLength += measureNullChild(i5);
      } else if (view.getVisibility() == 8) {
        i5 += getChildrenSkipCount(view, i5);
      } else {
        if (hasDividerBeforeChildAt(i5))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (k == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i6 = this.mTotalLength;
          this.mTotalLength = Math.max(i6, layoutParams.topMargin + i6 + layoutParams.bottomMargin);
          i6 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i10 = 0;
          } else {
            i10 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i11 = this.mTotalLength;
          } else {
            i11 = 0;
          } 
          measureChildBeforeLayout(view, i5, paramInt1, 0, paramInt2, i11);
          if (i10 != Integer.MIN_VALUE)
            layoutParams.height = i10; 
          int i11 = view.getMeasuredHeight();
          int i10 = this.mTotalLength;
          this.mTotalLength = Math.max(i10, i10 + i11 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          if (bool)
            i2 = Math.max(i11, i2); 
        } 
        int i9 = i5;
        if (m >= 0 && m == i9 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i9 >= m || layoutParams.weight <= 0.0F) {
          if (j != 1073741824 && layoutParams.width == -1) {
            i5 = 1;
            bool1 = true;
          } else {
            i5 = 0;
          } 
          int i10 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i11 = view.getMeasuredWidth() + i10;
          int i12 = Math.max(i1, i11);
          int i13 = View.combineMeasuredStates(n, view.getMeasuredState());
          if (i7 && layoutParams.width == -1) {
            n = 1;
          } else {
            n = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i5 == 0)
              i10 = i11; 
            i7 = Math.max(i3, i10);
            i3 = i4;
          } else {
            if (i5 == 0)
              i10 = i11; 
            i4 = Math.max(i4, i10);
            i7 = i3;
            i3 = i4;
          } 
          i5 = getChildrenSkipCount(view, i9);
          i4 = n;
          i1 = i7;
          n = i13;
          i5 += i9;
          i10 = i12;
          i7 = i4;
          i4 = i3;
          i3 = i1;
          i1 = i10;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i5++;
    } 
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i))
      this.mTotalLength += this.mDividerHeight; 
    if (bool) {
      i5 = k;
      if (i5 == Integer.MIN_VALUE || i5 == 0) {
        this.mTotalLength = 0;
        for (i5 = 0; i5 < i; i5++) {
          View view = getVirtualChildAt(i5);
          if (view == null) {
            this.mTotalLength += measureNullChild(i5);
          } else if (view.getVisibility() == 8) {
            i5 += getChildrenSkipCount(view, i5);
          } else {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int i9 = this.mTotalLength;
            this.mTotalLength = Math.max(i9, i9 + i2 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          } 
        } 
      } 
    } 
    i5 = k;
    this.mTotalLength += getPaddingTop() + getPaddingBottom();
    int i8 = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), paramInt2, 0);
    k = (0xFFFFFF & i8) - this.mTotalLength;
    if (i6 != 0 || (k != 0 && f > 0.0F)) {
      if (this.mWeightSum > 0.0F)
        f = this.mWeightSum; 
      this.mTotalLength = 0;
      i6 = 0;
      i3 = i4;
      i2 = i5;
      for (i5 = i6; i5 < i; i5++) {
        View view = getVirtualChildAt(i5);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          float f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i6 = (int)(k * f1 / f);
            int i10 = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width);
            if (layoutParams.height != 0 || i2 != 1073741824) {
              int i11 = view.getMeasuredHeight() + i6;
              i4 = i11;
              if (i11 < 0)
                i4 = 0; 
              view.measure(i10, View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
            } else {
              if (i6 > 0) {
                i4 = i6;
              } else {
                i4 = 0;
              } 
              view.measure(i10, View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
            } 
            n = View.combineMeasuredStates(n, view.getMeasuredState() & 0xFFFFFF00);
            k -= i6;
            f -= f1;
          } 
          int i9 = layoutParams.leftMargin + layoutParams.rightMargin;
          i6 = view.getMeasuredWidth() + i9;
          i1 = Math.max(i1, i6);
          if (j != 1073741824 && layoutParams.width == -1) {
            i4 = 1;
          } else {
            i4 = 0;
          } 
          if (i4 != 0) {
            i4 = i9;
          } else {
            i4 = i6;
          } 
          i4 = Math.max(i3, i4);
          if (i7 != 0 && layoutParams.width == -1) {
            i3 = 1;
          } else {
            i3 = 0;
          } 
          i7 = this.mTotalLength;
          this.mTotalLength = Math.max(i7, i7 + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          i7 = i3;
          i3 = i4;
        } 
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
      k = i3;
      i3 = n;
      n = k;
    } else {
      k = Math.max(i4, i3);
      if (bool && i5 != 1073741824)
        for (i3 = 0; i3 < i; i3++) {
          View view = getVirtualChildAt(i3);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824)); 
        }  
      i3 = n;
      n = k;
    } 
    if (i7 != 0 || j == 1073741824)
      n = i1; 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(n + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, i3), i8);
    if (bool1)
      forceUniformWidth(i, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    } 
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    if ((0x800007 & this.mGravity) != paramInt) {
      this.mGravity = paramInt | this.mGravity & 0xFF7FFFF8;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    if ((this.mGravity & 0x70) != paramInt) {
      this.mGravity = paramInt | this.mGravity & 0xFFFFFF8F;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface OrientationMode {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */