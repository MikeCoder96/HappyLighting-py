package com.qh.WheelView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class WheelScroller {
  public static final int MIN_DELTA_FOR_SCROLLING = 1;
  
  private static final int SCROLLING_DURATION = 400;
  
  private final int MESSAGE_JUSTIFY = 1;
  
  private final int MESSAGE_SCROLL = 0;
  
  private Handler animationHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        WheelScroller.this.scroller.computeScrollOffset();
        int i = WheelScroller.this.scroller.getCurrY();
        int j = WheelScroller.this.lastScrollY - i;
        WheelScroller.access$002(WheelScroller.this, i);
        if (j != 0)
          WheelScroller.this.listener.onScroll(j); 
        if (Math.abs(i - WheelScroller.this.scroller.getFinalY()) < 1) {
          WheelScroller.this.scroller.getFinalY();
          WheelScroller.this.scroller.forceFinished(true);
        } 
        if (!WheelScroller.this.scroller.isFinished()) {
          WheelScroller.this.animationHandler.sendEmptyMessage(param1Message.what);
        } else if (param1Message.what == 0) {
          WheelScroller.this.justify();
        } else {
          WheelScroller.this.finishScrolling();
        } 
      }
    };
  
  private Context context;
  
  private GestureDetector gestureDetector;
  
  private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
      public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
        WheelScroller.access$002(WheelScroller.this, 0);
        WheelScroller.this.scroller.fling(0, WheelScroller.this.lastScrollY, 0, (int)-param1Float2, 0, 0, -2147483647, 2147483647);
        WheelScroller.this.setNextMessage(0);
        return true;
      }
      
      public boolean onScroll(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
        return true;
      }
    };
  
  private boolean isScrollingPerformed;
  
  private int lastScrollY;
  
  private float lastTouchedY;
  
  private ScrollingListener listener;
  
  private Scroller scroller;
  
  public WheelScroller(Context paramContext, ScrollingListener paramScrollingListener) {
    this.gestureDetector = new GestureDetector(paramContext, (GestureDetector.OnGestureListener)this.gestureListener);
    this.gestureDetector.setIsLongpressEnabled(false);
    this.scroller = new Scroller(paramContext);
    this.listener = paramScrollingListener;
    this.context = paramContext;
  }
  
  private void clearMessages() {
    this.animationHandler.removeMessages(0);
    this.animationHandler.removeMessages(1);
  }
  
  private void justify() {
    this.listener.onJustify();
    setNextMessage(1);
  }
  
  private void setNextMessage(int paramInt) {
    clearMessages();
    this.animationHandler.sendEmptyMessage(paramInt);
  }
  
  private void startScrolling() {
    if (!this.isScrollingPerformed) {
      this.isScrollingPerformed = true;
      this.listener.onStarted();
    } 
  }
  
  void finishScrolling() {
    if (this.isScrollingPerformed) {
      this.listener.onFinished();
      this.isScrollingPerformed = false;
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i == 2) {
        i = (int)(paramMotionEvent.getY() - this.lastTouchedY);
        if (i != 0) {
          startScrolling();
          this.listener.onScroll(i);
          this.lastTouchedY = paramMotionEvent.getY();
        } 
      } 
    } else {
      this.lastTouchedY = paramMotionEvent.getY();
      this.scroller.forceFinished(true);
      clearMessages();
    } 
    if (!this.gestureDetector.onTouchEvent(paramMotionEvent) && paramMotionEvent.getAction() == 1)
      justify(); 
    return true;
  }
  
  public void scroll(int paramInt1, int paramInt2) {
    this.scroller.forceFinished(true);
    this.lastScrollY = 0;
    Scroller scroller = this.scroller;
    if (paramInt2 == 0)
      paramInt2 = 400; 
    scroller.startScroll(0, 0, 0, paramInt1, paramInt2);
    setNextMessage(0);
    startScrolling();
  }
  
  public void setInterpolator(Interpolator paramInterpolator) {
    this.scroller.forceFinished(true);
    this.scroller = new Scroller(this.context, paramInterpolator);
  }
  
  public void stopScrolling() {
    this.scroller.forceFinished(true);
  }
  
  public static interface ScrollingListener {
    void onFinished();
    
    void onJustify();
    
    void onScroll(int param1Int);
    
    void onStarted();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\WheelScroller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */