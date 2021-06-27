package com.qh.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MyDialog extends Dialog {
  public MyDialog(Context paramContext, int paramInt1, int paramInt2, View paramView, int paramInt3) {
    super(paramContext, paramInt3);
    setContentView(paramView);
    Window window = getWindow();
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    layoutParams.gravity = 17;
    window.setAttributes(layoutParams);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\MyDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */