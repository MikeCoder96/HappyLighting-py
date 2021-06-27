package com.qh.blelight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ChangeBgActivity extends Activity {
  private ImageView img_1;
  
  private ImageView img_2;
  
  private ImageView img_3;
  
  private ImageView img_4;
  
  private ImageView img_5;
  
  private ImageView img_6;
  
  private ImageView img_7;
  
  private ImageView img_8;
  
  private ImageView img_9;
  
  private RelativeLayout lin_back;
  
  private MyApplication mMyApplication;
  
  public View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        switch (param1View.getId()) {
          case 2131230861:
            ChangeBgActivity.this.mMyApplication.setBG(8);
            break;
          case 2131230860:
            ChangeBgActivity.this.mMyApplication.setBG(7);
            break;
          case 2131230859:
            ChangeBgActivity.this.mMyApplication.setBG(6);
            break;
          case 2131230858:
            ChangeBgActivity.this.mMyApplication.setBG(5);
            break;
          case 2131230857:
            ChangeBgActivity.this.mMyApplication.setBG(4);
            break;
          case 2131230856:
            ChangeBgActivity.this.mMyApplication.setBG(3);
            break;
          case 2131230855:
            ChangeBgActivity.this.mMyApplication.setBG(2);
            break;
          case 2131230854:
            ChangeBgActivity.this.mMyApplication.setBG(1);
            break;
          case 2131230853:
            ChangeBgActivity.this.mMyApplication.setBG(0);
            break;
        } 
        ChangeBgActivity.this.finish();
      }
    };
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361824);
    this.lin_back = (RelativeLayout)findViewById(2131230957);
    this.lin_back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ChangeBgActivity.this.finish();
          }
        });
    this.mMyApplication = (MyApplication)getApplication();
    this.img_1 = (ImageView)findViewById(2131230853);
    this.img_2 = (ImageView)findViewById(2131230854);
    this.img_3 = (ImageView)findViewById(2131230855);
    this.img_4 = (ImageView)findViewById(2131230856);
    this.img_5 = (ImageView)findViewById(2131230857);
    this.img_6 = (ImageView)findViewById(2131230858);
    this.img_7 = (ImageView)findViewById(2131230859);
    this.img_8 = (ImageView)findViewById(2131230860);
    this.img_9 = (ImageView)findViewById(2131230861);
    this.img_1.setOnClickListener(this.myOnClickListener);
    this.img_2.setOnClickListener(this.myOnClickListener);
    this.img_3.setOnClickListener(this.myOnClickListener);
    this.img_4.setOnClickListener(this.myOnClickListener);
    this.img_5.setOnClickListener(this.myOnClickListener);
    this.img_6.setOnClickListener(this.myOnClickListener);
    this.img_7.setOnClickListener(this.myOnClickListener);
    this.img_8.setOnClickListener(this.myOnClickListener);
    this.img_9.setOnClickListener(this.myOnClickListener);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\ChangeBgActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */