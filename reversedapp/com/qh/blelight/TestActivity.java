package com.qh.blelight;

import android.app.Activity;
import android.os.Bundle;
import com.qh.ui.ColorWheelView1;
import com.qh.ui.RotateableView;

public class TestActivity extends Activity {
  public RotateableView img_slideline;
  
  public ColorWheelView1 mColorWheelView;
  
  private void initview() {
    this.img_slideline = (RotateableView)findViewById(2131230927);
    this.mColorWheelView = (ColorWheelView1)findViewById(2131230997);
    this.mColorWheelView.setOnColorChangeInterface(new ColorWheelView1.ColorChangeInterface() {
          public void colorChange(int param1Int, float param1Float, boolean param1Boolean) {
            if (TestActivity.this.mColorWheelView.getRing())
              TestActivity.this.img_slideline.setRotateDegrees(param1Float); 
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361842);
    initview();
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\TestActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */