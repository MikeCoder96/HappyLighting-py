package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
  public static final int ARROW_DIRECTION_END = 3;
  
  public static final int ARROW_DIRECTION_LEFT = 0;
  
  public static final int ARROW_DIRECTION_RIGHT = 1;
  
  public static final int ARROW_DIRECTION_START = 2;
  
  private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
  
  private float mArrowHeadLength;
  
  private float mArrowShaftLength;
  
  private float mBarGap;
  
  private float mBarLength;
  
  private int mDirection = 2;
  
  private float mMaxCutForBarSize;
  
  private final Paint mPaint = new Paint();
  
  private final Path mPath = new Path();
  
  private float mProgress;
  
  private final int mSize;
  
  private boolean mSpin;
  
  private boolean mVerticalMirror = false;
  
  public DrawerArrowDrawable(Context paramContext) {
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mPaint.setStrokeJoin(Paint.Join.MITER);
    this.mPaint.setStrokeCap(Paint.Cap.BUTT);
    this.mPaint.setAntiAlias(true);
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
    setColor(typedArray.getColor(R.styleable.DrawerArrowToggle_color, 0));
    setBarThickness(typedArray.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
    setSpinEnabled(typedArray.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
    setGapSize(Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
    this.mSize = typedArray.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
    this.mBarLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
    this.mArrowHeadLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
    this.mArrowShaftLength = typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
    typedArray.recycle();
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return paramFloat1 + (paramFloat2 - paramFloat1) * paramFloat3;
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getBounds : ()Landroid/graphics/Rect;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield mDirection : I
    //   9: istore_3
    //   10: iconst_0
    //   11: istore #4
    //   13: iconst_1
    //   14: istore #5
    //   16: iload_3
    //   17: iconst_3
    //   18: if_icmpeq -> 66
    //   21: iload #4
    //   23: istore #6
    //   25: iload_3
    //   26: tableswitch default -> 48, 0 -> 80, 1 -> 60
    //   48: iload #4
    //   50: istore #6
    //   52: aload_0
    //   53: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   56: iconst_1
    //   57: if_icmpne -> 80
    //   60: iconst_1
    //   61: istore #6
    //   63: goto -> 80
    //   66: iload #4
    //   68: istore #6
    //   70: aload_0
    //   71: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   74: ifne -> 80
    //   77: goto -> 60
    //   80: aload_0
    //   81: getfield mArrowHeadLength : F
    //   84: aload_0
    //   85: getfield mArrowHeadLength : F
    //   88: fmul
    //   89: fconst_2
    //   90: fmul
    //   91: f2d
    //   92: invokestatic sqrt : (D)D
    //   95: d2f
    //   96: fstore #7
    //   98: aload_0
    //   99: getfield mBarLength : F
    //   102: fload #7
    //   104: aload_0
    //   105: getfield mProgress : F
    //   108: invokestatic lerp : (FFF)F
    //   111: fstore #8
    //   113: aload_0
    //   114: getfield mBarLength : F
    //   117: aload_0
    //   118: getfield mArrowShaftLength : F
    //   121: aload_0
    //   122: getfield mProgress : F
    //   125: invokestatic lerp : (FFF)F
    //   128: fstore #9
    //   130: fconst_0
    //   131: aload_0
    //   132: getfield mMaxCutForBarSize : F
    //   135: aload_0
    //   136: getfield mProgress : F
    //   139: invokestatic lerp : (FFF)F
    //   142: invokestatic round : (F)I
    //   145: i2f
    //   146: fstore #10
    //   148: fconst_0
    //   149: getstatic android/support/v7/graphics/drawable/DrawerArrowDrawable.ARROW_HEAD_ANGLE : F
    //   152: aload_0
    //   153: getfield mProgress : F
    //   156: invokestatic lerp : (FFF)F
    //   159: fstore #11
    //   161: iload #6
    //   163: ifeq -> 172
    //   166: fconst_0
    //   167: fstore #7
    //   169: goto -> 176
    //   172: ldc -180.0
    //   174: fstore #7
    //   176: iload #6
    //   178: ifeq -> 188
    //   181: ldc 180.0
    //   183: fstore #12
    //   185: goto -> 191
    //   188: fconst_0
    //   189: fstore #12
    //   191: fload #7
    //   193: fload #12
    //   195: aload_0
    //   196: getfield mProgress : F
    //   199: invokestatic lerp : (FFF)F
    //   202: fstore #7
    //   204: fload #8
    //   206: f2d
    //   207: dstore #13
    //   209: fload #11
    //   211: f2d
    //   212: dstore #15
    //   214: dload #15
    //   216: invokestatic cos : (D)D
    //   219: dstore #17
    //   221: dload #13
    //   223: invokestatic isNaN : (D)Z
    //   226: pop
    //   227: dload #17
    //   229: dload #13
    //   231: dmul
    //   232: invokestatic round : (D)J
    //   235: l2f
    //   236: fstore #19
    //   238: dload #15
    //   240: invokestatic sin : (D)D
    //   243: dstore #15
    //   245: dload #13
    //   247: invokestatic isNaN : (D)Z
    //   250: pop
    //   251: dload #13
    //   253: dload #15
    //   255: dmul
    //   256: invokestatic round : (D)J
    //   259: l2f
    //   260: fstore #8
    //   262: aload_0
    //   263: getfield mPath : Landroid/graphics/Path;
    //   266: invokevirtual rewind : ()V
    //   269: aload_0
    //   270: getfield mBarGap : F
    //   273: aload_0
    //   274: getfield mPaint : Landroid/graphics/Paint;
    //   277: invokevirtual getStrokeWidth : ()F
    //   280: fadd
    //   281: aload_0
    //   282: getfield mMaxCutForBarSize : F
    //   285: fneg
    //   286: aload_0
    //   287: getfield mProgress : F
    //   290: invokestatic lerp : (FFF)F
    //   293: fstore #12
    //   295: fload #9
    //   297: fneg
    //   298: fconst_2
    //   299: fdiv
    //   300: fstore #11
    //   302: aload_0
    //   303: getfield mPath : Landroid/graphics/Path;
    //   306: fload #11
    //   308: fload #10
    //   310: fadd
    //   311: fconst_0
    //   312: invokevirtual moveTo : (FF)V
    //   315: aload_0
    //   316: getfield mPath : Landroid/graphics/Path;
    //   319: fload #9
    //   321: fload #10
    //   323: fconst_2
    //   324: fmul
    //   325: fsub
    //   326: fconst_0
    //   327: invokevirtual rLineTo : (FF)V
    //   330: aload_0
    //   331: getfield mPath : Landroid/graphics/Path;
    //   334: fload #11
    //   336: fload #12
    //   338: invokevirtual moveTo : (FF)V
    //   341: aload_0
    //   342: getfield mPath : Landroid/graphics/Path;
    //   345: fload #19
    //   347: fload #8
    //   349: invokevirtual rLineTo : (FF)V
    //   352: aload_0
    //   353: getfield mPath : Landroid/graphics/Path;
    //   356: fload #11
    //   358: fload #12
    //   360: fneg
    //   361: invokevirtual moveTo : (FF)V
    //   364: aload_0
    //   365: getfield mPath : Landroid/graphics/Path;
    //   368: fload #19
    //   370: fload #8
    //   372: fneg
    //   373: invokevirtual rLineTo : (FF)V
    //   376: aload_0
    //   377: getfield mPath : Landroid/graphics/Path;
    //   380: invokevirtual close : ()V
    //   383: aload_1
    //   384: invokevirtual save : ()I
    //   387: pop
    //   388: aload_0
    //   389: getfield mPaint : Landroid/graphics/Paint;
    //   392: invokevirtual getStrokeWidth : ()F
    //   395: fstore #9
    //   397: aload_2
    //   398: invokevirtual height : ()I
    //   401: i2f
    //   402: ldc_w 3.0
    //   405: fload #9
    //   407: fmul
    //   408: fsub
    //   409: aload_0
    //   410: getfield mBarGap : F
    //   413: fconst_2
    //   414: fmul
    //   415: fsub
    //   416: f2i
    //   417: iconst_4
    //   418: idiv
    //   419: iconst_2
    //   420: imul
    //   421: i2f
    //   422: fstore #10
    //   424: aload_0
    //   425: getfield mBarGap : F
    //   428: fstore #12
    //   430: aload_1
    //   431: aload_2
    //   432: invokevirtual centerX : ()I
    //   435: i2f
    //   436: fload #10
    //   438: fload #9
    //   440: ldc_w 1.5
    //   443: fmul
    //   444: fload #12
    //   446: fadd
    //   447: fadd
    //   448: invokevirtual translate : (FF)V
    //   451: aload_0
    //   452: getfield mSpin : Z
    //   455: ifeq -> 484
    //   458: aload_0
    //   459: getfield mVerticalMirror : Z
    //   462: iload #6
    //   464: ixor
    //   465: ifeq -> 471
    //   468: iconst_m1
    //   469: istore #5
    //   471: aload_1
    //   472: fload #7
    //   474: iload #5
    //   476: i2f
    //   477: fmul
    //   478: invokevirtual rotate : (F)V
    //   481: goto -> 495
    //   484: iload #6
    //   486: ifeq -> 495
    //   489: aload_1
    //   490: ldc 180.0
    //   492: invokevirtual rotate : (F)V
    //   495: aload_1
    //   496: aload_0
    //   497: getfield mPath : Landroid/graphics/Path;
    //   500: aload_0
    //   501: getfield mPaint : Landroid/graphics/Paint;
    //   504: invokevirtual drawPath : (Landroid/graphics/Path;Landroid/graphics/Paint;)V
    //   507: aload_1
    //   508: invokevirtual restore : ()V
    //   511: return
  }
  
  public float getArrowHeadLength() {
    return this.mArrowHeadLength;
  }
  
  public float getArrowShaftLength() {
    return this.mArrowShaftLength;
  }
  
  public float getBarLength() {
    return this.mBarLength;
  }
  
  public float getBarThickness() {
    return this.mPaint.getStrokeWidth();
  }
  
  @ColorInt
  public int getColor() {
    return this.mPaint.getColor();
  }
  
  public int getDirection() {
    return this.mDirection;
  }
  
  public float getGapSize() {
    return this.mBarGap;
  }
  
  public int getIntrinsicHeight() {
    return this.mSize;
  }
  
  public int getIntrinsicWidth() {
    return this.mSize;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public final Paint getPaint() {
    return this.mPaint;
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getProgress() {
    return this.mProgress;
  }
  
  public boolean isSpinEnabled() {
    return this.mSpin;
  }
  
  public void setAlpha(int paramInt) {
    if (paramInt != this.mPaint.getAlpha()) {
      this.mPaint.setAlpha(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setArrowHeadLength(float paramFloat) {
    if (this.mArrowHeadLength != paramFloat) {
      this.mArrowHeadLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setArrowShaftLength(float paramFloat) {
    if (this.mArrowShaftLength != paramFloat) {
      this.mArrowShaftLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarLength(float paramFloat) {
    if (this.mBarLength != paramFloat) {
      this.mBarLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarThickness(float paramFloat) {
    if (this.mPaint.getStrokeWidth() != paramFloat) {
      this.mPaint.setStrokeWidth(paramFloat);
      double d1 = (paramFloat / 2.0F);
      double d2 = Math.cos(ARROW_HEAD_ANGLE);
      Double.isNaN(d1);
      this.mMaxCutForBarSize = (float)(d1 * d2);
      invalidateSelf();
    } 
  }
  
  public void setColor(@ColorInt int paramInt) {
    if (paramInt != this.mPaint.getColor()) {
      this.mPaint.setColor(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDirection(int paramInt) {
    if (paramInt != this.mDirection) {
      this.mDirection = paramInt;
      invalidateSelf();
    } 
  }
  
  public void setGapSize(float paramFloat) {
    if (paramFloat != this.mBarGap) {
      this.mBarGap = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setProgress(@FloatRange(from = 0.0D, to = 1.0D) float paramFloat) {
    if (this.mProgress != paramFloat) {
      this.mProgress = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setSpinEnabled(boolean paramBoolean) {
    if (this.mSpin != paramBoolean) {
      this.mSpin = paramBoolean;
      invalidateSelf();
    } 
  }
  
  public void setVerticalMirror(boolean paramBoolean) {
    if (this.mVerticalMirror != paramBoolean) {
      this.mVerticalMirror = paramBoolean;
      invalidateSelf();
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface ArrowDirection {}
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\android\support\v7\graphics\drawable\DrawerArrowDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */