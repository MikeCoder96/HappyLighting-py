package com.qh.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollAlwaysTextView extends TextView {
  public ScrollAlwaysTextView(Context paramContext) {
    this(paramContext, null);
  }
  
  public ScrollAlwaysTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 16842884);
  }
  
  public ScrollAlwaysTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean isFocused() {
    return true;
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    if (paramBoolean)
      super.onFocusChanged(paramBoolean, paramInt, paramRect); 
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    if (paramBoolean)
      super.onWindowFocusChanged(paramBoolean); 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\ScrollAlwaysTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */