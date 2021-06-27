package com.qh.blelight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class PermissionActivity extends Activity {
  private Handler mHandler = new Handler();
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361836);
    getWindow().setFlags(1024, 1024);
  }
  
  protected void onResume() {
    if (Build.VERSION.SDK_INT >= 23) {
      if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
        startActivity(new Intent((Context)this, MainActivity.class));
        finish();
      } 
    } else {
      startActivity(new Intent((Context)this, MainActivity.class));
      finish();
    } 
    super.onResume();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\PermissionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */