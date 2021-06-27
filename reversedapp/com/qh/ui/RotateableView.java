package com.qh.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.qh.blelight.MainActivity;

public class RotateableView extends View {
  private Drawable mBackGroudDrawable;
  
  private int mBackGroudDrawableId = 2131165432;
  
  private int mBackGroundHeight;
  
  private int mBackGroundWidth;
  
  private float mRotateDegrees;
  
  public RotateableView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.mBackGroudDrawable = paramContext.getResources().getDrawable(this.mBackGroudDrawableId);
    this.mRotateDegrees = 340.0F;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mRotateDegrees == 90.0F) {
      paramCanvas.rotate(this.mRotateDegrees, 0.0F, 0.0F);
      paramCanvas.translate(0.0F, -this.mBackGroundHeight);
    } else {
      paramCanvas.rotate(this.mRotateDegrees, (this.mBackGroundWidth / 2), (this.mBackGroundHeight / 2));
    } 
    this.mBackGroudDrawable.setBounds(0, 0, this.mBackGroundWidth, this.mBackGroundHeight);
    this.mBackGroudDrawable.draw(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (MainActivity.widthPixels <= 720) {
      this.mBackGroundHeight = this.mBackGroudDrawable.getMinimumHeight() - 50;
      this.mBackGroundWidth = this.mBackGroudDrawable.getMinimumWidth() - 50;
    } else {
      this.mBackGroundHeight = this.mBackGroudDrawable.getMinimumHeight();
      this.mBackGroundWidth = this.mBackGroudDrawable.getMinimumWidth();
    } 
    if (this.mRotateDegrees == 90.0F) {
      setMeasuredDimension(this.mBackGroundHeight, this.mBackGroundWidth);
    } else {
      setMeasuredDimension(this.mBackGroundWidth, this.mBackGroundHeight);
    } 
  }
  
  public void setBG(int paramInt) {
    this.mBackGroudDrawableId = paramInt;
  }
  
  public void setRotateDegrees(float paramFloat) {
    this.mRotateDegrees = paramFloat;
    invalidate();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\RotateableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */