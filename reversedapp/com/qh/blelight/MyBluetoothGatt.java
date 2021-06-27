package com.qh.blelight;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.consmart.ble.AES2;
import com.qh.data.DreamYdata;
import com.qh.data.MyColor;
import com.qh.data.NewTime;
import com.qh.tools.DBAdapter;
import com.qh.tools.SampleGattAttributes;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TargetApi(18)
public class MyBluetoothGatt {
  public static final String COMPANY_NAME = "Consmart";
  
  public static final int INT_PHOTOGRAPH = 2;
  
  public static final int SREVICE_UPDATA = 5;
  
  public static final int STATE_CONNECTED = 2;
  
  public static final int STATE_CONNECTING = 1;
  
  public static final int STATE_DISCONNECTED = 0;
  
  public static final byte[] TIME_HEAD = new byte[] { 35, 37, 39, 67, 69, 71 };
  
  public static final byte[] TIME_TAIL = new byte[] { 50, 82, 114, 52, 84, 116 };
  
  public static final String TRIONES_NAME = "^Triones-|^Triones\\+|^Triones";
  
  public final byte[] Mods = new byte[] { 
      37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 
      47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
      97, 98, 99 };
  
  public BluetoothAdapter bluetoothAdapter;
  
  private byte[] cachecmd;
  
  public String cachenewpwd = "1234";
  
  private Handler checkHandler = new Handler();
  
  private int checkNum = 0;
  
  public Runnable checkRunnable = new Runnable() {
      public void run() {
        if (MyBluetoothGatt.this.checkNum >= 5) {
          if (MyBluetoothGatt.this.isread180a)
            return; 
          MyBluetoothGatt.this.mBluetoothLeService.unlinkBleDevices.put(MyBluetoothGatt.this.mAddr, MyBluetoothGatt.this.mAddr);
          MyBluetoothGatt.this.stopLEService();
          MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 400);
          return;
        } 
        MyBluetoothGatt.this.setAES();
        MyBluetoothGatt.this.checkHandler.postDelayed(MyBluetoothGatt.this.checkRunnable, 1000L);
        MyBluetoothGatt.access$708(MyBluetoothGatt.this);
      }
    };
  
  private Handler connHandler = new Handler();
  
  public Context context;
  
  public byte[] datas = new byte[] { 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0 };
  
  private DBAdapter dbAdapter;
  
  public byte[] huancaidata = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
  
  public boolean isLong = false;
  
  public boolean isReadFData = false;
  
  public boolean isTriones = false;
  
  public boolean ischeckpwd = false;
  
  boolean isopenmyMIC = false;
  
  public boolean isread180a = false;
  
  public boolean isreadTimeData = false;
  
  public boolean isreadhuancai = false;
  
  public long linktime = 0L;
  
  public String mAddr = "";
  
  private BluetoothGatt mBluetoothGatt;
  
  public BluetoothLeService mBluetoothLeService;
  
  public int mConnectionState = 0;
  
  public DreamYdata mDreamYdata = new DreamYdata();
  
  private BluetoothGattCallback mGattCallback;
  
  private Handler mHandler;
  
  public BluetoothDevice mLEdevice;
  
  public Hashtable<Integer, NewTime> mNewTimelist = new Hashtable<Integer, NewTime>();
  
  private Handler mTimeHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          return false;
        }
      });
  
  public int modId = 0;
  
  public int num = 0;
  
  public BluetoothGattCharacteristic photoCharacteristic;
  
  public String pwd = "1234";
  
  public MyColor savecolor;
  
  public String scheckpwd = "1234";
  
  private byte[] sendsrcAES = new byte[] { 
      -5, 2, 5, 5, 16, 8, 35, 1, 2, 0, 
      5, 85, 34, 1, 18, 19, 20, -6 };
  
  public SharedPreferences settings;
  
  private byte[] srcAES = new byte[] { 
      2, 5, 5, 16, 8, 35, 1, 2, 0, 5, 
      85, 34, 1, 18, 19, 20 };
  
  public Handler timeHandler = new Handler();
  
  public Runnable timeRunnable = new Runnable() {
      public void run() {
        if (MyBluetoothGatt.this.isReadFData && MyBluetoothGatt.this.timeadata.size() >= 6 && MyBluetoothGatt.this.isreadhuancai) {
          MyBluetoothGatt.this.timeHandler.removeCallbacks(MyBluetoothGatt.this.timeRunnable);
          return;
        } 
        if (!MyBluetoothGatt.this.isReadFData)
          MyBluetoothGatt.this.getLightData(); 
        if (MyBluetoothGatt.this.timeadata.size() < 6)
          MyBluetoothGatt.this.getTimeData(); 
        if (!MyBluetoothGatt.this.isreadhuancai)
          MyBluetoothGatt.this.readhuancai(); 
        MyBluetoothGatt.this.timeHandler.removeCallbacks(MyBluetoothGatt.this.timeRunnable);
        MyBluetoothGatt.this.timeHandler.postDelayed(MyBluetoothGatt.this.timeRunnable, 500L);
      }
    };
  
  public HashMap<Integer, Integer> timeadata = new HashMap<Integer, Integer>();
  
  public byte[] timedata = new byte[] { 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0 };
  
  public MediaPlayer waitiingMP;
  
  Queue<MyColor> writeQueue = new LinkedList<MyColor>();
  
  public MyBluetoothGatt(Context paramContext, BluetoothAdapter paramBluetoothAdapter, final BluetoothLeService mBluetoothLeService, final Handler mHandler, DBAdapter paramDBAdapter) {
    this.context = paramContext;
    this.settings = this.context.getSharedPreferences("setting", 0);
    this.dbAdapter = paramDBAdapter;
    this.bluetoothAdapter = paramBluetoothAdapter;
    this.mBluetoothLeService = mBluetoothLeService;
    Log.e("MyBluetoothGatt", "MyBluetoothGatt 1");
    updataSrc();
    this.mHandler = mHandler;
    this.mGattCallback = new BluetoothGattCallback() {
        private long fastdata = 0L;
        
        private long fasttime = 0L;
        
        boolean flay = true;
        
        private int timeDataNum = 0;
        
        private boolean timeflay = false;
        
        public void onCharacteristicChanged(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic) {
          if ("0000ffd4-0000-1000-8000-00805f9b34fb".equals(param1BluetoothGattCharacteristic.getUuid().toString())) {
            byte[] arrayOfByte = param1BluetoothGattCharacteristic.getValue();
            MyBluetoothGatt.this.mConnectionState = 2;
            int i = 0;
            String str = "";
            int j = 0;
            while (true) {
              StringBuilder stringBuilder1;
              int k = arrayOfByte.length;
              boolean bool = true;
              if (j < k) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(String.format("%x", new Object[] { Byte.valueOf(arrayOfByte[j]) }));
                stringBuilder.append(" ");
                str = stringBuilder.toString();
                j++;
                continue;
              } 
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append("sdata=");
              stringBuilder2.append(str);
              stringBuilder2.append(" mac=");
              stringBuilder2.append(MyBluetoothGatt.this.mAddr);
              Log.e("--", stringBuilder2.toString());
              if (arrayOfByte != null && arrayOfByte.length == 8 && arrayOfByte[0] == -123 && arrayOfByte[7] == 88) {
                for (j = i; j < MyBluetoothGatt.this.huancaidata.length; j++)
                  MyBluetoothGatt.this.huancaidata[j] = arrayOfByte[j]; 
                MyBluetoothGatt.this.isreadhuancai = true;
                return;
              } 
              if (arrayOfByte.length == 12 && (arrayOfByte[0] & 0xFF) == 208 && (arrayOfByte[11] & 0xFF) == 13) {
                MyBluetoothGatt.this.mDreamYdata = new DreamYdata(arrayOfByte);
                if (MyBluetoothGatt.this.mDreamYdata.connect_need_pwd)
                  MyBluetoothGatt.this.mTimeHandler.postDelayed(new Runnable() {
                        public void run() {
                          MyBluetoothGatt.this.checkpwd(MyBluetoothGatt.this.pwd);
                        }
                      },  20L); 
                return;
              } 
              if (arrayOfByte.length == 4 && (arrayOfByte[0] & 0xFF) == 112 && (arrayOfByte[3] & 0xFF) == 7)
                if ((arrayOfByte[1] & 0xFF) == 240) {
                  if (mBluetoothLeService.myApplication.huancaiHandler != null)
                    mBluetoothLeService.myApplication.huancaiHandler.sendEmptyMessage(1); 
                } else if (mBluetoothLeService.myApplication.huancaiHandler != null) {
                  mBluetoothLeService.myApplication.huancaiHandler.sendEmptyMessage(2);
                }  
              if (arrayOfByte != null && arrayOfByte.length == 3 && arrayOfByte[0] == -75 && arrayOfByte[2] == 91)
                if (arrayOfByte[1] == -16) {
                  MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 500);
                } else {
                  MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 501);
                }  
              if (arrayOfByte != null && arrayOfByte.length == 4 && arrayOfByte[0] == -80 && arrayOfByte[3] == 11) {
                if (arrayOfByte[1] == -16) {
                  MyBluetoothGatt.this.pwd = MyBluetoothGatt.this.cachenewpwd;
                  if (mBluetoothLeService.mresetpwd != null)
                    mBluetoothLeService.mresetpwd.resetpwd(MyBluetoothGatt.this.mAddr, 1, MyBluetoothGatt.this.cachenewpwd); 
                  if (MyBluetoothGatt.this.settings != null) {
                    SharedPreferences.Editor editor = MyBluetoothGatt.this.settings.edit();
                    stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("pwd");
                    stringBuilder1.append(MyBluetoothGatt.this.mAddr);
                    editor.putString(stringBuilder1.toString(), MyBluetoothGatt.this.cachenewpwd);
                    editor.commit();
                  } 
                } 
                if (arrayOfByte[1] == 15 && mBluetoothLeService.mresetpwd != null)
                  mBluetoothLeService.mresetpwd.resetpwd(MyBluetoothGatt.this.mAddr, 0, MyBluetoothGatt.this.cachenewpwd); 
                if (arrayOfByte[1] == -18 && mBluetoothLeService.mresetpwd != null)
                  mBluetoothLeService.mresetpwd.resetpwd(MyBluetoothGatt.this.mAddr, 2, MyBluetoothGatt.this.cachenewpwd); 
                return;
              } 
              if (arrayOfByte != null && arrayOfByte.length == 10 && arrayOfByte[0] == -48 && arrayOfByte[9] == 13 && mBluetoothLeService.mQicaidata != null) {
                boolean bool1;
                if (arrayOfByte[3] == 0) {
                  bool1 = false;
                } else {
                  bool1 = true;
                } 
                mBluetoothLeService.mQicaidata.readqicai(MyBluetoothGatt.this.mAddr, arrayOfByte[1], arrayOfByte[2], bool1);
                if (bool1 && !MyBluetoothGatt.this.ischeckpwd) {
                  if ("".contains(":"))
                    MyBluetoothGatt.this.mTimeHandler.postDelayed(new Runnable() {
                          public void run() {
                            Log.e("checkpwd(pwd);", "checkpwd(pwd);");
                            MyBluetoothGatt.this.checkpwd(MyBluetoothGatt.this.pwd);
                          }
                        }500L); 
                } else {
                  MyBluetoothGatt.this.ischeckpwd = true;
                } 
              } 
              if (arrayOfByte != null && arrayOfByte.length == 4 && arrayOfByte[0] == -60 && arrayOfByte[3] == 76 && mBluetoothLeService.mQicaidata != null)
                mBluetoothLeService.mQicaidata.readqicai(MyBluetoothGatt.this.mAddr, arrayOfByte[1], arrayOfByte[2], false); 
              if (arrayOfByte != null && arrayOfByte.length == 4 && arrayOfByte[0] == -96 && arrayOfByte[3] == 10) {
                MyBluetoothGatt.this.ischeckpwd = true;
                if (arrayOfByte[1] == -16) {
                  stringBuilder2 = new StringBuilder();
                  stringBuilder2.append("mima= ok");
                  stringBuilder2.append(MyBluetoothGatt.this.scheckpwd);
                  Log.e("mima", stringBuilder2.toString());
                  if (mBluetoothLeService.mCheckpwd != null)
                    mBluetoothLeService.mCheckpwd.checkpwd(MyBluetoothGatt.this.mAddr, 1, MyBluetoothGatt.this.scheckpwd); 
                  MyBluetoothGatt.this.pwd = MyBluetoothGatt.this.scheckpwd;
                  if (MyBluetoothGatt.this.settings != null) {
                    SharedPreferences.Editor editor = MyBluetoothGatt.this.settings.edit();
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("pwd");
                    stringBuilder2.append(MyBluetoothGatt.this.mAddr);
                    editor.putString(stringBuilder2.toString(), MyBluetoothGatt.this.scheckpwd);
                    editor.commit();
                  } 
                  stringBuilder2 = new StringBuilder();
                  stringBuilder2.append("scheckpwd ");
                  stringBuilder2.append(MyBluetoothGatt.this.scheckpwd);
                  Log.e("11", stringBuilder2.toString());
                } else if (arrayOfByte[1] == 15) {
                  Log.e("mima", "mima= false");
                  if (mBluetoothLeService.mCheckpwd != null)
                    mBluetoothLeService.mCheckpwd.checkpwd(MyBluetoothGatt.this.mAddr, 0, MyBluetoothGatt.this.scheckpwd); 
                } else if (arrayOfByte[1] == -18) {
                  Log.e("mima", "mima= ee");
                  if (mBluetoothLeService.mCheckpwd != null)
                    mBluetoothLeService.mCheckpwd.checkpwd(MyBluetoothGatt.this.mAddr, 2, MyBluetoothGatt.this.scheckpwd); 
                } 
              } 
              if (arrayOfByte != null && arrayOfByte.length == 8)
                MyBluetoothGatt.this.reSetData(arrayOfByte); 
              if (arrayOfByte != null) {
                if (this.timeflay) {
                  for (j = 0; j < arrayOfByte.length; j++) {
                    if (MyBluetoothGatt.this.timedata.length > this.timeDataNum && this.timeflay) {
                      MyBluetoothGatt.this.timedata[this.timeDataNum] = arrayOfByte[j];
                      this.timeDataNum++;
                    } 
                  } 
                  if (arrayOfByte.length == 7)
                    this.timeflay = false; 
                } 
                if (arrayOfByte[0] == 37) {
                  this.timeDataNum = 0;
                  this.timeflay = true;
                  for (j = 0; j < arrayOfByte.length; j++) {
                    if (MyBluetoothGatt.this.timedata.length > this.timeDataNum && this.timeflay) {
                      MyBluetoothGatt.this.timedata[this.timeDataNum] = arrayOfByte[j];
                      this.timeDataNum++;
                    } 
                  } 
                  if (arrayOfByte.length == 20)
                    MyBluetoothGatt.this.isLong = true; 
                } else if (arrayOfByte[0] == 102) {
                  MyBluetoothGatt.this.isReadFData = true;
                  for (j = 0; j < arrayOfByte.length; j++)
                    MyBluetoothGatt.this.datas[j] = arrayOfByte[j]; 
                  stringBuilder2 = new StringBuilder();
                  stringBuilder2.append("data[2] ");
                  stringBuilder2.append(MyBluetoothGatt.this.mAddr);
                  Log.e("66", stringBuilder2.toString());
                  if (arrayOfByte[2] == 35) {
                    MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 0);
                  } else if (arrayOfByte[2] == 36) {
                    MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 0);
                  } 
                  if (arrayOfByte.length == 12) {
                    k = arrayOfByte[6] & 0xFF;
                    i = arrayOfByte[7] & 0xFF;
                    j = arrayOfByte[8] & 0xFF;
                    float f1 = 1.0F;
                    float f2 = f1;
                    if (k >= i) {
                      f2 = f1;
                      if (k >= j)
                        f2 = 255.0F / k; 
                    } 
                    f1 = f2;
                    if (i >= k) {
                      f1 = f2;
                      if (i >= j)
                        f1 = 255.0F / i; 
                    } 
                    f2 = f1;
                    if (j >= k) {
                      f2 = f1;
                      if (j >= i)
                        f2 = 255.0F / j; 
                    } 
                    int m = (int)(k * f2);
                    i = (int)(i * f2);
                    k = (int)(j * f2);
                    j = m;
                    if (m > 255)
                      j = 255; 
                    if (j > 255)
                      i = 255; 
                    if (j > 255)
                      k = 255; 
                    j = Color.argb(255, j, i, k);
                    MyBluetoothGatt.this.savecolor = new MyColor(j, arrayOfByte[9], 100);
                    MyBluetoothGatt.this.synTimedata((byte)(arrayOfByte[3] & 0xFF), (byte)(arrayOfByte[5] & 0xFF), (byte)(arrayOfByte[6] & 0xFF), (byte)(arrayOfByte[7] & 0xFF), (byte)(arrayOfByte[8] & 0xFF), (byte)(0xFF & arrayOfByte[9]));
                  } 
                } 
              } 
              if (arrayOfByte.length == 18 && arrayOfByte[0] == -7 && arrayOfByte[17] == -8) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("sdata=");
                stringBuilder2.append((String)stringBuilder1);
                Log.e("--", stringBuilder2.toString());
                try {
                  byte[] arrayOfByte1 = AES2.Encrypt(MyBluetoothGatt.this.srcAES);
                  if (arrayOfByte1 != null) {
                    j = 0;
                    boolean bool1 = bool;
                    while (j < arrayOfByte1.length) {
                      k = arrayOfByte1[j];
                      i = j + 1;
                      j = i;
                      if (k != arrayOfByte[i]) {
                        bool1 = false;
                        j = i;
                      } 
                    } 
                    if (bool1) {
                      MyBluetoothGatt.this.checkHandler.removeCallbacks(MyBluetoothGatt.this.checkRunnable);
                    } else {
                      MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 400);
                      MyBluetoothGatt.this.stopLEService();
                    } 
                    StringBuilder stringBuilder = new StringBuilder();
                    this();
                    stringBuilder.append("ischeck = ");
                    stringBuilder.append(bool1);
                    stringBuilder.append(" ");
                    stringBuilder.append(MyBluetoothGatt.this.mAddr);
                    Log.e("-", stringBuilder.toString());
                  } 
                  break;
                } catch (Exception exception) {
                  exception.printStackTrace();
                } 
                return;
              } 
              break;
            } 
          } 
        }
        
        public void onCharacteristicRead(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic, int param1Int) {
          if ("00002a25-0000-1000-8000-00805f9b34fb".equals(param1BluetoothGattCharacteristic.getUuid().toString())) {
            byte[] arrayOfByte = param1BluetoothGattCharacteristic.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CONSMART_BLE_2a25_UUID ");
            stringBuilder.append(MyBluetoothGatt.this.mAddr);
            Log.e("a", stringBuilder.toString());
            if (arrayOfByte != null && arrayOfByte.length > 0) {
              String str = "";
              for (param1Int = 0; param1Int < arrayOfByte.length; param1Int++) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append(str);
                stringBuilder1.append(" ");
                stringBuilder1.append(String.format("%02x", new Object[] { Integer.valueOf(arrayOfByte[param1Int] & 0xFF) }));
                str = stringBuilder1.toString();
              } 
            } 
            if (arrayOfByte.length == 12 && arrayOfByte[0] == 81 && arrayOfByte[1] == 72 && arrayOfByte[2] == 45 && arrayOfByte[3] == 81 && arrayOfByte[4] == 88 && arrayOfByte[5] == 68 && arrayOfByte[6] == 45 && arrayOfByte[7] == 85 && arrayOfByte[8] == 65 && arrayOfByte[9] == 82 && arrayOfByte[10] == 84 && arrayOfByte[11] == 0)
              MyBluetoothGatt.this.isread180a = true; 
          } 
        }
        
        public void onCharacteristicWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic, int param1Int) {
          byte[] arrayOfByte = param1BluetoothGattCharacteristic.getValue();
          String str = "";
          for (byte b = 0; b < arrayOfByte.length; b++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(String.format("%02x", new Object[] { Integer.valueOf(arrayOfByte[b] & 0xFF) }));
            str = stringBuilder.toString();
          } 
          if ("0000ffd9-0000-1000-8000-00805f9b34fb".equals(param1BluetoothGattCharacteristic.getUuid().toString()) && MyBluetoothGatt.this.writeQueue.size() > 0) {
            MyColor myColor = MyBluetoothGatt.this.writeQueue.poll();
            MyBluetoothGatt.this.setColor(myColor);
          } 
          super.onCharacteristicWrite(param1BluetoothGatt, param1BluetoothGattCharacteristic, param1Int);
        }
        
        public void onConnectionStateChange(final BluetoothGatt gatt, int param1Int1, int param1Int2) {
          MyBluetoothGatt.this.mConnectionState = param1Int1;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("__");
          stringBuilder.append(param1Int1);
          Log.e("--", stringBuilder.toString());
          if (param1Int2 == 2) {
            Date date = new Date();
            MyBluetoothGatt.this.linktime = date.getTime();
            if (MyBluetoothGatt.this.mBluetoothGatt != null) {
              MyBluetoothGatt.this.mConnectionState = 1;
              if (MyBluetoothGatt.this.isDreamY(gatt.getDevice().getName())) {
                mHandler.postDelayed(new Runnable() {
                      public void run() {
                        gatt.discoverServices();
                      }
                    },  100L);
              } else {
                mHandler.postDelayed(new Runnable() {
                      public void run() {
                        gatt.discoverServices();
                      }
                    },  500L);
              } 
            } 
          } else if (param1Int2 == 0) {
            MyBluetoothGatt.this.mConnectionState = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("---");
            stringBuilder.append(gatt.getDevice().getAddress());
            Log.e("", stringBuilder.toString());
            mBluetoothLeService.MyBluetoothGatts.remove(gatt.getDevice().getAddress());
            MyBluetoothGatt.this.setMsg(gatt.getDevice().getAddress(), 4);
            try {
              MyBluetoothGatt.this.mBluetoothGatt.close();
              MyBluetoothGatt.access$002(MyBluetoothGatt.this, null);
              if (mBluetoothLeService != null && MyBluetoothGatt.this.mAddr != null && "null".equals(MyBluetoothGatt.this.mAddr) && !mBluetoothLeService.unlinkBleDevices.containsKey(MyBluetoothGatt.this.mAddr)) {
                Handler handler = MyBluetoothGatt.this.connHandler;
                Runnable runnable = new Runnable() {
                    public void run() {
                      mBluetoothLeService.connBLE(MyBluetoothGatt.this.mAddr);
                    }
                  };
                super(this);
                handler.postDelayed(runnable, 1000L);
              } 
            } catch (Exception exception) {}
          } else if (param1Int1 == 133) {
            MyBluetoothGatt.this.mBluetoothGatt.close();
            MyBluetoothGatt.access$002(MyBluetoothGatt.this, null);
          } 
        }
        
        public void onDescriptorWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattDescriptor param1BluetoothGattDescriptor, int param1Int) {
          String str = MyBluetoothGatt.this.mLEdevice.getName();
          if (str.length() == 20 && MyBluetoothGatt.this.isTriones && str.contains("-FFFFF10000")) {
            Integer integer = Integer.valueOf(str.substring(18, 20), 16);
            if (integer.intValue() >= 0)
              integer.intValue(); 
          } 
          str.contains("Triones~");
          Log.e("onrite(pwd);", "onDescriptorWrite(pwd);");
          super.onDescriptorWrite(param1BluetoothGatt, param1BluetoothGattDescriptor, param1Int);
        }
        
        public void onServicesDiscovered(BluetoothGatt param1BluetoothGatt, int param1Int) {
          if (param1BluetoothGatt != null)
            MyBluetoothGatt.access$002(MyBluetoothGatt.this, param1BluetoothGatt); 
          if (param1Int == 0) {
            MyBluetoothGatt.this.mConnectionState = 2;
            if (MyBluetoothGatt.this.isDreamY(param1BluetoothGatt.getDevice().getName()))
              mHandler.postDelayed(new Runnable() {
                    public void run() {
                      MyBluetoothGatt.this.checkpwd(MyBluetoothGatt.this.pwd);
                    }
                  },  50L); 
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.setNotify();
                  }
                },  100L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.getLightData();
                  }
                },  600L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.getFLightData();
                  }
                },  700L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.getTimeData();
                  }
                },  900L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.getTimeData();
                  }
                },  1300L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.setDate();
                  }
                },  1500L);
            mHandler.postDelayed(new Runnable() {
                  public void run() {
                    MyBluetoothGatt.this.read180a();
                  }
                },  1600L);
            if (MyBluetoothGatt.this.mLEdevice.getName().contains("Dream") || MyBluetoothGatt.this.mLEdevice.getName().contains("Flash")) {
              mHandler.postDelayed(new Runnable() {
                    public void run() {
                      MyBluetoothGatt.this.readhuancai();
                    }
                  },  300L);
            } else if (MyBluetoothGatt.this.mLEdevice.getName().contains(":")) {
              mHandler.postDelayed(new Runnable() {
                    public void run() {
                      MyBluetoothGatt.this.readqicai();
                    }
                  },  300L);
            } 
            MainActivity.ControlMACs.put(MyBluetoothGatt.this.mAddr, MyBluetoothGatt.this.mAddr);
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(MyBluetoothGatt.this.mAddr);
            bundle.putString("deviceAddr", stringBuilder.toString());
            message.setData(bundle);
            mHandler.sendMessageDelayed(message, 500L);
            MyBluetoothGatt.this.num = 0;
          } 
        }
      };
  }
  
  private void read180a() {
    readData("0000180a-0000-1000-8000-00805f9b34fb", "00002a25-0000-1000-8000-00805f9b34fb");
  }
  
  private void setAES() {
    try {
      Log.e("-", "check aes!");
      if (this.isTriones)
        if (this.mLEdevice.getName().length() == 16) {
          this.sendsrcAES[0] = (byte)-53;
          this.sendsrcAES[17] = (byte)-54;
        } else {
          this.sendsrcAES[0] = (byte)-5;
          this.sendsrcAES[17] = (byte)-6;
        }  
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", this.sendsrcAES);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  private void synTimedata(int paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6) {
    int i;
    int j = 0;
    if (paramByte1 == 65) {
      for (paramByte1 = j; paramByte1 < 6; paramByte1++) {
        byte[] arrayOfByte = this.timedata;
        i = paramByte1 * 14;
        j = i + 10;
        arrayOfByte[j] = (byte)paramByte3;
        this.timedata[i + 11] = (byte)paramByte4;
        this.timedata[i + 12] = (byte)paramByte5;
        this.timedata[i + 13] = (byte)paramByte6;
        this.timedata[i + 9] = (byte)65;
        this.timedata[j] = (byte)paramByte3;
      } 
    } else {
      for (paramByte3 = 0; paramByte3 < 6; paramByte3++) {
        byte[] arrayOfByte = this.timedata;
        int k = paramByte3 * 14;
        int m = k + 10;
        arrayOfByte[m] = (byte)i;
        this.timedata[k + 11] = (byte)0;
        this.timedata[k + 12] = (byte)0;
        this.timedata[k + 13] = (byte)0;
        this.timedata[k + 9] = (byte)paramByte1;
        this.timedata[m] = (byte)i;
      } 
    } 
  }
  
  private void updataSrc() {
    Random random = new Random();
    byte b = 0;
    while (b < this.srcAES.length) {
      int i = random.nextInt(90);
      byte[] arrayOfByte = this.srcAES;
      i = (byte)(i + 1 & 0xFF);
      arrayOfByte[b] = (byte)i;
      arrayOfByte = this.sendsrcAES;
      arrayOfByte[++b] = (byte)i;
    } 
  }
  
  public void addWriteQueue(int paramInt1, byte paramByte, int paramInt2) {
    MyColor myColor = new MyColor(paramInt1, paramByte, paramInt2);
    this.writeQueue.offer(myColor);
    if (this.writeQueue.size() > 10)
      this.writeQueue.poll(); 
    if (this.writeQueue.size() == 1)
      setColor(new MyColor(paramInt1, paramByte, paramInt2)); 
  }
  
  public void checkpwd(String paramString) {
    if (paramString == null || paramString.length() != 4)
      return; 
    try {
      int i = Integer.parseInt(paramString);
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { -49, (byte)(i / 1000), (byte)(i % 1000 / 100), (byte)(i % 100 / 10), (byte)(i % 10), -4 }, true);
      this.scheckpwd = paramString;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("pwd=");
      stringBuilder.append(paramString);
      Log.e("checkpwd22", stringBuilder.toString());
    } catch (NumberFormatException numberFormatException) {
      Log.e("00", "NumberFormatException");
    } 
  }
  
  @SuppressLint({"NewApi"})
  public void connectGatt(String paramString) {
    if (this.bluetoothAdapter == null)
      return; 
    this.linktime = (new Date()).getTime();
    this.mAddr = paramString;
    SharedPreferences sharedPreferences = this.settings;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("pwd");
    stringBuilder2.append(this.mAddr);
    this.pwd = sharedPreferences.getString(stringBuilder2.toString(), "1234");
    this.cachenewpwd = this.pwd;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("pwd=");
    stringBuilder1.append(this.pwd);
    Log.e("pwd", stringBuilder1.toString());
    this.ischeckpwd = false;
    this.mLEdevice = this.bluetoothAdapter.getRemoteDevice(paramString);
    this.mBluetoothGatt = this.mLEdevice.connectGatt(this.context, false, this.mGattCallback);
    this.mConnectionState = 1;
    setMsg(this.mAddr, 0);
    if (this.mLEdevice == null || this.mLEdevice.getName() == null) {
      this.mHandler.postDelayed(new Runnable() {
            public void run() {
              if (MyBluetoothGatt.this.mConnectionState == 1) {
                MyBluetoothGatt.this.mConnectionState = 4;
                MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 4);
              } 
            }
          }2000L);
      return;
    } 
    if (Pattern.compile("^Triones-|^Triones\\+|^Triones").matcher(this.mLEdevice.getName()).find()) {
      this.isTriones = true;
    } else {
      this.isTriones = false;
    } 
    this.mHandler.postDelayed(new Runnable() {
          public void run() {
            if (MyBluetoothGatt.this.mConnectionState == 1) {
              MyBluetoothGatt.this.mConnectionState = 4;
              MyBluetoothGatt.this.mBluetoothLeService.unlinkBleDevices.put(MyBluetoothGatt.this.mAddr, MyBluetoothGatt.this.mAddr);
              MyBluetoothGatt.this.stopLEService();
              MyBluetoothGatt.this.setMsg(MyBluetoothGatt.this.mAddr, 4);
            } 
          }
        }16000L);
  }
  
  public void getFLightData() {
    if (this.mLEdevice != null && this.mLEdevice.getName().contains("Flash"))
      this.timeHandler.postDelayed(this.timeRunnable, 100L); 
  }
  
  public void getLightData() {
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { -17, 1, 119 }, true);
  }
  
  public void getTimeData() {
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { 36, 42, 43, 66 }, true);
  }
  
  public BluetoothGatt getmBluetoothGatt() {
    return this.mBluetoothGatt;
  }
  
  public BluetoothGattCallback getmGattCallback() {
    return this.mGattCallback;
  }
  
  public BluetoothDevice getmLEdevice() {
    return this.mLEdevice;
  }
  
  public boolean isDreamY(String paramString) {
    Pattern pattern = Pattern.compile("^Dream\\#|^Dream~");
    return TextUtils.isEmpty(paramString) ? false : ((pattern == null) ? false : (pattern.matcher(paramString).find()));
  }
  
  public void openLight(boolean paramBoolean) {
    if (this.datas == null || this.datas.length < 4)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("openLight ");
    stringBuilder.append(paramBoolean);
    Log.e("openLight", stringBuilder.toString());
    byte[] arrayOfByte = new byte[3];
    arrayOfByte[0] = -52;
    arrayOfByte[1] = 35;
    arrayOfByte[2] = 51;
    if (!paramBoolean) {
      arrayOfByte = new byte[3];
      arrayOfByte[0] = -52;
      arrayOfByte[1] = 36;
      arrayOfByte[2] = 51;
      this.datas[2] = (byte)36;
    } else {
      this.datas[2] = (byte)35;
    } 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
  }
  
  public void openmic(boolean paramBoolean) {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = -122;
    arrayOfByte[1] = -16;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 104;
    if (paramBoolean) {
      arrayOfByte[1] = (byte)-16;
    } else {
      arrayOfByte[1] = (byte)15;
    } 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
    if (!paramBoolean)
      this.mTimeHandler.postDelayed(new Runnable() {
            public void run() {
              if (MyBluetoothGatt.this.savecolor != null) {
                MyBluetoothGatt.this.savecolor.warmWhite = (byte)0;
                MyBluetoothGatt.this.savecolor.color = Color.argb(255, 128, 128, 128);
                MyBluetoothGatt.this.setColor(MyBluetoothGatt.this.savecolor);
              } 
            }
          }500L); 
  }
  
  public void reSetCMD() {
    if (this.cachecmd != null)
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", this.cachecmd); 
  }
  
  public void reSetData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length != 8)
      return; 
    for (byte b = 0; b < TIME_TAIL.length; b++) {
      if (TIME_TAIL[b] == paramArrayOfbyte[0]) {
        this.timeadata.put(Integer.valueOf(b), Integer.valueOf(b));
        byte[] arrayOfByte = this.timedata;
        int i = b * 14;
        arrayOfByte[i + 5] = (byte)paramArrayOfbyte[2];
        this.timedata[i + 6] = (byte)paramArrayOfbyte[3];
        this.timedata[i + 8] = (byte)paramArrayOfbyte[5];
        this.timedata[i + 1] = (byte)paramArrayOfbyte[1];
        this.timedata[i + 14] = (byte)paramArrayOfbyte[6];
      } 
      if (this.mBluetoothLeService.myApplication.TimingHandler != null)
        this.mBluetoothLeService.myApplication.TimingHandler.sendEmptyMessage(4); 
    } 
  }
  
  public void readData(String paramString1, String paramString2) {
    UUID uUID2 = UUID.fromString(paramString1);
    UUID uUID1 = UUID.fromString(paramString2);
    if (this.mBluetoothGatt == null)
      return; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID2);
    if (bluetoothGattService == null)
      return; 
    BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID1);
    if (bluetoothGattCharacteristic == null)
      return; 
    this.mBluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
  }
  
  public void readhuancai() {
    Log.e("--", "readhuancai");
    byte[] arrayOfByte1 = new byte[4];
    arrayOfByte1[0] = 29;
    arrayOfByte1[1] = -16;
    arrayOfByte1[2] = 0;
    arrayOfByte1[3] = -15;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (this.mLEdevice != null) {
      arrayOfByte2 = arrayOfByte1;
      if (isDreamY(this.mLEdevice.getName())) {
        arrayOfByte2 = new byte[3];
        arrayOfByte2[0] = -59;
        arrayOfByte2[1] = -16;
        arrayOfByte2[2] = 92;
      } 
    } 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte2, true);
  }
  
  public void readqicai() {
    if (this.mLEdevice.getName().contains("Triones:")) {
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { -27, -16, 94 }, true);
    } else if (this.mLEdevice.getName().contains("Triones#")) {
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { 105, 0, 0, -16, -106 }, true);
    } 
  }
  
  public void sendColor_m_data(boolean paramBoolean, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[0] = 100;
    arrayOfByte[1] = -16;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = 0;
    arrayOfByte[5] = 0;
    arrayOfByte[6] = 0;
    if (paramBoolean) {
      arrayOfByte[1] = (byte)-16;
      this.isopenmyMIC = true;
    } else {
      this.isopenmyMIC = false;
      arrayOfByte[1] = (byte)15;
    } 
    paramInt1 = (byte)(paramInt1 & 0xFF);
    arrayOfByte[2] = (byte)paramInt1;
    byte b = (byte)(paramInt2 & 0xFF);
    arrayOfByte[3] = (byte)b;
    arrayOfByte[4] = (byte)0;
    arrayOfByte[5] = (byte)-18;
    arrayOfByte[6] = (byte)118;
    Matcher matcher = Pattern.compile("^QHM").matcher(this.mLEdevice.getName());
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("modid: ");
    stringBuilder.append(paramInt2);
    Log.e(" = = = = ", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("sendColor_m_data: ");
    stringBuilder.append(Arrays.toString(arrayOfByte));
    Log.e(" = = = = ", stringBuilder.toString());
    if (matcher.find()) {
      arrayOfByte = new byte[6];
      arrayOfByte[0] = 1;
      arrayOfByte[1] = -16;
      arrayOfByte[2] = 0;
      arrayOfByte[3] = 0;
      arrayOfByte[4] = 0;
      arrayOfByte[5] = 24;
      if (paramBoolean) {
        arrayOfByte[1] = (byte)-16;
        this.isopenmyMIC = true;
      } else {
        arrayOfByte[1] = (byte)15;
        this.isopenmyMIC = false;
      } 
      arrayOfByte[2] = (byte)paramInt1;
      arrayOfByte[3] = (byte)b;
      arrayOfByte[4] = (byte)0;
    } 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
  }
  
  public void sendColor_m_data(boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2) {
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[0] = 100;
    arrayOfByte[1] = -16;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = 0;
    arrayOfByte[5] = 0;
    arrayOfByte[6] = 0;
    if (paramBoolean1) {
      arrayOfByte[1] = (byte)-16;
      this.isopenmyMIC = true;
    } else {
      this.isopenmyMIC = false;
      arrayOfByte[1] = (byte)15;
    } 
    paramInt1 = (byte)(paramInt1 & 0xFF);
    arrayOfByte[2] = (byte)paramInt1;
    paramInt2 = (byte)(paramInt2 & 0xFF);
    arrayOfByte[3] = (byte)paramInt2;
    arrayOfByte[4] = (byte)0;
    arrayOfByte[5] = (byte)-18;
    arrayOfByte[6] = (byte)118;
    if (Pattern.compile("^QHM").matcher(this.mLEdevice.getName()).find()) {
      arrayOfByte = new byte[6];
      arrayOfByte[0] = 1;
      arrayOfByte[1] = -16;
      arrayOfByte[2] = 0;
      arrayOfByte[3] = 0;
      arrayOfByte[4] = 0;
      arrayOfByte[5] = 24;
      if (paramBoolean1) {
        arrayOfByte[1] = (byte)-16;
        this.isopenmyMIC = true;
      } else {
        arrayOfByte[1] = (byte)15;
        this.isopenmyMIC = false;
      } 
      arrayOfByte[2] = (byte)paramInt1;
      arrayOfByte[3] = (byte)paramInt2;
      arrayOfByte[4] = (byte)0;
    } 
    if (paramBoolean2)
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("sendColor_m_data1111: ");
    stringBuilder.append(Arrays.toString(arrayOfByte));
    Log.e(" = = = = ", stringBuilder.toString());
  }
  
  public void sendDimmer(boolean paramBoolean) {
    byte[] arrayOfByte = new byte[5];
    arrayOfByte[0] = -119;
    arrayOfByte[1] = 15;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = -104;
    if (paramBoolean) {
      arrayOfByte[1] = (byte)0;
    } else {
      arrayOfByte[1] = (byte)31;
    } 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
  }
  
  public void sendDreamWM(boolean paramBoolean, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[6];
    arrayOfByte[0] = -95;
    arrayOfByte[1] = -16;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = 0;
    arrayOfByte[5] = -88;
    if (paramBoolean) {
      arrayOfByte[1] = (byte)-16;
      this.isopenmyMIC = true;
    } else {
      this.isopenmyMIC = false;
      arrayOfByte[1] = (byte)15;
    } 
    arrayOfByte[2] = (byte)(byte)(paramInt1 & 0xFF);
    arrayOfByte[3] = (byte)(byte)(paramInt2 & 0xFF);
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
    String str = "";
    for (paramInt1 = 0; paramInt1 < arrayOfByte.length; paramInt1++) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(String.format("%02x", new Object[] { Integer.valueOf(arrayOfByte[paramInt1] & 0xFF) }));
      str = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("sendDreamWM: ");
    stringBuilder.append(str);
    Log.e(" = = = = ", stringBuilder.toString());
  }
  
  public void sendNewMod(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { -102, 0, (byte)paramInt1, (byte)paramInt2, (byte)paramInt3, (byte)paramInt4, -87 });
  }
  
  public void sendNewTime(int paramInt) {
    if (this.mBluetoothGatt == null)
      return; 
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = 35;
    arrayOfByte[1] = 35;
    arrayOfByte[2] = 35;
    arrayOfByte[3] = 35;
    arrayOfByte[4] = 35;
    arrayOfByte[5] = 35;
    arrayOfByte[6] = 35;
    arrayOfByte[7] = 35;
    if (this.mNewTimelist.containsKey(Integer.valueOf(paramInt))) {
      NewTime newTime = this.mNewTimelist.get(Integer.valueOf(paramInt));
      if (newTime != null) {
        arrayOfByte[0] = (byte)TIME_HEAD[paramInt];
        arrayOfByte[7] = (byte)TIME_TAIL[paramInt];
        boolean bool = newTime.valid;
        byte b = 15;
        if (bool) {
          paramInt = 240;
        } else {
          paramInt = 15;
        } 
        arrayOfByte[1] = (byte)(byte)paramInt;
        arrayOfByte[2] = (byte)(byte)(newTime.h & 0xFF);
        arrayOfByte[3] = (byte)(byte)(newTime.m & 0xFF);
        arrayOfByte[4] = (byte)(byte)(newTime.s & 0xFF);
        arrayOfByte[5] = (byte)newTime.w;
        paramInt = b;
        if (newTime.open)
          paramInt = 240; 
        arrayOfByte[6] = (byte)(byte)paramInt;
        UUID uUID2 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
        UUID uUID1 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
        BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID2);
        if (bluetoothGattService != null) {
          BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID1);
          if (bluetoothGattCharacteristic != null) {
            bluetoothGattCharacteristic.setValue(arrayOfByte);
            this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
          } 
        } 
      } 
    } 
  }
  
  public void sendNewTime(int paramInt, boolean paramBoolean) {
    Log.e("--", "sendNewTime");
    if (this.mBluetoothGatt == null)
      return; 
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = 35;
    arrayOfByte[1] = 35;
    arrayOfByte[2] = 35;
    arrayOfByte[3] = 35;
    arrayOfByte[4] = 35;
    arrayOfByte[5] = 35;
    arrayOfByte[6] = 35;
    arrayOfByte[7] = 35;
    if (this.mNewTimelist.containsKey(Integer.valueOf(paramInt))) {
      NewTime newTime = this.mNewTimelist.get(Integer.valueOf(paramInt));
      if (newTime != null) {
        arrayOfByte[0] = (byte)TIME_HEAD[paramInt];
        arrayOfByte[7] = (byte)TIME_TAIL[paramInt];
        newTime.valid = paramBoolean;
        paramBoolean = newTime.valid;
        char c1 = '\017';
        if (paramBoolean) {
          c2 = 'ð';
        } else {
          c2 = '\017';
        } 
        arrayOfByte[1] = (byte)(byte)c2;
        arrayOfByte[2] = (byte)(byte)(newTime.h & 0xFF);
        arrayOfByte[3] = (byte)(byte)(newTime.m & 0xFF);
        arrayOfByte[4] = (byte)(byte)(newTime.s & 0xFF);
        arrayOfByte[5] = (byte)newTime.w;
        char c2 = c1;
        if (newTime.open)
          c2 = 'ð'; 
        arrayOfByte[6] = (byte)(byte)c2;
        UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
        UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
        BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
        if (bluetoothGattService != null) {
          BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
          if (bluetoothGattCharacteristic != null && this.mBluetoothGatt != null) {
            bluetoothGattCharacteristic.setValue(arrayOfByte);
            this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
          } 
        } 
      } 
      this.mNewTimelist.put(Integer.valueOf(paramInt), newTime);
    } 
  }
  
  public void setColor(MyColor paramMyColor) {
    if (paramMyColor == null)
      return; 
    if (this.mBluetoothGatt == null)
      return; 
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService != null) {
      BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
      if (bluetoothGattCharacteristic != null) {
        int i;
        if (paramMyColor.progress <= 3) {
          i = 3;
        } else {
          i = paramMyColor.progress;
        } 
        paramMyColor.progress = i;
        byte[] arrayOfByte = new byte[7];
        arrayOfByte[0] = (byte)86;
        arrayOfByte[1] = (byte)(byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100);
        arrayOfByte[2] = (byte)(byte)(Color.green(paramMyColor.color) * paramMyColor.progress / 100);
        arrayOfByte[3] = (byte)(byte)(Color.blue(paramMyColor.color) * paramMyColor.progress / 100);
        arrayOfByte[4] = (byte)(byte)(paramMyColor.warmWhite * paramMyColor.progress / 100 & 0xFF);
        arrayOfByte[5] = (byte)-16;
        arrayOfByte[6] = (byte)-86;
        synTimedata((byte)65, (byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.green(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.blue(paramMyColor.color) * paramMyColor.progress / 100), (byte)0);
        if (paramMyColor.warmWhite != 0) {
          arrayOfByte[1] = (byte)0;
          arrayOfByte[2] = (byte)0;
          arrayOfByte[3] = (byte)0;
          arrayOfByte[5] = (byte)15;
          arrayOfByte[4] = (byte)(byte)(paramMyColor.progress * 255 / 100 & 0xFF);
          synTimedata((byte)65, (byte)0, (byte)0, (byte)0, (byte)0, (byte)(paramMyColor.progress * 255 / 100 & 0xFF));
        } 
        this.cachecmd = arrayOfByte;
        bluetoothGattCharacteristic.setValue(arrayOfByte);
        this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        this.savecolor = paramMyColor;
      } 
    } 
  }
  
  public void setColorProportion(int paramInt) {
    if (this.savecolor != null) {
      this.savecolor.progress = paramInt;
      setColor(this.savecolor);
    } 
  }
  
  public void setDate() {
    int i3;
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int i = calendar.get(7) - 1;
    int j = i;
    if (i < 0)
      j = 0; 
    int k = calendar.get(1);
    int m = calendar.get(2);
    int n = calendar.get(5);
    int i1 = calendar.get(11);
    i = calendar.get(12);
    int i2 = calendar.get(13);
    if (this.mLEdevice == null || this.mLEdevice.getName() == null)
      return; 
    if (Pattern.compile("^Dream|^Flash").matcher(this.mLEdevice.getName()).find()) {
      j = (byte)j;
      i3 = j;
    } else {
      switch (j) {
        default:
          j = 0;
          i3 = j;
          break;
        case 6:
          j = 6;
          i3 = j;
          break;
        case 5:
          j = 5;
          i3 = j;
          break;
        case 4:
          j = 4;
          i3 = j;
          break;
        case 3:
          j = 3;
          i3 = j;
          break;
        case 2:
          j = 2;
          i3 = j;
          break;
        case 1:
          j = 1;
          i3 = j;
          break;
        case 0:
          j = 7;
          i3 = j;
          break;
      } 
    } 
    byte b1 = (byte)(k % 100 & 0xFF);
    byte b2 = (byte)(m + 1 & 0xFF);
    byte b3 = (byte)(n & 0xFF);
    byte b4 = (byte)(i1 & 0xFF);
    byte b5 = (byte)(i & 0xFF);
    byte b6 = (byte)(i2 & 0xFF);
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    if (this.mBluetoothGatt == null)
      return; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService == null)
      return; 
    BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
    if (bluetoothGattCharacteristic == null)
      return; 
    bluetoothGattCharacteristic.setWriteType(2);
    bluetoothGattCharacteristic.setValue(new byte[] { 
          16, 20, b1, b2, b3, b4, b5, b6, i3, 0, 
          1 });
    try {
      Thread thread = new Thread();
      Runnable runnable = new Runnable() {
          public void run() {
            boolean bool;
            if (MyBluetoothGatt.this.mBluetoothGatt != null) {
              bool = MyBluetoothGatt.this.mBluetoothGatt.writeCharacteristic(cli);
            } else {
              bool = false;
            } 
            while (!bool) {
              try {
                Thread.sleep(120L);
              } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
              } 
              if (MyBluetoothGatt.this.mBluetoothGatt != null)
                bool = MyBluetoothGatt.this.mBluetoothGatt.writeCharacteristic(cli); 
            } 
          }
        };
      super(this, bluetoothGattCharacteristic);
      this(runnable);
      thread.start();
    } catch (Exception exception) {}
  }
  
  public void setDayData() {
    byte[] arrayOfByte = new byte[87];
    arrayOfByte[0] = 35;
    arrayOfByte[1] = 0;
    arrayOfByte[2] = 2;
    arrayOfByte[3] = 2;
    arrayOfByte[4] = 2;
    arrayOfByte[5] = 2;
    arrayOfByte[6] = 2;
    arrayOfByte[7] = 2;
    arrayOfByte[8] = 2;
    arrayOfByte[9] = 0;
    arrayOfByte[10] = 0;
    arrayOfByte[11] = 0;
    arrayOfByte[12] = 0;
    arrayOfByte[13] = 0;
    arrayOfByte[14] = 2;
    arrayOfByte[15] = 0;
    arrayOfByte[16] = 0;
    arrayOfByte[17] = 0;
    arrayOfByte[18] = 0;
    arrayOfByte[19] = 0;
    arrayOfByte[20] = 0;
    arrayOfByte[21] = 0;
    arrayOfByte[22] = 0;
    arrayOfByte[23] = 0;
    arrayOfByte[24] = 0;
    arrayOfByte[25] = 0;
    arrayOfByte[26] = 0;
    arrayOfByte[27] = 0;
    arrayOfByte[28] = 0;
    arrayOfByte[29] = 0;
    arrayOfByte[30] = 0;
    arrayOfByte[31] = 0;
    arrayOfByte[32] = 0;
    arrayOfByte[33] = 0;
    arrayOfByte[34] = 0;
    arrayOfByte[35] = 0;
    arrayOfByte[36] = 0;
    arrayOfByte[37] = 0;
    arrayOfByte[38] = 0;
    arrayOfByte[39] = 0;
    arrayOfByte[40] = 0;
    arrayOfByte[41] = 0;
    arrayOfByte[42] = 0;
    arrayOfByte[43] = 0;
    arrayOfByte[44] = 0;
    arrayOfByte[45] = 0;
    arrayOfByte[46] = 0;
    arrayOfByte[47] = 0;
    arrayOfByte[48] = 0;
    arrayOfByte[49] = 0;
    arrayOfByte[50] = 0;
    arrayOfByte[51] = 0;
    arrayOfByte[52] = 0;
    arrayOfByte[53] = 0;
    arrayOfByte[54] = 0;
    arrayOfByte[55] = 0;
    arrayOfByte[56] = 0;
    arrayOfByte[57] = 0;
    arrayOfByte[58] = 0;
    arrayOfByte[59] = 0;
    arrayOfByte[60] = 0;
    arrayOfByte[61] = 0;
    arrayOfByte[62] = 0;
    arrayOfByte[63] = 0;
    arrayOfByte[64] = 0;
    arrayOfByte[65] = 0;
    arrayOfByte[66] = 0;
    arrayOfByte[67] = 0;
    arrayOfByte[68] = 0;
    arrayOfByte[69] = 0;
    arrayOfByte[70] = 0;
    arrayOfByte[71] = 0;
    arrayOfByte[72] = 0;
    arrayOfByte[73] = 0;
    arrayOfByte[74] = 0;
    arrayOfByte[75] = 0;
    arrayOfByte[76] = 0;
    arrayOfByte[77] = 0;
    arrayOfByte[78] = 0;
    arrayOfByte[79] = 0;
    arrayOfByte[80] = 0;
    arrayOfByte[81] = 0;
    arrayOfByte[82] = 0;
    arrayOfByte[83] = 0;
    arrayOfByte[84] = 0;
    arrayOfByte[85] = 0;
    arrayOfByte[86] = 50;
    arrayOfByte[0] = (byte)35;
    for (byte b = 1; b < this.timedata.length; b++)
      arrayOfByte[b] = (byte)this.timedata[b]; 
    arrayOfByte[this.timedata.length - 1] = (byte)50;
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    if (this.mBluetoothGatt == null)
      return; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService == null)
      return; 
    BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
    if (bluetoothGattCharacteristic == null)
      return; 
    bluetoothGattCharacteristic.setWriteType(2);
    bluetoothGattCharacteristic.setValue(arrayOfByte);
    boolean bool = this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("f = ");
    stringBuilder.append(bool);
    Log.e("f", stringBuilder.toString());
  }
  
  public void setDreamMod(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt2 + 1;
    paramInt2 = i;
    if (i <= 1)
      paramInt2 = 1; 
    i = paramInt2;
    if (paramInt2 >= 255)
      i = 255; 
    paramInt4 += 128;
    paramInt2 = paramInt4;
    if (paramInt4 >= 255)
      paramInt2 = 255; 
    paramInt4 = paramInt1;
    if (paramInt1 >= 210)
      paramInt4 = 210; 
    byte[] arrayOfByte = new byte[10];
    arrayOfByte[0] = (byte)-1;
    arrayOfByte[1] = (byte)3;
    arrayOfByte[2] = (byte)0;
    arrayOfByte[3] = (byte)(byte)(paramInt4 & 0xFF);
    arrayOfByte[4] = (byte)(byte)(i & 0xFF);
    arrayOfByte[5] = (byte)(byte)(paramInt3 + 25 & 0xFF);
    arrayOfByte[6] = (byte)0;
    arrayOfByte[7] = (byte)(byte)(paramInt2 & 0xFF);
    arrayOfByte[8] = (byte)0;
    arrayOfByte[9] = (byte)-2;
    paramInt4 = arrayOfByte[1];
    paramInt1 = arrayOfByte[2];
    i = arrayOfByte[3];
    paramInt3 = arrayOfByte[4];
    paramInt2 = arrayOfByte[5];
    arrayOfByte[8] = (byte)(byte)(arrayOfByte[6] ^ paramInt3 ^ paramInt4 ^ paramInt1 ^ i ^ paramInt2 ^ arrayOfByte[7]);
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
  }
  
  public void setMODY(int paramInt1, int paramInt2) {
    int i = paramInt1 + 1;
    paramInt1 = i;
    if (i <= 1)
      paramInt1 = 1; 
    i = paramInt1;
    if (paramInt1 >= 255)
      i = 255; 
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", new byte[] { -98, -1, -1, (byte)(i & 0xFF), (byte)(paramInt2 + 25 & 0xFF), 0, -23 });
  }
  
  public void setMod(int paramInt1, int paramInt2) {
    if (paramInt1 < 0 || paramInt1 >= this.Mods.length)
      return; 
    this.modId = paramInt1;
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)-69;
    arrayOfByte[1] = (byte)this.Mods[paramInt1];
    byte b = (byte)(paramInt2 & 0xFF);
    arrayOfByte[2] = b;
    arrayOfByte[3] = (byte)68;
    this.cachecmd = arrayOfByte;
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setMod Speed=");
    stringBuilder.append(paramInt2);
    stringBuilder.append(" mod=");
    stringBuilder.append(this.Mods[paramInt1]);
    Log.e("setMod", stringBuilder.toString());
    synTimedata(this.Mods[paramInt1], b, (byte)0, (byte)0, (byte)0, (byte)0);
  }
  
  public void setMsg(String paramString, int paramInt) {
    Message message = new Message();
    message.what = paramInt;
    Bundle bundle = new Bundle();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramString);
    bundle.putString("deviceAddr", stringBuilder.toString());
    message.setData(bundle);
    if (this.mHandler != null)
      this.mHandler.sendMessage(message); 
  }
  
  public void setMusicColor(int paramInt) {
    if (this.mBluetoothGatt == null)
      return; 
    if (this.isopenmyMIC)
      return; 
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService != null) {
      BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
      if (bluetoothGattCharacteristic != null) {
        byte b1 = (byte)Color.red(paramInt);
        byte b2 = (byte)Color.green(paramInt);
        byte b3 = (byte)Color.blue(paramInt);
        byte b4 = b1;
        int i = b2;
        byte b5 = b3;
        if (b1 == 0) {
          byte b;
          if (b2 == 0) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (b3 == 0) {
            b = 1;
          } else {
            b = 0;
          } 
          b4 = b1;
          i = b2;
          b5 = b3;
          if ((paramInt & b) != 0) {
            b2 = 16;
            b = 5;
            paramInt = 5;
            b5 = b;
            i = paramInt;
            b4 = b2;
          } 
        } 
        this.cachecmd = new byte[] { 86, b4, i, b5, 0, -16, -86 };
        bluetoothGattCharacteristic.setValue(new byte[] { 120, b4, i, b5, 0, -16, -18 });
        this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
      } 
    } 
  }
  
  public void setMusicColor(int paramInt, double paramDouble) {
    if (this.mBluetoothGatt == null)
      return; 
    if (this.isopenmyMIC)
      return; 
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService != null) {
      BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
      if (bluetoothGattCharacteristic != null) {
        byte b1 = (byte)Color.red(paramInt);
        byte b2 = (byte)Color.green(paramInt);
        byte b3 = (byte)Color.blue(paramInt);
        int i = b1;
        byte b4 = b2;
        byte b5 = b3;
        if (b1 == 0) {
          byte b6;
          if (b2 == 0) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (b3 == 0) {
            b6 = 1;
          } else {
            b6 = 0;
          } 
          i = b1;
          b4 = b2;
          b5 = b3;
          if ((paramInt & b6) != 0) {
            paramInt = 16;
            b6 = 5;
            b1 = 5;
            b5 = b1;
            b4 = b6;
            i = paramInt;
          } 
        } 
        byte[] arrayOfByte = new byte[7];
        arrayOfByte[0] = (byte)120;
        arrayOfByte[1] = i;
        arrayOfByte[2] = b4;
        arrayOfByte[3] = b5;
        arrayOfByte[4] = (byte)0;
        arrayOfByte[5] = (byte)-16;
        arrayOfByte[6] = (byte)-18;
        paramInt = (int)paramDouble / 4;
        byte b = 20;
        if (paramInt >= 20)
          paramInt = b; 
        if (paramDouble == 0.0D) {
          arrayOfByte[4] = (byte)(byte)(paramInt | 0x0);
        } else {
          arrayOfByte[4] = (byte)(byte)(paramInt | 0x80);
        } 
        this.cachecmd = new byte[] { 86, i, b4, b5, 0, -16, -86 };
        bluetoothGattCharacteristic.setValue(arrayOfByte);
        this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        String str = "";
        for (paramInt = 0; paramInt < arrayOfByte.length; paramInt++) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(str);
          stringBuilder1.append(String.format("%02x", new Object[] { Integer.valueOf(arrayOfByte[paramInt] & 0xFF) }));
          str = stringBuilder1.toString();
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("length=");
        stringBuilder.append(this.mLEdevice.getName());
        Log.e("-Write-", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("length=");
        stringBuilder.append(arrayOfByte.length);
        stringBuilder.append(" Write = ");
        stringBuilder.append(str);
        Log.e("-Write-", stringBuilder.toString());
      } 
    } 
  }
  
  public void setMusicColor1(int paramInt, double paramDouble, boolean paramBoolean) {
    if (this.mBluetoothGatt == null)
      return; 
    if (this.isopenmyMIC)
      return; 
    UUID uUID1 = UUID.fromString("0000ffd5-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd9-0000-1000-8000-00805f9b34fb");
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService != null) {
      BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
      if (bluetoothGattCharacteristic != null) {
        byte b1 = (byte)Color.red(paramInt);
        byte b2 = (byte)Color.green(paramInt);
        byte b3 = (byte)Color.blue(paramInt);
        byte b4 = b1;
        byte b5 = b2;
        int i = b3;
        if (b1 == 0) {
          byte b6;
          if (b2 == 0) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (b3 == 0) {
            b6 = 1;
          } else {
            b6 = 0;
          } 
          b4 = b1;
          b5 = b2;
          i = b3;
          if ((paramInt & b6) != 0) {
            b2 = 16;
            b6 = 5;
            paramInt = 5;
            i = paramInt;
            b5 = b6;
            b4 = b2;
          } 
        } 
        byte[] arrayOfByte = new byte[7];
        arrayOfByte[0] = (byte)120;
        arrayOfByte[1] = b4;
        arrayOfByte[2] = b5;
        arrayOfByte[3] = i;
        arrayOfByte[4] = (byte)0;
        arrayOfByte[5] = (byte)-16;
        arrayOfByte[6] = (byte)-18;
        paramInt = (int)paramDouble / 4;
        byte b = 20;
        if (paramInt >= 20)
          paramInt = b; 
        if (!paramBoolean) {
          arrayOfByte[4] = (byte)(byte)(paramInt | 0x0);
        } else {
          arrayOfByte[4] = (byte)(byte)(paramInt | 0x80);
        } 
        this.cachecmd = new byte[] { 86, b4, b5, i, 0, -16, -86 };
        bluetoothGattCharacteristic.setValue(arrayOfByte);
        this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
      } 
    } 
  }
  
  public void setNewTime(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, byte paramByte, boolean paramBoolean2) {
    this.mNewTimelist.put(Integer.valueOf(paramInt1), new NewTime(paramBoolean1, paramInt2, paramInt3, paramInt4, paramByte, paramBoolean2));
  }
  
  public void setNotify() {
    Log.e("setNotify", "setNotify");
    UUID uUID1 = UUID.fromString("0000ffd0-0000-1000-8000-00805f9b34fb");
    UUID uUID2 = UUID.fromString("0000ffd4-0000-1000-8000-00805f9b34fb");
    if (this.mBluetoothGatt == null)
      return; 
    BluetoothGattService bluetoothGattService = this.mBluetoothGatt.getService(uUID1);
    if (bluetoothGattService == null) {
      Log.e("Consmart", "mService == null");
      return;
    } 
    this.photoCharacteristic = bluetoothGattService.getCharacteristic(uUID2);
    this.mBluetoothGatt.setCharacteristicNotification(this.photoCharacteristic, true);
    BluetoothGattDescriptor bluetoothGattDescriptor = this.photoCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
    if (bluetoothGattDescriptor == null) {
      Log.e("Consmart", "descriptor == null");
      return;
    } 
    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    boolean bool = this.mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setNotify =");
    stringBuilder.append(bool);
    Log.e("setNotify", stringBuilder.toString());
  }
  
  public void setSpeed(int paramInt) {
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("setSpeed Speed=");
    stringBuilder2.append(paramInt);
    stringBuilder2.append(" modId=");
    stringBuilder2.append(this.modId);
    Log.e("setMod", stringBuilder2.toString());
    if (this.modId < 0 || this.modId >= this.Mods.length)
      return; 
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)-69;
    arrayOfByte[1] = (byte)this.Mods[this.modId];
    byte b = (byte)(paramInt & 0xFF);
    arrayOfByte[2] = b;
    arrayOfByte[3] = (byte)68;
    this.cachecmd = arrayOfByte;
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("setMod Speed=");
    stringBuilder1.append(paramInt);
    stringBuilder1.append(" mod=");
    stringBuilder1.append(this.Mods[this.modId]);
    Log.e("setMod", stringBuilder1.toString());
    synTimedata(this.Mods[this.modId], b, (byte)0, (byte)0, (byte)0, (byte)0);
  }
  
  public void setSpeed(int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt2 + 1;
    paramInt2 = i;
    if (i <= 1)
      paramInt2 = 1; 
    i = paramInt2;
    if (paramInt2 >= 255)
      i = 255; 
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[0] = (byte)-98;
    arrayOfByte[1] = (byte)0;
    arrayOfByte[2] = (byte)(byte)(paramInt1 & 0xFF);
    arrayOfByte[3] = (byte)(byte)(i & 0xFF);
    arrayOfByte[4] = (byte)(byte)(paramInt3 + 25 & 0xFF);
    arrayOfByte[5] = (byte)0;
    arrayOfByte[6] = (byte)-23;
    this.cachecmd = arrayOfByte;
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
  }
  
  public void setValues(byte[] paramArrayOfbyte) {
    writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", paramArrayOfbyte, 1);
  }
  
  public void setmGattCallback(BluetoothGattCallback paramBluetoothGattCallback) {
    this.mGattCallback = paramBluetoothGattCallback;
  }
  
  public void setpwd(String paramString) {
    if (this.pwd == null || this.pwd.length() != 4)
      return; 
    if (paramString == null || paramString.length() != 4)
      return; 
    byte[] arrayOfByte = new byte[10];
    arrayOfByte[0] = -33;
    arrayOfByte[1] = 0;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = 0;
    arrayOfByte[5] = 0;
    arrayOfByte[6] = 0;
    arrayOfByte[7] = 0;
    arrayOfByte[8] = 0;
    arrayOfByte[9] = -3;
    try {
      int i = Integer.parseInt(this.pwd);
      byte b1 = (byte)(i / 1000);
      byte b2 = (byte)(i % 1000 / 100);
      byte b3 = (byte)(i % 100 / 10);
      i = (byte)(i % 10);
      int j = Integer.parseInt(paramString);
      byte b4 = (byte)(j / 1000);
      byte b5 = (byte)(j % 1000 / 100);
      byte b6 = (byte)(j % 100 / 10);
      j = (byte)(j % 10);
      arrayOfByte[1] = (byte)b1;
      arrayOfByte[2] = (byte)b2;
      arrayOfByte[3] = (byte)b3;
      arrayOfByte[4] = (byte)i;
      arrayOfByte[5] = (byte)b4;
      arrayOfByte[6] = (byte)b5;
      arrayOfByte[7] = (byte)b6;
      arrayOfByte[8] = (byte)j;
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
      this.cachenewpwd = paramString;
    } catch (NumberFormatException numberFormatException) {}
  }
  
  public void setqicaidata(int paramInt1, int paramInt2, boolean paramBoolean) {
    if (this.mLEdevice.getName().contains("Triones:")) {
      byte[] arrayOfByte = new byte[10];
      arrayOfByte[0] = -10;
      arrayOfByte[1] = 0;
      arrayOfByte[2] = 0;
      arrayOfByte[3] = 0;
      arrayOfByte[4] = 0;
      arrayOfByte[5] = 0;
      arrayOfByte[6] = 0;
      arrayOfByte[7] = 0;
      arrayOfByte[8] = 0;
      arrayOfByte[9] = 111;
      arrayOfByte[1] = (byte)(byte)paramInt1;
      arrayOfByte[2] = (byte)(byte)paramInt2;
      if (paramBoolean) {
        arrayOfByte[3] = (byte)1;
      } else {
        arrayOfByte[3] = (byte)0;
      } 
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
    } else if (this.mLEdevice.getName().contains("Triones#")) {
      byte[] arrayOfByte = new byte[5];
      arrayOfByte[0] = 105;
      arrayOfByte[1] = 0;
      arrayOfByte[2] = 0;
      arrayOfByte[3] = 15;
      arrayOfByte[4] = -106;
      arrayOfByte[1] = (byte)(byte)paramInt1;
      arrayOfByte[2] = (byte)(byte)paramInt2;
      writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte, true);
    } 
  }
  
  public void stopLEService() {
    if (this.mBluetoothGatt == null)
      return; 
    try {
      this.mBluetoothGatt.disconnect();
    } catch (Exception exception) {}
  }
  
  public void writeCharacteristic(String paramString1, String paramString2, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   6: astore #4
    //   8: aload_2
    //   9: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   12: astore_1
    //   13: aload_0
    //   14: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   17: astore_2
    //   18: aload_2
    //   19: ifnonnull -> 25
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: aload_0
    //   26: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   29: aload #4
    //   31: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   34: astore_2
    //   35: aload_2
    //   36: ifnonnull -> 42
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: aload_2
    //   43: aload_1
    //   44: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   47: astore_1
    //   48: aload_1
    //   49: ifnonnull -> 55
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: aload_1
    //   56: aload_3
    //   57: invokevirtual setValue : ([B)Z
    //   60: pop
    //   61: new java/lang/Thread
    //   64: astore_2
    //   65: new com/qh/blelight/MyBluetoothGatt$5
    //   68: astore_3
    //   69: aload_3
    //   70: aload_0
    //   71: aload_1
    //   72: invokespecial <init> : (Lcom/qh/blelight/MyBluetoothGatt;Landroid/bluetooth/BluetoothGattCharacteristic;)V
    //   75: aload_2
    //   76: aload_3
    //   77: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   80: aload_2
    //   81: invokevirtual start : ()V
    //   84: aload_0
    //   85: monitorexit
    //   86: return
    //   87: astore_1
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_1
    //   91: athrow
    //   92: astore_1
    //   93: goto -> 84
    // Exception table:
    //   from	to	target	type
    //   2	18	87	finally
    //   25	35	87	finally
    //   42	48	87	finally
    //   55	61	87	finally
    //   61	84	92	java/lang/Exception
    //   61	84	87	finally
  }
  
  public void writeCharacteristic(String paramString1, String paramString2, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   6: astore #5
    //   8: aload_2
    //   9: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   12: astore_1
    //   13: aload_0
    //   14: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   17: astore_2
    //   18: aload_2
    //   19: ifnonnull -> 25
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: aload_0
    //   26: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   29: aload #5
    //   31: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   34: astore_2
    //   35: aload_2
    //   36: ifnonnull -> 42
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: aload_2
    //   43: aload_1
    //   44: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   47: astore_1
    //   48: aload_1
    //   49: ifnonnull -> 55
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: aload_3
    //   56: ifnull -> 71
    //   59: aload_3
    //   60: arraylength
    //   61: bipush #20
    //   63: if_icmpgt -> 71
    //   66: aload_1
    //   67: iconst_2
    //   68: invokevirtual setWriteType : (I)V
    //   71: aload_1
    //   72: aload_3
    //   73: invokevirtual setValue : ([B)Z
    //   76: pop
    //   77: new java/lang/Thread
    //   80: astore_2
    //   81: new com/qh/blelight/MyBluetoothGatt$6
    //   84: astore_3
    //   85: aload_3
    //   86: aload_0
    //   87: aload_1
    //   88: iload #4
    //   90: invokespecial <init> : (Lcom/qh/blelight/MyBluetoothGatt;Landroid/bluetooth/BluetoothGattCharacteristic;Z)V
    //   93: aload_2
    //   94: aload_3
    //   95: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   98: aload_2
    //   99: invokevirtual start : ()V
    //   102: aload_0
    //   103: monitorexit
    //   104: return
    //   105: astore_1
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_1
    //   109: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	105	finally
    //   25	35	105	finally
    //   42	48	105	finally
    //   59	71	105	finally
    //   71	102	105	finally
  }
  
  public boolean writeCharacteristic(String paramString1, String paramString2, byte[] paramArrayOfbyte, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   6: astore_1
    //   7: aload_2
    //   8: invokestatic fromString : (Ljava/lang/String;)Ljava/util/UUID;
    //   11: astore_2
    //   12: aload_0
    //   13: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   16: astore #5
    //   18: aload #5
    //   20: ifnonnull -> 27
    //   23: aload_0
    //   24: monitorexit
    //   25: iconst_0
    //   26: ireturn
    //   27: aload_0
    //   28: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   31: aload_1
    //   32: invokevirtual getService : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattService;
    //   35: astore_1
    //   36: aload_1
    //   37: ifnonnull -> 44
    //   40: aload_0
    //   41: monitorexit
    //   42: iconst_0
    //   43: ireturn
    //   44: aload_1
    //   45: aload_2
    //   46: invokevirtual getCharacteristic : (Ljava/util/UUID;)Landroid/bluetooth/BluetoothGattCharacteristic;
    //   49: astore_1
    //   50: aload_1
    //   51: ifnonnull -> 58
    //   54: aload_0
    //   55: monitorexit
    //   56: iconst_0
    //   57: ireturn
    //   58: aload_3
    //   59: ifnull -> 74
    //   62: aload_3
    //   63: arraylength
    //   64: bipush #20
    //   66: if_icmpgt -> 74
    //   69: aload_1
    //   70: iconst_2
    //   71: invokevirtual setWriteType : (I)V
    //   74: aload_1
    //   75: aload_3
    //   76: invokevirtual setValue : ([B)Z
    //   79: pop
    //   80: aload_0
    //   81: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   84: astore_2
    //   85: aload_2
    //   86: ifnonnull -> 93
    //   89: aload_0
    //   90: monitorexit
    //   91: iconst_0
    //   92: ireturn
    //   93: aload_0
    //   94: getfield mBluetoothGatt : Landroid/bluetooth/BluetoothGatt;
    //   97: aload_1
    //   98: invokevirtual writeCharacteristic : (Landroid/bluetooth/BluetoothGattCharacteristic;)Z
    //   101: istore #6
    //   103: aload_0
    //   104: monitorexit
    //   105: iload #6
    //   107: ireturn
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	108	finally
    //   27	36	108	finally
    //   44	50	108	finally
    //   62	74	108	finally
    //   74	85	108	finally
    //   93	103	108	finally
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\MyBluetoothGatt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */