package com.qh.WheelView;

public class ItemsRange {
  private int count;
  
  private int first;
  
  public ItemsRange() {
    this(0, 0);
  }
  
  public ItemsRange(int paramInt1, int paramInt2) {
    this.first = paramInt1;
    this.count = paramInt2;
  }
  
  public boolean contains(int paramInt) {
    boolean bool;
    if (paramInt >= getFirst() && paramInt <= getLast()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getCount() {
    return this.count;
  }
  
  public int getFirst() {
    return this.first;
  }
  
  public int getLast() {
    return getFirst() + getCount() - 1;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\ItemsRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */