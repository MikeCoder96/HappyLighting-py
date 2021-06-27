package com.qh.blelight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class FristActivity extends Activity {
  private Handler mHandler = new Handler();
  
  private SharedPreferences setting;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361825);
    getWindow().setFlags(1024, 1024);
    this.setting = getSharedPreferences("BleLight", 0);
    if (this.setting.getBoolean("tongyi", false)) {
      startActivity(new Intent((Context)this, MainActivity.class));
    } else {
      startActivity(new Intent((Context)this, PrivacyActivity.class));
    } 
    this.mHandler.postDelayed(new Runnable() {
          public void run() {
            FristActivity.this.finish();
          }
        },  300L);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\FristActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */