package com.qh.blelight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputpwdActivity extends MyActivity {
  public TextView Cancel;
  
  public TextView OK;
  
  public String addr = "";
  
  public Context context;
  
  public EditText et_pwd;
  
  public Resources mResources;
  
  public MyApplication myApplication;
  
  private void init() {
    this.et_pwd = (EditText)findViewById(2131230794);
    this.OK = (TextView)findViewById(2131230725);
    this.Cancel = (TextView)findViewById(2131230722);
    (new Handler()).postDelayed(new Runnable() {
          public void run() {
            InputpwdActivity.this.et_pwd.requestFocus();
            ((InputMethodManager)InputpwdActivity.this.getSystemService("input_method")).showSoftInput((View)InputpwdActivity.this.et_pwd, 1);
          }
        }200L);
    this.OK.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = InputpwdActivity.this.et_pwd.getText().toString();
            if (str.length() != 4) {
              Toast.makeText(InputpwdActivity.this.context, InputpwdActivity.this.mResources.getString(2131624033), 1).show();
              return;
            } 
            if (InputpwdActivity.this.myApplication.isDreamY(InputpwdActivity.this.addr)) {
              SharedPreferences.Editor editor = InputpwdActivity.this.context.getSharedPreferences("setting", 0).edit();
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("pwd");
              stringBuilder.append(InputpwdActivity.this.addr);
              editor.putString(stringBuilder.toString(), str);
              editor.commit();
            } 
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(InputpwdActivity.this.addr);
            stringBuilder2.append(" ");
            stringBuilder2.append(str);
            Log.e("addr", stringBuilder2.toString());
            InputpwdActivity.this.myApplication.checkpwd(InputpwdActivity.this.addr, str);
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(InputpwdActivity.this.addr);
            stringBuilder1.append(" ");
            stringBuilder1.append(InputpwdActivity.this.context.getSharedPreferences("setting", 0).edit());
            Log.e("addr22", stringBuilder1.toString());
            InputpwdActivity.this.finish();
          }
        });
    this.Cancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (InputpwdActivity.this.myApplication.isDreamY(InputpwdActivity.this.addr))
              Toast.makeText(InputpwdActivity.this.context, InputpwdActivity.this.mResources.getString(2131624034), 1).show(); 
            InputpwdActivity.this.myApplication.disconn(InputpwdActivity.this.addr);
            InputpwdActivity.this.finish();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361877);
    setTintColor(0);
    this.addr = getIntent().getStringExtra("addr");
    this.myApplication = (MyApplication)getApplication();
    this.context = getApplicationContext();
    this.mResources = getResources();
    init();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (4 == paramInt) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\InputpwdActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */