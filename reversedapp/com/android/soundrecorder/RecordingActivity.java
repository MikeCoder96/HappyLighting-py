package com.android.soundrecorder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.qh.blelight.MainActivity;
import com.qh.blelight.MyApplication;
import com.qh.blelight.MyBluetoothGatt;
import java.util.Iterator;

public class RecordingActivity extends Activity {
  private static final int ANIMATIONEACHOFFSET = 600;
  
  private AnimationSet aniSet;
  
  private AnimationSet aniSet2;
  
  private AnimationSet aniSet3;
  
  private ImageView btn;
  
  public int dreamType = 0;
  
  private Handler handler = new Handler() {
      public void handleMessage(Message param1Message) {
        if (param1Message.what == 546) {
          RecordingActivity.this.wave2.startAnimation((Animation)RecordingActivity.this.aniSet2);
        } else if (param1Message.what == 819) {
          RecordingActivity.this.wave3.startAnimation((Animation)RecordingActivity.this.aniSet3);
        } 
        super.handleMessage(param1Message);
      }
    };
  
  public boolean isfrist = true;
  
  public boolean iswaiM = true;
  
  private MyApplication mMyApplication;
  
  public Handler mRecordinghandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NonNull Message param1Message) {
          Log.e("88", "88 ");
          if (!RecordingActivity.this.mMyApplication.isHaveColor_A_or_QHM()) {
            RecordingActivity.this.setVMtag(false);
            RecordingActivity.this.rel_color_add.setVisibility(8);
            RecordingActivity.this.rel_dreamxx.setVisibility(8);
            RecordingActivity.this.rel_g1.setVisibility(8);
            Log.e("22", "81");
          } else {
            RecordingActivity.this.setVMtag(true);
            Log.e("22", "82");
            RecordingActivity.this.rel_g1.setVisibility(0);
            if (RecordingActivity.this.isfrist) {
              RecordingActivity.this.isfrist = false;
              RecordingActivity.this.rg2.check(2131231025);
              RecordingActivity.this.iswaiM = true;
            } 
            if (RecordingActivity.this.mMyApplication.isHaveColor_A_or_QHM2()) {
              if (RecordingActivity.this.iswaiM) {
                RecordingActivity.this.rel_color_add.setVisibility(0);
              } else {
                RecordingActivity.this.rel_color_add.setVisibility(8);
              } 
            } else {
              RecordingActivity.this.rel_color_add.setVisibility(8);
            } 
            if (RecordingActivity.this.mMyApplication.isHaveDreamXX()) {
              if (RecordingActivity.this.iswaiM) {
                RecordingActivity.this.rel_dreamxx.setVisibility(0);
              } else {
                RecordingActivity.this.rel_dreamxx.setVisibility(8);
              } 
            } else {
              RecordingActivity.this.rel_dreamxx.setVisibility(8);
            } 
            RecordingActivity.this.sendColor_m_data(false);
          } 
          return false;
        }
      });
  
  public View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        switch (param1View.getId()) {
          case 2131231065:
            RecordingActivity.this.dreamType = 7;
            break;
          case 2131231064:
            RecordingActivity.this.dreamType = 6;
            break;
          case 2131231063:
            RecordingActivity.this.dreamType = 5;
            break;
          case 2131231062:
            RecordingActivity.this.dreamType = 4;
            break;
          case 2131231061:
            RecordingActivity.this.dreamType = 3;
            break;
          case 2131231060:
            RecordingActivity.this.dreamType = 2;
            break;
          case 2131231059:
            RecordingActivity.this.dreamType = 1;
            break;
          case 2131231058:
            RecordingActivity.this.dreamType = 0;
            break;
        } 
        RecordingActivity.this.sendDreamWM();
        RecordingActivity.this.showDreamxx();
      }
    };
  
  private RadioButton rb1;
  
  private RadioButton rb2;
  
  private RadioButton rb3;
  
  private RadioButton rb4;
  
  private RadioButton rba1;
  
  private RadioButton rba2;
  
  private RelativeLayout rel_color_add;
  
  public RelativeLayout rel_dream1;
  
  public RelativeLayout rel_dream2;
  
  public RelativeLayout rel_dream3;
  
  public RelativeLayout rel_dream4;
  
  public RelativeLayout rel_dream5;
  
  public RelativeLayout rel_dream6;
  
  public RelativeLayout rel_dream7;
  
  public RelativeLayout rel_dream8;
  
  private RelativeLayout rel_dreamxx;
  
  private RelativeLayout rel_g1;
  
  private RadioGroup rg1;
  
  private RadioGroup rg2;
  
  private SeekBar seekbar_speed;
  
  private SeekBar seekbar_speed1;
  
  public TextView tv_dream1;
  
  public TextView tv_dream2;
  
  public TextView tv_dream3;
  
  public TextView tv_dream4;
  
  public TextView tv_dream5;
  
  public TextView tv_dream6;
  
  public TextView tv_dream7;
  
  public TextView tv_dream8;
  
  private View v1;
  
  private View v2;
  
  private View v3;
  
  private View v4;
  
  private View v_m_1;
  
  private View v_m_2;
  
  private ImageView wave1;
  
  private ImageView wave2;
  
  private ImageView wave3;
  
  private void cancalWaveAnimation() {
    this.wave1.clearAnimation();
    this.wave2.clearAnimation();
    this.wave3.clearAnimation();
    this.wave1.setBackgroundResource(2131165348);
    this.wave2.setBackgroundResource(2131165348);
    this.wave3.setBackgroundResource(2131165348);
  }
  
  private AnimationSet getNewAnimationSet() {
    AnimationSet animationSet = new AnimationSet(true);
    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 3.3F, 1.0F, 3.3F, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setDuration(1800L);
    scaleAnimation.setRepeatCount(-1);
    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.1F);
    alphaAnimation.setRepeatCount(-1);
    animationSet.setDuration(1800L);
    animationSet.addAnimation((Animation)scaleAnimation);
    animationSet.addAnimation((Animation)alphaAnimation);
    return animationSet;
  }
  
  private void setMusicColor(int paramInt) {
    Iterator<String> iterator = MainActivity.ControlMACs.keySet().iterator();
    if (!this.mMyApplication.isOpenMusicHop())
      return; 
    while (iterator.hasNext()) {
      String str = iterator.next();
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = (MyBluetoothGatt)this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        bool2 = bool1;
        if (myBluetoothGatt != null) {
          bool2 = bool1;
          if ((myBluetoothGatt.datas[2] & 0xFF) == 35)
            bool2 = true; 
        } 
      } 
      if (bool2 && this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = (MyBluetoothGatt)this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2 && myBluetoothGatt.mLEdevice != null && !TextUtils.isEmpty(myBluetoothGatt.mLEdevice.getName()) && (myBluetoothGatt.mLEdevice.getName().contains("Dream#") || myBluetoothGatt.mLEdevice.getName().contains("Dream~")))
          myBluetoothGatt.setMusicColor(paramInt, 0.0D); 
      } 
    } 
  }
  
  private void setVMtag(boolean paramBoolean) {
    if (paramBoolean) {
      this.seekbar_speed.setVisibility(0);
      this.seekbar_speed1.setVisibility(8);
    } else {
      this.seekbar_speed.setVisibility(8);
      this.seekbar_speed1.setVisibility(0);
    } 
  }
  
  private void showWaveAnimation() {
    this.aniSet = getNewAnimationSet();
    this.aniSet2 = getNewAnimationSet();
    this.aniSet3 = getNewAnimationSet();
    this.wave1.startAnimation((Animation)this.aniSet);
    this.handler.sendEmptyMessageDelayed(546, 600L);
    this.handler.sendEmptyMessageDelayed(819, 1200L);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361830);
    this.rel_color_add = (RelativeLayout)findViewById(2131231057);
    this.rel_g1 = (RelativeLayout)findViewById(2131231068);
    this.rg1 = (RadioGroup)findViewById(2131231100);
    this.rb1 = (RadioButton)findViewById(2131231020);
    this.rb2 = (RadioButton)findViewById(2131231021);
    this.rb3 = (RadioButton)findViewById(2131231022);
    this.rb4 = (RadioButton)findViewById(2131231023);
    this.v1 = findViewById(2131231262);
    this.v2 = findViewById(2131231263);
    this.v3 = findViewById(2131231264);
    this.v4 = findViewById(2131231265);
    this.v_m_1 = findViewById(2131231267);
    this.v_m_2 = findViewById(2131231268);
    this.rg2 = (RadioGroup)findViewById(2131231101);
    this.rba1 = (RadioButton)findViewById(2131231024);
    this.rba2 = (RadioButton)findViewById(2131231025);
    this.rg1.check(2131231020);
    this.rg2.check(2131231024);
    this.btn = (ImageView)findViewById(2131230762);
    this.wave1 = (ImageView)findViewById(2131231271);
    this.wave2 = (ImageView)findViewById(2131231272);
    this.wave3 = (ImageView)findViewById(2131231273);
    this.rel_dreamxx = (RelativeLayout)findViewById(2131231066);
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.mMediaRecorderDemo.startRecord();
    this.mMyApplication.RecordingHandler = this.mRecordinghandler;
    this.seekbar_speed = (SeekBar)findViewById(2131231129);
    this.seekbar_speed1 = (SeekBar)findViewById(2131231130);
    this.seekbar_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onProgressChanged = ");
            stringBuilder.append(param1SeekBar.getProgress());
            Log.e("1", stringBuilder.toString());
            RecordingActivity.this.mMyApplication.limitdb = param1SeekBar.getProgress();
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("seekbar_speed: ");
            stringBuilder.append(RecordingActivity.this.mMyApplication.limitdb);
            Log.e(" = = =   ", stringBuilder.toString());
            RecordingActivity.this.mMyApplication.limitdb = param1SeekBar.getProgress();
            SharedPreferences.Editor editor = RecordingActivity.this.mMyApplication.settings.edit();
            editor.putInt("limitdb", RecordingActivity.this.mMyApplication.limitdb);
            editor.commit();
            if (RecordingActivity.this.mMyApplication.isHaveColor_A_or_QHM()) {
              RecordingActivity.this.sendColor_m_data();
              RecordingActivity.this.sendDreamWM();
            } 
          }
        });
    this.seekbar_speed1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onProgressChanged = ");
            stringBuilder.append(param1SeekBar.getProgress());
            Log.e("1", stringBuilder.toString());
            RecordingActivity.this.mMyApplication.limitdb = param1SeekBar.getProgress();
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {
            if (RecordingActivity.this.mMyApplication.isHaveColor_A_or_QHM()) {
              RecordingActivity.this.sendColor_m_data();
              RecordingActivity.this.sendDreamWM();
            } 
          }
        });
    this.rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("i=");
            stringBuilder2.append(param1RadioGroup.getCheckedRadioButtonId());
            Log.e("i", stringBuilder2.toString());
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("i=");
            stringBuilder1.append(param1Int);
            Log.e("i", stringBuilder1.toString());
          }
        });
    this.v1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Log.e("i", "rb1");
            RecordingActivity.this.rg1.check(2131231020);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
          }
        });
    this.v2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            RecordingActivity.this.rg1.check(2131231021);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
          }
        });
    this.v3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            RecordingActivity.this.rg1.check(2131231022);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
          }
        });
    this.v4.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            RecordingActivity.this.rg1.check(2131231023);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
          }
        });
    this.v_m_1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            RecordingActivity.this.setVMtag(false);
            RecordingActivity.this.cancalWaveAnimation();
            RecordingActivity.this.showWaveAnimation();
            Log.e("rba1", "rba1");
            RecordingActivity.this.rg2.check(2131231024);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
            RecordingActivity.this.mMyApplication.limitdb = RecordingActivity.this.seekbar_speed1.getProgress();
            RecordingActivity.this.mMyApplication.mMediaRecorderDemo.startRecord();
          }
        });
    this.v_m_2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            RecordingActivity.this.setVMtag(true);
            RecordingActivity.this.mMyApplication.mMediaRecorderDemo.stopRecord();
            RecordingActivity.this.cancalWaveAnimation();
            RecordingActivity.this.showWaveAnimation();
            Log.e("rba2", "rba2");
            RecordingActivity.this.rg2.check(2131231025);
            RecordingActivity.this.sendColor_m_data();
            RecordingActivity.this.sendDreamWM();
            RecordingActivity.this.mMyApplication.limitdb = RecordingActivity.this.seekbar_speed.getProgress();
          }
        });
    this.rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
            if (param1Int == 2131231025) {
              if (RecordingActivity.this.mMyApplication.isHaveColor_A_or_QHM2()) {
                RecordingActivity.this.rel_color_add.setVisibility(0);
              } else {
                RecordingActivity.this.rel_color_add.setVisibility(8);
              } 
              if (RecordingActivity.this.mMyApplication.isHaveDreamXX()) {
                RecordingActivity.this.rel_dreamxx.setVisibility(0);
              } else {
                RecordingActivity.this.rel_dreamxx.setVisibility(8);
              } 
            } else {
              RecordingActivity.this.rel_color_add.setVisibility(8);
              RecordingActivity.this.rel_dreamxx.setVisibility(8);
            } 
          }
        });
    this.rel_dream1 = (RelativeLayout)findViewById(2131231058);
    this.rel_dream2 = (RelativeLayout)findViewById(2131231059);
    this.rel_dream3 = (RelativeLayout)findViewById(2131231060);
    this.rel_dream4 = (RelativeLayout)findViewById(2131231061);
    this.rel_dream5 = (RelativeLayout)findViewById(2131231062);
    this.rel_dream6 = (RelativeLayout)findViewById(2131231063);
    this.rel_dream7 = (RelativeLayout)findViewById(2131231064);
    this.rel_dream8 = (RelativeLayout)findViewById(2131231065);
    this.tv_dream1 = (TextView)findViewById(2131231199);
    this.tv_dream2 = (TextView)findViewById(2131231200);
    this.tv_dream3 = (TextView)findViewById(2131231201);
    this.tv_dream4 = (TextView)findViewById(2131231202);
    this.tv_dream5 = (TextView)findViewById(2131231203);
    this.tv_dream6 = (TextView)findViewById(2131231204);
    this.tv_dream7 = (TextView)findViewById(2131231205);
    this.tv_dream8 = (TextView)findViewById(2131231206);
    this.rel_dream1.setOnClickListener(this.myOnClickListener);
    this.rel_dream2.setOnClickListener(this.myOnClickListener);
    this.rel_dream3.setOnClickListener(this.myOnClickListener);
    this.rel_dream4.setOnClickListener(this.myOnClickListener);
    this.rel_dream5.setOnClickListener(this.myOnClickListener);
    this.rel_dream6.setOnClickListener(this.myOnClickListener);
    this.rel_dream7.setOnClickListener(this.myOnClickListener);
    this.rel_dream8.setOnClickListener(this.myOnClickListener);
  }
  
  protected void onPause() {
    super.onPause();
    if (this.mMyApplication.mMediaRecorderDemo != null && this.mMyApplication.mMediaRecorderDemo.isPlay)
      this.mMyApplication.mMediaRecorderDemo.stopRecord(); 
  }
  
  protected void onRestart() {
    super.onRestart();
  }
  
  protected void onResume() {
    Log.e("--", "-onResume-");
    this.handler.postDelayed(new Runnable() {
          public void run() {
            RecordingActivity.this.setMusicColor(Color.argb(255, 60, 255, 0));
          }
        }200L);
    cancalWaveAnimation();
    this.mRecordinghandler.postDelayed(new Runnable() {
          public void run() {
            RecordingActivity.this.showWaveAnimation();
          }
        },  50L);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("^Color\\+|^Color-|^QHM|^Dream~=");
    stringBuilder.append(this.mMyApplication.isHaveColor_A_or_QHM());
    Log.e("----", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("^Color\\+|^Color-|^QHM=");
    stringBuilder.append(this.mMyApplication.isHaveColor_A_or_QHM2());
    Log.e("----", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("^Dream~=");
    stringBuilder.append(this.mMyApplication.isHaveDreamXX());
    Log.e("----", stringBuilder.toString());
    if (!this.mMyApplication.isHaveColor_A_or_QHM()) {
      this.rel_color_add.setVisibility(8);
      this.rel_dreamxx.setVisibility(8);
      if (!this.isfrist) {
        this.rel_g1.setVisibility(0);
      } else {
        this.rel_g1.setVisibility(8);
      } 
      this.iswaiM = false;
      this.mRecordinghandler.postDelayed(new Runnable() {
            public void run() {
              RecordingActivity.this.mMyApplication.mMediaRecorderDemo.startRecord();
            }
          },  100L);
      setVMtag(false);
    } else {
      this.rel_g1.setVisibility(0);
      setVMtag(true);
      if (this.isfrist) {
        this.isfrist = false;
        this.rg2.check(2131231025);
        this.iswaiM = true;
      } 
      if (this.mMyApplication.isHaveColor_A_or_QHM2()) {
        if (this.iswaiM) {
          this.rel_color_add.setVisibility(0);
        } else {
          this.rel_color_add.setVisibility(8);
        } 
      } else {
        this.rel_color_add.setVisibility(8);
      } 
      if (this.mMyApplication.isHaveDreamXX()) {
        if (this.iswaiM) {
          this.rel_dreamxx.setVisibility(0);
        } else {
          this.rel_dreamxx.setVisibility(8);
        } 
      } else {
        this.rel_dreamxx.setVisibility(8);
      } 
      sendColor_m_data();
      sendDreamWM();
    } 
    this.mRecordinghandler.postDelayed(new Runnable() {
          public void run() {
            RecordingActivity.this.mMyApplication.setData(new byte[] { -86, 0, -16, 85 }, );
          }
        },  50L);
    if (this.iswaiM) {
      this.mMyApplication.mMediaRecorderDemo.stopRecord();
    } else {
      this.mRecordinghandler.postDelayed(new Runnable() {
            public void run() {
              RecordingActivity.this.mMyApplication.mMediaRecorderDemo.startRecord();
            }
          },  100L);
    } 
    super.onResume();
  }
  
  protected void onStart() {
    super.onStart();
  }
  
  protected void onStop() {
    Log.e("onStop", "onStop-==");
    cancalWaveAnimation();
    if (!this.iswaiM)
      this.handler.postDelayed(new Runnable() {
            public void run() {
              RecordingActivity.this.setMusicColor(Color.argb(255, 60, 255, 0));
            }
          }200L); 
    super.onStop();
  }
  
  public void sendColor_m_data() {
    byte b;
    SeekBar seekBar;
    switch (this.rg1.getCheckedRadioButtonId()) {
      default:
        b = 0;
        break;
      case 2131231023:
        b = 3;
        break;
      case 2131231022:
        b = 2;
        break;
      case 2131231021:
        b = 1;
        break;
    } 
    if (this.rg2.getCheckedRadioButtonId() == 2131231025) {
      this.iswaiM = true;
      if (this.mMyApplication.isHaveColor_A_or_QHM2()) {
        if (this.iswaiM) {
          this.rel_color_add.setVisibility(0);
        } else {
          this.rel_color_add.setVisibility(8);
        } 
      } else {
        this.rel_color_add.setVisibility(8);
      } 
      if (this.mMyApplication.isHaveDreamXX()) {
        if (this.iswaiM) {
          this.rel_dreamxx.setVisibility(0);
        } else {
          this.rel_dreamxx.setVisibility(8);
        } 
      } else {
        this.rel_dreamxx.setVisibility(8);
      } 
    } else {
      this.iswaiM = false;
      this.rel_color_add.setVisibility(8);
      this.rel_dreamxx.setVisibility(8);
    } 
    MyApplication myApplication = this.mMyApplication;
    boolean bool = this.iswaiM;
    if (this.iswaiM) {
      seekBar = this.seekbar_speed;
    } else {
      seekBar = this.seekbar_speed1;
    } 
    int i = seekBar.getProgress();
    myApplication.sendColor_m_data(bool, i, b);
  }
  
  public void sendColor_m_data(boolean paramBoolean) {
    byte b;
    switch (this.rg1.getCheckedRadioButtonId()) {
      default:
        b = 0;
        break;
      case 2131231023:
        b = 3;
        break;
      case 2131231022:
        b = 2;
        break;
      case 2131231021:
        b = 1;
        break;
    } 
    if (this.rg2.getCheckedRadioButtonId() == 2131231025) {
      this.iswaiM = true;
    } else {
      this.iswaiM = false;
    } 
    if (paramBoolean) {
      SeekBar seekBar;
      MyApplication myApplication = this.mMyApplication;
      paramBoolean = this.iswaiM;
      if (this.iswaiM) {
        seekBar = this.seekbar_speed;
      } else {
        seekBar = this.seekbar_speed1;
      } 
      int i = seekBar.getProgress();
      myApplication.sendColor_m_data(paramBoolean, i, b);
    } else {
      SeekBar seekBar;
      MyApplication myApplication = this.mMyApplication;
      boolean bool = this.iswaiM;
      if (this.iswaiM) {
        seekBar = this.seekbar_speed;
      } else {
        seekBar = this.seekbar_speed1;
      } 
      int i = seekBar.getProgress();
      myApplication.sendColor_m_data(bool, i, b, paramBoolean);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("modid=");
    stringBuilder.append(b);
    stringBuilder.append(" iswaiM=");
    stringBuilder.append(this.iswaiM);
    Log.e("id", stringBuilder.toString());
  }
  
  public void sendDreamWM() {
    SeekBar seekBar;
    if (this.rg2.getCheckedRadioButtonId() == 2131231025) {
      this.iswaiM = true;
    } else {
      this.iswaiM = false;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("sendDreamWM= iswaiM=");
    stringBuilder.append(this.iswaiM);
    Log.e("sendDreamWM", stringBuilder.toString());
    MyApplication myApplication = this.mMyApplication;
    boolean bool = this.iswaiM;
    if (this.iswaiM) {
      seekBar = this.seekbar_speed;
    } else {
      seekBar = this.seekbar_speed1;
    } 
    int i = seekBar.getProgress();
    myApplication.sendDreamWM(bool, i, this.dreamType);
  }
  
  public void showDreamxx() {
    this.tv_dream1.setBackgroundResource(2131165296);
    this.tv_dream2.setBackgroundResource(2131165296);
    this.tv_dream3.setBackgroundResource(2131165296);
    this.tv_dream4.setBackgroundResource(2131165296);
    this.tv_dream5.setBackgroundResource(2131165296);
    this.tv_dream6.setBackgroundResource(2131165296);
    this.tv_dream7.setBackgroundResource(2131165296);
    this.tv_dream8.setBackgroundResource(2131165296);
    switch (this.dreamType) {
      default:
        return;
      case 7:
        this.tv_dream8.setBackgroundResource(2131165295);
      case 6:
        this.tv_dream7.setBackgroundResource(2131165295);
      case 5:
        this.tv_dream6.setBackgroundResource(2131165295);
      case 4:
        this.tv_dream5.setBackgroundResource(2131165295);
      case 3:
        this.tv_dream4.setBackgroundResource(2131165295);
      case 2:
        this.tv_dream3.setBackgroundResource(2131165295);
      case 1:
        this.tv_dream2.setBackgroundResource(2131165295);
      case 0:
        break;
    } 
    this.tv_dream1.setBackgroundResource(2131165295);
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\android\soundrecorder\RecordingActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */