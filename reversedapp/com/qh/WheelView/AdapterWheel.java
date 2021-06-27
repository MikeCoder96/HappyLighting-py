package com.qh.WheelView;

import android.content.Context;

public class AdapterWheel extends AbstractWheelTextAdapter {
  private WheelAdapter adapter;
  
  public AdapterWheel(Context paramContext, WheelAdapter paramWheelAdapter) {
    super(paramContext);
    this.adapter = paramWheelAdapter;
  }
  
  public WheelAdapter getAdapter() {
    return this.adapter;
  }
  
  protected CharSequence getItemText(int paramInt) {
    return this.adapter.getItem(paramInt);
  }
  
  public int getItemsCount() {
    return this.adapter.getItemsCount();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\WheelView\AdapterWheel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */