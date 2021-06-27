package com.qh.ui.dialog;

import android.content.Context;
import android.view.WindowManager;

public class DialogUtil {
  public static int getWindowHei(Context paramContext) {
    return ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getHeight();
  }
  
  public static int getWindowWid(Context paramContext) {
    return ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getWidth();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\DialogUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */