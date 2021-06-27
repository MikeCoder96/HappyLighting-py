package com.qh.blelight.scroll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.qh.blelight.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class SwitchViewActivity extends Activity {
  private Button button;
  
  public int[] guides;
  
  private LinearLayout mLinearLayout;
  
  private int mNum = 0;
  
  private Resources mResources;
  
  ViewPager mViewPager;
  
  private SharedPreferences setting;
  
  private int size;
  
  public void initWithPageGuideMode() {
    if ("cn".equals(this.mResources.getString(2131624035))) {
      this.guides = new int[] { 2131165372, 2131165373, 2131165374, 2131165375, 2131165376, 2131165377, 2131165378, 2131165379 };
    } else {
      this.guides = new int[] { 2131165380, 2131165381, 2131165382, 2131165383, 2131165384, 2131165385, 2131165386, 2131165387 };
    } 
    this.button = (Button)findViewById(2131230766);
    this.mLinearLayout = (LinearLayout)findViewById(2131230983);
    ArrayList<View> arrayList = new ArrayList();
    LayoutInflater layoutInflater = LayoutInflater.from((Context)this);
    for (int i : this.guides) {
      View view = layoutInflater.inflate(2131361874, null);
      ((ImageView)view.findViewById(2131230849)).setImageResource(i);
      arrayList.add(view);
      view = new View((Context)this);
      view.setBackgroundResource(2131165319);
      view.setEnabled(false);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.size, this.size);
      layoutParams.leftMargin = 20;
      this.mLinearLayout.addView(view, (ViewGroup.LayoutParams)layoutParams);
    } 
    MViewPageAdapter mViewPageAdapter = new MViewPageAdapter(arrayList);
    this.mViewPager.setAdapter(mViewPageAdapter);
    this.mViewPager.addOnPageChangeListener(mViewPageAdapter);
    this.mViewPager.setCurrentItem(0);
    this.mLinearLayout.getChildAt(0).setEnabled(true);
    this.button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (!SwitchViewActivity.this.setting.getBoolean("isfrist", false)) {
              SharedPreferences.Editor editor = SwitchViewActivity.this.setting.edit();
              editor.putBoolean("isfrist", true);
              editor.commit();
              Intent intent = new Intent((Context)SwitchViewActivity.this, MainActivity.class);
              SwitchViewActivity.this.startActivity(intent);
            } 
            SwitchViewActivity.this.finish();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361887);
    this.mResources = getResources();
    this.setting = getSharedPreferences("BleLight", 0);
    this.mViewPager = (ViewPager)findViewById(2131231270);
    if (MainActivity.widthPixels <= 720) {
      this.size = 20;
    } else {
      this.size = 25;
    } 
    initWithPageGuideMode();
  }
  
  class MViewPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<View> mViewList;
    
    public MViewPageAdapter(List<View> param1List) {
      this.mViewList = param1List;
    }
    
    public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
      param1ViewGroup.removeView(this.mViewList.get(param1Int));
    }
    
    public int getCount() {
      return this.mViewList.size();
    }
    
    public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
      param1ViewGroup.addView(this.mViewList.get(param1Int), 0);
      return this.mViewList.get(param1Int);
    }
    
    public boolean isViewFromObject(View param1View, Object param1Object) {
      boolean bool;
      if (param1View == param1Object) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {
      SwitchViewActivity.this.mLinearLayout.getChildAt(SwitchViewActivity.this.mNum).setEnabled(false);
      SwitchViewActivity.this.mLinearLayout.getChildAt(param1Int).setEnabled(true);
      SwitchViewActivity.access$102(SwitchViewActivity.this, param1Int);
    }
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\scroll\SwitchViewActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */