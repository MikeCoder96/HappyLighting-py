package com.qh.blelight;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.qh.WheelView.OnWheelScrollListener;
import com.qh.WheelView.WheelView;
import com.qh.WheelView.WheelViewAdapter;
import com.qh.data.MyChild;
import com.qh.data.TimeData;
import com.qh.tools.DBAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class TimingActivity extends Activity {
  public static final int EVERYDAY = 254;
  
  public static final int FRI = 32;
  
  public static final int MON = 2;
  
  public static final int SAT = 64;
  
  public static final int SUN = 128;
  
  public static final int THU = 16;
  
  public static final int TUE = 4;
  
  public static final int WED = 8;
  
  public Hashtable<Integer, TimeData> TimeDatas = new Hashtable<Integer, TimeData>();
  
  public DBAdapter dbAdapter;
  
  public Context mContext;
  
  private ArrayList mDataArrayList = new ArrayList();
  
  private LayoutInflater mInflator;
  
  public Resources mResources;
  
  public TimeAdapter mTimeAdapter;
  
  public String mTimerFormat = "%02d:%02d";
  
  public MyApplication myApplication;
  
  private MyWheelViewAdapter myWheelViewAdapter;
  
  private String selectMac = "";
  
  public byte[] timeData;
  
  private ListView time_list;
  
  public Handler timingHander = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 3) {
            String str = param1Message.getData().getString("deviceAddr", "");
            if ("".equals(str) || "null".equals(str))
              return false; 
            if (str.equals(TimingActivity.this.selectMac)) {
              if (TimingActivity.this.myApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(TimingActivity.this.selectMac)) {
                MyBluetoothGatt myBluetoothGatt = TimingActivity.this.myApplication.mBluetoothLeService.MyBluetoothGatts.get(TimingActivity.this.selectMac);
                if (myBluetoothGatt != null) {
                  TimingActivity.access$002(TimingActivity.this, myBluetoothGatt.mAddr);
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("");
                  stringBuilder.append(myBluetoothGatt.timedata.length);
                  Log.e("", stringBuilder.toString());
                  TimingActivity.this.timeData = myBluetoothGatt.timedata;
                } 
              } 
              TimingActivity.this.setData();
              if (TimingActivity.this.mTimeAdapter != null)
                TimingActivity.this.mTimeAdapter.notifyDataSetChanged(); 
            } 
          } 
          if (param1Message.what == 4) {
            TimingActivity.this.setData();
            if (TimingActivity.this.mTimeAdapter != null)
              TimingActivity.this.mTimeAdapter.notifyDataSetChanged(); 
          } 
          return false;
        }
      });
  
  private WheelView timing_wheelview;
  
  private String getDay(int paramInt) {
    String str1 = "";
    if ((paramInt & 0x2) == 2) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append("");
      stringBuilder.append(this.mResources.getString(2131623939));
      str1 = stringBuilder.toString();
    } 
    String str2 = str1;
    if ((paramInt & 0x4) == 4) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623953));
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if ((paramInt & 0x8) == 8) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623956));
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if ((paramInt & 0x10) == 16) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623952));
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if ((paramInt & 0x20) == 32) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623937));
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if ((paramInt & 0x40) == 64) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623949));
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if ((paramInt & 0x80) == 128) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(" ");
      stringBuilder.append(this.mResources.getString(2131623950));
      String str = stringBuilder.toString();
    } 
    if (paramInt == 254)
      str1 = this.mResources.getString(2131623936); 
    return str1;
  }
  
  private void setData() {
    this.myWheelViewAdapter = new MyWheelViewAdapter();
    this.mDataArrayList.clear();
    if (this.myApplication.mBluetoothLeService == null)
      return; 
    for (String str : this.myApplication.mBluetoothLeService.MyBluetoothGatts.keySet()) {
      MyBluetoothGatt myBluetoothGatt = this.myApplication.mBluetoothLeService.MyBluetoothGatts.get(str);
      if (myBluetoothGatt != null)
        this.mDataArrayList.add(myBluetoothGatt); 
    } 
    this.TimeDatas.clear();
    setTimedata();
    this.myWheelViewAdapter.setData(this.mDataArrayList);
    this.timing_wheelview.setViewAdapter(this.myWheelViewAdapter);
    if (this.myWheelViewAdapter != null) {
      if (this.myWheelViewAdapter.getItemsCount() <= 1) {
        this.time_list.setVisibility(8);
        this.timing_wheelview.setCurrentItem(0);
      } 
    } else {
      this.time_list.setVisibility(8);
    } 
  }
  
  private void setTimedata() {
    if (this.timeData != null)
      for (byte b = 0; b < 6; b++) {
        TimeData timeData = new TimeData();
        byte[] arrayOfByte = this.timeData;
        int i = b * 14;
        timeData.day = arrayOfByte[i + 8] & 0xFF;
        timeData.hour = this.timeData[i + 5];
        timeData.minite = this.timeData[i + 6];
        if ((this.timeData[i + 1] & 0xFF) == 240)
          timeData.isWork = true; 
        if ((this.timeData[i + 14] & 0xFF) == 240)
          timeData.isNO = true; 
        this.TimeDatas.put(Integer.valueOf(b), timeData);
      }  
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("requestCode ");
    stringBuilder.append(paramInt1);
    Log.e("", stringBuilder.toString());
    if (this.myApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(this.selectMac)) {
      MyBluetoothGatt myBluetoothGatt = this.myApplication.mBluetoothLeService.MyBluetoothGatts.get(this.selectMac);
      if (myBluetoothGatt != null) {
        this.selectMac = myBluetoothGatt.mAddr;
        stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(myBluetoothGatt.timedata.length);
        Log.e("", stringBuilder.toString());
        this.timeData = myBluetoothGatt.timedata;
      } 
    } 
    setData();
    this.mTimeAdapter.notifyDataSetChanged();
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361843);
    this.mInflator = getLayoutInflater();
    this.mResources = getResources();
    this.myApplication = (MyApplication)getApplication();
    this.mContext = getApplicationContext();
    this.dbAdapter = DBAdapter.init((Context)this);
    this.dbAdapter.open();
    this.mTimeAdapter = new TimeAdapter();
    this.timing_wheelview = (WheelView)findViewById(2131231178);
    this.time_list = (ListView)findViewById(2131231175);
    this.time_list.setAdapter((ListAdapter)this.mTimeAdapter);
    this.time_list.setVisibility(4);
    setData();
    this.timing_wheelview.setVisibleItems(1);
    this.time_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            if ("".equals(TimingActivity.this.selectMac) || TimingActivity.this.selectMac == null) {
              Toast.makeText(TimingActivity.this.mContext, TimingActivity.this.mResources.getString(2131624071), 0).show();
              return;
            } 
            Intent intent = new Intent((Context)TimingActivity.this, PopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", param1Int);
            bundle.putString("MAC", TimingActivity.this.selectMac);
            if (TimingActivity.this.TimeDatas.containsKey(Integer.valueOf(param1Int))) {
              TimeData timeData = TimingActivity.this.TimeDatas.get(Integer.valueOf(param1Int));
              if (timeData != null) {
                bundle.putInt("day", timeData.day);
                bundle.putInt("hour", timeData.hour);
                bundle.putInt("minite", timeData.minite);
                bundle.putBoolean("isNO", timeData.isNO);
                bundle.putBoolean("isWork", timeData.isWork);
              } 
            } else {
              bundle.putInt("day", 0);
              bundle.putInt("hour", 0);
              bundle.putInt("minite", 0);
              bundle.putBoolean("isNO", false);
              bundle.putBoolean("isWork", false);
            } 
            intent.putExtras(bundle);
            TimingActivity.this.startActivityForResult(intent, 100);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("selectMac = ");
            stringBuilder.append(TimingActivity.this.selectMac);
            Log.e("", stringBuilder.toString());
          }
        });
    this.timing_wheelview.addScrollingListener(new OnWheelScrollListener() {
          public void onScrollingFinished(WheelView param1WheelView) {
            int i = param1WheelView.getCurrentItem();
            int j = TimingActivity.this.mDataArrayList.size();
            if (j > --i)
              if (i < 0) {
                TimingActivity.access$002(TimingActivity.this, "");
                TimingActivity.this.time_list.setVisibility(4);
              } else {
                TimingActivity.this.time_list.setVisibility(0);
                MyBluetoothGatt myBluetoothGatt = TimingActivity.this.mDataArrayList.get(i);
                if (myBluetoothGatt != null) {
                  TimingActivity.access$002(TimingActivity.this, myBluetoothGatt.mAddr);
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append(" ");
                  stringBuilder.append(myBluetoothGatt.timedata.length);
                  Log.e("", stringBuilder.toString());
                  TimingActivity.this.timeData = myBluetoothGatt.timedata;
                  myBluetoothGatt.setDate();
                } 
                TimingActivity.this.setTimedata();
                TimingActivity.this.mTimeAdapter.notifyDataSetChanged();
              }  
            if ("".equals(TimingActivity.this.selectMac)) {
              TimingActivity.this.TimeDatas.clear();
              TimingActivity.this.mTimeAdapter.notifyDataSetChanged();
            } 
          }
          
          public void onScrollingStarted(WheelView param1WheelView) {}
        });
    this.myApplication.TimingHandler = this.timingHander;
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
    setData();
    super.onResume();
  }
  
  private class MyOnClickListener implements View.OnClickListener {
    private boolean isWork = false;
    
    private TimingActivity.TimeViewHolder mTimeViewHolder;
    
    private int position;
    
    public MyOnClickListener(int param1Int, TimingActivity.TimeViewHolder param1TimeViewHolder) {
      this.position = param1Int;
      this.mTimeViewHolder = param1TimeViewHolder;
    }
    
    public void onClick(View param1View) {
      if (TimingActivity.this.myApplication.mBluetoothLeService.MyBluetoothGatts.containsKey(TimingActivity.this.selectMac)) {
        final MyBluetoothGatt mMyBluetoothGatt = TimingActivity.this.myApplication.mBluetoothLeService.MyBluetoothGatts.get(TimingActivity.this.selectMac);
        if (myBluetoothGatt != null) {
          if (this.isWork) {
            myBluetoothGatt.timedata[this.position * 14 + 1] = (byte)15;
            this.mTimeViewHolder.time_open.setImageResource(2131165349);
            this.isWork = false;
          } else {
            myBluetoothGatt.timedata[this.position * 14 + 1] = (byte)-16;
            this.mTimeViewHolder.time_open.setImageResource(2131165415);
            this.isWork = true;
          } 
          TimeData timeData = TimingActivity.this.TimeDatas.get(Integer.valueOf(this.position));
          timeData.isWork = this.isWork;
          if (myBluetoothGatt.isTriones) {
            if (myBluetoothGatt.isLong) {
              TimingActivity.this.timingHander.postDelayed(new Runnable() {
                    public void run() {
                      mMyBluetoothGatt.setDayData();
                    }
                  },  100L);
            } else {
              myBluetoothGatt.setNewTime(this.position, this.isWork, timeData.hour, timeData.minite, 0, (byte)(timeData.day & 0xFF), timeData.isNO);
              myBluetoothGatt.sendNewTime(this.position, this.isWork);
            } 
          } else {
            myBluetoothGatt.setNewTime(this.position, this.isWork, timeData.hour, timeData.minite, 0, (byte)(timeData.day & 0xFF), timeData.isNO);
            myBluetoothGatt.sendNewTime(this.position, this.isWork);
          } 
          TimingActivity.this.timingHander.postDelayed(new Runnable() {
                public void run() {
                  mMyBluetoothGatt.setDate();
                }
              },  4000L);
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("position = ");
      stringBuilder.append(this.position);
      stringBuilder.append(" isWork = ");
      stringBuilder.append(this.isWork);
      Log.e("", stringBuilder.toString());
    }
    
    public MyOnClickListener setIsWork(boolean param1Boolean) {
      this.isWork = param1Boolean;
      return this;
    }
  }
  
  class null implements Runnable {
    public void run() {
      mMyBluetoothGatt.setDayData();
    }
  }
  
  class null implements Runnable {
    public void run() {
      mMyBluetoothGatt.setDate();
    }
  }
  
  private class MyWheelViewAdapter implements WheelViewAdapter {
    public ArrayList<String> arraylistName = new ArrayList<String>();
    
    public ArrayList<MyBluetoothGatt> data = new ArrayList<MyBluetoothGatt>();
    
    private MyWheelViewAdapter() {}
    
    public View getEmptyItem(View param1View, ViewGroup param1ViewGroup) {
      return null;
    }
    
    public View getItem(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = TimingActivity.this.mInflator.inflate(2131361889, null);
      TextView textView = (TextView)view.findViewById(2131231244);
      ((ImageView)view.findViewById(2131230881)).setVisibility(8);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(this.arraylistName.get(param1Int));
      textView.setText(stringBuilder.toString());
      return view;
    }
    
    public int getItemsCount() {
      return this.arraylistName.size();
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {}
    
    public void setData(ArrayList<MyBluetoothGatt> param1ArrayList) {
      this.arraylistName.clear();
      ArrayList<String> arrayList = this.arraylistName;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(TimingActivity.this.mResources.getString(2131624032));
      arrayList.add(stringBuilder.toString());
      for (byte b = 0; b < param1ArrayList.size(); b++) {
        MyBluetoothGatt myBluetoothGatt = param1ArrayList.get(b);
        if (myBluetoothGatt != null) {
          MyChild myChild;
          if (TimingActivity.this.myApplication.mMyExpandableListAdapter.DBdata.containsKey(myBluetoothGatt.mAddr)) {
            myChild = TimingActivity.this.myApplication.mMyExpandableListAdapter.DBdata.get(myBluetoothGatt.mAddr);
            this.arraylistName.add(myChild.name);
          } else if (TimingActivity.this.myApplication.mBluetoothLeService.mDevices.containsKey(((MyBluetoothGatt)myChild).mAddr)) {
            BluetoothDevice bluetoothDevice = TimingActivity.this.myApplication.mBluetoothLeService.mDevices.get(((MyBluetoothGatt)myChild).mAddr);
            ArrayList<String> arrayList1 = this.arraylistName;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("");
            stringBuilder1.append(bluetoothDevice.getName());
            arrayList1.add(stringBuilder1.toString());
          } 
        } 
      } 
      this.data.addAll(param1ArrayList);
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {}
  }
  
  private class TimeAdapter extends BaseAdapter {
    private ArrayList mArrayList = new ArrayList();
    
    private LayoutInflater mInflator = TimingActivity.this.getLayoutInflater();
    
    private HashMap<Integer, View> mItemHashMap = new HashMap<Integer, View>();
    
    public TimeAdapter() {
      for (byte b = 0; b < 6; b++)
        this.mArrayList.add(Integer.valueOf(b)); 
    }
    
    public int getCount() {
      return this.mArrayList.size();
    }
    
    public Object getItem(int param1Int) {
      Object object;
      if (this.mItemHashMap.containsKey(Integer.valueOf(param1Int))) {
        object = this.mItemHashMap.get(Integer.valueOf(param1Int));
      } else {
        object = null;
      } 
      return object;
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = this.mInflator.inflate(2131361884, null);
      TimingActivity.TimeViewHolder timeViewHolder2 = (TimingActivity.TimeViewHolder)view.getTag();
      TimingActivity.TimeViewHolder timeViewHolder1 = timeViewHolder2;
      if (timeViewHolder2 == null) {
        timeViewHolder1 = new TimingActivity.TimeViewHolder();
        timeViewHolder1.tx_timing = (TextView)view.findViewById(2131231255);
        timeViewHolder1.operation_switch = (TextView)view.findViewById(2131231005);
        timeViewHolder1.tx_day = (TextView)view.findViewById(2131231240);
        timeViewHolder1.time_open = (ImageView)view.findViewById(2131231176);
        timeViewHolder1.img1 = (ImageView)view.findViewById(2131230850);
      } 
      TimingActivity.MyOnClickListener myOnClickListener = new TimingActivity.MyOnClickListener(param1Int, timeViewHolder1);
      timeViewHolder1.time_open.setOnClickListener(myOnClickListener);
      timeViewHolder1.tx_timing.setText("00:00");
      if (TimingActivity.this.TimeDatas.containsKey(Integer.valueOf(param1Int))) {
        TimeData timeData = TimingActivity.this.TimeDatas.get(Integer.valueOf(param1Int));
        if (timeData != null) {
          String str = String.format(TimingActivity.this.mTimerFormat, new Object[] { Integer.valueOf(timeData.hour), Integer.valueOf(timeData.minite) });
          TextView textView1 = timeViewHolder1.tx_timing;
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("");
          stringBuilder2.append(str);
          textView1.setText(stringBuilder2.toString());
          TextView textView2 = timeViewHolder1.tx_day;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("");
          stringBuilder1.append(TimingActivity.this.getDay(timeData.day));
          textView2.setText(stringBuilder1.toString());
          if (timeData.isWork) {
            timeViewHolder1.time_open.setImageResource(2131165415);
            timeViewHolder1.time_open.setOnClickListener(myOnClickListener.setIsWork(true));
          } else {
            timeViewHolder1.time_open.setImageResource(2131165349);
          } 
          if (timeData.isNO) {
            timeViewHolder1.operation_switch.setText(TimingActivity.this.mResources.getString(2131623942));
            timeViewHolder1.img1.setImageResource(2131165395);
          } else {
            timeViewHolder1.operation_switch.setText(TimingActivity.this.mResources.getString(2131623944));
            timeViewHolder1.img1.setImageResource(2131165397);
          } 
        } 
      } else {
        String str = String.format(TimingActivity.this.mTimerFormat, new Object[] { Integer.valueOf(0), Integer.valueOf(0) });
        TextView textView = timeViewHolder1.tx_timing;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(str);
        textView.setText(stringBuilder.toString());
        timeViewHolder1.tx_day.setText("");
        timeViewHolder1.time_open.setImageResource(2131165349);
        timeViewHolder1.operation_switch.setText(TimingActivity.this.mResources.getString(2131623944));
      } 
      this.mItemHashMap.put(Integer.valueOf(param1Int), view);
      view.setTag(timeViewHolder1);
      return view;
    }
  }
  
  public class TimeViewHolder {
    ImageView img1;
    
    TextView operation_switch;
    
    ImageView time_open;
    
    TextView tx_day;
    
    TextView tx_timing;
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\TimingActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */