package com.qh.WheelView.loopview;

import android.view.GestureDetector;
import android.view.MotionEvent;

final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
  final LoopView loopView;
  
  LoopViewGestureListener(LoopView paramLoopView) {
    this.loopView = paramLoopView;
  }
  
  public final boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
    this.loopView.scrollBy(paramFloat2);
    return true;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\loopview\LoopViewGestureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */