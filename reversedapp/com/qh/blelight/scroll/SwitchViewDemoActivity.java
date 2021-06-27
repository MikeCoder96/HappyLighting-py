package com.qh.blelight.scroll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.qh.blelight.MainActivity;

public class SwitchViewDemoActivity extends Activity implements OnViewChangeListener, View.OnClickListener {
  private Button btn_over;
  
  private FrameLayout help_f1;
  
  private FrameLayout help_f2;
  
  private FrameLayout help_f3;
  
  private FrameLayout help_f4;
  
  private FrameLayout help_f5;
  
  private FrameLayout help_f6;
  
  private FrameLayout help_f7;
  
  private FrameLayout help_f8;
  
  private ImageView img1;
  
  private ImageView img2;
  
  private ImageView img3;
  
  private ImageView img4;
  
  private ImageView img5;
  
  private ImageView img6;
  
  private ImageView img7;
  
  private ImageView img8;
  
  private int mCurSel;
  
  private ImageView[] mImageViews;
  
  private Resources mResources;
  
  private MyScrollLayout mScrollLayout;
  
  private int mViewCount;
  
  private SharedPreferences setting;
  
  private void init() {
    this.btn_over = (Button)findViewById(2131230763);
    this.mScrollLayout = (MyScrollLayout)findViewById(2131230728);
    LinearLayout linearLayout = (LinearLayout)findViewById(2131230980);
    this.mViewCount = this.mScrollLayout.getChildCount();
    this.mImageViews = new ImageView[this.mViewCount];
    for (byte b = 0; b < this.mViewCount; b++) {
      this.mImageViews[b] = (ImageView)linearLayout.getChildAt(b);
      this.mImageViews[b].setEnabled(true);
      this.mImageViews[b].setOnClickListener(this);
      this.mImageViews[b].setTag(Integer.valueOf(b));
    } 
    this.mCurSel = 0;
    this.mImageViews[this.mCurSel].setEnabled(false);
    this.mScrollLayout.SetOnViewChangeListener(this);
    this.help_f1 = (FrameLayout)findViewById(2131230810);
    this.help_f2 = (FrameLayout)findViewById(2131230811);
    this.help_f3 = (FrameLayout)findViewById(2131230812);
    this.help_f4 = (FrameLayout)findViewById(2131230813);
    this.help_f5 = (FrameLayout)findViewById(2131230814);
    this.help_f6 = (FrameLayout)findViewById(2131230815);
    this.help_f7 = (FrameLayout)findViewById(2131230816);
    this.help_f8 = (FrameLayout)findViewById(2131230817);
    this.img1 = (ImageView)findViewById(2131230883);
    this.img2 = (ImageView)findViewById(2131230884);
    this.img3 = (ImageView)findViewById(2131230885);
    this.img4 = (ImageView)findViewById(2131230886);
    this.img5 = (ImageView)findViewById(2131230887);
    this.img6 = (ImageView)findViewById(2131230888);
    this.img7 = (ImageView)findViewById(2131230889);
    this.img8 = (ImageView)findViewById(2131230890);
    if ("cn".equals(this.mResources.getString(2131624035))) {
      this.img1.setImageResource(2131165372);
      this.img2.setImageResource(2131165373);
      this.img3.setImageResource(2131165374);
      this.img4.setImageResource(2131165375);
      this.img5.setImageResource(2131165376);
      this.img6.setImageResource(2131165377);
      this.img7.setImageResource(2131165378);
      this.img8.setImageResource(2131165379);
    } 
    this.btn_over.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (!SwitchViewDemoActivity.this.setting.getBoolean("isfrist", false)) {
              SharedPreferences.Editor editor = SwitchViewDemoActivity.this.setting.edit();
              editor.putBoolean("isfrist", true);
              editor.commit();
              Intent intent = new Intent((Context)SwitchViewDemoActivity.this, MainActivity.class);
              SwitchViewDemoActivity.this.startActivity(intent);
            } 
            SwitchViewDemoActivity.this.finish();
          }
        });
  }
  
  private void setCurPoint(int paramInt) {
    if (paramInt < 0 || paramInt > this.mViewCount - 1 || this.mCurSel == paramInt)
      return; 
    this.mImageViews[this.mCurSel].setEnabled(true);
    this.mImageViews[paramInt].setEnabled(false);
    this.mCurSel = paramInt;
  }
  
  public void OnViewChange(int paramInt) {
    setCurPoint(paramInt);
  }
  
  public void onClick(View paramView) {
    int i = ((Integer)paramView.getTag()).intValue();
    setCurPoint(i);
    this.mScrollLayout.snapToScreen(i);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361864);
    this.mResources = getResources();
    this.setting = getSharedPreferences("BleLight", 0);
    init();
    Log.v("@@@@@@", "this is in  SwitchViewDemoActivity onClick()");
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\scroll\SwitchViewDemoActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */