package com.qh.WheelView.loopview;

final class SmoothScrollTimerTask implements Runnable {
  final LoopView loopView;
  
  int offset;
  
  int realOffset;
  
  int realTotalOffset;
  
  SmoothScrollTimerTask(LoopView paramLoopView, int paramInt) {
    this.loopView = paramLoopView;
    this.offset = paramInt;
    this.realTotalOffset = Integer.MAX_VALUE;
    this.realOffset = 0;
  }
  
  public final void run() {
    if (this.realTotalOffset == Integer.MAX_VALUE)
      this.realTotalOffset = this.offset; 
    this.realOffset = (int)(this.realTotalOffset * 0.1F);
    if (this.realOffset == 0)
      if (this.realTotalOffset < 0) {
        this.realOffset = -1;
      } else {
        this.realOffset = 1;
      }  
    if (Math.abs(this.realTotalOffset) <= 0) {
      this.loopView.cancelFuture();
      this.loopView.handler.sendEmptyMessage(3000);
    } else {
      this.loopView.totalScrollY += this.realOffset;
      this.loopView.handler.sendEmptyMessage(1000);
      this.realTotalOffset -= this.realOffset;
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\loopview\SmoothScrollTimerTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */