package com.qh.ui.dialog;

import android.view.View;
import android.widget.RelativeLayout;

public class LoadingDialog extends BaseDialogFragment {
  private RelativeLayout mRlMain;
  
  public int getLayoutId() {
    return 2131361857;
  }
  
  protected void init(View paramView) {}
  
  public void onStart() {
    super.onStart();
    initWindowSize(1);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\LoadingDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */