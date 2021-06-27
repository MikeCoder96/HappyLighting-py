package com.qh.blelight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qh.blelight.scroll.SwitchViewActivity;

public class PrivacyActivity extends MyActivity {
  private RelativeLayout lin_back;
  
  public Context mContext;
  
  private MyApplication mMyApplication;
  
  public TextView tv_butongy;
  
  public TextView tv_tongy;
  
  public TextView tv_yongh;
  
  public TextView tv_ys;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361837);
    this.mMyApplication = (MyApplication)getApplication();
    this.mContext = getApplicationContext();
    this.tv_yongh = (TextView)findViewById(2131231232);
    this.tv_ys = (TextView)findViewById(2131231233);
    this.tv_tongy = (TextView)findViewById(2131231230);
    this.tv_butongy = (TextView)findViewById(2131231194);
    this.tv_yongh.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str;
            if ("zh".equals((PrivacyActivity.this.getResources().getConfiguration()).locale.getLanguage())) {
              str = "https://www.qh-tek.com/SerHappyCN.html";
            } else {
              str = "https://www.qh-tek.com/Service.html";
            } 
            Intent intent = new Intent(PrivacyActivity.this.mContext, WebActivity.class);
            intent.putExtra("URL", str);
            PrivacyActivity.this.startActivity(intent);
          }
        });
    this.tv_ys.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str;
            if ("zh".equals((PrivacyActivity.this.getResources().getConfiguration()).locale.getLanguage())) {
              str = "https://www.qh-tek.com/happlightCN.html";
            } else {
              str = "https://www.qh-tek.com/happlight.html";
            } 
            Intent intent = new Intent(PrivacyActivity.this.mContext, WebActivity.class);
            intent.putExtra("URL", str);
            PrivacyActivity.this.startActivity(intent);
          }
        });
    this.tv_tongy.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            PrivacyActivity.this.mMyApplication.settings.getBoolean("tongyi", false);
            SharedPreferences.Editor editor = PrivacyActivity.this.mMyApplication.settings.edit();
            editor.putBoolean("tongyi", true);
            editor.commit();
            if (!PrivacyActivity.this.mMyApplication.settings.getBoolean("isfrist", false)) {
              Intent intent = new Intent((Context)PrivacyActivity.this, SwitchViewActivity.class);
              PrivacyActivity.this.startActivity(intent);
            } else {
              Intent intent = new Intent((Context)PrivacyActivity.this, MainActivity.class);
              PrivacyActivity.this.startActivity(intent);
            } 
          }
        });
    this.tv_butongy.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            PrivacyActivity.this.finish();
          }
        });
  }
  
  protected void onDestroy() {
    super.onDestroy();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    Intent intent;
    if (4 == paramInt) {
      intent = new Intent("android.intent.action.MAIN");
      intent.setFlags(268435456);
      intent.addCategory("android.intent.category.HOME");
      startActivity(intent);
      return true;
    } 
    return super.onKeyDown(paramInt, (KeyEvent)intent);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\PrivacyActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */