package com.qh.blelight.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScrollLayout extends ViewGroup {
  private static final int SNAP_VELOCITY = 600;
  
  private static final String TAG = "ScrollLayout";
  
  private int mCurScreen;
  
  private int mDefaultScreen = 0;
  
  private float mLastMotionX;
  
  private OnViewChangeListener mOnViewChangeListener;
  
  private Scroller mScroller;
  
  private VelocityTracker mVelocityTracker;
  
  public MyScrollLayout(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public MyScrollLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public MyScrollLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private boolean IsCanMove(int paramInt) {
    return (getScrollX() <= 0 && paramInt < 0) ? false : (!(getScrollX() >= (getChildCount() - 1) * getWidth() && paramInt > 0));
  }
  
  private void init(Context paramContext) {
    this.mCurScreen = this.mDefaultScreen;
    this.mScroller = new Scroller(paramContext);
  }
  
  public void SetOnViewChangeListener(OnViewChangeListener paramOnViewChangeListener) {
    this.mOnViewChangeListener = paramOnViewChangeListener;
  }
  
  public void computeScroll() {
    if (this.mScroller.computeScrollOffset()) {
      scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
      postInvalidate();
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramBoolean) {
      paramInt4 = getChildCount();
      paramInt1 = 0;
      for (paramInt2 = 0; paramInt1 < paramInt4; paramInt2 = paramInt3) {
        View view = getChildAt(paramInt1);
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8) {
          paramInt3 = view.getMeasuredWidth() + paramInt2;
          view.layout(paramInt2, 0, paramInt3, view.getMeasuredHeight());
        } 
        paramInt1++;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getSize(paramInt1);
    View.MeasureSpec.getMode(paramInt1);
    int j = getChildCount();
    for (byte b = 0; b < j; b++)
      getChildAt(b).measure(paramInt1, paramInt2); 
    scrollTo(this.mCurScreen * i, 0);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    float f = paramMotionEvent.getX();
    paramMotionEvent.getY();
    int j = 0;
    switch (i) {
      default:
        return true;
      case 2:
        j = (int)(this.mLastMotionX - f);
        if (IsCanMove(j)) {
          if (this.mVelocityTracker != null)
            this.mVelocityTracker.addMovement(paramMotionEvent); 
          this.mLastMotionX = f;
          scrollBy(j, 0);
        } 
      case 1:
        if (this.mVelocityTracker != null) {
          this.mVelocityTracker.addMovement(paramMotionEvent);
          this.mVelocityTracker.computeCurrentVelocity(1000);
          j = (int)this.mVelocityTracker.getXVelocity();
        } 
        if (j > 600 && this.mCurScreen > 0) {
          snapToScreen(this.mCurScreen - 1);
        } else if (j < -600 && this.mCurScreen < getChildCount() - 1) {
          Log.e("ScrollLayout", "snap right");
          snapToScreen(this.mCurScreen + 1);
        } else {
          snapToDestination();
        } 
        if (this.mVelocityTracker != null) {
          this.mVelocityTracker.recycle();
          this.mVelocityTracker = null;
        } 
      case 0:
        break;
    } 
    Log.i("", "onTouchEvent  ACTION_DOWN");
    if (this.mVelocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
    } 
    if (!this.mScroller.isFinished())
      this.mScroller.abortAnimation(); 
    this.mLastMotionX = f;
  }
  
  public void snapToDestination() {
    int i = getWidth();
    snapToScreen((getScrollX() + i / 2) / i);
  }
  
  public void snapToScreen(int paramInt) {
    int i = Math.max(0, Math.min(paramInt, getChildCount() - 1));
    if (getScrollX() != getWidth() * i) {
      paramInt = getWidth() * i - getScrollX();
      this.mScroller.startScroll(getScrollX(), 0, paramInt, 0, Math.abs(paramInt) * 2);
      this.mCurScreen = i;
      invalidate();
      if (this.mOnViewChangeListener != null)
        this.mOnViewChangeListener.OnViewChange(this.mCurScreen); 
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\scroll\MyScrollLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */