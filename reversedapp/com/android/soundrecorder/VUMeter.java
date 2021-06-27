package com.android.soundrecorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class VUMeter extends View {
  static final long ANIMATION_INTERVAL = 70L;
  
  static final float DROPOFF_STEP = 0.18F;
  
  static final float PIVOT_RADIUS = 3.5F;
  
  static final float PIVOT_Y_OFFSET = 10.0F;
  
  static final float SHADOW_OFFSET = 2.0F;
  
  static final float SURGE_STEP = 0.35F;
  
  float mCurrentAngle;
  
  Paint mPaint;
  
  Recorder mRecorder;
  
  Paint mShadow;
  
  public Handler mShowHandler;
  
  public VUMeter(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public VUMeter(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  void init(Context paramContext) {
    this.mPaint = new Paint(1);
    this.mPaint.setColor(-1);
    this.mShadow = new Paint(1);
    this.mShadow.setColor(Color.argb(60, 0, 0, 0));
    this.mRecorder = null;
    this.mCurrentAngle = 0.0F;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    Recorder recorder = this.mRecorder;
    float f = 0.3926991F;
    if (recorder != null)
      f = 0.3926991F + this.mRecorder.getMaxAmplitude() * 2.3561947F / 32768.0F; 
    if (f > this.mCurrentAngle) {
      this.mCurrentAngle = f;
    } else {
      this.mCurrentAngle = Math.max(f, this.mCurrentAngle - 0.18F);
    } 
    this.mCurrentAngle = Math.min(2.7488937F, this.mCurrentAngle);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("getMaxAmplitude --");
    stringBuilder.append((int)(this.mCurrentAngle * 2.0F));
    Log.e("", stringBuilder.toString());
    if (this.mShowHandler != null)
      this.mShowHandler.sendEmptyMessage((int)(this.mCurrentAngle * 2.0F)); 
    getWidth();
    getHeight();
    Math.sin(this.mCurrentAngle);
    Math.cos(this.mCurrentAngle);
    if (this.mRecorder != null && this.mRecorder.state() == 1)
      postInvalidateDelayed(200L); 
  }
  
  public void setRecorder(Recorder paramRecorder) {
    this.mRecorder = paramRecorder;
    invalidate();
  }
  
  public void setShowHandler(Handler paramHandler) {
    this.mShowHandler = paramHandler;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\VUMeter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */