package com.qh.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.qh.ui.dialog.listener.ISingleDialogEnsureClickListener;

public class SingleEnsureDialog extends Dialog {
  private ISingleDialogEnsureClickListener listener;
  
  private TextView mTvEnsure;
  
  private TextView mTvMessage;
  
  private String msg;
  
  private String title;
  
  public SingleEnsureDialog(Context paramContext, String paramString1, String paramString2, ISingleDialogEnsureClickListener paramISingleDialogEnsureClickListener) {
    super(paramContext, 2131689474);
    View view = LayoutInflater.from(paramContext).inflate(2131361860, null);
    setContentView(view);
    this.title = paramString1;
    this.msg = paramString2;
    this.listener = paramISingleDialogEnsureClickListener;
    init(view);
  }
  
  protected void init(View paramView) {
    this.mTvEnsure = (TextView)paramView.findViewById(2131231207);
    this.mTvMessage = (TextView)paramView.findViewById(2131231212);
    if (!TextUtils.isEmpty(this.msg))
      this.mTvMessage.setText(this.msg); 
    this.mTvEnsure.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (SingleEnsureDialog.this.listener != null)
              SingleEnsureDialog.this.listener.onEnsureClick(); 
            SingleEnsureDialog.this.dismiss();
          }
        });
  }
  
  public void setOnClickListener(ISingleDialogEnsureClickListener paramISingleDialogEnsureClickListener) {
    this.listener = paramISingleDialogEnsureClickListener;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\SingleEnsureDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */