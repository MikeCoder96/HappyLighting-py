package com.qh.WheelView.loopview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.qh.blelight.MainActivity;
import com.xiaoyu.hlight.R;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoopView extends View {
  private static final float DEFAULT_LINE_SPACE = 2.0F;
  
  private static final int DEFAULT_TEXT_SIZE = (int)((Resources.getSystem().getDisplayMetrics()).density * 15.0F);
  
  private static final int DEFAULT_VISIBIE_ITEMS = 9;
  
  private Paint Dbleft;
  
  Bitmap bitmap = null;
  
  Bitmap bitmap1 = null;
  
  int centerTextColor;
  
  int change;
  
  private Context context;
  
  int dividerColor;
  
  String[] drawingStrings;
  
  int firstLineY;
  
  private GestureDetector flingGestureDetector;
  
  int halfCircumference;
  
  Handler handler;
  
  int initPosition;
  
  boolean isLoop;
  
  List<String> items;
  
  int itemsVisibleCount;
  
  float lineSpacingMultiplier;
  
  ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
  
  private ScheduledFuture<?> mFuture;
  
  private int mOffset = 0;
  
  int maxTextHeight;
  
  int measuredHeight;
  
  int measuredWidth;
  
  OnItemSelectedListener onItemSelectedListener;
  
  int outerTextColor;
  
  private int paddingLeft;
  
  private int paddingRight;
  
  private Paint paintCenterText;
  
  private Paint paintIndicator;
  
  private Paint paintOuterText;
  
  int preCurrentIndex;
  
  private float previousY;
  
  int radius;
  
  private float scaleX = 1.05F;
  
  int secondLineY;
  
  private int selectedItem;
  
  long startTime = 0L;
  
  private Rect tempRect = new Rect();
  
  int textSize;
  
  int totalScrollY;
  
  public LoopView(Context paramContext) {
    super(paramContext);
    initLoopView(paramContext, (AttributeSet)null);
  }
  
  public LoopView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initLoopView(paramContext, paramAttributeSet);
  }
  
  public LoopView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initLoopView(paramContext, paramAttributeSet);
  }
  
  private int getTextX(String paramString, Paint paramPaint, Rect paramRect) {
    paramPaint.getTextBounds(paramString, 0, paramString.length(), paramRect);
    int i = (int)(paramRect.width() * this.scaleX);
    return (this.measuredWidth - this.paddingLeft - i) / 2 + this.paddingLeft;
  }
  
  private int getbitX(String paramString, Paint paramPaint, Rect paramRect) {
    paramPaint.getTextBounds(paramString, 0, paramString.length(), paramRect);
    int i = paramRect.width();
    return (this.measuredWidth - this.paddingLeft) / 2 - i;
  }
  
  private void initLoopView(Context paramContext, AttributeSet paramAttributeSet) {
    this.context = paramContext;
    this.handler = new MessageHandler(this);
    this.flingGestureDetector = new GestureDetector(paramContext, (GestureDetector.OnGestureListener)new LoopViewGestureListener(this));
    this.flingGestureDetector.setIsLongpressEnabled(false);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.androidWheelView);
    this.textSize = typedArray.getInteger(8, DEFAULT_TEXT_SIZE);
    this.textSize = (int)((Resources.getSystem().getDisplayMetrics()).density * this.textSize);
    this.lineSpacingMultiplier = typedArray.getFloat(5, 2.0F);
    this.centerTextColor = typedArray.getInteger(0, -1282278);
    this.outerTextColor = typedArray.getInteger(6, -5263441);
    this.dividerColor = typedArray.getInteger(1, -3815995);
    this.itemsVisibleCount = typedArray.getInteger(4, 9);
    if (this.itemsVisibleCount % 2 == 0)
      this.itemsVisibleCount = 9; 
    this.isLoop = typedArray.getBoolean(3, true);
    typedArray.recycle();
    this.drawingStrings = new String[this.itemsVisibleCount];
    this.totalScrollY = 0;
    this.initPosition = -1;
    initPaints();
  }
  
  private void initPaints() {
    this.paintOuterText = new Paint();
    this.paintOuterText.setColor(this.outerTextColor);
    this.paintOuterText.setAntiAlias(true);
    this.paintOuterText.setTypeface(Typeface.MONOSPACE);
    this.paintOuterText.setTextSize(this.textSize);
    this.paintCenterText = new Paint();
    this.paintCenterText.setColor(this.centerTextColor);
    this.paintCenterText.setAntiAlias(true);
    this.paintCenterText.setTextScaleX(this.scaleX);
    this.paintCenterText.setTypeface(Typeface.MONOSPACE);
    this.paintCenterText.setTextSize(this.textSize);
    this.paintIndicator = new Paint();
    this.paintIndicator.setColor(this.dividerColor);
    this.paintIndicator.setAntiAlias(true);
    this.Dbleft = new Paint();
    this.Dbleft.setStyle(Paint.Style.FILL);
    if (MainActivity.widthPixels <= 720) {
      this.bitmap = resizeBitmap(BitmapFactory.decodeResource(getResources(), 2131165408), 60, 60);
      this.bitmap1 = resizeBitmap(BitmapFactory.decodeResource(getResources(), 2131165405), 60, 60);
    } else {
      this.bitmap = resizeBitmap(BitmapFactory.decodeResource(getResources(), 2131165408), 80, 80);
      this.bitmap1 = resizeBitmap(BitmapFactory.decodeResource(getResources(), 2131165405), 80, 80);
    } 
  }
  
  private void remeasure() {
    if (this.items == null && this.items.size() > 0)
      return; 
    this.measuredWidth = getMeasuredWidth();
    this.measuredHeight = getMeasuredHeight();
    if (this.measuredWidth == 0 || this.measuredHeight == 0)
      return; 
    this.paddingLeft = getPaddingLeft();
    this.paddingRight = getPaddingRight();
    this.measuredWidth -= this.paddingRight;
    this.paintCenterText.getTextBounds("星期", 0, 2, this.tempRect);
    this.maxTextHeight = this.tempRect.height();
    double d = this.measuredHeight;
    Double.isNaN(d);
    this.halfCircumference = (int)(d * Math.PI / 2.0D);
    this.maxTextHeight = (int)(this.halfCircumference / this.lineSpacingMultiplier * (this.itemsVisibleCount - 1));
    this.radius = this.measuredHeight / 2;
    this.firstLineY = (int)((this.measuredHeight - this.lineSpacingMultiplier * this.maxTextHeight) / 2.0F);
    this.secondLineY = (int)((this.measuredHeight + this.lineSpacingMultiplier * this.maxTextHeight) / 2.0F);
    if (this.initPosition == -1)
      if (this.isLoop) {
        this.initPosition = (this.items.size() + 1) / 2;
      } else {
        this.initPosition = 0;
      }  
    this.preCurrentIndex = this.initPosition;
  }
  
  public void cancelFuture() {
    if (this.mFuture != null && !this.mFuture.isCancelled()) {
      this.mFuture.cancel(true);
      this.mFuture = null;
    } 
  }
  
  public final int getSelectedItem() {
    return this.selectedItem;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.items == null)
      return; 
    this.change = (int)(this.totalScrollY / this.lineSpacingMultiplier * this.maxTextHeight);
    this.preCurrentIndex = this.initPosition + this.change % this.items.size();
    if (!this.isLoop) {
      if (this.preCurrentIndex < 0)
        this.preCurrentIndex = 0; 
      if (this.preCurrentIndex > this.items.size() - 1)
        this.preCurrentIndex = this.items.size() - 1; 
    } else {
      if (this.preCurrentIndex < 0)
        this.preCurrentIndex = this.items.size() + this.preCurrentIndex; 
      if (this.preCurrentIndex > this.items.size() - 1)
        this.preCurrentIndex -= this.items.size(); 
    } 
    int i = (int)(this.totalScrollY % this.lineSpacingMultiplier * this.maxTextHeight);
    int j;
    for (j = 0; j < this.itemsVisibleCount; j++) {
      int k = this.preCurrentIndex - this.itemsVisibleCount / 2 - j;
      if (this.isLoop) {
        int m;
        while (true) {
          m = k;
          if (k < 0) {
            k += this.items.size();
            continue;
          } 
          break;
        } 
        while (m > this.items.size() - 1)
          m -= this.items.size(); 
        this.drawingStrings[j] = this.items.get(m);
      } else if (k < 0) {
        this.drawingStrings[j] = "";
      } else if (k > this.items.size() - 1) {
        this.drawingStrings[j] = "";
      } else {
        this.drawingStrings[j] = this.items.get(k);
      } 
    } 
    for (byte b = 0; b < this.itemsVisibleCount; b++) {
      paramCanvas.save();
      float f = this.maxTextHeight * this.lineSpacingMultiplier;
      double d1 = (b * f - i);
      Double.isNaN(d1);
      double d2 = this.halfCircumference;
      Double.isNaN(d2);
      double d3 = d1 * Math.PI / d2;
      if (d3 >= Math.PI || d3 <= 0.0D) {
        paramCanvas.restore();
      } else {
        double d4 = this.radius;
        d2 = Math.cos(d3);
        double d5 = this.radius;
        Double.isNaN(d5);
        Double.isNaN(d4);
        double d6 = Math.sin(d3);
        d1 = this.maxTextHeight;
        Double.isNaN(d1);
        j = (int)(d4 - d2 * d5 - d6 * d1 / 2.0D);
        paramCanvas.translate(0.0F, j);
        paramCanvas.scale(1.0F, (float)Math.sin(d3));
        if (j <= this.firstLineY && this.maxTextHeight + j >= this.firstLineY) {
          paramCanvas.save();
          paramCanvas.clipRect(0, 0, this.measuredWidth, this.firstLineY - j);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintOuterText);
          paramCanvas.drawBitmap(this.bitmap, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
          paramCanvas.restore();
          paramCanvas.save();
          paramCanvas.clipRect(0, this.firstLineY - j, this.measuredWidth, (int)f);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintCenterText);
          paramCanvas.drawBitmap(this.bitmap1, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
          paramCanvas.restore();
        } else if (j <= this.secondLineY && this.maxTextHeight + j >= this.secondLineY) {
          paramCanvas.save();
          paramCanvas.clipRect(0, 0, this.measuredWidth, this.secondLineY - j);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintCenterText);
          paramCanvas.drawBitmap(this.bitmap1, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
          paramCanvas.restore();
          paramCanvas.save();
          paramCanvas.clipRect(0, this.secondLineY - j, this.measuredWidth, (int)f);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintOuterText);
          paramCanvas.drawBitmap(this.bitmap, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
          paramCanvas.restore();
        } else if (j >= this.firstLineY && this.maxTextHeight + j <= this.secondLineY) {
          paramCanvas.clipRect(0, 0, this.measuredWidth, (int)f);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintCenterText);
          this.selectedItem = this.items.indexOf(this.drawingStrings[b]);
          paramCanvas.drawBitmap(this.bitmap1, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
        } else {
          paramCanvas.clipRect(0, 0, this.measuredWidth, (int)f);
          paramCanvas.drawText(this.drawingStrings[b], (this.measuredWidth / 2 - 80), (this.maxTextHeight - this.textSize / 3), this.paintOuterText);
          paramCanvas.drawBitmap(this.bitmap, (this.measuredWidth / 2 - 250), 0.0F, this.Dbleft);
        } 
        paramCanvas.restore();
      } 
    } 
  }
  
  protected final void onItemSelected() {
    if (this.onItemSelectedListener != null)
      postDelayed(new OnItemSelectedRunnable(this), 200L); 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    remeasure();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = this.flingGestureDetector.onTouchEvent(paramMotionEvent);
    float f = this.lineSpacingMultiplier * this.maxTextHeight;
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i != 2) {
        if (!bool) {
          float f1 = paramMotionEvent.getY();
          double d1 = Math.acos(((this.radius - f1) / this.radius));
          double d2 = this.radius;
          Double.isNaN(d2);
          double d3 = (f / 2.0F);
          Double.isNaN(d3);
          double d4 = f;
          Double.isNaN(d4);
          i = (int)((d1 * d2 + d3) / d4);
          f1 = this.totalScrollY;
          this.mOffset = (int)((i - this.itemsVisibleCount / 2) * f - (f1 % f + f) % f);
          if (System.currentTimeMillis() - this.startTime > 120L) {
            smoothScroll(ACTION.DAGGLE);
          } else {
            smoothScroll(ACTION.CLICK);
          } 
        } 
        if (getParent() != null)
          getParent().requestDisallowInterceptTouchEvent(false); 
      } else {
        float f2 = this.previousY;
        float f1 = paramMotionEvent.getRawY();
        this.previousY = paramMotionEvent.getRawY();
        this.totalScrollY = (int)(this.totalScrollY + f2 - f1);
        if (!this.isLoop) {
          f1 = -this.initPosition * f;
          f = (this.items.size() - 1 - this.initPosition) * f;
          if (this.totalScrollY < f1) {
            this.totalScrollY = (int)f1;
          } else if (this.totalScrollY > f) {
            this.totalScrollY = (int)f;
          } 
        } 
      } 
    } else {
      this.startTime = System.currentTimeMillis();
      cancelFuture();
      this.previousY = paramMotionEvent.getRawY();
      if (getParent() != null)
        getParent().requestDisallowInterceptTouchEvent(true); 
    } 
    invalidate();
    return true;
  }
  
  public Bitmap resizeBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = paramInt1 / i;
    float f2 = paramInt2 / j;
    Matrix matrix = new Matrix();
    matrix.postScale(f1, f2);
    return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, matrix, true);
  }
  
  protected final void scrollBy(float paramFloat) {
    cancelFuture();
    this.mFuture = this.mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, paramFloat), 0L, 10L, TimeUnit.MILLISECONDS);
  }
  
  public void setCenterTextColor(int paramInt) {
    this.centerTextColor = paramInt;
    this.paintCenterText.setColor(paramInt);
  }
  
  public void setCurrentPosition(int paramInt) {
    if (this.items == null || this.items.isEmpty())
      return; 
    int i = this.items.size();
    if (paramInt >= 0 && paramInt < i && paramInt != this.selectedItem) {
      this.initPosition = paramInt;
      this.totalScrollY = 0;
      this.mOffset = 0;
      invalidate();
    } 
  }
  
  public void setDividerColor(int paramInt) {
    this.dividerColor = paramInt;
    this.paintIndicator.setColor(paramInt);
  }
  
  public final void setInitPosition(int paramInt) {
    if (paramInt < 0) {
      this.initPosition = 0;
    } else if (this.items != null && this.items.size() > paramInt) {
      this.initPosition = paramInt;
    } 
  }
  
  public final void setItems(List<String> paramList) {
    this.items = paramList;
    remeasure();
    invalidate();
  }
  
  public void setItemsVisibleCount(int paramInt) {
    if (paramInt % 2 == 0)
      return; 
    if (paramInt != this.itemsVisibleCount) {
      this.itemsVisibleCount = paramInt;
      this.drawingStrings = new String[this.itemsVisibleCount];
    } 
  }
  
  public void setLineSpacingMultiplier(float paramFloat) {
    if (paramFloat > 1.0F)
      this.lineSpacingMultiplier = paramFloat; 
  }
  
  public final void setListener(OnItemSelectedListener paramOnItemSelectedListener) {
    this.onItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setNotLoop() {
    this.isLoop = false;
  }
  
  public void setOuterTextColor(int paramInt) {
    this.outerTextColor = paramInt;
    this.paintOuterText.setColor(paramInt);
  }
  
  public void setScaleX(float paramFloat) {
    this.scaleX = paramFloat;
  }
  
  public final void setTextSize(float paramFloat) {
    if (paramFloat > 0.0F) {
      this.textSize = (int)((this.context.getResources().getDisplayMetrics()).density * paramFloat);
      this.paintOuterText.setTextSize(this.textSize);
      this.paintCenterText.setTextSize(this.textSize);
    } 
  }
  
  void smoothScroll(ACTION paramACTION) {
    cancelFuture();
    if (paramACTION == ACTION.FLING || paramACTION == ACTION.DAGGLE) {
      float f = this.lineSpacingMultiplier * this.maxTextHeight;
      this.mOffset = (int)((this.totalScrollY % f + f) % f);
      if (this.mOffset > f / 2.0F) {
        this.mOffset = (int)(f - this.mOffset);
      } else {
        this.mOffset = -this.mOffset;
      } 
    } 
    this.mFuture = this.mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, this.mOffset), 0L, 10L, TimeUnit.MILLISECONDS);
  }
  
  public enum ACTION {
    CLICK, DAGGLE, FLING;
    
    static {
      $VALUES = new ACTION[] { CLICK, FLING, DAGGLE };
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\loopview\LoopView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */