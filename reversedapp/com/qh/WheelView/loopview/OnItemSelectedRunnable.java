package com.qh.WheelView.loopview;

final class OnItemSelectedRunnable implements Runnable {
  final LoopView loopView;
  
  OnItemSelectedRunnable(LoopView paramLoopView) {
    this.loopView = paramLoopView;
  }
  
  public final void run() {
    this.loopView.onItemSelectedListener.onItemSelected(this.loopView.getSelectedItem());
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\loopview\OnItemSelectedRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */