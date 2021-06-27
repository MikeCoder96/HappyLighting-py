package com.qh.blelight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {
  private RelativeLayout lin_back;
  
  public Context mContext;
  
  private MyApplication mMyApplication;
  
  private RelativeLayout rel_main;
  
  private TextView tv_version;
  
  public TextView tv_yongh;
  
  public TextView tv_ys;
  
  public String getVersion() {
    try {
      String str = (getPackageManager().getPackageInfo(getPackageName(), 0)).versionName;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(getString(2131623955));
      stringBuilder.append(str);
      return stringBuilder.toString();
    } catch (Exception exception) {
      exception.printStackTrace();
      return getString(2131624043);
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361820);
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.mMyApplication = (MyApplication)getApplication();
    this.mContext = getApplicationContext();
    this.rel_main = (RelativeLayout)findViewById(2131231070);
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AboutActivity.this.finish();
          }
        });
    this.tv_version = (TextView)findViewById(2131231231);
    this.tv_version.setText(getVersion());
    if (this.mMyApplication.bgsrc.length > this.mMyApplication.typebg)
      this.rel_main.setBackgroundResource(this.mMyApplication.bgsrc[this.mMyApplication.typebg]); 
    this.tv_yongh = (TextView)findViewById(2131231232);
    this.tv_ys = (TextView)findViewById(2131231233);
    this.tv_yongh.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str;
            if ("zh".equals((AboutActivity.this.getResources().getConfiguration()).locale.getLanguage())) {
              str = "https://www.qh-tek.com/SerHappyCN.html";
            } else {
              str = "https://www.qh-tek.com/Service.html";
            } 
            Intent intent = new Intent(AboutActivity.this.mContext, WebActivity.class);
            intent.putExtra("URL", str);
            AboutActivity.this.startActivity(intent);
          }
        });
    this.tv_ys.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str;
            if ("zh".equals((AboutActivity.this.getResources().getConfiguration()).locale.getLanguage())) {
              str = "https://www.qh-tek.com/happlightCN.html";
            } else {
              str = "https://www.qh-tek.com/happlight.html";
            } 
            Intent intent = new Intent(AboutActivity.this.mContext, WebActivity.class);
            intent.putExtra("URL", str);
            AboutActivity.this.startActivity(intent);
          }
        });
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\AboutActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */