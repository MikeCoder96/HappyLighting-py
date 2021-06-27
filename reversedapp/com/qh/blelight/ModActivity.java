package com.qh.blelight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.qh.WheelView.WheelViewAdapter;
import com.qh.WheelView.loopview.LoopView;
import com.qh.WheelView.loopview.OnItemSelectedListener;
import com.qh.tools.Tool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class ModActivity extends Activity {
  private ImageView cacheImageView;
  
  private TextView cacheTextView;
  
  public int colorID = 0;
  
  public String[] data = new String[] { 
      "�߲ʽ���", "��ɫ����", "��ɫ����", "��ɫ����", "��ɫ����", "��ɫ����", "��ɫ����", "��ɫ����", "���̽���", "��������", 
      "��������", "�߲�Ƶ��", "��ɫƵ��", "��ɫƵ��", "��ɫƵ��", "��ɫƵ��", "��ɫƵ��", "��ɫƵ��", "��ɫƵ��", "�߲�����" };
  
  public String[] data2 = new String[256];
  
  public int[] dataNewMod = new int[] { 
      2131492867, 2131492874, 2131492875, 2131492876, 2131492877, 2131492878, 2131492879, 2131492880, 2131492881, 2131492868, 
      2131492869, 2131492870, 2131492871, 2131492872, 2131492873 };
  
  public int huancaispeed;
  
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
  
  public ImageView ic_select_hook1;
  
  public ImageView ic_select_hook2;
  
  public ImageView ic_select_hook3;
  
  public ImageView ic_select_hook4;
  
  public ImageView ic_select_hook5;
  
  public ImageView ic_select_hook6;
  
  public int id = 0;
  
  public ImageView img_mod_color1;
  
  public ImageView img_mod_color10;
  
  public ImageView img_mod_color11;
  
  public ImageView img_mod_color12;
  
  public ImageView img_mod_color2;
  
  public ImageView img_mod_color3;
  
  public ImageView img_mod_color4;
  
  public ImageView img_mod_color5;
  
  public ImageView img_mod_color6;
  
  public ImageView img_mod_color7;
  
  public ImageView img_mod_color8;
  
  public ImageView img_mod_color9;
  
  private ImageView img_x;
  
  private HashMap<Integer, ImageView> imgs = new HashMap<Integer, ImageView>();
  
  public boolean isOnlyDreamY = false;
  
  public ArrayList<String> item1 = new ArrayList<String>();
  
  public ArrayList<String> item2 = new ArrayList<String>();
  
  private LinearLayoutManager linearLayoutManager;
  
  public LoopView loopview;
  
  private Context mContext;
  
  private LayoutInflater mInflator;
  
  public Handler mModHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 0)
            ModActivity.this.onresume(); 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("ishaveT=");
          stringBuilder.append(ModActivity.this.mMyApplication.ishaveT);
          Log.e("ishaveT", stringBuilder.toString());
          stringBuilder = new StringBuilder();
          stringBuilder.append("ishaveDream=");
          stringBuilder.append(ModActivity.this.mMyApplication.ishaveDream);
          Log.e("ishaveT", stringBuilder.toString());
          stringBuilder = new StringBuilder();
          stringBuilder.append("ishaveColor=");
          stringBuilder.append(ModActivity.this.mMyApplication.ishaveColor);
          Log.e("ishaveT", stringBuilder.toString());
          if (param1Message.what == 1) {
            if (ModActivity.this.mMyApplication.ishaveDream || ModActivity.this.mMyApplication.ishaveColor) {
              ModActivity.this.tv_huancai.setVisibility(0);
              if (ModActivity.this.mMyApplication.ishaveT) {
                ModActivity.this.tv_qicai.setVisibility(0);
              } else {
                ModActivity.this.tv_qicai.setVisibility(8);
                if (!ModActivity.this.mMyApplication.isOnlyHaveX())
                  ModActivity.this.setMod(2); 
                if (ModActivity.this.mMyApplication.ishaveColor && !ModActivity.this.mMyApplication.ishaveDream) {
                  ModActivity.this.rel_b.setVisibility(8);
                } else {
                  ModActivity.this.rel_b.setVisibility(0);
                } 
              } 
            } else {
              ModActivity.this.tv_huancai.setVisibility(8);
              ModActivity.this.tv_qicai.setVisibility(0);
              ModActivity.this.mMyApplication.isOnlyHaveX();
              ModActivity.this.setMod(1);
            } 
            if (ModActivity.this.mMyApplication.isHaveX()) {
              if (ModActivity.this.mMyApplication.isOnlyHaveX()) {
                ModActivity.this.setMod(3);
                ModActivity.this.img_x.setVisibility(0);
                ModActivity.this.tv_huancai.setVisibility(8);
                ModActivity.this.tv_qicai.setVisibility(8);
              } else {
                ModActivity.this.img_x.setVisibility(0);
              } 
            } else {
              ModActivity.this.img_x.setVisibility(8);
            } 
          } 
          if (param1Message.what == 301)
            ModActivity.this.setModTv(); 
          return false;
        }
      });
  
  public MyApplication mMyApplication;
  
  public PagerAdapter mPagerAdapter = new PagerAdapter() {
      public void destroyItem(View param1View, int param1Int, Object param1Object) {
        ((ViewPager)param1View).removeView(ModActivity.this.pageview.get(param1Int));
      }
      
      public int getCount() {
        return ModActivity.this.pageview.size();
      }
      
      public Object instantiateItem(View param1View, int param1Int) {
        ((ViewPager)param1View).addView(ModActivity.this.pageview.get(param1Int));
        return ModActivity.this.pageview.get(param1Int);
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
    };
  
  public Resources mResources;
  
  private HashMap<Integer, TextView> mTextViews = new HashMap<Integer, TextView>();
  
  public int modType = 1;
  
  public int modid = 0;
  
  public int[] modselects = new int[] { 0, 1, 2, 3, 4, 5 };
  
  public SeekBar.OnSeekBarChangeListener myOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
      
      public void onStartTrackingTouch(SeekBar param1SeekBar) {}
      
      public void onStopTrackingTouch(SeekBar param1SeekBar) {
        ModActivity.this.mMyApplication.sendNewMod(ModActivity.this.modid, ModActivity.this.seekbar_mod_speed.getProgress(), ModActivity.this.seekbar_mod_br.getProgress(), ModActivity.this.colorID);
      }
    };
  
  public View.OnClickListener mynewModOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        switch (param1View.getId()) {
          case 2131230912:
            ModActivity.this.colorID = 8;
            break;
          case 2131230911:
            ModActivity.this.colorID = 7;
            break;
          case 2131230910:
            ModActivity.this.colorID = 6;
            break;
          case 2131230909:
            ModActivity.this.colorID = 5;
            break;
          case 2131230908:
            ModActivity.this.colorID = 4;
            break;
          case 2131230907:
            ModActivity.this.colorID = 3;
            break;
          case 2131230906:
            ModActivity.this.colorID = 2;
            break;
          case 2131230905:
            ModActivity.this.colorID = 1;
            break;
          case 2131230904:
            ModActivity.this.colorID = 11;
            break;
          case 2131230903:
            ModActivity.this.colorID = 10;
            break;
          case 2131230902:
            ModActivity.this.colorID = 9;
            break;
          case 2131230901:
            ModActivity.this.colorID = 0;
            break;
        } 
        ModActivity.this.select(ModActivity.this.colorID);
        ModActivity.this.mMyApplication.sendNewMod(ModActivity.this.modid, ModActivity.this.seekbar_mod_speed.getProgress(), ModActivity.this.seekbar_mod_br.getProgress(), ModActivity.this.colorID);
      }
    };
  
  public int[] newmodcolors = new int[] { -65536, -16711936, -16776961, -1280, -11862022, -5373697 };
  
  public ArrayList<View> pageview = new ArrayList<View>();
  
  public int qicaispeed;
  
  public RelativeLayout rel_b;
  
  public RelativeLayout rel_f;
  
  public LinearLayout rel_mod1;
  
  public RelativeLayout rel_mod2;
  
  public RecyclerView rl_view;
  
  public SeekBar seekbar_b;
  
  public SeekBar seekbar_f;
  
  public SeekBar seekbar_mod_br;
  
  public SeekBar seekbar_mod_speed;
  
  public SeekBar seekbar_speed;
  
  public View.OnClickListener selectModOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        ModActivity.this.ic_select_hook1.setVisibility(8);
        ModActivity.this.ic_select_hook2.setVisibility(8);
        ModActivity.this.ic_select_hook3.setVisibility(8);
        ModActivity.this.ic_select_hook4.setVisibility(8);
        ModActivity.this.ic_select_hook5.setVisibility(8);
        ModActivity.this.ic_select_hook6.setVisibility(8);
        int i = param1View.getId();
        boolean bool = false;
        switch (i) {
          case 2131231223:
            if (ModActivity.this.selectModid == 5) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 5;
            ModActivity.this.ic_select_hook6.setVisibility(0);
            break;
          case 2131231222:
            if (ModActivity.this.selectModid == 4) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 4;
            ModActivity.this.ic_select_hook5.setVisibility(0);
            break;
          case 2131231221:
            if (ModActivity.this.selectModid == 3) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 3;
            ModActivity.this.ic_select_hook4.setVisibility(0);
            break;
          case 2131231220:
            if (ModActivity.this.selectModid == 2) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 2;
            ModActivity.this.ic_select_hook3.setVisibility(0);
            break;
          case 2131231219:
            if (ModActivity.this.selectModid == 1) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 1;
            ModActivity.this.ic_select_hook2.setVisibility(0);
            break;
          case 2131231218:
            if (ModActivity.this.selectModid == 0) {
              ModActivity.this.selectModid = -1;
              break;
            } 
            ModActivity.this.selectModid = 0;
            ModActivity.this.ic_select_hook1.setVisibility(0);
            break;
        } 
        i = bool;
        if (ModActivity.this.mMyApplication.myMediaPlayer != null) {
          i = bool;
          if (ModActivity.this.mMyApplication.myMediaPlayer.isPlaying())
            i = 1; 
        } 
        if (i != 0)
          ModActivity.this.mModHandler.postDelayed(new Runnable() {
                public void run() {
                  if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
                    ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
                  } else if (ModActivity.this.selectModid != -1 && ModActivity.this.selectModid >= 0 && ModActivity.this.selectModid < ModActivity.this.modselects.length && ModActivity.this.modType != 1) {
                    ModActivity.this.setMod(ModActivity.this.modselects[ModActivity.this.selectModid], ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
                    ModActivity.this.id = ModActivity.this.modselects[ModActivity.this.selectModid];
                    ModActivity.this.loopview.setCurrentPosition(ModActivity.this.modselects[ModActivity.this.selectModid]);
                  } 
                }
              }200L); 
        if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
          ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
        } else if (ModActivity.this.selectModid != -1 && ModActivity.this.selectModid >= 0 && ModActivity.this.selectModid < ModActivity.this.modselects.length && ModActivity.this.modType != 1) {
          ModActivity.this.setMod(ModActivity.this.modselects[ModActivity.this.selectModid], ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
          ModActivity.this.id = ModActivity.this.modselects[ModActivity.this.selectModid];
          ModActivity.this.loopview.setCurrentPosition(ModActivity.this.modselects[ModActivity.this.selectModid]);
        } 
      }
    };
  
  public int selectModid = -1;
  
  public RelativeLayout select_mod;
  
  public int speed = 16;
  
  public View.OnClickListener tvOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        ModActivity.this.tv_1.setTextColor(ModActivity.this.getResources().getColor(2131034216));
        ModActivity.this.tv_50.setTextColor(ModActivity.this.getResources().getColor(2131034216));
        ModActivity.this.tv_100.setTextColor(ModActivity.this.getResources().getColor(2131034216));
        ModActivity.this.tv_150.setTextColor(ModActivity.this.getResources().getColor(2131034216));
        ModActivity.this.tv_200.setTextColor(ModActivity.this.getResources().getColor(2131034216));
        switch (param1View.getId()) {
          case 2131231190:
            ModActivity.this.loopview.setCurrentPosition(49);
            ModActivity.this.tv_50.setTextColor(ModActivity.this.getResources().getColor(2131034217));
            break;
          case 2131231188:
            ModActivity.this.loopview.setCurrentPosition(199);
            ModActivity.this.tv_200.setTextColor(ModActivity.this.getResources().getColor(2131034217));
            break;
          case 2131231187:
            ModActivity.this.loopview.setCurrentPosition(149);
            ModActivity.this.tv_150.setTextColor(ModActivity.this.getResources().getColor(2131034217));
            break;
          case 2131231185:
            ModActivity.this.loopview.setCurrentPosition(99);
            ModActivity.this.tv_100.setTextColor(ModActivity.this.getResources().getColor(2131034217));
            break;
          case 2131231184:
            ModActivity.this.loopview.setCurrentPosition(0);
            ModActivity.this.tv_1.setTextColor(ModActivity.this.getResources().getColor(2131034217));
            break;
        } 
        ModActivity.this.id = ModActivity.this.loopview.getSelectedItem();
      }
    };
  
  public TextView tv_1;
  
  public TextView tv_100;
  
  public TextView tv_150;
  
  public TextView tv_200;
  
  public TextView tv_50;
  
  public TextView tv_huancai;
  
  public TextView tv_qicai;
  
  public TextView tv_select1;
  
  public TextView tv_select2;
  
  public TextView tv_select3;
  
  public TextView tv_select4;
  
  public TextView tv_select5;
  
  public TextView tv_select6;
  
  private WeekAdapter weekAdapter;
  
  ArrayList<Integer> weekList = new ArrayList<Integer>();
  
  private void initData() {
    this.weekList.add(Integer.valueOf(0));
    this.weekList.add(Integer.valueOf(0));
    for (byte b = 0; b < this.dataNewMod.length; b++)
      this.weekList.add(Integer.valueOf(this.dataNewMod[b])); 
    this.weekList.add(Integer.valueOf(0));
    this.weekList.add(Integer.valueOf(0));
    this.weekList.add(Integer.valueOf(0));
  }
  
  private void initListener() {
    this.weekAdapter.changePostion(7);
    this.linearLayoutManager.scrollToPositionWithOffset(5, 0);
    this.linearLayoutManager.setStackFromEnd(true);
    this.modid = 5;
    this.weekAdapter.setOnWeekClickListener(new WeekAdapter.OnWeekClickListener() {
          public void scrollMid(int param1Int) {
            int i = param1Int - 2;
            if (i >= 0) {
              ModActivity.this.weekAdapter.changePostion(param1Int);
              ModActivity.this.linearLayoutManager.scrollToPositionWithOffset(i, 0);
              ModActivity.this.linearLayoutManager.setStackFromEnd(true);
            } 
            ModActivity.this.modid = i;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("pos=");
            stringBuilder.append(i);
            Log.e("--", stringBuilder.toString());
            ModActivity.this.mMyApplication.sendNewMod(ModActivity.this.modid, ModActivity.this.seekbar_mod_speed.getProgress(), ModActivity.this.seekbar_mod_br.getProgress(), ModActivity.this.colorID);
          }
        });
    this.rl_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
          public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {
            if (param1Int == 0) {
              int i = ModActivity.this.linearLayoutManager.findFirstVisibleItemPosition();
              param1Int = i;
              if (i >= ModActivity.this.dataNewMod.length - 1)
                param1Int = ModActivity.this.dataNewMod.length - 1; 
              ModActivity.this.weekAdapter.changePostion(param1Int + 2);
              ModActivity.this.linearLayoutManager.scrollToPositionWithOffset(param1Int, 0);
              ModActivity.this.linearLayoutManager.setStackFromEnd(true);
              ModActivity.this.modid = param1Int;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("newState=");
              stringBuilder.append(param1Int);
              Log.e("newState", stringBuilder.toString());
              ModActivity.this.mMyApplication.sendNewMod(ModActivity.this.modid, ModActivity.this.seekbar_mod_speed.getProgress(), ModActivity.this.seekbar_mod_br.getProgress(), ModActivity.this.colorID);
            } 
          }
          
          public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
        });
    this.img_x.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ModActivity.this.setMod(3);
          }
        });
    this.img_mod_color1.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color2.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color3.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color4.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color5.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color6.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color7.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color8.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color9.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color10.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color11.setOnClickListener(this.mynewModOnClickListener);
    this.img_mod_color12.setOnClickListener(this.mynewModOnClickListener);
    this.seekbar_mod_speed.setOnSeekBarChangeListener(this.myOnSeekBarChangeListener);
    this.seekbar_mod_br.setOnSeekBarChangeListener(this.myOnSeekBarChangeListener);
    this.tv_1 = (TextView)findViewById(2131231184);
    this.tv_50 = (TextView)findViewById(2131231190);
    this.tv_100 = (TextView)findViewById(2131231185);
    this.tv_150 = (TextView)findViewById(2131231187);
    this.tv_200 = (TextView)findViewById(2131231188);
    this.tv_1.setOnClickListener(this.tvOnClickListener);
    this.tv_50.setOnClickListener(this.tvOnClickListener);
    this.tv_100.setOnClickListener(this.tvOnClickListener);
    this.tv_150.setOnClickListener(this.tvOnClickListener);
    this.tv_200.setOnClickListener(this.tvOnClickListener);
  }
  
  private void initView() {
    this.linearLayoutManager = new LinearLayoutManager((Context)this, 0, false);
    this.rl_view.setLayoutManager((RecyclerView.LayoutManager)this.linearLayoutManager);
    if (this.weekAdapter == null) {
      this.weekAdapter = new WeekAdapter((Context)this, this.weekList);
      this.rl_view.setAdapter(this.weekAdapter);
    } else {
      this.weekAdapter.notifyDataSetChanged();
    } 
    this.img_mod_color1 = (ImageView)findViewById(2131230901);
    this.img_mod_color2 = (ImageView)findViewById(2131230905);
    this.img_mod_color3 = (ImageView)findViewById(2131230906);
    this.img_mod_color4 = (ImageView)findViewById(2131230907);
    this.img_mod_color5 = (ImageView)findViewById(2131230908);
    this.img_mod_color6 = (ImageView)findViewById(2131230909);
    this.img_mod_color7 = (ImageView)findViewById(2131230910);
    this.img_mod_color8 = (ImageView)findViewById(2131230911);
    this.img_mod_color9 = (ImageView)findViewById(2131230912);
    this.img_mod_color10 = (ImageView)findViewById(2131230902);
    this.img_mod_color11 = (ImageView)findViewById(2131230903);
    this.img_mod_color12 = (ImageView)findViewById(2131230904);
    setBG(this.img_mod_color1, this.newmodcolors[0]);
    setBG(this.img_mod_color2, this.newmodcolors[1]);
    setBG(this.img_mod_color3, this.newmodcolors[2]);
    setBG(this.img_mod_color4, this.newmodcolors[3]);
    setBG(this.img_mod_color5, this.newmodcolors[4]);
    setBG(this.img_mod_color6, this.newmodcolors[5]);
    this.seekbar_mod_speed = (SeekBar)findViewById(2131231127);
    this.seekbar_mod_br = (SeekBar)findViewById(2131231126);
    this.tv_select1 = (TextView)findViewById(2131231218);
    this.tv_select2 = (TextView)findViewById(2131231219);
    this.tv_select3 = (TextView)findViewById(2131231220);
    this.tv_select4 = (TextView)findViewById(2131231221);
    this.tv_select5 = (TextView)findViewById(2131231222);
    this.tv_select6 = (TextView)findViewById(2131231223);
    this.ic_select_hook1 = (ImageView)findViewById(2131230833);
    this.ic_select_hook2 = (ImageView)findViewById(2131230834);
    this.ic_select_hook3 = (ImageView)findViewById(2131230835);
    this.ic_select_hook4 = (ImageView)findViewById(2131230836);
    this.ic_select_hook5 = (ImageView)findViewById(2131230837);
    this.ic_select_hook6 = (ImageView)findViewById(2131230838);
    this.select_mod = (RelativeLayout)findViewById(2131231135);
    this.modselects[0] = this.mMyApplication.settings.getInt("selectmodid0", 0);
    this.modselects[1] = this.mMyApplication.settings.getInt("selectmodid1", 1);
    this.modselects[2] = this.mMyApplication.settings.getInt("selectmodid2", 2);
    this.modselects[3] = this.mMyApplication.settings.getInt("selectmodid3", 3);
    this.modselects[4] = this.mMyApplication.settings.getInt("selectmodid4", 4);
    this.modselects[5] = this.mMyApplication.settings.getInt("selectmodid5", 5);
    setModTv();
    this.tv_select1.setOnClickListener(this.selectModOnClickListener);
    this.tv_select2.setOnClickListener(this.selectModOnClickListener);
    this.tv_select3.setOnClickListener(this.selectModOnClickListener);
    this.tv_select4.setOnClickListener(this.selectModOnClickListener);
    this.tv_select5.setOnClickListener(this.selectModOnClickListener);
    this.tv_select6.setOnClickListener(this.selectModOnClickListener);
  }
  
  private int random(int paramInt) {
    return (new Random()).nextInt(paramInt) % (paramInt + 0 + 1) + 0;
  }
  
  private void setMODY(int paramInt1, int paramInt2) {
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          byte b = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
          myBluetoothGatt.setMODY(paramInt1, paramInt2); 
      } 
    } 
  }
  
  private void setMod(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int i = paramInt2;
    if (paramInt3 == 1) {
      int j = paramInt2;
      if (paramInt2 > 31)
        j = 31; 
      i = j;
      if (j < 1)
        i = 1; 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setMod mod=");
    stringBuilder.append(paramInt1);
    stringBuilder.append(" speed=");
    stringBuilder.append(i);
    Log.e("setMod", stringBuilder.toString());
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          paramInt2 = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2) {
          if (this.mMyApplication.isDreamf(myBluetoothGatt.getmLEdevice().getName())) {
            myBluetoothGatt.setDreamMod(paramInt1, i, paramInt4, paramInt5);
            continue;
          } 
          if (paramInt3 == 1) {
            myBluetoothGatt.setMod(paramInt1, i);
            continue;
          } 
          myBluetoothGatt.setSpeed(paramInt1, i, paramInt4);
        } 
      } 
    } 
  }
  
  private void setSpeed(int paramInt) {
    int i = paramInt;
    if (paramInt > 31)
      i = 31; 
    paramInt = i;
    if (i < 1)
      paramInt = 1; 
    this.mMyApplication.isopenmic = false;
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("speed=");
    stringBuilder.append(paramInt);
    Log.e("speed", stringBuilder.toString());
    this.mMyApplication.isopenmic = false;
    if (this.mMyApplication.AdjustHandler != null)
      this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          i = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
          myBluetoothGatt.setSpeed(paramInt); 
      } 
    } 
  }
  
  private void setSpeed(int paramInt1, int paramInt2, int paramInt3) {
    if (this.mMyApplication.isOpenMusicHop())
      this.mMyApplication.setMusicHop(false, true); 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    if (this.mMyApplication.AdjustHandler != null)
      this.mMyApplication.AdjustHandler.sendEmptyMessage(5); 
    for (String str : MainActivity.ControlMACs.keySet()) {
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null)
          byte b = myBluetoothGatt.datas[2]; 
      } 
      if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(str)) {
        MyBluetoothGatt myBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
        if (myBluetoothGatt != null && myBluetoothGatt.mConnectionState == 2)
          myBluetoothGatt.setSpeed(paramInt1, paramInt2, paramInt3); 
      } 
    } 
  }
  
  private void setWvData(final int index) {
    boolean bool;
    if (this.mMyApplication.myMediaPlayer != null && this.mMyApplication.myMediaPlayer.isPlaying()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mMyApplication.MusicHandler != null)
      this.mMyApplication.MusicHandler.sendEmptyMessage(2); 
    this.tv_1.setTextColor(getResources().getColor(2131034216));
    this.tv_50.setTextColor(getResources().getColor(2131034216));
    this.tv_100.setTextColor(getResources().getColor(2131034216));
    this.tv_150.setTextColor(getResources().getColor(2131034216));
    this.tv_200.setTextColor(getResources().getColor(2131034216));
    if (index >= 49 && index < 99) {
      this.tv_50.setTextColor(getResources().getColor(2131034217));
    } else if (index >= 99 && index < 149) {
      this.tv_100.setTextColor(getResources().getColor(2131034217));
    } else if (index >= 149 && index < 199) {
      this.tv_150.setTextColor(getResources().getColor(2131034217));
    } else if (index >= 199) {
      this.tv_200.setTextColor(getResources().getColor(2131034217));
    } else {
      this.tv_1.setTextColor(getResources().getColor(2131034217));
    } 
    if (this.mTextViews.containsKey(Integer.valueOf(index))) {
      TextView textView = this.mTextViews.get(Integer.valueOf(index));
      textView.setTextColor(-11872414);
      if (this.cacheTextView != null)
        this.cacheTextView.setTextColor(-1); 
      this.cacheTextView = textView;
    } 
    if (this.imgs.containsKey(Integer.valueOf(index))) {
      ImageView imageView = this.imgs.get(Integer.valueOf(index));
      imageView.setImageResource(2131165405);
      if (this.cacheImageView != null)
        this.cacheImageView.setImageResource(2131165408); 
      this.cacheImageView = imageView;
    } 
    if (index != this.id) {
      if (this.mTextViews.containsKey(Integer.valueOf(this.id)))
        ((TextView)this.mTextViews.get(Integer.valueOf(this.id))).setTextColor(getResources().getColor(2131034216)); 
      if (this.imgs.containsKey(Integer.valueOf(this.id)))
        ((ImageView)this.imgs.get(Integer.valueOf(this.id))).setImageResource(2131165408); 
    } 
    if (bool)
      this.mModHandler.postDelayed(new Runnable() {
            public void run() {
              if (ModActivity.this.modType == 1) {
                ModActivity.this.speed = 31 - ModActivity.this.seekbar_speed.getProgress();
                ModActivity.this.setMod(index, ModActivity.this.speed, ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
              } else {
                ModActivity.this.setMod(index, ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
                if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
                  ModActivity.this.selectModid = -1;
                  ModActivity.this.ic_select_hook1.setVisibility(8);
                } 
                if (ModActivity.this.selectModid != -1 && ModActivity.this.selectModid >= 0 && ModActivity.this.selectModid < ModActivity.this.modselects.length) {
                  ModActivity.this.modselects[ModActivity.this.selectModid] = index;
                  ModActivity.this.mModHandler.sendEmptyMessage(301);
                  ModActivity.this.save();
                } 
              } 
            }
          }200L); 
    if (this.modType == 1) {
      this.speed = 31 - this.seekbar_speed.getProgress();
      setMod(index, this.speed, this.modType, this.seekbar_b.getProgress(), this.seekbar_f.getProgress());
    } else {
      setMod(index, this.seekbar_speed.getProgress(), this.modType, this.seekbar_b.getProgress(), this.seekbar_f.getProgress());
      if (this.isOnlyDreamY && this.selectModid == 0) {
        this.selectModid = -1;
        this.ic_select_hook1.setVisibility(8);
      } 
      if (this.selectModid != -1 && this.selectModid >= 0 && this.selectModid < this.modselects.length) {
        this.modselects[this.selectModid] = index;
        this.mModHandler.sendEmptyMessage(301);
        save();
      } 
    } 
    if (this.mMyApplication.mainHandler != null)
      this.mMyApplication.mainHandler.sendEmptyMessage(60001); 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int i = displayMetrics.widthPixels;
    int j = displayMetrics.heightPixels;
    float f = i * 1.0F / j;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("widthPixels=");
    stringBuilder.append(i);
    stringBuilder.append(" heightPixels=");
    stringBuilder.append(j);
    Log.e("--", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("=");
    stringBuilder.append(f);
    Log.e("--", stringBuilder.toString());
    Log.e("--", "1920=0.5625");
    Log.e("--", "1280=0.5625");
    Log.e("--", "1280-=0.6081081");
    this.mContext = getApplicationContext();
    Tool.checkDeviceHasNavigationBar(this.mContext);
    setContentView(2131361831);
    this.mResources = getResources();
    this.data = this.mResources.getStringArray(2130837505);
    String str = this.mResources.getString(2131624039);
    boolean bool = false;
    for (j = 0; j < this.data2.length; j = i) {
      String[] arrayOfString = this.data2;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      i = j + 1;
      stringBuilder1.append(String.format("%03d", new Object[] { Integer.valueOf(i) }));
      arrayOfString[j] = stringBuilder1.toString();
    } 
    this.tv_qicai = (TextView)findViewById(2131231215);
    this.tv_huancai = (TextView)findViewById(2131231211);
    this.rel_b = (RelativeLayout)findViewById(2131231055);
    this.rel_b.setVisibility(8);
    this.mInflator = getLayoutInflater();
    this.loopview = (LoopView)findViewById(2131230981);
    this.loopview.setCenterTextColor(getResources().getColor(2131034217));
    this.loopview.setTextSize(18.0F);
    this.seekbar_speed = (SeekBar)findViewById(2131231129);
    this.seekbar_b = (SeekBar)findViewById(2131231124);
    this.mMyApplication = (MyApplication)getApplication();
    this.mMyApplication.ModHandler = this.mModHandler;
    if (this.mMyApplication != null && this.mMyApplication.mBluetoothLeService != null) {
      Iterator<String> iterator = this.mMyApplication.mBluetoothLeService.mDevices.keySet().iterator();
      while (iterator.hasNext()) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("--");
        stringBuilder1.append(iterator.next());
        Log.e("", stringBuilder1.toString());
      } 
    } 
    this.rel_f = (RelativeLayout)findViewById(2131231067);
    this.seekbar_f = (SeekBar)findViewById(2131231125);
    i = 0;
    while (true) {
      j = bool;
      if (i < this.data.length) {
        this.item1.add(this.data[i]);
        i++;
        continue;
      } 
      break;
    } 
    while (j < this.data2.length) {
      this.item2.add(this.data2[j]);
      j++;
    } 
    this.loopview.setItems(this.item1);
    this.loopview.setListener(new OnItemSelectedListener() {
          public void onItemSelected(int param1Int) {
            ModActivity.this.setWvData(param1Int);
          }
        });
    this.seekbar_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(final SeekBar seekBar) {
            if (ModActivity.this.modType == 1) {
              ModActivity.this.qicaispeed = ModActivity.this.seekbar_speed.getProgress();
            } else {
              ModActivity.this.huancaispeed = ModActivity.this.seekbar_speed.getProgress();
            } 
            boolean bool1 = false;
            boolean bool2 = bool1;
            if (ModActivity.this.mMyApplication.myMediaPlayer != null) {
              bool2 = bool1;
              if (ModActivity.this.mMyApplication.myMediaPlayer.isPlaying())
                bool2 = true; 
            } 
            if (bool2)
              ModActivity.this.mModHandler.postDelayed(new Runnable() {
                    public void run() {
                      if (ModActivity.this.modType == 1) {
                        ModActivity.this.speed = 31 - seekBar.getProgress();
                        ModActivity.this.setSpeed(ModActivity.this.speed);
                      } else if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
                        ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
                      } else {
                        ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
                      } 
                    }
                  }200L); 
            if (ModActivity.this.modType == 1) {
              ModActivity.this.speed = 31 - seekBar.getProgress();
              ModActivity.this.setSpeed(ModActivity.this.speed);
            } else if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
              ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
            } else {
              ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
            } 
          }
        });
    this.seekbar_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {
            boolean bool;
            if (ModActivity.this.mMyApplication.myMediaPlayer != null && ModActivity.this.mMyApplication.myMediaPlayer.isPlaying()) {
              bool = true;
            } else {
              bool = false;
            } 
            if (bool)
              ModActivity.this.mModHandler.postDelayed(new Runnable() {
                    public void run() {
                      if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
                        ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
                      } else {
                        ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
                      } 
                    }
                  }200L); 
            if (ModActivity.this.isOnlyDreamY && ModActivity.this.selectModid == 0) {
              ModActivity.this.setMODY(ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.seekbar_b.getProgress());
            } else {
              ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
            } 
          }
        });
    this.seekbar_f.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {
            boolean bool;
            if (ModActivity.this.mMyApplication.myMediaPlayer != null && ModActivity.this.mMyApplication.myMediaPlayer.isPlaying()) {
              bool = true;
            } else {
              bool = false;
            } 
            if (bool)
              ModActivity.this.mModHandler.postDelayed(new Runnable() {
                    public void run() {
                      ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
                    }
                  }200L); 
            ModActivity.this.setMod(ModActivity.this.loopview.getSelectedItem(), ModActivity.this.seekbar_speed.getProgress(), ModActivity.this.modType, ModActivity.this.seekbar_b.getProgress(), ModActivity.this.seekbar_f.getProgress());
          }
        });
    this.tv_qicai.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (1 == ModActivity.this.modType)
              return; 
            ModActivity.this.seekbar_speed.setProgress(ModActivity.this.qicaispeed);
            ModActivity.this.setMod(1);
          }
        });
    this.tv_huancai.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (2 == ModActivity.this.modType)
              return; 
            ModActivity.this.seekbar_speed.setProgress(ModActivity.this.huancaispeed);
            ModActivity.this.setMod(2);
          }
        });
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
    this.rel_mod1 = (LinearLayout)findViewById(2131231073);
    this.rel_mod2 = (RelativeLayout)findViewById(2131231074);
    this.img_x = (ImageView)findViewById(2131230931);
    this.rl_view = (RecyclerView)findViewById(2131231108);
    initData();
    initView();
    initListener();
    this.mModHandler.sendEmptyMessage(1);
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
  
  protected void onresume() {
    (new Handler()).postDelayed(new Runnable() {
          public void run() {
            if (ModActivity.this.loopview != null)
              if (ModActivity.this.modType == 1) {
                if (ModActivity.this.data != null && ModActivity.this.data.length > 0) {
                  int i = ModActivity.this.random(ModActivity.this.data.length);
                  ModActivity.this.id = i;
                  ModActivity.this.loopview.setCurrentPosition(i);
                  ModActivity.this.setWvData(i);
                } 
              } else if (ModActivity.this.modType == 2 && ModActivity.this.data2 != null && ModActivity.this.data2.length > 0) {
                int i = ModActivity.this.random(ModActivity.this.data2.length);
                ModActivity.this.id = i;
                ModActivity.this.loopview.setCurrentPosition(i);
                ModActivity.this.setWvData(i);
              }  
          }
        },  200L);
  }
  
  public void save() {
    if (this.selectModid != -1 && this.selectModid >= 0 && this.selectModid < this.modselects.length && this.modType != 1) {
      SharedPreferences.Editor editor = this.mMyApplication.settings.edit();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("selectmodid");
      stringBuilder.append(this.selectModid);
      editor.putInt(stringBuilder.toString(), this.modselects[this.selectModid]);
      editor.commit();
    } 
  }
  
  public void select(int paramInt) {
    this.ic_hook1.setVisibility(8);
    this.ic_hook2.setVisibility(8);
    this.ic_hook3.setVisibility(8);
    this.ic_hook4.setVisibility(8);
    this.ic_hook5.setVisibility(8);
    this.ic_hook6.setVisibility(8);
    this.ic_hook7.setVisibility(8);
    this.ic_hook8.setVisibility(8);
    this.ic_hook9.setVisibility(8);
    this.ic_hook10.setVisibility(8);
    this.ic_hook11.setVisibility(8);
    this.ic_hook12.setVisibility(8);
    switch (paramInt) {
      default:
        return;
      case 11:
        this.ic_hook12.setVisibility(0);
      case 10:
        this.ic_hook11.setVisibility(0);
      case 9:
        this.ic_hook10.setVisibility(0);
      case 8:
        this.ic_hook9.setVisibility(0);
      case 7:
        this.ic_hook8.setVisibility(0);
      case 6:
        this.ic_hook7.setVisibility(0);
      case 5:
        this.ic_hook6.setVisibility(0);
      case 4:
        this.ic_hook5.setVisibility(0);
      case 3:
        this.ic_hook4.setVisibility(0);
      case 2:
        this.ic_hook3.setVisibility(0);
      case 1:
        this.ic_hook2.setVisibility(0);
      case 0:
        break;
    } 
    this.ic_hook1.setVisibility(0);
  }
  
  public void setBG(ImageView paramImageView, int paramInt) {
    if (paramImageView != null)
      paramImageView.setColorFilter(paramInt); 
  }
  
  public void setMod(int paramInt) {
    this.id = 0;
    if (paramInt == 1) {
      if (this.mMyApplication.ishavesQHM()) {
        String[] arrayOfString1 = this.mResources.getStringArray(2130837505);
        String[] arrayOfString2 = this.mResources.getStringArray(2130837506);
        String[] arrayOfString3 = new String[arrayOfString1.length + arrayOfString2.length];
        for (paramInt = 0; paramInt < arrayOfString3.length; paramInt++) {
          if (paramInt < arrayOfString1.length) {
            arrayOfString3[paramInt] = arrayOfString1[paramInt];
          } else {
            arrayOfString3[paramInt] = arrayOfString2[paramInt - arrayOfString1.length];
          } 
        } 
        this.data = arrayOfString3;
      } else if (this.mMyApplication.ishavess()) {
        String[] arrayOfString2 = this.mResources.getStringArray(2130837505);
        String[] arrayOfString1 = this.mResources.getStringArray(2130837510);
        String[] arrayOfString3 = new String[arrayOfString2.length + arrayOfString1.length];
        for (paramInt = 0; paramInt < arrayOfString3.length; paramInt++) {
          if (paramInt < arrayOfString2.length) {
            arrayOfString3[paramInt] = arrayOfString2[paramInt];
          } else {
            arrayOfString3[paramInt] = arrayOfString1[paramInt - arrayOfString2.length];
          } 
        } 
        this.data = arrayOfString3;
      } else {
        this.data = this.mResources.getStringArray(2130837505);
      } 
      this.modType = 1;
      if (this.item1 != null)
        this.item1.clear(); 
      for (paramInt = 0; paramInt < this.data.length; paramInt++)
        this.item1.add(this.data[paramInt]); 
      this.loopview.setItems(this.item1);
      this.tv_qicai.setBackgroundResource(2131165297);
      this.tv_huancai.setBackgroundResource(2131165298);
      this.rel_b.setVisibility(8);
      this.rel_f.setVisibility(8);
      this.img_x.setImageResource(2131165407);
      this.rel_mod1.setVisibility(0);
      this.rel_mod2.setVisibility(8);
      this.select_mod.setVisibility(8);
    } else if (paramInt == 2) {
      this.modType = 2;
      String str = this.mResources.getString(2131624039);
      if (this.mMyApplication.ishaveColor && !this.mMyApplication.ishaveDream) {
        this.data2 = new String[30];
        for (paramInt = 0; paramInt < this.data2.length; paramInt = i) {
          String[] arrayOfString = this.data2;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(str);
          int i = paramInt + 1;
          stringBuilder1.append(String.format("%03d", new Object[] { Integer.valueOf(i) }));
          arrayOfString[paramInt] = stringBuilder1.toString();
        } 
        this.rel_b.setVisibility(8);
        this.rel_f.setVisibility(8);
        this.select_mod.setVisibility(8);
      } else {
        this.data2 = new String[256];
        for (paramInt = 0; paramInt < this.data2.length; paramInt = i) {
          String[] arrayOfString = this.data2;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(str);
          int i = paramInt + 1;
          stringBuilder1.append(String.format("%03d", new Object[] { Integer.valueOf(i) }));
          arrayOfString[paramInt] = stringBuilder1.toString();
        } 
        this.rel_b.setVisibility(0);
        this.select_mod.setVisibility(0);
        if (this.mMyApplication.isDreamf()) {
          this.rel_f.setVisibility(0);
          this.select_mod.setVisibility(8);
        } 
        if (this.mMyApplication.isOnlyDreamf()) {
          this.data2 = new String[210];
          for (paramInt = 0; paramInt < this.data2.length; paramInt = i) {
            String[] arrayOfString = this.data2;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(str);
            int i = paramInt + 1;
            stringBuilder1.append(String.format("%03d", new Object[] { Integer.valueOf(i) }));
            arrayOfString[paramInt] = stringBuilder1.toString();
          } 
        } 
        setModTv();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("isDreamf=");
      stringBuilder.append(this.mMyApplication.isDreamf());
      Log.e("66", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("isOnlyDreamf=");
      stringBuilder.append(this.mMyApplication.isOnlyDreamf());
      Log.e("66", stringBuilder.toString());
      if (this.item2 != null)
        this.item2.clear(); 
      for (paramInt = 0; paramInt < this.data2.length; paramInt++)
        this.item2.add(this.data2[paramInt]); 
      this.loopview.setItems(this.item2);
      this.tv_qicai.setBackgroundResource(2131165298);
      this.tv_huancai.setBackgroundResource(2131165297);
      this.seekbar_speed.setMax(255);
      this.seekbar_b.setMax(230);
      this.img_x.setImageResource(2131165407);
      this.rel_mod1.setVisibility(0);
      this.rel_mod2.setVisibility(8);
    } else {
      this.img_x.setImageResource(2131165406);
      this.tv_qicai.setBackgroundResource(2131165298);
      this.tv_huancai.setBackgroundResource(2131165298);
      this.rel_mod1.setVisibility(8);
      this.rel_mod2.setVisibility(0);
      this.modType = 3;
    } 
    this.loopview.setCurrentPosition(this.id);
  }
  
  public void setModTv() {
    if (this.mMyApplication.isIshaveDreamBLX()) {
      this.tv_select1.setText("AUTO");
      this.isOnlyDreamY = true;
    } else {
      this.isOnlyDreamY = false;
      TextView textView = this.tv_select1;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[0] + 1) }));
      textView.setText(stringBuilder.toString());
    } 
    TextView textView2 = this.tv_select2;
    StringBuilder stringBuilder4 = new StringBuilder();
    stringBuilder4.append("");
    stringBuilder4.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[1] + 1) }));
    textView2.setText(stringBuilder4.toString());
    TextView textView4 = this.tv_select3;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[2] + 1) }));
    textView4.setText(stringBuilder2.toString());
    TextView textView1 = this.tv_select4;
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append("");
    stringBuilder3.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[3] + 1) }));
    textView1.setText(stringBuilder3.toString());
    textView1 = this.tv_select5;
    stringBuilder3 = new StringBuilder();
    stringBuilder3.append("");
    stringBuilder3.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[4] + 1) }));
    textView1.setText(stringBuilder3.toString());
    TextView textView3 = this.tv_select6;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("");
    stringBuilder1.append(String.format("%03d", new Object[] { Integer.valueOf(this.modselects[5] + 1) }));
    textView3.setText(stringBuilder1.toString());
  }
  
  public class ModWheelViewAdapter implements WheelViewAdapter {
    private String[] data;
    
    public View getEmptyItem(View param1View, ViewGroup param1ViewGroup) {
      return param1View;
    }
    
    public View getItem(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = ModActivity.this.mInflator.inflate(2131361889, null);
      TextView textView = (TextView)view.findViewById(2131231244);
      textView.setText(this.data[param1Int]);
      ModActivity.this.mTextViews.put(Integer.valueOf(param1Int), textView);
      ImageView imageView = (ImageView)view.findViewById(2131230881);
      ModActivity.this.imgs.put(Integer.valueOf(param1Int), imageView);
      textView.setTextColor(-1);
      imageView.setImageResource(2131165408);
      if (ModActivity.this.id == param1Int) {
        textView.setTextColor(-11872414);
        imageView.setImageResource(2131165405);
      } else {
        textView.setTextColor(-1);
        imageView.setImageResource(2131165408);
      } 
      return view;
    }
    
    public int getItemsCount() {
      return this.data.length;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    public void setData(String[] param1ArrayOfString) {
      if (param1ArrayOfString != null && param1ArrayOfString.length > 0) {
        this.data = new String[param1ArrayOfString.length];
        for (byte b = 0; b < param1ArrayOfString.length; b++)
          this.data[b] = param1ArrayOfString[b]; 
      } 
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    private class ViewHolder {
      private ImageView m;
      
      private TextView tx;
    }
  }
  
  private class ViewHolder {
    private ImageView m;
    
    private TextView tx;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\ModActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */