package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class ContentFrameLayout extends FrameLayout {
  private OnAttachListener mAttachListener;
  
  private final Rect mDecorPadding = new Rect();
  
  private TypedValue mFixedHeightMajor;
  
  private TypedValue mFixedHeightMinor;
  
  private TypedValue mFixedWidthMajor;
  
  private TypedValue mFixedWidthMinor;
  
  private TypedValue mMinWidthMajor;
  
  private TypedValue mMinWidthMinor;
  
  public ContentFrameLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void dispatchFitSystemWindows(Rect paramRect) {
    fitSystemWindows(paramRect);
  }
  
  public TypedValue getFixedHeightMajor() {
    if (this.mFixedHeightMajor == null)
      this.mFixedHeightMajor = new TypedValue(); 
    return this.mFixedHeightMajor;
  }
  
  public TypedValue getFixedHeightMinor() {
    if (this.mFixedHeightMinor == null)
      this.mFixedHeightMinor = new TypedValue(); 
    return this.mFixedHeightMinor;
  }
  
  public TypedValue getFixedWidthMajor() {
    if (this.mFixedWidthMajor == null)
      this.mFixedWidthMajor = new TypedValue(); 
    return this.mFixedWidthMajor;
  }
  
  public TypedValue getFixedWidthMinor() {
    if (this.mFixedWidthMinor == null)
      this.mFixedWidthMinor = new TypedValue(); 
    return this.mFixedWidthMinor;
  }
  
  public TypedValue getMinWidthMajor() {
    if (this.mMinWidthMajor == null)
      this.mMinWidthMajor = new TypedValue(); 
    return this.mMinWidthMajor;
  }
  
  public TypedValue getMinWidthMinor() {
    if (this.mMinWidthMinor == null)
      this.mMinWidthMinor = new TypedValue(); 
    return this.mMinWidthMinor;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.mAttachListener != null)
      this.mAttachListener.onAttachedFromWindow(); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mAttachListener != null)
      this.mAttachListener.onDetachedFromWindow(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getContext : ()Landroid/content/Context;
    //   4: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   7: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   10: astore_3
    //   11: aload_3
    //   12: getfield widthPixels : I
    //   15: istore #4
    //   17: aload_3
    //   18: getfield heightPixels : I
    //   21: istore #5
    //   23: iconst_1
    //   24: istore #6
    //   26: iload #4
    //   28: iload #5
    //   30: if_icmpge -> 39
    //   33: iconst_1
    //   34: istore #4
    //   36: goto -> 42
    //   39: iconst_0
    //   40: istore #4
    //   42: iload_1
    //   43: invokestatic getMode : (I)I
    //   46: istore #7
    //   48: iload_2
    //   49: invokestatic getMode : (I)I
    //   52: istore #8
    //   54: iload #7
    //   56: ldc -2147483648
    //   58: if_icmpne -> 192
    //   61: iload #4
    //   63: ifeq -> 75
    //   66: aload_0
    //   67: getfield mFixedWidthMinor : Landroid/util/TypedValue;
    //   70: astore #9
    //   72: goto -> 81
    //   75: aload_0
    //   76: getfield mFixedWidthMajor : Landroid/util/TypedValue;
    //   79: astore #9
    //   81: aload #9
    //   83: ifnull -> 192
    //   86: aload #9
    //   88: getfield type : I
    //   91: ifeq -> 192
    //   94: aload #9
    //   96: getfield type : I
    //   99: iconst_5
    //   100: if_icmpne -> 115
    //   103: aload #9
    //   105: aload_3
    //   106: invokevirtual getDimension : (Landroid/util/DisplayMetrics;)F
    //   109: f2i
    //   110: istore #5
    //   112: goto -> 149
    //   115: aload #9
    //   117: getfield type : I
    //   120: bipush #6
    //   122: if_icmpne -> 146
    //   125: aload #9
    //   127: aload_3
    //   128: getfield widthPixels : I
    //   131: i2f
    //   132: aload_3
    //   133: getfield widthPixels : I
    //   136: i2f
    //   137: invokevirtual getFraction : (FF)F
    //   140: f2i
    //   141: istore #5
    //   143: goto -> 149
    //   146: iconst_0
    //   147: istore #5
    //   149: iload #5
    //   151: ifle -> 192
    //   154: iload #5
    //   156: aload_0
    //   157: getfield mDecorPadding : Landroid/graphics/Rect;
    //   160: getfield left : I
    //   163: aload_0
    //   164: getfield mDecorPadding : Landroid/graphics/Rect;
    //   167: getfield right : I
    //   170: iadd
    //   171: isub
    //   172: iload_1
    //   173: invokestatic getSize : (I)I
    //   176: invokestatic min : (II)I
    //   179: ldc 1073741824
    //   181: invokestatic makeMeasureSpec : (II)I
    //   184: istore #10
    //   186: iconst_1
    //   187: istore #11
    //   189: goto -> 198
    //   192: iconst_0
    //   193: istore #11
    //   195: iload_1
    //   196: istore #10
    //   198: iload_2
    //   199: istore #5
    //   201: iload #8
    //   203: ldc -2147483648
    //   205: if_icmpne -> 337
    //   208: iload #4
    //   210: ifeq -> 222
    //   213: aload_0
    //   214: getfield mFixedHeightMajor : Landroid/util/TypedValue;
    //   217: astore #9
    //   219: goto -> 228
    //   222: aload_0
    //   223: getfield mFixedHeightMinor : Landroid/util/TypedValue;
    //   226: astore #9
    //   228: iload_2
    //   229: istore #5
    //   231: aload #9
    //   233: ifnull -> 337
    //   236: iload_2
    //   237: istore #5
    //   239: aload #9
    //   241: getfield type : I
    //   244: ifeq -> 337
    //   247: aload #9
    //   249: getfield type : I
    //   252: iconst_5
    //   253: if_icmpne -> 267
    //   256: aload #9
    //   258: aload_3
    //   259: invokevirtual getDimension : (Landroid/util/DisplayMetrics;)F
    //   262: f2i
    //   263: istore_1
    //   264: goto -> 299
    //   267: aload #9
    //   269: getfield type : I
    //   272: bipush #6
    //   274: if_icmpne -> 297
    //   277: aload #9
    //   279: aload_3
    //   280: getfield heightPixels : I
    //   283: i2f
    //   284: aload_3
    //   285: getfield heightPixels : I
    //   288: i2f
    //   289: invokevirtual getFraction : (FF)F
    //   292: f2i
    //   293: istore_1
    //   294: goto -> 299
    //   297: iconst_0
    //   298: istore_1
    //   299: iload_2
    //   300: istore #5
    //   302: iload_1
    //   303: ifle -> 337
    //   306: iload_1
    //   307: aload_0
    //   308: getfield mDecorPadding : Landroid/graphics/Rect;
    //   311: getfield top : I
    //   314: aload_0
    //   315: getfield mDecorPadding : Landroid/graphics/Rect;
    //   318: getfield bottom : I
    //   321: iadd
    //   322: isub
    //   323: iload_2
    //   324: invokestatic getSize : (I)I
    //   327: invokestatic min : (II)I
    //   330: ldc 1073741824
    //   332: invokestatic makeMeasureSpec : (II)I
    //   335: istore #5
    //   337: aload_0
    //   338: iload #10
    //   340: iload #5
    //   342: invokespecial onMeasure : (II)V
    //   345: aload_0
    //   346: invokevirtual getMeasuredWidth : ()I
    //   349: istore #8
    //   351: iload #8
    //   353: ldc 1073741824
    //   355: invokestatic makeMeasureSpec : (II)I
    //   358: istore #10
    //   360: iload #11
    //   362: ifne -> 500
    //   365: iload #7
    //   367: ldc -2147483648
    //   369: if_icmpne -> 500
    //   372: iload #4
    //   374: ifeq -> 386
    //   377: aload_0
    //   378: getfield mMinWidthMinor : Landroid/util/TypedValue;
    //   381: astore #9
    //   383: goto -> 392
    //   386: aload_0
    //   387: getfield mMinWidthMajor : Landroid/util/TypedValue;
    //   390: astore #9
    //   392: aload #9
    //   394: ifnull -> 500
    //   397: aload #9
    //   399: getfield type : I
    //   402: ifeq -> 500
    //   405: aload #9
    //   407: getfield type : I
    //   410: iconst_5
    //   411: if_icmpne -> 425
    //   414: aload #9
    //   416: aload_3
    //   417: invokevirtual getDimension : (Landroid/util/DisplayMetrics;)F
    //   420: f2i
    //   421: istore_1
    //   422: goto -> 457
    //   425: aload #9
    //   427: getfield type : I
    //   430: bipush #6
    //   432: if_icmpne -> 455
    //   435: aload #9
    //   437: aload_3
    //   438: getfield widthPixels : I
    //   441: i2f
    //   442: aload_3
    //   443: getfield widthPixels : I
    //   446: i2f
    //   447: invokevirtual getFraction : (FF)F
    //   450: f2i
    //   451: istore_1
    //   452: goto -> 457
    //   455: iconst_0
    //   456: istore_1
    //   457: iload_1
    //   458: istore_2
    //   459: iload_1
    //   460: ifle -> 481
    //   463: iload_1
    //   464: aload_0
    //   465: getfield mDecorPadding : Landroid/graphics/Rect;
    //   468: getfield left : I
    //   471: aload_0
    //   472: getfield mDecorPadding : Landroid/graphics/Rect;
    //   475: getfield right : I
    //   478: iadd
    //   479: isub
    //   480: istore_2
    //   481: iload #8
    //   483: iload_2
    //   484: if_icmpge -> 500
    //   487: iload_2
    //   488: ldc 1073741824
    //   490: invokestatic makeMeasureSpec : (II)I
    //   493: istore_1
    //   494: iload #6
    //   496: istore_2
    //   497: goto -> 505
    //   500: iconst_0
    //   501: istore_2
    //   502: iload #10
    //   504: istore_1
    //   505: iload_2
    //   506: ifeq -> 516
    //   509: aload_0
    //   510: iload_1
    //   511: iload #5
    //   513: invokespecial onMeasure : (II)V
    //   516: return
  }
  
  public void setAttachListener(OnAttachListener paramOnAttachListener) {
    this.mAttachListener = paramOnAttachListener;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setDecorPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mDecorPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    if (ViewCompat.isLaidOut((View)this))
      requestLayout(); 
  }
  
  public static interface OnAttachListener {
    void onAttachedFromWindow();
    
    void onDetachedFromWindow();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\widget\ContentFrameLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */