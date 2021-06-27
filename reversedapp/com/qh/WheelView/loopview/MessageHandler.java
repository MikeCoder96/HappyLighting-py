package com.qh.WheelView.loopview;

import android.os.Handler;
import android.os.Message;

final class MessageHandler extends Handler {
  public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
  
  public static final int WHAT_ITEM_SELECTED = 3000;
  
  public static final int WHAT_SMOOTH_SCROLL = 2000;
  
  final LoopView loopview;
  
  MessageHandler(LoopView paramLoopView) {
    this.loopview = paramLoopView;
  }
  
  public final void handleMessage(Message paramMessage) {
    int i = paramMessage.what;
    if (i != 1000) {
      if (i != 2000) {
        if (i == 3000)
          this.loopview.onItemSelected(); 
      } else {
        this.loopview.smoothScroll(LoopView.ACTION.FLING);
      } 
    } else {
      this.loopview.invalidate();
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\loopview\MessageHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */