package com.qh.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.qh.blelight.AdjustActivity;
import com.qh.blelight.MainActivity;
import com.qh.tools.tools;
import com.xiaoyu.hlight.R;

@SuppressLint({"DrawAllocation"})
public class ColorWheelView1 extends View {
  private final String TAG = "ColorPicker";
  
  public int alpha = 0;
  
  public float angle = 0.0F;
  
  public int bigColor = -1;
  
  public int blue = 0;
  
  public Bitmap bmp;
  
  private float bmpBlackX;
  
  private float bmpBlackY;
  
  public Bitmap bmpSlide;
  
  public Bitmap bmpSlideLine;
  
  public float bmpSlidePR;
  
  private float bmpSlideR = 340.0F;
  
  private float bmpSlideX;
  
  private float bmpSlideY;
  
  private int color = 16767744;
  
  private int color2 = 16767744;
  
  private Context context;
  
  public boolean debug = false;
  
  public float fprogress = 0.0F;
  
  public int green = 0;
  
  private boolean isDown = true;
  
  private boolean isPicture = false;
  
  private boolean isRing = true;
  
  private boolean isSet = false;
  
  private Bitmap mBitmap;
  
  private Canvas mBitmapCanvas = null;
  
  private Paint mBlackp;
  
  private Bitmap mCanvasBitmap = null;
  
  private float mCanvasBitmapR = 0.0F;
  
  private int[] mCircleColors;
  
  private int[] mColors = new int[13];
  
  private ComposeShader mCombinedShader = null;
  
  private Paint mCursorPaint = new Paint();
  
  public Handler mHandler;
  
  private int mHeight;
  
  private float[] mHsv = new float[3];
  
  private Paint mHsvPaint = new Paint();
  
  private Shader mRadialGradient;
  
  private RadialGradient mRadialShader = null;
  
  private Paint mSlidePaint;
  
  private SweepGradient mSweepShader = null;
  
  private int mWidth;
  
  private Matrix matrix = new Matrix();
  
  private Paint mbigPaint;
  
  private Bitmap mgetBitmap;
  
  private int mh = 0;
  
  private int mw = 0;
  
  public ColorChangeInterface myColorChangeInterface;
  
  private int myoffsetcenter;
  
  private int myprogress;
  
  private int mysetcenter = 160;
  
  private float oldangle = 0.0F;
  
  private long oldtime = 0L;
  
  public int p1;
  
  public int p2;
  
  public int p3;
  
  public int p4;
  
  public int p5;
  
  public int p6;
  
  public int p7;
  
  public int progress = 0;
  
  private float r;
  
  public int red = 0;
  
  private int wx = 0;
  
  private int wy = 0;
  
  public ColorWheelView1(Context paramContext) {
    super(paramContext);
    getContext();
    init();
  }
  
  public ColorWheelView1(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.MyView);
    this.mWidth = (int)typedArray.getDimension(3, 1000.0F);
    this.mHeight = (int)typedArray.getDimension(0, 1000.0F);
    this.myprogress = (int)typedArray.getDimension(2, 1000.0F);
    this.myoffsetcenter = typedArray.getInteger(1, 50);
    this.r = (this.mHeight / 2);
    if (this.mWidth == 750)
      AdjustActivity.is720p = false; 
    this.mHsv[0] = 0.0F;
    this.mHsv[1] = 1.0F;
    this.mHsv[2] = 1.0F;
    for (byte b = 0; b < 12; b++) {
      this.mColors[b] = Color.HSVToColor(this.mHsv);
      float[] arrayOfFloat = this.mHsv;
      arrayOfFloat[0] = arrayOfFloat[0] + 30.0F;
    } 
    this.mColors[12] = this.mColors[0];
    this.mHsvPaint.setDither(true);
    init();
  }
  
  public ColorWheelView1(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    getContext();
    init();
  }
  
  private int ave(int paramInt1, int paramInt2, float paramFloat) {
    return paramInt1 + Math.round(paramFloat * (paramInt2 - paramInt1));
  }
  
  private float calculateX(float paramFloat1, float paramFloat2, float paramFloat3) {
    return paramFloat3 * paramFloat1 / paramFloat2;
  }
  
  private float calculateY(float paramFloat1, float paramFloat2, float paramFloat3) {
    return paramFloat3 * paramFloat1 / paramFloat2;
  }
  
  private float getPOSAngle(float paramFloat1, float paramFloat2) {
    float f1 = this.r;
    double d = (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
    f1 = f1 / (float)Math.sqrt(d) * paramFloat1;
    float f2 = this.r / (float)Math.sqrt(d) * paramFloat2;
    if (f1 <= 0.0F && f2 >= 0.0F) {
      paramFloat1 = (float)Math.abs(Math.toDegrees(Math.atan((f1 / f2))));
    } else {
      paramFloat1 = 0.0F;
    } 
    paramFloat2 = paramFloat1;
    if (f1 <= 0.0F) {
      paramFloat2 = paramFloat1;
      if (f2 <= 0.0F)
        paramFloat2 = 180.0F - (float)Math.abs(Math.toDegrees(Math.atan((f1 / f2)))); 
    } 
    paramFloat1 = paramFloat2;
    if (f1 >= 0.0F) {
      paramFloat1 = paramFloat2;
      if (f2 <= 0.0F)
        paramFloat1 = (float)Math.abs(Math.toDegrees(Math.atan((f1 / f2)))) + 180.0F; 
    } 
    paramFloat2 = paramFloat1;
    if (f1 >= 0.0F) {
      paramFloat2 = paramFloat1;
      if (f2 >= 0.0F)
        paramFloat2 = 360.0F - (float)Math.abs(Math.toDegrees(Math.atan((f1 / f2)))); 
    } 
    return paramFloat2;
  }
  
  private float getR(float paramFloat1, float paramFloat2) {
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  private boolean inCenter(float paramFloat1, float paramFloat2, float paramFloat3) {
    double d1 = paramFloat3;
    Double.isNaN(d1);
    Double.isNaN(d1);
    double d2 = (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
    Double.isNaN(d2);
    return (d2 * Math.PI < d1 * Math.PI * d1) ? true : true;
  }
  
  private boolean inColorCircle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    double d1 = (140.0F + paramFloat3);
    Double.isNaN(d1);
    double d2 = paramFloat3;
    Double.isNaN(d2);
    double d3 = (paramFloat4 - this.mysetcenter);
    Double.isNaN(d3);
    double d4 = paramFloat4;
    Double.isNaN(d4);
    double d5 = (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
    Double.isNaN(d5);
    d5 *= Math.PI;
    return (d5 < d1 * Math.PI * d2 + 100.0D && d5 > d3 * Math.PI * d4 - 100.0D);
  }
  
  @SuppressLint({"NewApi"})
  private void init() {
    setMinimumHeight(this.mHeight);
    setMinimumWidth(this.mWidth);
    this.bmp = BitmapFactory.decodeResource(getResources(), 2131165434);
    this.bmpSlide = BitmapFactory.decodeResource(getResources(), 2131165339);
    this.bmpSlideLine = BitmapFactory.decodeResource(getResources(), 2131165432);
    this.bmpSlidePR = (this.bmpSlide.getHeight() / 2);
    if (MainActivity.widthPixels <= 720) {
      this.p1 = 100;
      this.p2 = 70;
      this.p3 = 100;
      this.p4 = 70;
      this.p5 = 20;
      this.p6 = 20;
      this.p7 = 24;
      this.bmpSlideR = 256.0F;
      this.angle = 208.0F;
      this.bmpSlideX = 102.43669F;
      this.bmpSlideY = -234.62523F;
      if (this.bmpSlideLine != null)
        this.bmpSlideLine.recycle(); 
      if (this.bmpSlide != null)
        this.bmpSlide.recycle(); 
      this.bmpSlide = BitmapFactory.decodeResource(getResources(), 2131165340);
      this.bmpSlideLine = BitmapFactory.decodeResource(getResources(), 2131165433);
    } else {
      this.p1 = 170;
      this.p2 = 110;
      this.p3 = 160;
      this.p4 = 120;
      this.p5 = 30;
      this.p6 = 30;
      this.p7 = 25;
      this.bmpSlideR = 340.0F;
      this.angle = 208.0F;
      this.bmpSlideX = 156.33669F;
      this.bmpSlideY = -301.92523F;
    } 
    this.mCircleColors = new int[] { -16711936, -16711681, -16776961, -65281, -65536, -256, -16711936 };
    this.mRadialGradient = (Shader)new SweepGradient(0.0F, 0.0F, this.mCircleColors, null);
    this.mbigPaint = new Paint(1);
    this.mbigPaint.setShader(this.mRadialGradient);
    this.mbigPaint.setStyle(Paint.Style.STROKE);
    this.mbigPaint.setStrokeWidth(this.p5);
    setSunColor(-9472);
    this.mCanvasBitmap = BitmapFactory.decodeResource(getResources(), 2131165338);
    this.mCanvasBitmap = Bitmap.createScaledBitmap(this.mCanvasBitmap, this.mWidth - 100, this.mWidth - 100, true);
    this.mCanvasBitmapR = ((this.mWidth - 100) / 2);
    this.mBlackp = new Paint(1);
    this.mBlackp.setStyle(Paint.Style.STROKE);
    this.mBlackp.setColor(-65536);
    this.mBlackp.setStrokeWidth(4.0F);
  }
  
  private int interpCircleColor(int[] paramArrayOfint, float paramFloat) {
    if (paramFloat <= 0.0F)
      return paramArrayOfint[0]; 
    if (paramFloat >= 1.0F)
      return paramArrayOfint[paramArrayOfint.length - 1]; 
    paramFloat *= (paramArrayOfint.length - 1);
    int i = (int)paramFloat;
    paramFloat -= i;
    int j = paramArrayOfint[i];
    int k = paramArrayOfint[i + 1];
    int m = ave(Color.alpha(j), Color.alpha(k), paramFloat);
    int n = ave(Color.red(j), Color.red(k), paramFloat);
    i = ave(Color.green(j), Color.green(k), paramFloat);
    j = ave(Color.blue(j), Color.blue(k), paramFloat);
    this.alpha = m;
    this.red = n;
    this.green = i;
    this.blue = j;
    return Color.argb(m, n, i, j);
  }
  
  public float getAngle() {
    return this.angle;
  }
  
  public boolean getPicture() {
    return this.isPicture;
  }
  
  public boolean getRing() {
    return this.isRing;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    paramCanvas.translate((this.mWidth / 2), (this.mHeight / 2));
    if (!this.isSet) {
      this.mh = paramCanvas.getHeight();
      this.mw = paramCanvas.getWidth();
      int i = this.bmpSlideLine.getWidth();
      float f = this.mw / i / 1.25F;
      this.bmpSlideLine = tools.postScale(this.bmpSlideLine, f);
      this.isSet = true;
    } 
    if (this.isPicture && this.mBitmap != null) {
      int j = this.mBitmap.getWidth();
      int k = this.mBitmap.getHeight();
      int m = this.mWidth;
      int i = this.mHeight;
      float f2 = m / j;
      float f1 = i / k;
      Matrix matrix = new Matrix();
      matrix.postScale(f2, f1);
      this.mBitmap = Bitmap.createBitmap(this.mBitmap, 0, 0, j, k, matrix, true);
      paramCanvas.drawBitmap(this.mBitmap, (-this.mWidth / 2), (-this.mHeight / 2), null);
      this.mBlackp.setColor(Color.argb(255, 255 - Color.red(this.bigColor), 255 - Color.green(this.bigColor), 255 - Color.blue(this.bigColor)));
      paramCanvas.drawCircle(this.wx, this.wy, 10.0F, this.mBlackp);
      return;
    } 
    if (this.isRing) {
      paramCanvas.drawCircle(0.0F, 0.0F, this.r - 80.0F, this.mbigPaint);
      int k = this.color;
      int j = this.color;
      int i = this.color2;
      float f = this.p1;
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      RadialGradient radialGradient = new RadialGradient(0.0F, 0.0F, f, new int[] { k, j, i }, null, tileMode);
      Paint paint = new Paint(1);
      paint.setShader((Shader)radialGradient);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(this.p2);
      paramCanvas.drawCircle(0.0F, 0.0F, this.p3, paint);
      paint = new Paint(1);
      paint.setColor(this.color);
      paint.setStrokeWidth(20.0F);
      paramCanvas.drawCircle(0.0F, 0.0F, this.p4, paint);
    } else {
      int i = this.bigColor;
      Paint paint = new Paint(1);
      paint.setColor(this.bigColor);
      paint.setStrokeWidth(20.0F);
      paramCanvas.drawCircle(0.0F, 0.0F, ((this.mWidth - 100) / 2 + 20), paint);
      paramCanvas.drawBitmap(this.bmp, (-this.mWidth / 2 + this.mWidth / 4 - 50), -this.mWidth / 2.4F + (this.mWidth / 10), null);
      paramCanvas.drawBitmap(this.mCanvasBitmap, (-(this.mWidth - 100) / 2), (-(this.mHeight - 100) / 2), null);
      this.mBlackp.setColor(Color.argb(255, 255 - Color.red(this.bigColor), 255 - Color.green(this.bigColor), 255 - Color.blue(this.bigColor)));
      paramCanvas.drawCircle(this.bmpBlackX, this.bmpBlackY, 10.0F, this.mBlackp);
    } 
    super.onDraw(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(this.mWidth, this.mHeight);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f1 = paramMotionEvent.getX() - (this.mWidth / 2);
    float f2 = paramMotionEvent.getY() - (this.mHeight / 2);
    this.wx = (int)f1;
    this.wy = (int)f2;
    if (this.isPicture && this.mBitmap != null) {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      int k = i;
      if (i < 0)
        k = 0; 
      i = j;
      if (j < 0)
        i = 0; 
      j = k;
      if (k > this.mBitmap.getWidth() - 5)
        j = this.mBitmap.getWidth() - 5; 
      k = i;
      if (i > this.mBitmap.getHeight() - 5)
        k = this.mBitmap.getHeight() - 5; 
      k = this.mBitmap.getPixel(j, k);
      if (this.myColorChangeInterface != null) {
        this.bigColor = k;
        if (paramMotionEvent.getAction() == 1) {
          this.myColorChangeInterface.colorChange(k, this.angle + 13.0F, true);
        } else {
          this.myColorChangeInterface.colorChange(k, this.angle + 130.0F, false);
        } 
      } 
      invalidate();
      return true;
    } 
    double d = (float)Math.atan2(f2, f1);
    Double.isNaN(d);
    float f3 = (float)(d / 6.283185307179586D);
    float f4 = f3;
    if (f3 < 0.0F)
      f4 = f3 + 1.0F; 
    if (this.isRing) {
      this.angle = getPOSAngle(f1, f2);
      this.bmpSlideX = calculateX(this.bmpSlideR, getR(f1, f2), f1);
      this.bmpSlideY = calculateX(this.bmpSlideR, getR(f1, f2), f2);
      int i = interpCircleColor(this.mCircleColors, f4);
      setSunColor(i);
      if (this.myColorChangeInterface != null)
        if (paramMotionEvent.getAction() == 1) {
          this.myColorChangeInterface.colorChange(i, this.angle + 130.0F, true);
        } else {
          this.myColorChangeInterface.colorChange(i, this.angle + 130.0F, false);
        }  
    } else {
      f4 = (int)Math.sqrt((f1 * f1 + f2 * f2));
      if (f4 < this.mCanvasBitmapR) {
        this.bmpBlackX = calculateX(f4, getR(f1, f2), f1);
        this.bmpBlackY = calculateX(f4, getR(f1, f2), f2);
      } else {
        this.bmpBlackX = calculateX(this.mCanvasBitmapR, getR(f1, f2), f1);
        this.bmpBlackY = calculateX(this.mCanvasBitmapR, getR(f1, f2), f2);
      } 
      paramMotionEvent.getX();
      paramMotionEvent.getY();
      int i = -5;
      if (f1 >= 0.0F) {
        j = -5;
      } else {
        j = 5;
      } 
      if (f2 < 0.0F)
        i = 5; 
      int j = (int)(this.mCanvasBitmapR + this.bmpBlackX + j);
      i = (int)(this.mCanvasBitmapR + this.bmpBlackY + i);
      j = this.mCanvasBitmap.getPixel(j, i);
      if (this.myColorChangeInterface != null) {
        this.bigColor = j;
        if (paramMotionEvent.getAction() == 1) {
          this.myColorChangeInterface.colorChange(j, this.angle, true);
        } else {
          this.myColorChangeInterface.colorChange(j, this.angle, false);
        } 
      } 
    } 
    invalidate();
    return true;
  }
  
  public void setAngle(float paramFloat) {
    this.angle = paramFloat;
  }
  
  public void setHandler(Handler paramHandler) {
    this.mHandler = paramHandler;
  }
  
  public void setOnColorChangeInterface(ColorChangeInterface paramColorChangeInterface) {
    this.myColorChangeInterface = paramColorChangeInterface;
  }
  
  public void setPicture(boolean paramBoolean, Bitmap paramBitmap) {
    if (this.mBitmap != null)
      this.mBitmap.recycle(); 
    this.isPicture = paramBoolean;
    this.mBitmap = paramBitmap;
    invalidate();
  }
  
  public void setRing(boolean paramBoolean) {
    this.isRing = paramBoolean;
    invalidate();
  }
  
  public void setSunColor(int paramInt) {
    paramInt &= 0xFFFFFF;
    this.color = -16777216 + paramInt;
    this.color2 = paramInt + 16777216;
  }
  
  public static interface ColorChangeInterface {
    void colorChange(int param1Int, float param1Float, boolean param1Boolean);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\ColorWheelView1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */