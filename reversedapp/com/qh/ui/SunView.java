package com.qh.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.xiaoyu.hlight.R;

public class SunView extends View {
  private int color = 16767744;
  
  private int color2 = 16767744;
  
  private int mHeight = 1000;
  
  private int mWidth = 1000;
  
  public SunView(Context paramContext) {
    super(paramContext);
  }
  
  public SunView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MyView);
    this.mWidth = (int)typedArray.getDimension(3, 1000.0F);
    this.mHeight = (int)typedArray.getDimension(0, 1000.0F);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("mWidth = ");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" mHeight = ");
    stringBuilder.append(this.mHeight);
    Log.e("", stringBuilder.toString());
  }
  
  public SunView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    paramCanvas.translate((this.mWidth / 2), (this.mHeight / 2));
    int i = this.color;
    int j = this.color;
    int k = this.color2;
    float f = (this.mWidth / 2);
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    RadialGradient radialGradient = new RadialGradient(0.0F, 0.0F, f, new int[] { i, j, k }, null, tileMode);
    Paint paint = new Paint(1);
    paint.setShader((Shader)radialGradient);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth((this.mWidth / 2 - 40));
    paramCanvas.drawCircle(0.0F, 0.0F, (this.mWidth / 2), paint);
    paint = new Paint(1);
    paint.setColor(this.color);
    paint.setStrokeWidth((this.mWidth / 4 + 30));
    paramCanvas.drawCircle(0.0F, 0.0F, (this.mWidth / 4 + 30), paint);
    paramCanvas.drawCircle(0.0F, 0.0F, (this.mWidth / 4 + 30), paint);
    super.onDraw(paramCanvas);
  }
  
  public void setColor(int paramInt) {
    paramInt &= 0xFFFFFF;
    this.color = -16777216 + paramInt;
    this.color2 = paramInt + 16777216;
    invalidate();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\SunView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */