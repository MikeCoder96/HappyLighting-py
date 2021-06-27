package com.qh.blelight;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qh.tools.AnimationTool;

public class ResetpwdActivity extends MyActivity {
  public TextView Cancel;
  
  public TextView OK;
  
  public String addr = "";
  
  public Context context;
  
  public EditText et_pwd1;
  
  public EditText et_pwd2;
  
  public Resources mResources;
  
  public BluetoothLeService.Resetpwd mresetpwd = new BluetoothLeService.Resetpwd() {
      public void resetpwd(String param1String1, int param1Int, String param1String2) {
        ResetpwdActivity.this.uiHandler.sendEmptyMessage(param1Int);
      }
    };
  
  public MyApplication myApplication;
  
  public long sendt = -1L;
  
  public TextView tv_content;
  
  public Handler uiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          switch (param1Message.what) {
            default:
              return false;
            case 2:
              ResetpwdActivity.this.showmsg(ResetpwdActivity.this.mResources.getString(2131624075));
            case 1:
              ResetpwdActivity.this.showmsg(ResetpwdActivity.this.mResources.getString(2131624077));
            case 0:
              break;
          } 
          ResetpwdActivity.this.showmsg(ResetpwdActivity.this.mResources.getString(2131624074));
        }
      });
  
  private void init() {
    this.et_pwd1 = (EditText)findViewById(2131230795);
    this.et_pwd2 = (EditText)findViewById(2131230796);
    this.OK = (TextView)findViewById(2131230725);
    this.Cancel = (TextView)findViewById(2131230722);
    this.tv_content = (TextView)findViewById(2131231198);
    (new Handler()).postDelayed(new Runnable() {
          public void run() {
            ResetpwdActivity.this.et_pwd1.requestFocus();
            ((InputMethodManager)ResetpwdActivity.this.getSystemService("input_method")).showSoftInput((View)ResetpwdActivity.this.et_pwd1, 1);
          }
        }200L);
    this.et_pwd1.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
            if (param1CharSequence.length() <= 0)
              ResetpwdActivity.this.tv_content.setText(""); 
          }
        });
    this.et_pwd2.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
            if (param1CharSequence.length() <= 0)
              ResetpwdActivity.this.tv_content.setText(""); 
          }
        });
    this.OK.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            long l = System.currentTimeMillis();
            if (l - ResetpwdActivity.this.sendt <= 3500L)
              return; 
            ResetpwdActivity.this.sendt = l;
            Log.e("addr", ResetpwdActivity.this.addr);
            String str = ResetpwdActivity.this.et_pwd1.getText().toString();
            if (str.length() != 4) {
              AnimationTool.overshootAnimation(5, (View)ResetpwdActivity.this.tv_content);
              ResetpwdActivity.this.tv_content.setText(ResetpwdActivity.this.mResources.getString(2131624033));
              return;
            } 
            if (!str.equals(ResetpwdActivity.this.et_pwd2.getText().toString())) {
              AnimationTool.overshootAnimation(5, (View)ResetpwdActivity.this.tv_content);
              ResetpwdActivity.this.tv_content.setText(ResetpwdActivity.this.mResources.getString(2131624018));
              return;
            } 
            Log.e("pwd1", str);
            ResetpwdActivity.this.myApplication.resetpwd(ResetpwdActivity.this.addr, str);
            ResetpwdActivity.this.tv_content.setVisibility(8);
            ResetpwdActivity.this.finish();
          }
        });
    this.Cancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ResetpwdActivity.this.finish();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361878);
    setTintColor(0);
    this.addr = getIntent().getStringExtra("addr");
    this.myApplication = (MyApplication)getApplication();
    this.myApplication.mBluetoothLeService.mresetpwd = this.mresetpwd;
    this.context = getApplicationContext();
    this.mResources = getResources();
    init();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (4 == paramInt) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public void showmsg(String paramString) {
    Toast.makeText(this.context, paramString, 0).show();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\ResetpwdActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */