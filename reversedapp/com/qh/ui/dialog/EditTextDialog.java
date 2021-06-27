package com.qh.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextDialog extends Dialog {
  private TextView btn_cancle;
  
  private TextView btn_confirm;
  
  private String cancel;
  
  private Context context;
  
  private EditText et_cart_dialog;
  
  private ISingleDialogEnsureClickListener1 listener;
  
  private String msg;
  
  private String ok;
  
  private String title;
  
  private TextView tv_title;
  
  public EditTextDialog(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, ISingleDialogEnsureClickListener1 paramISingleDialogEnsureClickListener1) {
    super(paramContext, 2131689474);
    View view = LayoutInflater.from(paramContext).inflate(2131361858, null);
    setContentView(view);
    this.context = paramContext;
    this.title = paramString1;
    this.msg = paramString2;
    this.listener = paramISingleDialogEnsureClickListener1;
    this.cancel = paramString3;
    this.ok = paramString4;
    init(view);
  }
  
  protected void init(View paramView) {
    this.btn_confirm = (TextView)paramView.findViewById(2131231207);
    this.btn_cancle = (TextView)paramView.findViewById(2131231196);
    this.tv_title = (TextView)paramView.findViewById(2131231212);
    this.et_cart_dialog = (EditText)paramView.findViewById(2131230792);
    if (!TextUtils.isEmpty(this.title)) {
      this.tv_title.setVisibility(0);
      this.tv_title.setText(this.title);
    } 
    if (!TextUtils.isEmpty(this.msg)) {
      this.et_cart_dialog.setText(this.msg);
      this.et_cart_dialog.requestFocus(this.msg.length());
    } else {
      this.et_cart_dialog.requestFocus();
    } 
    this.btn_cancle.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EditTextDialog.this.listener != null)
              EditTextDialog.this.listener.onEnsureClick(""); 
            EditTextDialog.this.dismiss();
          }
        });
    this.btn_confirm.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (EditTextDialog.this.listener != null && !TextUtils.isEmpty((CharSequence)EditTextDialog.this.et_cart_dialog.getText())) {
              EditTextDialog.this.listener.onEnsureClick(EditTextDialog.this.et_cart_dialog.getText().toString());
              EditTextDialog.this.dismiss();
            } 
          }
        });
  }
  
  public static interface ISingleDialogEnsureClickListener {
    void onEnsureClick();
  }
  
  public static interface ISingleDialogEnsureClickListener1 {
    void onEnsureClick(String param1String);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\q\\ui\dialog\EditTextDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */