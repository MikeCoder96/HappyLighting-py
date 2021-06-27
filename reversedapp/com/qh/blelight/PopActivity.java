package com.qh.blelight;

import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qh.WheelView.OnWheelScrollListener;
import com.qh.WheelView.WheelView;
import com.qh.WheelView.WheelViewAdapter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PopActivity extends MyActivity {
  public static final int EVERYDAY = 254;
  
  public static final byte EVERY_DAY = 127;
  
  public static final int FRI = 32;
  
  public static final String[] HOUR = new String[] { 
      "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", 
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
      "20", "21", "22", "23" };
  
  public static final String[] MINUTE = new String[] { 
      "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", 
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
      "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", 
      "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", 
      "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", 
      "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
  
  public static final int MON = 2;
  
  public static final int SAT = 64;
  
  public static final int SUN = 128;
  
  public static final int THU = 16;
  
  public static final int TUE = 4;
  
  public static final int WED = 8;
  
  public TextView Cencel;
  
  public TextView OK;
  
  private int dayData = 0;
  
  private MyWheelViewAdapter hMyWheelViewAdapter;
  
  private int hourData = 0;
  
  private WheelView hour_wheelview;
  
  public ImageView img_open;
  
  public ImageView img_time_open;
  
  public int index = 0;
  
  private boolean isNO = false;
  
  private boolean isWork = false;
  
  public Handler mHandler = new Handler();
  
  private LayoutInflater mInflator;
  
  public MyApplication mMyApplication;
  
  public MyBluetoothGatt mMyBluetoothGatt;
  
  private MyWheelViewAdapter mMyWheelViewAdapter;
  
  public Resources mResources;
  
  public HashMap<Integer, Boolean> mSelectDays = new HashMap<Integer, Boolean>();
  
  public String mac = "";
  
  private int miniteData = 0;
  
  private WheelView minute_wheelview;
  
  public String msg;
  
  private View.OnClickListener myOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        boolean bool;
        if (PopActivity.this.mSelectDays.containsKey(Integer.valueOf(param1View.getId()))) {
          bool = ((Boolean)PopActivity.this.mSelectDays.get(Integer.valueOf(param1View.getId()))).booleanValue();
        } else {
          bool = false;
        } 
        switch (param1View.getId()) {
          case 2131231257:
            PopActivity.this.showTX = PopActivity.this.tx_wed;
            break;
          case 2131231256:
            PopActivity.this.showTX = PopActivity.this.tx_tue;
            break;
          case 2131231254:
            PopActivity.this.showTX = PopActivity.this.tx_thu;
            break;
          case 2131231253:
            PopActivity.this.showTX = PopActivity.this.tx_sun;
            break;
          case 2131231251:
            PopActivity.this.showTX = PopActivity.this.tx_sat;
            break;
          case 2131231247:
            PopActivity.this.showTX = PopActivity.this.tx_mon;
            break;
          case 2131231243:
            PopActivity.this.showTX = PopActivity.this.tx_fri;
            break;
        } 
        if (bool) {
          PopActivity.this.showTX.setBackgroundResource(2131165290);
          PopActivity.this.mSelectDays.put(Integer.valueOf(PopActivity.this.showTX.getId()), Boolean.valueOf(false));
        } else {
          PopActivity.this.showTX.setBackgroundResource(2131165289);
          PopActivity.this.mSelectDays.put(Integer.valueOf(PopActivity.this.showTX.getId()), Boolean.valueOf(true));
        } 
      }
    };
  
  public RelativeLayout rel11;
  
  public TextView showTX;
  
  public TextView tx_fri;
  
  public TextView tx_mon;
  
  private TextView tx_msg;
  
  public TextView tx_sat;
  
  public TextView tx_sun;
  
  public TextView tx_thu;
  
  public TextView tx_tue;
  
  public TextView tx_wed;
  
  private int getDayData() {
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231247)) && ((Boolean)this.mSelectDays.get(Integer.valueOf(2131231247))).booleanValue()) {
      i = 2;
    } else {
      i = 0;
    } 
    int j = i;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231256))) {
      j = i;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231256))).booleanValue())
        j = i + 4; 
    } 
    int i = j;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231257))) {
      i = j;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231257))).booleanValue())
        i = j + 8; 
    } 
    j = i;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231254))) {
      j = i;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231254))).booleanValue())
        j = i + 16; 
    } 
    int k = j;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231243))) {
      k = j;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231243))).booleanValue())
        k = j + 32; 
    } 
    i = k;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231251))) {
      i = k;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231251))).booleanValue())
        i = k + 64; 
    } 
    j = i;
    if (this.mSelectDays.containsKey(Integer.valueOf(2131231253))) {
      j = i;
      if (((Boolean)this.mSelectDays.get(Integer.valueOf(2131231253))).booleanValue())
        j = i + 128; 
    } 
    return j;
  }
  
  private void init() {
    this.tx_mon = (TextView)findViewById(2131231247);
    this.tx_wed = (TextView)findViewById(2131231257);
    this.tx_thu = (TextView)findViewById(2131231254);
    this.tx_tue = (TextView)findViewById(2131231256);
    this.tx_fri = (TextView)findViewById(2131231243);
    this.tx_sat = (TextView)findViewById(2131231251);
    this.tx_sun = (TextView)findViewById(2131231253);
    this.img_open = (ImageView)findViewById(2131230915);
    this.img_time_open = (ImageView)findViewById(2131230928);
    this.tx_mon.setOnClickListener(this.myOnClickListener);
    this.tx_wed.setOnClickListener(this.myOnClickListener);
    this.tx_thu.setOnClickListener(this.myOnClickListener);
    this.tx_tue.setOnClickListener(this.myOnClickListener);
    this.tx_fri.setOnClickListener(this.myOnClickListener);
    this.tx_sat.setOnClickListener(this.myOnClickListener);
    this.tx_sun.setOnClickListener(this.myOnClickListener);
    this.img_open.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (PopActivity.this.mSelectDays.containsKey(Integer.valueOf(2131230915)))
              PopActivity.access$302(PopActivity.this, ((Boolean)PopActivity.this.mSelectDays.get(Integer.valueOf(2131230915))).booleanValue()); 
            PopActivity.access$302(PopActivity.this, PopActivity.this.isNO ^ true);
            PopActivity.this.mSelectDays.put(Integer.valueOf(2131230915), Boolean.valueOf(PopActivity.this.isNO));
            if (PopActivity.this.isNO) {
              PopActivity.this.img_open.setImageResource(2131165415);
            } else {
              PopActivity.this.img_open.setImageResource(2131165349);
            } 
          }
        });
    this.img_time_open.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (PopActivity.this.mSelectDays.containsKey(Integer.valueOf(2131230928)))
              PopActivity.access$402(PopActivity.this, ((Boolean)PopActivity.this.mSelectDays.get(Integer.valueOf(2131230928))).booleanValue()); 
            PopActivity.access$402(PopActivity.this, PopActivity.this.isWork ^ true);
            PopActivity.this.mSelectDays.put(Integer.valueOf(2131230928), Boolean.valueOf(PopActivity.this.isWork));
            if (PopActivity.this.isWork) {
              PopActivity.this.img_time_open.setImageResource(2131165415);
            } else {
              PopActivity.this.img_time_open.setImageResource(2131165349);
            } 
          }
        });
    this.OK = (TextView)findViewById(2131230725);
    this.Cencel = (TextView)findViewById(2131230722);
    this.OK.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dayData :");
            stringBuilder.append(PopActivity.this.getDayData());
            stringBuilder.append(" hourData = ");
            stringBuilder.append(PopActivity.this.hourData);
            stringBuilder.append(" miniteData");
            stringBuilder.append(PopActivity.this.miniteData);
            Log.e("", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("isNO :");
            stringBuilder.append(PopActivity.this.isNO);
            stringBuilder.append(" isWork = ");
            stringBuilder.append(PopActivity.this.isWork);
            Log.e("", stringBuilder.toString());
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int i = calendar.get(1);
            int j = calendar.get(2);
            int k = calendar.get(5);
            if (PopActivity.this.mMyBluetoothGatt != null) {
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 2] = (byte)(byte)(i % 100 & 0xFF);
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 3] = (byte)(byte)(j + 1 & 0xFF);
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 4] = (byte)(byte)(k & 0xFF);
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 8] = (byte)(byte)(PopActivity.this.getDayData() & 0xFF);
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 5] = (byte)(byte)(PopActivity.this.hourData & 0xFF);
              PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 6] = (byte)(byte)(PopActivity.this.miniteData & 0xFF);
              if (PopActivity.this.isWork) {
                PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 1] = (byte)-16;
              } else {
                PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 1] = (byte)15;
              } 
              if (PopActivity.this.isNO) {
                PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 14] = (byte)-16;
              } else {
                PopActivity.this.mMyBluetoothGatt.timedata[PopActivity.this.index * 14 + 14] = (byte)15;
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("11miniteData = ");
              stringBuilder1.append(PopActivity.this.miniteData);
              Log.e("22", stringBuilder1.toString());
              if (PopActivity.this.mMyBluetoothGatt.isTriones) {
                Log.e("--", "88 setNewTime 1");
                if (PopActivity.this.mMyBluetoothGatt.isLong) {
                  PopActivity.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                          PopActivity.this.mMyBluetoothGatt.setDayData();
                        }
                      },  100L);
                } else {
                  Log.e("--", "88 setNewTime2");
                  PopActivity.this.mMyBluetoothGatt.setNewTime(PopActivity.this.index, PopActivity.this.isWork, (byte)(PopActivity.this.hourData & 0xFF), (byte)(PopActivity.this.miniteData & 0xFF), 0, (byte)(PopActivity.this.getDayData() & 0xFF), PopActivity.this.isNO);
                  PopActivity.this.mMyBluetoothGatt.sendNewTime(PopActivity.this.index);
                } 
              } else {
                Log.e("--", "88 setNewTime 3");
                PopActivity.this.mMyBluetoothGatt.setNewTime(PopActivity.this.index, PopActivity.this.isWork, (byte)(PopActivity.this.hourData & 0xFF), (byte)(PopActivity.this.miniteData & 0xFF), 0, (byte)(PopActivity.this.getDayData() & 0xFF), PopActivity.this.isNO);
                PopActivity.this.mMyBluetoothGatt.sendNewTime(PopActivity.this.index);
              } 
              PopActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                      PopActivity.this.mMyBluetoothGatt.setDate();
                    }
                  },  4000L);
            } else {
              Log.e("", "mMyBluetoothGatt==null");
            } 
            Intent intent = new Intent();
            intent.putExtra("result", "ok");
            PopActivity.this.setResult(-1, intent);
            PopActivity.this.finish();
          }
        });
    this.Cencel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent();
            intent.putExtra("result", "Cencel");
            PopActivity.this.setResult(-1, intent);
            PopActivity.this.finish();
          }
        });
  }
  
  private void setDayView() {
    if ((this.dayData & 0x2) == 2) {
      this.tx_mon.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231247), Boolean.valueOf(true));
    } else {
      this.tx_mon.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231247), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x4) == 4) {
      this.tx_tue.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231256), Boolean.valueOf(true));
    } else {
      this.tx_tue.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231256), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x8) == 8) {
      this.tx_wed.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231257), Boolean.valueOf(true));
    } else {
      this.tx_wed.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231257), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x10) == 16) {
      this.tx_thu.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231254), Boolean.valueOf(true));
    } else {
      this.tx_thu.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231254), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x20) == 32) {
      this.tx_fri.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231243), Boolean.valueOf(true));
    } else {
      this.tx_fri.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231243), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x40) == 64) {
      this.tx_sat.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231251), Boolean.valueOf(true));
    } else {
      this.tx_sat.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231251), Boolean.valueOf(false));
    } 
    if ((this.dayData & 0x80) == 128) {
      this.tx_sun.setBackgroundResource(2131165289);
      this.mSelectDays.put(Integer.valueOf(2131231253), Boolean.valueOf(true));
    } else {
      this.tx_sun.setBackgroundResource(2131165290);
      this.mSelectDays.put(Integer.valueOf(2131231253), Boolean.valueOf(false));
    } 
  }
  
  private void setNoAndWork() {
    if (this.isNO) {
      this.img_open.setImageResource(2131165415);
    } else {
      this.img_open.setImageResource(2131165349);
    } 
    this.mSelectDays.put(Integer.valueOf(2131230915), Boolean.valueOf(this.isNO));
    if (this.isWork) {
      this.img_time_open.setImageResource(2131165415);
    } else {
      this.img_time_open.setImageResource(2131165349);
    } 
    this.mSelectDays.put(Integer.valueOf(2131230928), Boolean.valueOf(this.isWork));
  }
  
  private void setTime() {
    this.hour_wheelview.setCurrentItem(this.hourData);
    this.minute_wheelview.setCurrentItem(this.miniteData);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361876);
    this.mResources = getResources();
    this.mInflator = getLayoutInflater();
    this.mMyApplication = (MyApplication)getApplication();
    Intent intent = getIntent();
    if (intent != null) {
      Bundle bundle = intent.getExtras();
      if (bundle != null) {
        this.mac = bundle.getString("MAC", "");
        this.index = bundle.getInt("index", -1);
        this.dayData = bundle.getInt("day", 0);
        this.hourData = bundle.getInt("hour", 0);
        this.miniteData = bundle.getInt("minite", 0);
        this.isNO = bundle.getBoolean("isNO", false);
        this.isWork = bundle.getBoolean("isWork", false);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("mac :");
        stringBuilder2.append(this.mac);
        stringBuilder2.append(" index = ");
        stringBuilder2.append(this.index);
        Log.e("", stringBuilder2.toString());
        if (this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(this.mac))
          this.mMyBluetoothGatt = this.mMyApplication.mBluetoothLeService.MyBluetoothGatts.get(this.mac); 
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(11);
        int j = calendar.get(12);
        if (this.hourData <= 0 && this.miniteData <= 0) {
          this.hourData = i;
          this.miniteData = j;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("dayData :");
        stringBuilder1.append(this.dayData);
        stringBuilder1.append(" hourData = ");
        stringBuilder1.append(this.hourData);
        stringBuilder1.append(" miniteData");
        stringBuilder1.append(this.miniteData);
        Log.e("", stringBuilder1.toString());
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("isNO :");
        stringBuilder1.append(this.isNO);
        stringBuilder1.append(" isWork = ");
        stringBuilder1.append(this.isWork);
        Log.e("", stringBuilder1.toString());
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("isNO :");
        stringBuilder1.append(this.isNO);
        stringBuilder1.append(" isWork = ");
        stringBuilder1.append(this.isWork);
        Log.e("", stringBuilder1.toString());
      } 
    } 
    this.hour_wheelview = (WheelView)findViewById(2131230820);
    this.minute_wheelview = (WheelView)findViewById(2131230989);
    this.hMyWheelViewAdapter = new MyWheelViewAdapter();
    this.mMyWheelViewAdapter = new MyWheelViewAdapter();
    this.hMyWheelViewAdapter.setData(HOUR);
    this.mMyWheelViewAdapter.setData(MINUTE);
    this.hour_wheelview.setViewAdapter(this.hMyWheelViewAdapter);
    this.minute_wheelview.setViewAdapter(this.mMyWheelViewAdapter);
    this.hour_wheelview.setVisibleItems(1);
    this.minute_wheelview.setVisibleItems(1);
    this.minute_wheelview.setCyclic(true);
    this.hour_wheelview.setCyclic(true);
    init();
    setDayView();
    setTime();
    setNoAndWork();
    this.hour_wheelview.addScrollingListener(new OnWheelScrollListener() {
          public void onScrollingFinished(WheelView param1WheelView) {
            PopActivity.access$102(PopActivity.this, param1WheelView.getCurrentItem());
          }
          
          public void onScrollingStarted(WheelView param1WheelView) {}
        });
    this.minute_wheelview.addScrollingListener(new OnWheelScrollListener() {
          public void onScrollingFinished(WheelView param1WheelView) {
            PopActivity.access$202(PopActivity.this, param1WheelView.getCurrentItem());
          }
          
          public void onScrollingStarted(WheelView param1WheelView) {}
        });
  }
  
  protected void onDestroy() {
    super.onDestroy();
  }
  
  protected void onResume() {
    super.onResume();
  }
  
  private class MyWheelViewAdapter implements WheelViewAdapter {
    public String[] arraylistName;
    
    private MyWheelViewAdapter() {}
    
    public View getEmptyItem(View param1View, ViewGroup param1ViewGroup) {
      return null;
    }
    
    public View getItem(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      param1View = PopActivity.this.mInflator.inflate(2131361885, null);
      TextView textView = (TextView)param1View.findViewById(2131231245);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(this.arraylistName[param1Int]);
      textView.setText(stringBuilder.toString());
      textView.setTextColor(-1);
      return param1View;
    }
    
    public int getItemsCount() {
      return this.arraylistName.length;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    public void setData(String[] param1ArrayOfString) {
      this.arraylistName = param1ArrayOfString;
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {}
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\PopActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */