package com.qh.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.qh.ui.dialog.listener.IDialogEnsureClickListener;

public class EnsureDialog extends BaseDialogFragment {
  private IDialogEnsureClickListener listener;
  
  private TextView mTvCancel;
  
  private TextView mTvEnsure;
  
  private TextView mTvMessage;
  
  public int getLayoutId() {
    return 2131361856;
  }
  
  protected void init(View paramView) {
    this.mTvCancel = (TextView)paramView.findViewById(2131231196);
    this.mTvEnsure = (TextView)paramView.findViewById(2131231207);
    this.mTvMessage = (TextView)paramView.findViewById(2131231212);
    Bundle bundle = getArguments();
    if (bundle != null) {
      if (bundle.containsKey("message")) {
        String str = bundle.getString("message");
        this.mTvMessage.setText(str);
      } 
      if (bundle.containsKey("left")) {
        String str = bundle.getString("left");
        if (!TextUtils.isEmpty(str))
          this.mTvCancel.setText(str); 
      } 
      if (bundle.containsKey("right")) {
        String str = bundle.getString("right");
        if (!TextUtils.isEmpty(str))
          this.mTvEnsure.setText(str); 
      } 
    } 
    this.mTvCancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EnsureDialog.this.listener != null)
              EnsureDialog.this.listener.onCancelClick(); 
          }
        });
    this.mTvEnsure.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EnsureDialog.this.listener != null)
              EnsureDialog.this.listener.onEnsureClick(); 
          }
        });
  }
  
  public void onStart() {
    super.onStart();
    initWindowSize();
  }
  
  public void setOnClickListener(IDialogEnsureClickListener paramIDialogEnsureClickListener) {
    this.listener = paramIDialogEnsureClickListener;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\EnsureDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */