package com.qh.blelight;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.qh.data.MyColor;
import com.qh.tools.Tool;
import com.qh.ui.ColorWheelView1;
import com.qh.ui.RotateableView;
import com.qh.ui.SunView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AdjustActivity extends Activity {
  public static boolean is720p = true;
  
  public static int mainW;
  
  private float angle = 0.0F;
  
  private Button btn_nn;
  
  private Button btn_o;
  
  private Button btn_r;
  
  public int cachecolor = -1;
  
  public int[] classicColor = new int[] { 0, -1, -16711681, -65536, -16711936, -16776961 };
  
  private int clickNum = 0;
  
  public int[] commonColor = new int[] { -65536, -16711936, -16776961, -256, -65281, -16711681 };
  
  public Context context;
  
  public ImageView hookView;
  
  public ImageView ic_hook1;
  
  public ImageView ic_hook10;
  
  public ImageView ic_hook11;
  
  public ImageView ic_hook12;
  
  public ImageView ic_hook2;
  
  public ImageView ic_hook3;
  
  public ImageView ic_hook4;
  
  public ImageView ic_hook5;
  
  public ImageView ic_hook6;
  
  public ImageView ic_hook7;
  
  public ImageView ic_hook8;
  
  public ImageView ic_hook9;
  
  public ImageView img_color1;
  
  public ImageView img_color10;
  
  public ImageView img_color11;
  
  public ImageView img_color12;
  
  public ImageView img_color2;
  
  public ImageView img_color3;
  
  public ImageView img_color4;
  
  public ImageView img_color5;
  
  public ImageView img_color6;
  
  public ImageView img_color7;
  
  public ImageView img_color8;
  
  public ImageView img_color9;
  
  public ImageView img_colorwheel;
  
  private ImageView img_list;
  
  public ImageView img_pictrue;
  
  public ImageView img_rgb;
  
  public RotateableView img_slideline;
  
  public ImageView imgcache;
  
  public boolean isPicture = false;
  
  private RelativeLayout lRelativeLayout;
  
  private int len = 5;
  
  public RelativeLayout lin_colors1;
  
  public RelativeLayout lin_colors2;
  
  public LinearLayout lin_mic;
  
  public LinearLayout lin_rgb;
  
  public Handler mAdjustHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 0) {
            Random random = new Random();
            random.nextInt(3);
            float f = random.nextFloat() * (AdjustActivity.this.mCircleColors.length - 1);
            int i = (int)f;
            f -= i;
            int j = AdjustActivity.this.mCircleColors[i];
            i = AdjustActivity.this.mCircleColors[i + 1];
            j = Color.argb(255, AdjustActivity.this.ave(Color.red(j), Color.red(i), f), AdjustActivity.this.ave(Color.green(j), Color.green(i), f), AdjustActivity.this.ave(Color.blue(j), Color.blue(i), f));
            AdjustActivity.this.setColors(new MyColor(j, (byte)0, 100), true);
          } 
          if (param1Message.what == 1)
            AdjustActivity.this.img_slideline.setRotateDegrees(AdjustActivity.this.mRotate); 
          if (param1Message.what == 4) {
            if (AdjustActivity.this.mMyApplication.isopenmic()) {
              AdjustActivity.this.lin_mic.setVisibility(0);
            } else {
              AdjustActivity.this.lin_mic.setVisibility(8);
            } 
            if (AdjustActivity.this.mMyApplication.isHaveTri()) {
              AdjustActivity.this.lin_rgb.setVisibility(8);
              AdjustActivity.this.rel_rgb_all.setVisibility(8);
            } else if (!AdjustActivity.this.isPicture) {
              AdjustActivity.this.lin_rgb.setVisibility(0);
            } 
          } 
          if (param1Message.what == 5)
            AdjustActivity.this.uimic(); 
          return false;
        }
      });
  
  private int[] mCircleColors = new int[] { -16711936, -16711681, -16776961, -65281, -65536, -256, -16711936 };
  
  public ColorWheelView1 mColorWheelView;
  
  private DrawerLayout mDrawerLayout;
  
  private DrawerLayout mDrawerLayout2;
  
  private ExpandableListView mDrawerList;
  
  private ActionBarDrawerToggle mDrawerToggle;
  
  public MyApplication mMyApplication;
  
  private float mRotate = 0.0F;
  
  public SeekBar mSeekBar;
  
  public SunView mSunView;
  
  private Matrix matrix = new Matrix();
  
  public ImageView mic_open;
  
  private View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        AdjustActivity adjustActivity;
        if (AdjustActivity.this.hookView != null)
          AdjustActivity.this.hookView.setVisibility(8); 
        if (AdjustActivity.this.clickNum == param1View.getId()) {
          AdjustActivity.access$702(AdjustActivity.this, 0);
          return;
        } 
        AdjustActivity.access$702(AdjustActivity.this, param1View.getId());
        int i = param1View.getId();
        int j = 2;
        switch (i) {
          case 2131230879:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook9;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color9;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[2];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230878:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook8;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color8;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[1];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230877:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook7;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color7;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[0];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)-1, j), true);
            break;
          case 2131230876:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook6;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color6;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[5];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230875:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook5;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color5;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[4];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230874:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook4;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color4;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[3];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230873:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook3;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color3;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[2];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230872:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook2;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color2;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[1];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230871:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook12;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color12;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[5];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230870:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook11;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color11;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[4];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230869:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook10;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color10;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.classicColor[3];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
          case 2131230868:
            AdjustActivity.this.hookView = AdjustActivity.this.ic_hook1;
            AdjustActivity.this.shouImg = AdjustActivity.this.img_color1;
            adjustActivity = AdjustActivity.this;
            i = AdjustActivity.this.commonColor[0];
            if (AdjustActivity.this.mSeekBar.getProgress() > 0)
              j = AdjustActivity.this.mSeekBar.getProgress(); 
            adjustActivity.setColors(new MyColor(i, (byte)0, j), true);
            break;
        } 
        AdjustActivity.this.hookView.setVisibility(0);
      }
    };
  
  private SeekBar.OnSeekBarChangeListener myOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
        if (AdjustActivity.this.mMyApplication.mBluetoothLeService != null)
          for (String str : MainActivity.ControlMACs.keySet()) {
            if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
              MyBluetoothGatt myBluetoothGatt1 = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
              if (myBluetoothGatt1 != null)
                byte b = myBluetoothGatt1.datas[2]; 
            } 
            MyBluetoothGatt myBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
            if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
              param1Boolean = AdjustActivity.this.mMyApplication.isOpenMusicHop();
              int i = 1;
              if (param1Boolean)
                AdjustActivity.this.mMyApplication.setMusicHop(false, true); 
              if (myBluetoothGatt.mLEdevice.getName().contains("Dream") && !myBluetoothGatt.mLEdevice.getName().contains("Dream#") && !myBluetoothGatt.mLEdevice.getName().contains("Dream~"))
                continue; 
              if (param1Int > 0)
                i = param1Int; 
              myBluetoothGatt.setColorProportion(i);
            } 
          }  
      }
      
      public void onStartTrackingTouch(SeekBar param1SeekBar) {}
      
      public void onStopTrackingTouch(SeekBar param1SeekBar) {
        if (AdjustActivity.this.mMyApplication.mainHandler != null)
          AdjustActivity.this.mMyApplication.mainHandler.sendEmptyMessage(60001); 
        if (AdjustActivity.this.mMyApplication.isOpenMusicHop())
          AdjustActivity.this.mMyApplication.setMusicHop(false, true); 
        if (AdjustActivity.this.mMyApplication.MusicHandler != null)
          AdjustActivity.this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
        final int progress = param1SeekBar.getProgress();
        if (AdjustActivity.this.mMyApplication.mBluetoothLeService != null)
          for (String str : MainActivity.ControlMACs.keySet()) {
            if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
              MyBluetoothGatt myBluetoothGatt1 = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
              if (myBluetoothGatt1 != null)
                byte b = myBluetoothGatt1.datas[2]; 
            } 
            final MyBluetoothGatt mMyBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
            if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
              if (AdjustActivity.this.mMyApplication.isOpenMusicHop()) {
                AdjustActivity.this.mMyApplication.setMusicHop(false, true);
                AdjustActivity.this.mAdjustHandler.postDelayed(new Runnable() {
                      public void run() {
                        boolean bool;
                        MyBluetoothGatt myBluetoothGatt = mMyBluetoothGatt;
                        if (progress > 0) {
                          bool = progress;
                        } else {
                          bool = true;
                        } 
                        myBluetoothGatt.setColorProportion(bool);
                      }
                    },  200L);
                AdjustActivity.this.mAdjustHandler.postDelayed(new Runnable() {
                      public void run() {
                        boolean bool;
                        MyBluetoothGatt myBluetoothGatt = mMyBluetoothGatt;
                        if (progress > 0) {
                          bool = progress;
                        } else {
                          bool = true;
                        } 
                        myBluetoothGatt.setColorProportion(bool);
                      }
                    },  500L);
                continue;
              } 
              AdjustActivity.this.mAdjustHandler.postDelayed(new Runnable() {
                    public void run() {
                      boolean bool;
                      MyBluetoothGatt myBluetoothGatt = mMyBluetoothGatt;
                      if (progress > 0) {
                        bool = progress;
                      } else {
                        bool = true;
                      } 
                      myBluetoothGatt.setColorProportion(bool);
                    }
                  },  200L);
              AdjustActivity.this.mAdjustHandler.postDelayed(new Runnable() {
                    public void run() {
                      boolean bool;
                      MyBluetoothGatt myBluetoothGatt = mMyBluetoothGatt;
                      if (progress > 0) {
                        bool = progress;
                      } else {
                        bool = true;
                      } 
                      myBluetoothGatt.setColorProportion(bool);
                    }
                  },  500L);
            } 
          }  
      }
    };
  
  public SeekBar.OnSeekBarChangeListener mysetrOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
        if (param1Boolean)
          AdjustActivity.this.setRGBtv(false); 
      }
      
      public void onStartTrackingTouch(SeekBar param1SeekBar) {}
      
      public void onStopTrackingTouch(SeekBar param1SeekBar) {}
    };
  
  public RelativeLayout re_color_1;
  
  public RelativeLayout re_color_2;
  
  public RelativeLayout rel_brightness;
  
  public RelativeLayout rel_rgb_all;
  
  public SeekBar seekbar_speed_b;
  
  public SeekBar seekbar_speed_g;
  
  public SeekBar seekbar_speed_r;
  
  public long setbgtime = -1L;
  
  public SharedPreferences settings;
  
  public ImageView shouImg;
  
  public TextView tv_b;
  
  public TextView tv_cancel;
  
  public TextView tv_g;
  
  public TextView tv_ok;
  
  public TextView tv_r;
  
  public TextView tv_setb_num;
  
  public TextView tv_setg_num;
  
  public TextView tv_setr_num;
  
  Uri uri;
  
  private Uri uritempFile;
  
  private int ave(int paramInt1, int paramInt2, float paramFloat) {
    return paramInt1 + Math.round(paramFloat * (paramInt2 - paramInt1));
  }
  
  private boolean checkPermission(String paramString) {
    return (ActivityCompat.checkSelfPermission(MainActivity.context, paramString) == 0);
  }
  
  private void getPermission(String paramString, String[] paramArrayOfString) {
    if (Build.VERSION.SDK_INT >= 23)
      requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 1011); 
    ActivityCompat.requestPermissions((Activity)MainActivity.context, paramArrayOfString, 1);
  }
  
  private void saveCommonColor(int paramInt, boolean paramBoolean) {
    switch (this.shouImg.getId()) {
      case 2131230879:
        return;
      case 2131230878:
        return;
      case 2131230877:
        return;
      case 2131230876:
        this.commonColor[5] = paramInt;
        break;
      case 2131230875:
        this.commonColor[4] = paramInt;
        break;
      case 2131230874:
        this.commonColor[3] = paramInt;
        break;
      case 2131230873:
        this.commonColor[2] = paramInt;
        break;
      case 2131230872:
        this.commonColor[1] = paramInt;
        break;
      case 2131230871:
        return;
      case 2131230870:
        return;
      case 2131230869:
        return;
      case 2131230868:
        this.commonColor[0] = paramInt;
        break;
    } 
    setBG(this.shouImg, paramInt);
    if (paramBoolean) {
      SharedPreferences.Editor editor = this.settings.edit();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(this.shouImg.getId());
      editor.putInt(stringBuilder.toString(), paramInt);
      editor.commit();
    } 
  }
  
  private void setColors(final MyColor color, final boolean isStop) {
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    if (color == null)
      return; 
    this.cachecolor = color.color;
    if (isStop && this.mMyApplication.mainHandler != null)
      this.mMyApplication.mainHandler.sendEmptyMessage(60001); 
    if (color.warmWhite == 0) {
      TextView textView2 = this.tv_r;
      StringBuilder stringBuilder3 = new StringBuilder();
      stringBuilder3.append("");
      stringBuilder3.append(Color.red(color.color));
      textView2.setText(stringBuilder3.toString());
      TextView textView3 = this.tv_g;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(Color.green(color.color));
      textView3.setText(stringBuilder1.toString());
      TextView textView1 = this.tv_b;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("");
      stringBuilder2.append(Color.blue(color.color));
      textView1.setText(stringBuilder2.toString());
      this.tv_r.setBackgroundColor(Color.argb(255, Color.red(color.color), 0, 0));
      this.tv_g.setBackgroundColor(Color.argb(255, 0, Color.green(color.color), 0));
      this.tv_b.setBackgroundColor(Color.argb(255, 0, 0, Color.blue(color.color)));
    } 
    if (this.mMyApplication.myMediaPlayer != null)
      if (this.mMyApplication.myMediaPlayer.isPlaying()) {
        Log.e("00", "isPlaying 00 ");
        this.mAdjustHandler.postDelayed(new Runnable() {
              public void run() {
                for (String str : MainActivity.ControlMACs.keySet()) {
                  if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
                    MyBluetoothGatt myBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
                    if (myBluetoothGatt != null)
                      byte b = myBluetoothGatt.datas[2]; 
                  } 
                  if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
                    MyBluetoothGatt myBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
                    if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
                      if (myBluetoothGatt.mLEdevice != null && myBluetoothGatt.mLEdevice.getName().contains("Dream") && !myBluetoothGatt.mLEdevice.getName().contains("Dream#") && !myBluetoothGatt.mLEdevice.getName().contains("Dream~")) {
                        if (isStop)
                          myBluetoothGatt.setColor(color); 
                        continue;
                      } 
                      myBluetoothGatt.setColor(color);
                    } 
                  } 
                } 
              }
            },  500L);
      } else {
        Log.e("00", "isPlaying false ");
      }  
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          byte b = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
          if (myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && myBluetoothGatt.mLEdevice.getName().contains("Dream") && !myBluetoothGatt.mLEdevice.getName().contains("Dream#") && !myBluetoothGatt.mLEdevice.getName().contains("Dream~")) {
            if (isStop)
              myBluetoothGatt.setColor(color); 
            continue;
          } 
          myBluetoothGatt.setColor(color);
        } 
      } 
    } 
  }
  
  private void setColors(final MyColor color, final boolean isStop, int paramInt) {
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    if (color == null)
      return; 
    this.cachecolor = color.color;
    if (isStop && this.mMyApplication.mainHandler != null)
      this.mMyApplication.mainHandler.sendEmptyMessage(60001); 
    if (color.warmWhite == 0) {
      TextView textView = this.tv_r;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(Color.red(color.color));
      textView.setText(stringBuilder.toString());
      textView = this.tv_g;
      stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(Color.green(color.color));
      textView.setText(stringBuilder.toString());
      textView = this.tv_b;
      stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(Color.blue(color.color));
      textView.setText(stringBuilder.toString());
      this.tv_r.setBackgroundColor(Color.argb(255, Color.red(color.color), 0, 0));
      this.tv_g.setBackgroundColor(Color.argb(255, 0, Color.green(color.color), 0));
      this.tv_b.setBackgroundColor(Color.argb(255, 0, 0, Color.blue(color.color)));
    } 
    if (this.mMyApplication.myMediaPlayer != null)
      if (this.mMyApplication.myMediaPlayer.isPlaying()) {
        Log.e("00", "isPlaying 00 ");
        this.mAdjustHandler.postDelayed(new Runnable() {
              public void run() {
                for (String str : MainActivity.ControlMACs.keySet()) {
                  if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
                    MyBluetoothGatt myBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
                    if (myBluetoothGatt != null)
                      byte b = myBluetoothGatt.datas[2]; 
                  } 
                  if (AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
                    MyBluetoothGatt myBluetoothGatt = AdjustActivity.this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
                    if (myBluetoothGatt == null || myBluetoothGatt.mConnectionState != 2 || (myBluetoothGatt.mLEdevice != null && AdjustActivity.this.mMyApplication.isTri(myBluetoothGatt.mLEdevice.getName())))
                      continue; 
                    if (myBluetoothGatt.mLEdevice != null && myBluetoothGatt.mLEdevice.getName().contains("Dream") && !myBluetoothGatt.mLEdevice.getName().contains("Dream#") && !myBluetoothGatt.mLEdevice.getName().contains("Dream~")) {
                      if (isStop)
                        myBluetoothGatt.setColor(color); 
                      continue;
                    } 
                    myBluetoothGatt.setColor(color);
                  } 
                } 
              }
            },  500L);
      } else {
        Log.e("00", "isPlaying false ");
      }  
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          paramInt = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt == null || myBluetoothGatt.mConnectionState != 2 || (myBluetoothGatt.mLEdevice != null && this.mMyApplication.isTri(myBluetoothGatt.mLEdevice.getName())))
          continue; 
        if (myBluetoothGatt.mLEdevice != null && myBluetoothGatt.mLEdevice.getName().contains("Dream") && !myBluetoothGatt.mLEdevice.getName().contains("Dream#") && !myBluetoothGatt.mLEdevice.getName().contains("Dream~")) {
          if (isStop)
            myBluetoothGatt.setColor(color); 
          continue;
        } 
        myBluetoothGatt.setColor(color);
      } 
    } 
  }
  
  private void setView(final View v1) {
    v1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            AdjustActivity.this.setViews(Tool.px2dp(AdjustActivity.this.context, v1.getWidth()));
            v1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        });
  }
  
  private void setViews(float paramFloat) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setViews: ");
    stringBuilder.append(paramFloat);
    Log.e(" = = =  ", stringBuilder.toString());
    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.img_color1.getLayoutParams();
    layoutParams.height = (int)TypedValue.applyDimension(1, paramFloat, this.img_color1.getResources().getDisplayMetrics());
    this.img_color1.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color2.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color3.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color4.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color5.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color6.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color7.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color8.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color9.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color10.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color11.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.img_color12.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  private void showColors(boolean paramBoolean) {
    if (paramBoolean) {
      this.rel_brightness.setVisibility(0);
      this.lin_colors1.setVisibility(0);
      this.lin_colors2.setVisibility(0);
      this.mSunView.setVisibility(8);
    } else {
      this.rel_brightness.setVisibility(8);
      this.lin_colors1.setVisibility(8);
      this.lin_colors2.setVisibility(8);
      this.mSunView.setVisibility(0);
    } 
  }
  
  private void startPhotoZoom2(Uri paramUri) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(paramUri, "image/*");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("file:///");
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append(File.separator);
    stringBuilder.append("Android/icon_temp.jpg");
    this.uritempFile = Uri.parse(stringBuilder.toString());
    intent.putExtra("output", (Parcelable)this.uritempFile);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", true);
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", 250);
    intent.putExtra("outputY", 250);
    intent.putExtra("return-data", true);
    startActivityForResult(intent, 200);
  }
  
  public void getCommonColors() {
    int i = this.settings.getInt("2131230868", -1);
    int j = this.settings.getInt("2131230872", -1);
    int k = this.settings.getInt("2131230873", -1);
    int m = this.settings.getInt("2131230874", -1);
    int n = this.settings.getInt("2131230875", -1);
    int i1 = this.settings.getInt("2131230876", -1);
    if (i != -1)
      this.commonColor[0] = i; 
    if (j != -1)
      this.commonColor[1] = j; 
    if (k != -1)
      this.commonColor[2] = k; 
    if (m != -1)
      this.commonColor[3] = m; 
    if (n != -1)
      this.commonColor[4] = n; 
    if (i1 != -1)
      this.commonColor[5] = i1; 
  }
  
  public void initRGB() {
    this.cachecolor = this.mColorWheelView.bigColor;
    TextView textView2 = this.tv_r;
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append("");
    stringBuilder3.append(Color.red(this.mColorWheelView.bigColor));
    textView2.setText(stringBuilder3.toString());
    TextView textView3 = this.tv_g;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("");
    stringBuilder1.append(Color.green(this.mColorWheelView.bigColor));
    textView3.setText(stringBuilder1.toString());
    TextView textView1 = this.tv_b;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(Color.blue(this.mColorWheelView.bigColor));
    textView1.setText(stringBuilder2.toString());
    this.tv_r.setBackgroundColor(Color.argb(255, Color.red(this.mColorWheelView.bigColor), 0, 0));
    this.tv_g.setBackgroundColor(Color.argb(255, 0, Color.green(this.mColorWheelView.bigColor), 0));
    this.tv_b.setBackgroundColor(Color.argb(255, 0, 0, Color.blue(this.mColorWheelView.bigColor)));
    this.seekbar_speed_r = (SeekBar)findViewById(2131231133);
    this.seekbar_speed_g = (SeekBar)findViewById(2131231132);
    this.seekbar_speed_b = (SeekBar)findViewById(2131231131);
    this.img_rgb = (ImageView)findViewById(2131230922);
    this.tv_setr_num = (TextView)findViewById(2131231226);
    this.tv_setg_num = (TextView)findViewById(2131231225);
    this.tv_setb_num = (TextView)findViewById(2131231224);
    this.seekbar_speed_r.setOnSeekBarChangeListener(this.mysetrOnSeekBarChangeListener);
    this.seekbar_speed_g.setOnSeekBarChangeListener(this.mysetrOnSeekBarChangeListener);
    this.seekbar_speed_b.setOnSeekBarChangeListener(this.mysetrOnSeekBarChangeListener);
    this.tv_cancel = (TextView)findViewById(2131231196);
    this.tv_ok = (TextView)findViewById(2131231214);
    this.tv_cancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AdjustActivity.this.rel_rgb_all.setVisibility(8);
          }
        });
    this.tv_ok.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AdjustActivity.this.rel_rgb_all.setVisibility(8);
            int i = Color.argb(255, AdjustActivity.this.seekbar_speed_r.getProgress(), AdjustActivity.this.seekbar_speed_g.getProgress(), AdjustActivity.this.seekbar_speed_b.getProgress());
            AdjustActivity.this.cachecolor = i;
            MyColor myColor = new MyColor(AdjustActivity.this.cachecolor, (byte)0, AdjustActivity.this.mSeekBar.getProgress());
            AdjustActivity.this.setColors(myColor, true, 0);
            AdjustActivity.this.saveCommonColor(i, true);
          }
        });
  }
  
  public void initView() {
    this.re_color_1 = (RelativeLayout)findViewById(2131231040);
    this.re_color_2 = (RelativeLayout)findViewById(2131231041);
    this.ic_hook1 = (ImageView)findViewById(2131230821);
    this.ic_hook2 = (ImageView)findViewById(2131230825);
    this.ic_hook3 = (ImageView)findViewById(2131230826);
    this.ic_hook4 = (ImageView)findViewById(2131230827);
    this.ic_hook5 = (ImageView)findViewById(2131230828);
    this.ic_hook6 = (ImageView)findViewById(2131230829);
    this.ic_hook7 = (ImageView)findViewById(2131230830);
    this.ic_hook8 = (ImageView)findViewById(2131230831);
    this.ic_hook9 = (ImageView)findViewById(2131230832);
    this.ic_hook10 = (ImageView)findViewById(2131230822);
    this.ic_hook11 = (ImageView)findViewById(2131230823);
    this.ic_hook12 = (ImageView)findViewById(2131230824);
    this.hookView = this.ic_hook1;
    this.img_color1 = (ImageView)findViewById(2131230868);
    this.img_color2 = (ImageView)findViewById(2131230872);
    this.img_color3 = (ImageView)findViewById(2131230873);
    this.img_color4 = (ImageView)findViewById(2131230874);
    this.img_color5 = (ImageView)findViewById(2131230875);
    this.img_color6 = (ImageView)findViewById(2131230876);
    this.img_color7 = (ImageView)findViewById(2131230877);
    this.img_color8 = (ImageView)findViewById(2131230878);
    this.img_color9 = (ImageView)findViewById(2131230879);
    this.img_color10 = (ImageView)findViewById(2131230869);
    this.img_color11 = (ImageView)findViewById(2131230870);
    this.img_color12 = (ImageView)findViewById(2131230871);
    this.img_color1.setBackgroundResource(2131165287);
    this.img_color2.setBackgroundResource(2131165287);
    this.img_color3.setBackgroundResource(2131165287);
    this.img_color4.setBackgroundResource(2131165287);
    this.img_color5.setBackgroundResource(2131165287);
    this.img_color6.setBackgroundResource(2131165287);
    this.img_color7.setBackgroundResource(2131165287);
    this.img_color8.setBackgroundResource(2131165287);
    this.img_color9.setBackgroundResource(2131165287);
    this.img_color10.setBackgroundResource(2131165287);
    this.img_color11.setBackgroundResource(2131165287);
    this.img_color12.setBackgroundResource(2131165287);
    this.img_color1.setImageResource(2131165288);
    this.img_color2.setImageResource(2131165288);
    this.img_color3.setImageResource(2131165288);
    this.img_color4.setImageResource(2131165288);
    this.img_color5.setImageResource(2131165288);
    this.img_color6.setImageResource(2131165288);
    this.img_color8.setImageResource(2131165288);
    this.img_color9.setImageResource(2131165288);
    this.img_color10.setImageResource(2131165288);
    this.img_color11.setImageResource(2131165288);
    this.img_color12.setImageResource(2131165288);
    setBG(this.img_color1, this.commonColor[0]);
    setBG(this.img_color2, this.commonColor[1]);
    setBG(this.img_color3, this.commonColor[2]);
    setBG(this.img_color4, this.commonColor[3]);
    setBG(this.img_color5, this.commonColor[4]);
    setBG(this.img_color6, this.commonColor[5]);
    setBG(this.img_color8, this.classicColor[1]);
    setBG(this.img_color9, this.classicColor[2]);
    setBG(this.img_color10, this.classicColor[3]);
    setBG(this.img_color11, this.classicColor[4]);
    setBG(this.img_color12, this.classicColor[5]);
    this.img_color1.setOnClickListener(this.myOnClickListener);
    this.img_color2.setOnClickListener(this.myOnClickListener);
    this.img_color3.setOnClickListener(this.myOnClickListener);
    this.img_color4.setOnClickListener(this.myOnClickListener);
    this.img_color5.setOnClickListener(this.myOnClickListener);
    this.img_color6.setOnClickListener(this.myOnClickListener);
    this.img_color7.setOnClickListener(this.myOnClickListener);
    this.img_color8.setOnClickListener(this.myOnClickListener);
    this.img_color9.setOnClickListener(this.myOnClickListener);
    this.img_color10.setOnClickListener(this.myOnClickListener);
    this.img_color11.setOnClickListener(this.myOnClickListener);
    this.img_color12.setOnClickListener(this.myOnClickListener);
    this.shouImg = this.img_color1;
    this.rel_brightness = (RelativeLayout)findViewById(2131231056);
    this.lin_colors1 = (RelativeLayout)findViewById(2131230962);
    this.lin_colors2 = (RelativeLayout)findViewById(2131230963);
    this.mSunView = (SunView)findViewById(2131230982);
    this.mSunView.setColor(-9472);
    setView((View)this.img_color1);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == -1) {
      if (paramInt1 == 100) {
        if (paramIntent != null && paramIntent.getData() != null)
          startPhotoZoom2(paramIntent.getData()); 
      } else if (paramInt1 == 200 && paramIntent != null) {
        Uri uri1 = paramIntent.getData();
        Uri uri2 = uri1;
        if (uri1 == null)
          uri2 = this.uritempFile; 
        if (uri2 != null) {
          ContentResolver contentResolver = getContentResolver();
          try {
            Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri2));
            this.mColorWheelView.setPicture(true, bitmap);
            this.isPicture = true;
            this.lin_rgb.setVisibility(8);
            this.img_pictrue.setVisibility(8);
            showColors(false);
          } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            Toast.makeText(this.context, "error!", 0).show();
          } 
        } else {
          Bundle bundle = fileNotFoundException.getExtras();
          if (bundle != null) {
            Bitmap bitmap = (Bitmap)bundle.get("data");
            this.mColorWheelView.setPicture(true, bitmap);
            this.isPicture = true;
            this.img_pictrue.setVisibility(8);
            this.lin_rgb.setVisibility(8);
            showColors(false);
          } 
        } 
      } 
    } else {
      this.isPicture = false;
      if (this.mColorWheelView.getRing() && this.img_slideline.getVisibility() == 8)
        this.img_slideline.setVisibility(0); 
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.context = (Context)this;
    boolean bool = Tool.checkDeviceHasNavigationBar(this.context);
    Log.e("--", "1920=0.5625");
    Log.e("--", "1280=0.5625");
    Log.e("--", "1280-=0.6081081");
    setContentView(2131361823);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("isHasNavigationBar=");
    stringBuilder.append(bool);
    Log.e("--", stringBuilder.toString());
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.AdjustHandler = this.mAdjustHandler;
    this.settings = getSharedPreferences("BleLight", 0);
    getCommonColors();
    initView();
    this.mColorWheelView = (ColorWheelView1)findViewById(2131230997);
    this.img_colorwheel = (ImageView)findViewById(2131230880);
    this.img_pictrue = (ImageView)findViewById(2131230916);
    this.mSeekBar = (SeekBar)findViewById(2131231123);
    this.img_slideline = (RotateableView)findViewById(2131230927);
    this.rel_rgb_all = (RelativeLayout)findViewById(2131231080);
    this.rel_rgb_all.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            return true;
          }
        });
    this.lin_rgb = (LinearLayout)findViewById(2131230969);
    this.tv_r = (TextView)findViewById(2131231216);
    this.tv_g = (TextView)findViewById(2131231209);
    this.tv_b = (TextView)findViewById(2131231193);
    initRGB();
    this.lin_rgb.setVisibility(8);
    this.lin_rgb.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AdjustActivity.this.rel_rgb_all.setVisibility(0);
            AdjustActivity.this.seekbar_speed_r.setProgress(Color.red(AdjustActivity.this.cachecolor));
            AdjustActivity.this.seekbar_speed_g.setProgress(Color.green(AdjustActivity.this.cachecolor));
            AdjustActivity.this.seekbar_speed_b.setProgress(Color.blue(AdjustActivity.this.cachecolor));
            TextView textView2 = AdjustActivity.this.tv_setr_num;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("");
            stringBuilder3.append(Color.red(AdjustActivity.this.cachecolor));
            textView2.setText(stringBuilder3.toString());
            TextView textView3 = AdjustActivity.this.tv_setg_num;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("");
            stringBuilder1.append(Color.green(AdjustActivity.this.cachecolor));
            textView3.setText(stringBuilder1.toString());
            TextView textView1 = AdjustActivity.this.tv_setb_num;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(Color.blue(AdjustActivity.this.cachecolor));
            textView1.setText(stringBuilder2.toString());
            AdjustActivity.this.setRGBtv(false);
          }
        });
    this.mColorWheelView.invalidate();
    this.img_colorwheel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (AdjustActivity.this.isPicture) {
              AdjustActivity.this.mColorWheelView.setPicture(false, null);
              AdjustActivity.this.isPicture = false;
              if (AdjustActivity.this.mColorWheelView.getRing() && AdjustActivity.this.img_slideline.getVisibility() == 8)
                AdjustActivity.this.img_slideline.setVisibility(0); 
              if (AdjustActivity.this.img_pictrue.getVisibility() == 8)
                AdjustActivity.this.img_pictrue.setVisibility(0); 
              AdjustActivity.this.showColors(true);
              if (AdjustActivity.this.mMyApplication.isHaveTri()) {
                AdjustActivity.this.lin_rgb.setVisibility(8);
              } else if (!AdjustActivity.this.isPicture) {
                AdjustActivity.this.lin_rgb.setVisibility(0);
              } 
              return;
            } 
            if (AdjustActivity.this.mColorWheelView.getRing()) {
              AdjustActivity.this.img_colorwheel.setImageResource(2131165369);
              AdjustActivity.this.img_slideline.setVisibility(8);
            } else {
              AdjustActivity.this.img_colorwheel.setImageResource(2131165368);
              AdjustActivity.this.img_slideline.setVisibility(0);
            } 
            AdjustActivity.this.mColorWheelView.setRing(AdjustActivity.this.mColorWheelView.getRing() ^ true);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.get(7);
            int i = calendar.get(1) % 100;
            i /= 10;
            calendar.get(2);
            calendar.get(5);
            calendar.get(11);
            calendar.get(12);
            calendar.get(13);
            Display display = AdjustActivity.this.getWindowManager().getDefaultDisplay();
            int j = display.getWidth();
            i = display.getHeight();
            AdjustActivity.mainW = display.getWidth();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Width = ");
            stringBuilder.append(j);
            Log.e("Main", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("Height = ");
            stringBuilder.append(i);
            Log.e("Main", stringBuilder.toString());
          }
        });
    if (this.mColorWheelView.getRing()) {
      this.img_colorwheel.setImageResource(2131165368);
      this.img_slideline.setVisibility(0);
    } else {
      this.img_colorwheel.setImageResource(2131165369);
      this.img_slideline.setVisibility(8);
    } 
    this.img_pictrue.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (AdjustActivity.this.checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
              if (AdjustActivity.this.isPicture) {
                AdjustActivity.this.mColorWheelView.setPicture(false, null);
                AdjustActivity.this.isPicture = false;
                AdjustActivity.this.showColors(true);
              } else {
                AdjustActivity.this.openPictrue();
                AdjustActivity.this.img_slideline.setVisibility(8);
              } 
            } else {
              AdjustActivity.this.getPermission("android.permission.READ_EXTERNAL_STORAGE", new String[] { "android.permission.READ_EXTERNAL_STORAGE" });
            } 
          }
        });
    this.mColorWheelView.setOnColorChangeInterface(new ColorWheelView1.ColorChangeInterface() {
          public void colorChange(int param1Int, float param1Float, boolean param1Boolean) {
            byte b;
            if (AdjustActivity.this.mSeekBar.getProgress() > 0) {
              b = AdjustActivity.this.mSeekBar.getProgress();
            } else {
              b = 2;
            } 
            final MyColor mycolor = new MyColor(param1Int, (byte)0, b);
            AdjustActivity.this.setColors(myColor, false);
            if (AdjustActivity.this.clickNum != 0)
              AdjustActivity.this.saveCommonColor(param1Int, param1Boolean); 
            AdjustActivity.this.mSunView.setColor(param1Int);
            if (param1Boolean) {
              AdjustActivity.this.mAdjustHandler.postDelayed(new Runnable() {
                    public void run() {
                      AdjustActivity.this.setColors(mycolor, true);
                    }
                  }50L);
              if (AdjustActivity.this.mMyApplication.mainHandler != null)
                AdjustActivity.this.mMyApplication.mainHandler.sendEmptyMessage(60001); 
            } 
            if (AdjustActivity.this.mColorWheelView.getRing()) {
              AdjustActivity.access$302(AdjustActivity.this, param1Float);
              AdjustActivity.this.mAdjustHandler.sendEmptyMessage(1);
            } 
          }
        });
    this.mSeekBar.setOnSeekBarChangeListener(this.myOnSeekBarChangeListener);
    this.lin_mic = (LinearLayout)findViewById(2131230966);
    this.mic_open = (ImageView)findViewById(2131230987);
    uimic();
    this.mic_open.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AdjustActivity.this.mMyApplication.isopenmic ^= 0x1;
            AdjustActivity.this.mMyApplication.openmic(AdjustActivity.this.mMyApplication.isopenmic);
            AdjustActivity.this.uimic();
            AdjustActivity.this.mMyApplication.setMusicHop(false);
            SharedPreferences.Editor editor = AdjustActivity.this.settings.edit();
            editor.putBoolean("isOpenMusicHop", false);
            editor.commit();
          }
        });
    if (this.mMyApplication.isopenmic()) {
      this.lin_mic.setVisibility(0);
    } else {
      this.lin_mic.setVisibility(8);
    } 
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
  
  protected void onResume() {
    if (this.mMyApplication == null)
      return; 
    try {
      if (this.mMyApplication.isopenmic()) {
        this.lin_mic.setVisibility(0);
        Log.e("isopenmic", "isopenmic");
      } else {
        this.lin_mic.setVisibility(8);
      } 
    } catch (Exception exception) {}
    super.onResume();
  }
  
  public void openPictrue() {
    Intent intent = new Intent("android.intent.action.PICK", null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(intent, 100);
  }
  
  public void setBG(ImageView paramImageView, int paramInt) {
    if (paramImageView != null) {
      if (this.imgcache == paramImageView && System.currentTimeMillis() - this.setbgtime < 50L)
        return; 
      paramImageView.setColorFilter(paramInt);
      this.imgcache = paramImageView;
      this.setbgtime = System.currentTimeMillis();
    } 
  }
  
  public void setRGBtv(boolean paramBoolean) {
    TextView textView1 = this.tv_setb_num;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(this.seekbar_speed_b.getProgress());
    textView1.setText(stringBuilder2.toString());
    textView1 = this.tv_setr_num;
    stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(this.seekbar_speed_r.getProgress());
    textView1.setText(stringBuilder2.toString());
    TextView textView2 = this.tv_setg_num;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("");
    stringBuilder1.append(this.seekbar_speed_g.getProgress());
    textView2.setText(stringBuilder1.toString());
    setBG(this.img_rgb, Color.argb(255, this.seekbar_speed_r.getProgress(), this.seekbar_speed_g.getProgress(), this.seekbar_speed_b.getProgress()));
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("setBG ");
    stringBuilder1.append(this.seekbar_speed_r.getProgress());
    stringBuilder1.append(" ");
    stringBuilder1.append(this.seekbar_speed_g.getProgress());
    stringBuilder1.append(" ");
    stringBuilder1.append(this.seekbar_speed_b.getProgress());
    Log.e("setBG ", stringBuilder1.toString());
  }
  
  public void uimic() {
    if (this.mMyApplication.isopenmic) {
      this.mic_open.setImageResource(2131165458);
    } else {
      this.mic_open.setImageResource(2131165457);
    } 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\AdjustActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */